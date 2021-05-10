package com.toucan.shopping.cloud.user.controller;


import com.alibaba.fastjson.JSONObject;
import com.toucan.shopping.modules.common.lock.redis.RedisLock;
import com.toucan.shopping.modules.common.page.PageInfo;
import com.toucan.shopping.modules.common.util.*;
import com.toucan.shopping.modules.user.constant.UserLoginConstant;
import com.toucan.shopping.modules.user.constant.UserRegistConstant;
import com.toucan.shopping.modules.user.entity.*;
import com.toucan.shopping.modules.user.es.service.UserElasticSearchService;
import com.toucan.shopping.modules.user.kafka.message.UserCreateMessage;
import com.toucan.shopping.modules.user.page.UserPageInfo;
import com.toucan.shopping.modules.user.redis.UserCenterLoginRedisKey;
import com.toucan.shopping.modules.user.redis.UserCenterRegistRedisKey;
import com.toucan.shopping.modules.user.service.*;
import com.toucan.shopping.modules.user.vo.UserLoginVO;
import com.toucan.shopping.modules.user.vo.UserRegistVO;
import com.toucan.shopping.modules.user.vo.UserResultVO;
import com.toucan.shopping.modules.user.vo.UserVO;
import com.toucan.shopping.cloud.user.queue.NewUserMessageQueue;
import com.toucan.shopping.modules.common.generator.IdGenerator;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.common.vo.ResultVO;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * 用户注册、用户登录
 */
@RestController
@RequestMapping("/user")
public class UserController {


    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private UserService userService;

    @Autowired
    private RedisLock redisLock;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;


    @Autowired
    private IdGenerator idGenerator;

    @Autowired
    private UserRedisService userRedisService;

    @Autowired
    private UserElasticSearchService userElasticSearchService;

    @Autowired
    private UserMobilePhoneService userMobilePhoneService;

    @Autowired
    private UserUserNameService userUserNameService;

    @Autowired
    private UserDetailService userDetailService;

    @Autowired
    private UserEmailService userEmailService;


    @Autowired
    private NewUserMessageQueue newUserMessageQueue;

    @RequestMapping(value="/find/mobile/phone",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO findByMobilePhone(@RequestBody RequestJsonVO requestJsonVO){
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        if(requestJsonVO==null)
        {
            resultObjectVO.setCode(UserRegistConstant.NOT_FOUND_USER);
            resultObjectVO.setMsg("请求失败,没有找到请求对象");
            return resultObjectVO;
        }
        if (StringUtils.isEmpty(requestJsonVO.getAppCode())) {
            resultObjectVO.setCode(UserRegistConstant.NOT_FOUND_USER);
            resultObjectVO.setMsg("请求失败,没有找到应用编码");
            return resultObjectVO;
        }
        UserRegistVO userRegistVO = JSONObject.parseObject(requestJsonVO.getEntityJson(),UserRegistVO.class);
        try {

            if(StringUtils.isEmpty(userRegistVO.getMobilePhone())||!PhoneUtils.isChinaPhoneLegal(userRegistVO.getMobilePhone()))
            {
                resultObjectVO.setCode(UserRegistConstant.MOBILE_ERROR);
                resultObjectVO.setMsg("请求失败,手机号错误");
                return resultObjectVO;
            }
            List<UserMobilePhone> userEntityList = userMobilePhoneService.findListByMobilePhone(userRegistVO.getMobilePhone());
            resultObjectVO.setData(userEntityList);
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请求失败,请稍后重试");
        }
        return resultObjectVO;
    }


    @RequestMapping(value="/regist/mobile/phone",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO registByMobilePhone(@RequestBody RequestJsonVO requestJsonVO){
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        if(requestJsonVO==null)
        {
            resultObjectVO.setCode(UserRegistConstant.NOT_FOUND_USER);
            resultObjectVO.setMsg("请求失败,没有找到要注册的用户");
            return resultObjectVO;
        }

        if (StringUtils.isEmpty(requestJsonVO.getAppCode())) {
            resultObjectVO.setCode(UserRegistConstant.NOT_FOUND_USER);
            resultObjectVO.setMsg("请求失败,没有找到应用编码");
            return resultObjectVO;
        }
        UserRegistVO userRegistVO = JSONObject.parseObject(requestJsonVO.getEntityJson(),UserRegistVO.class);
        if(userRegistVO==null)
        {
            resultObjectVO.setCode(UserRegistConstant.NOT_FOUND_USER);
            resultObjectVO.setMsg("请求失败,没有找到要注册的用户");
            return resultObjectVO;
        }
        if(StringUtils.isEmpty(userRegistVO.getMobilePhone()))
        {
            resultObjectVO.setCode(UserRegistConstant.NOT_FOUND_MOBILE);
            resultObjectVO.setMsg("请求失败,请输入注册手机号");
            return resultObjectVO;
        }

        if(!PhoneUtils.isChinaPhoneLegal(userRegistVO.getMobilePhone()))
        {
            resultObjectVO.setCode(UserRegistConstant.MOBILE_ERROR);
            resultObjectVO.setMsg("请求失败,手机号错误");
            return resultObjectVO;
        }

        if(StringUtils.isEmpty(userRegistVO.getPassword()))
        {
            resultObjectVO.setCode(UserRegistConstant.PASSWORD_NOT_FOUND);
            resultObjectVO.setMsg("请求失败,请输入密码");
            return resultObjectVO;
        }

        if(!UserRegistUtil.checkPwd(userRegistVO.getPassword()))
        {
            resultObjectVO.setCode(UserRegistConstant.PASSWORD_ERROR);
            resultObjectVO.setMsg("请求失败,请输入6至15位的密码");
            return resultObjectVO;
        }



        try {
            boolean lockStatus = redisLock.lock(UserCenterRegistRedisKey.getRegistLockKey(userRegistVO.getMobilePhone()), userRegistVO.getMobilePhone());
            if (!lockStatus) {
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("超时重试");
                return resultObjectVO;
            }
            //查询手机号是否已注册
            List<UserMobilePhone> userEntityList = userMobilePhoneService.findListByMobilePhone(userRegistVO.getMobilePhone());
            if (!CollectionUtils.isEmpty(userEntityList)) {
                resultObjectVO.setCode(UserResultVO.FAILD);
                resultObjectVO.setMsg("手机号码已注册!");
            } else {
                User user = new User();
                user.setId(idGenerator.id());
                user.setUserMainId(idGenerator.id());
                user.setCreateDate(new Date());
                user.setPassword(MD5Util.md5(userRegistVO.getPassword()));
                user.setEnableStatus((short) 1);
                user.setDeleteStatus((short) 0);


                //保存用户主表
                int row = userService.save(user);
                if (row < 1) {
                    logger.warn("用户注册失败 {}", requestJsonVO.getEntityJson());
                    resultObjectVO.setCode(UserResultVO.FAILD);
                    resultObjectVO.setMsg("注册失败,请重试!");
                } else {
                    //保存用户手机子表
                    UserMobilePhone userMobilePhone = new UserMobilePhone();
                    userMobilePhone.setId(idGenerator.id());
                    //设置手机号
                    userMobilePhone.setMobilePhone(userRegistVO.getMobilePhone());
                    //设置用户主表ID
                    userMobilePhone.setUserMainId(user.getUserMainId());
                    userMobilePhone.setCreateDate(new Date());
                    userMobilePhone.setDeleteStatus((short) 0);
                    row = userMobilePhoneService.save(userMobilePhone);
                    if (row < 1) {
                        //修改用户主表数据删除状态
                        userService.deleteById(user.getId());
                        logger.warn("手机号注册失败 {}", requestJsonVO.getEntityJson());
                        resultObjectVO.setCode(UserResultVO.FAILD);
                        resultObjectVO.setMsg("手机号注册失败,请重试!");
                    } else {
                        //保存用户昵称
                        UserDetail userDetail = new UserDetail();
                        userDetail.setId(idGenerator.id());
                        userDetail.setUserMainId(user.getUserMainId());
                        userDetail.setNickName("用户"+userRegistVO.getMobilePhone());
                        userDetail.setSex((short)1);
                        userDetail.setCreateDate(new Date());
                        userDetail.setDeleteStatus((short) 0);

                        row = userDetailService.save(userDetail);
                        if (row < 1) {
                            logger.warn("保存默认用户昵称失败 {}",requestJsonVO.getEntityJson());
                        }else {
                            userRegistVO.setUserMainId(user.getUserMainId());
                            resultObjectVO.setData(userRegistVO);

                            //发送创建用户到缓存
                            UserCreateMessage userCreateMessage = new UserCreateMessage();
                            userCreateMessage.setId(user.getId());
                            userCreateMessage.setUserMainId(user.getUserMainId());
                            userCreateMessage.setEnableStatus(user.getEnableStatus());
                            userCreateMessage.setMobilePhone(userMobilePhone.getMobilePhone());
                            userCreateMessage.setNickName(userDetail.getNickName());
                            userCreateMessage.setSex(userDetail.getSex());
                            userCreateMessage.setDeleteStatus(user.getDeleteStatus());

                            //放入队列
                            newUserMessageQueue.push(userCreateMessage);
                        }
                    }
                }
            }
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请求失败,请稍后重试");
        }finally{
            redisLock.unLock(UserCenterRegistRedisKey.getRegistLockKey(userRegistVO.getMobilePhone()), userRegistVO.getMobilePhone());
        }
        return resultObjectVO;
    }


    /**
     * 关联用户名
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/connect/username",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO connectUsername(@RequestBody RequestJsonVO requestJsonVO){
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        if(requestJsonVO==null)
        {
            resultObjectVO.setCode(UserRegistConstant.NOT_FOUND_USER);
            resultObjectVO.setMsg("请求失败,没有找到要关联的用户");
            return resultObjectVO;
        }

        if (StringUtils.isEmpty(requestJsonVO.getAppCode())) {
            resultObjectVO.setCode(UserRegistConstant.NOT_FOUND_USER);
            resultObjectVO.setMsg("请求失败,没有找到应用编码");
            return resultObjectVO;
        }
        UserRegistVO userRegistVO = JSONObject.parseObject(requestJsonVO.getEntityJson(),UserRegistVO.class);
        if(userRegistVO==null)
        {
            resultObjectVO.setCode(UserRegistConstant.NOT_FOUND_USER);
            resultObjectVO.setMsg("请求失败,没有找到要注册的用户");
            return resultObjectVO;
        }
        if(StringUtils.isEmpty(userRegistVO.getUsername()))
        {
            resultObjectVO.setCode(UserRegistConstant.NOT_FOUND_MOBILE);
            resultObjectVO.setMsg("请求失败,请输入用户名");
            return resultObjectVO;
        }

        if(!UsernameUtils.isUsername(userRegistVO.getUsername()))
        {
            resultObjectVO.setCode(UserRegistConstant.MOBILE_ERROR);
            resultObjectVO.setMsg("请求失败,用户名格式错误");
            return resultObjectVO;
        }

        if(userRegistVO.getUserMainId()==null)
        {
            resultObjectVO.setCode(UserRegistConstant.NOT_FOUND_USER);
            resultObjectVO.setMsg("请求失败,没有找到要用户主ID");
            return resultObjectVO;
        }

        try {
            boolean lockStatus = redisLock.lock(UserCenterRegistRedisKey.getRegistLockKey(userRegistVO.getUsername()), userRegistVO.getUsername());
            if (!lockStatus) {
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("超时重试");
                return resultObjectVO;
            }
            //查询用户名是否已经关联
            UserUserName query = new UserUserName();
            query.setUsername(userRegistVO.getUsername());
            List<UserUserName> userUserNameList = userUserNameService.findListByEntity(query);
            if (!CollectionUtils.isEmpty(userUserNameList)) {
                resultObjectVO.setCode(UserResultVO.FAILD);
                resultObjectVO.setMsg("用户名已关联!");
            } else {
                //保存用户用户名子表
                UserUserName userUserName = new UserUserName();
                userUserName.setId(idGenerator.id());
                //设置用户名
                userUserName.setUsername(userRegistVO.getUsername());
                //设置用户主表ID
                userUserName.setUserMainId(userRegistVO.getUserMainId());
                userUserName.setCreateDate(new Date());
                userUserName.setDeleteStatus((short) 0);

                int row = userUserNameService.save(userUserName);
                if (row < 1) {
                    logger.warn("关联用户名失败 {}", requestJsonVO.getEntityJson());
                    resultObjectVO.setCode(UserResultVO.FAILD);
                    resultObjectVO.setMsg("请求失败,请重试!");
                }
            }
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请求失败,请稍后重试");
        }finally{
            redisLock.unLock(UserCenterRegistRedisKey.getRegistLockKey(userRegistVO.getUsername()), userRegistVO.getUsername());
        }
        return resultObjectVO;
    }



    /**
     * 查询用户名列表
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/find/username/list/by/username",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO findUsernameListByUsername(@RequestBody RequestJsonVO requestJsonVO){
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        if(requestJsonVO==null)
        {
            resultObjectVO.setCode(UserRegistConstant.NOT_FOUND_USER);
            resultObjectVO.setMsg("请求失败,没有找到要关联的用户");
            return resultObjectVO;
        }

        if (StringUtils.isEmpty(requestJsonVO.getAppCode())) {
            resultObjectVO.setCode(UserRegistConstant.NOT_FOUND_USER);
            resultObjectVO.setMsg("请求失败,没有找到应用编码");
            return resultObjectVO;
        }
        UserRegistVO userRegistVO = JSONObject.parseObject(requestJsonVO.getEntityJson(),UserRegistVO.class);
        if(userRegistVO==null)
        {
            resultObjectVO.setCode(UserRegistConstant.NOT_FOUND_USER);
            resultObjectVO.setMsg("请求失败,没有找到要注册的用户");
            return resultObjectVO;
        }
        if(StringUtils.isEmpty(userRegistVO.getUsername()))
        {
            resultObjectVO.setCode(UserRegistConstant.NOT_FOUND_MOBILE);
            resultObjectVO.setMsg("请求失败,请输入用户名");
            return resultObjectVO;
        }

        if(!UsernameUtils.isUsername(userRegistVO.getUsername()))
        {
            resultObjectVO.setCode(UserRegistConstant.MOBILE_ERROR);
            resultObjectVO.setMsg("请求失败,用户名格式错误");
            return resultObjectVO;
        }


        try {
            UserUserName query = new UserUserName();
            query.setUsername(userRegistVO.getUsername());
            List<UserUserName> userUserNameList = userUserNameService.findListByEntity(query);
            resultObjectVO.setData(userUserNameList);
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请求失败,请稍后重试");
        }finally{
        }
        return resultObjectVO;
    }


    /**
     * 关联邮箱
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/connect/email",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO connectEmail(@RequestBody RequestJsonVO requestJsonVO){
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        if(requestJsonVO==null)
        {
            resultObjectVO.setCode(UserRegistConstant.NOT_FOUND_USER);
            resultObjectVO.setMsg("请求失败,没有找到要关联的用户");
            return resultObjectVO;
        }

        if (StringUtils.isEmpty(requestJsonVO.getAppCode())) {
            resultObjectVO.setCode(UserRegistConstant.NOT_FOUND_USER);
            resultObjectVO.setMsg("请求失败,没有找到应用编码");
            return resultObjectVO;
        }
        UserRegistVO userRegistVO = JSONObject.parseObject(requestJsonVO.getEntityJson(),UserRegistVO.class);
        if(userRegistVO==null)
        {
            resultObjectVO.setCode(UserRegistConstant.NOT_FOUND_USER);
            resultObjectVO.setMsg("请求失败,没有找到要注册的用户");
            return resultObjectVO;
        }
        if(StringUtils.isEmpty(userRegistVO.getEmail()))
        {
            resultObjectVO.setCode(UserRegistConstant.NOT_FOUND_MOBILE);
            resultObjectVO.setMsg("请求失败,请输入邮箱");
            return resultObjectVO;
        }

        if(!EmailUtils.isEmail(userRegistVO.getEmail()))
        {
            resultObjectVO.setCode(UserRegistConstant.MOBILE_ERROR);
            resultObjectVO.setMsg("请求失败,邮箱格式错误");
            return resultObjectVO;
        }

        if(userRegistVO.getUserMainId()==null)
        {
            resultObjectVO.setCode(UserRegistConstant.NOT_FOUND_USER);
            resultObjectVO.setMsg("请求失败,没有找到要用户主ID");
            return resultObjectVO;
        }

        try {
            boolean lockStatus = redisLock.lock(UserCenterRegistRedisKey.getRegistLockKey(userRegistVO.getUsername()), userRegistVO.getUsername());
            if (!lockStatus) {
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("超时重试");
                return resultObjectVO;
            }
            //查询邮箱是否已经关联
            UserEmail query = new UserEmail();
            query.setEmail(userRegistVO.getEmail());
            List<UserEmail> userEmails = userEmailService.findListByEntity(query);
            if (!CollectionUtils.isEmpty(userEmails)) {
                resultObjectVO.setCode(UserResultVO.FAILD);
                resultObjectVO.setMsg("邮箱已关联!");
            } else {
                //保存用户邮箱子表
                UserEmail userEmail = new UserEmail();
                userEmail.setId(idGenerator.id());
                //设置用户名
                userEmail.setEmail(userRegistVO.getEmail());
                //设置用户主表ID
                userEmail.setUserMainId(userRegistVO.getUserMainId());
                userEmail.setCreateDate(new Date());
                userEmail.setDeleteStatus((short) 0);

                int row = userEmailService.save(userEmail);
                if (row < 1) {
                    logger.warn("关联邮箱失败 {}", requestJsonVO.getEntityJson());
                    resultObjectVO.setCode(UserResultVO.FAILD);
                    resultObjectVO.setMsg("请求失败,请重试!");
                }
            }
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请求失败,请稍后重试");
        }finally{
            redisLock.unLock(UserCenterRegistRedisKey.getRegistLockKey(userRegistVO.getUsername()), userRegistVO.getUsername());
        }
        return resultObjectVO;
    }



    /**
     * 查询邮箱列表
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/find/email/list/by/email",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO findEmailListByEmail(@RequestBody RequestJsonVO requestJsonVO){
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        if(requestJsonVO==null)
        {
            resultObjectVO.setCode(UserRegistConstant.NOT_FOUND_USER);
            resultObjectVO.setMsg("请求失败,没有找到要关联的用户");
            return resultObjectVO;
        }

        if (StringUtils.isEmpty(requestJsonVO.getAppCode())) {
            resultObjectVO.setCode(UserRegistConstant.NOT_FOUND_USER);
            resultObjectVO.setMsg("请求失败,没有找到应用编码");
            return resultObjectVO;
        }
        UserRegistVO userRegistVO = JSONObject.parseObject(requestJsonVO.getEntityJson(),UserRegistVO.class);
        if(userRegistVO==null)
        {
            resultObjectVO.setCode(UserRegistConstant.NOT_FOUND_USER);
            resultObjectVO.setMsg("请求失败,没有找到要注册的用户");
            return resultObjectVO;
        }
        if(StringUtils.isEmpty(userRegistVO.getEmail()))
        {
            resultObjectVO.setCode(UserRegistConstant.NOT_FOUND_MOBILE);
            resultObjectVO.setMsg("请求失败,请输入邮箱");
            return resultObjectVO;
        }

        if(!EmailUtils.isEmail(userRegistVO.getEmail()))
        {
            resultObjectVO.setCode(UserRegistConstant.MOBILE_ERROR);
            resultObjectVO.setMsg("请求失败,邮箱格式错误");
            return resultObjectVO;
        }

        try {
            UserEmail query = new UserEmail();
            query.setEmail(userRegistVO.getEmail());
            List<UserEmail> userEmails = userEmailService.findListByEntity(query);
            resultObjectVO.setData(userEmails);

        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请求失败,请稍后重试");
        }finally{
        }
        return resultObjectVO;
    }




    /**
     * 修改用户详情
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/update/detail",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO updateDetail(@RequestBody RequestJsonVO requestJsonVO){
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        if(requestJsonVO==null)
        {
            resultObjectVO.setCode(UserRegistConstant.NOT_FOUND_USER);
            resultObjectVO.setMsg("请求失败,没有找到要操作的用户");
            return resultObjectVO;
        }

        if (StringUtils.isEmpty(requestJsonVO.getAppCode())) {
            resultObjectVO.setCode(UserRegistConstant.NOT_FOUND_USER);
            resultObjectVO.setMsg("请求失败,没有找到应用编码");
            return resultObjectVO;
        }
        UserRegistVO userRegistVO = JSONObject.parseObject(requestJsonVO.getEntityJson(),UserRegistVO.class);
        if(userRegistVO==null)
        {
            resultObjectVO.setCode(UserRegistConstant.NOT_FOUND_USER);
            resultObjectVO.setMsg("请求失败,没有找到要修改的用户");
            return resultObjectVO;
        }
        if(userRegistVO.getUserMainId()==null)
        {
            resultObjectVO.setCode(UserRegistConstant.NOT_FOUND_USER);
            resultObjectVO.setMsg("请求失败,没有找到要用户主ID");
            return resultObjectVO;
        }
        try {
            boolean lockStatus = redisLock.lock(UserCenterRegistRedisKey.getRegistLockKey(String.valueOf(userRegistVO.getUserMainId())), String.valueOf(userRegistVO.getUserMainId()));
            if (!lockStatus) {
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("超时重试");
                return resultObjectVO;
            }
            UserDetail query = new UserDetail();
            query.setUserMainId(userRegistVO.getUserMainId());
            List<UserDetail> userDetails = userDetailService.findListByEntity(query);
            if(CollectionUtils.isNotEmpty(userDetails))
            {
                UserDetail userDetail = userDetails.get(0);
                //昵称
                if(StringUtils.isNotEmpty(userRegistVO.getNickName()))
                {
                    userDetail.setNickName(userRegistVO.getNickName());
                }
                //姓名
                if(StringUtils.isNotEmpty(userRegistVO.getTrueName()))
                {
                    userDetail.setTrueName(userRegistVO.getTrueName());
                }
                //身份证
                if(StringUtils.isNotEmpty(userRegistVO.getIdCard()))
                {
                    userDetail.setIdCard(userRegistVO.getIdCard());
                }
                //头像
                if(StringUtils.isNotEmpty(userRegistVO.getHeadSculpture()))
                {
                    userDetail.setHeadSculpture(userRegistVO.getHeadSculpture());
                }
                //性别
                if(userRegistVO.getSex()!=null)
                {
                    userDetail.setSex(userRegistVO.getSex());
                }
                //用户类型
                if(userRegistVO.getType()!=null)
                {
                    userDetail.setType(userRegistVO.getType());
                }
                int row = userDetailService.update(userDetail);
                if(row<=0)
                {
                    logger.warn("修改用户详情失败 {}", requestJsonVO.getEntityJson());
                    resultObjectVO.setCode(ResultVO.FAILD);
                    resultObjectVO.setMsg("请求失败,请稍后重试");
                }
            }

        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请求失败,请稍后重试");
        }finally{
            redisLock.unLock(UserCenterRegistRedisKey.getRegistLockKey(String.valueOf(userRegistVO.getUserMainId())), String.valueOf(userRegistVO.getUserMainId()));
        }
        return resultObjectVO;
    }






    /**
     * 密码登录
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/login/password",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO loginByPassword(@RequestBody RequestJsonVO requestJsonVO) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        if (requestJsonVO == null) {
            resultObjectVO.setCode(UserLoginConstant.NOT_FOUND_USER);
            resultObjectVO.setMsg("请求失败,没有找到要登录的用户");
            return resultObjectVO;
        }
        if (StringUtils.isEmpty(requestJsonVO.getAppCode())) {
            resultObjectVO.setCode(UserLoginConstant.NOT_FOUND_USER);
            resultObjectVO.setMsg("请求失败,没有找到应用编码");
            return resultObjectVO;
        }

        UserLoginVO userLogin = JSONObject.parseObject(requestJsonVO.getEntityJson(),UserLoginVO.class);
        if(userLogin==null)
        {
            resultObjectVO.setCode(UserLoginConstant.NOT_FOUND_USER);
            resultObjectVO.setMsg("请求失败,没有找到账号");
            return resultObjectVO;
        }
        logger.info(" 用户登录 {} ",requestJsonVO.getEntityJson());

        if (StringUtils.isEmpty(userLogin.getPassword())) {
            resultObjectVO.setCode(UserLoginConstant.PASSWORD_NOT_FOUND);
            resultObjectVO.setMsg("请求失败,请输入密码");
            return resultObjectVO;
        }
        if(StringUtils.isEmpty(userLogin.getLoginUserName()))
        {
            resultObjectVO.setCode(UserLoginConstant.USERNAME_NOT_FOUND);
            resultObjectVO.setMsg("请求失败,请输入账号");
            return resultObjectVO;
        }

        try {
            boolean lockStatus = redisLock.lock(UserCenterLoginRedisKey.getLoginLockKey(userLogin.getLoginUserName()), userLogin.getLoginUserName());
            if (!lockStatus) {
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("超时重试");
                return resultObjectVO;
            }

            //如果当前输入的是手机号判断手机号是否存在
            Long userId = 0L;
            if(PhoneUtils.isChinaPhoneLegal(userLogin.getLoginUserName()))
            {
                List<UserMobilePhone> userMobilePhones = userMobilePhoneService.findListByMobilePhone(userLogin.getLoginUserName());
                if(!CollectionUtils.isEmpty(userMobilePhones))
                {
                    if(userMobilePhones.get(0)!=null&&userMobilePhones.get(0).getUserMainId()!=null) {
                        userId = userMobilePhones.get(0).getUserMainId();
                    }
                }

            }else if(EmailUtils.isEmail(userLogin.getLoginUserName())) //如果输入邮箱,拿到邮箱对应的用户主ID
            {
                List<UserEmail> userEmails = userEmailService.findListByEmail(userLogin.getLoginUserName());
                if(!CollectionUtils.isEmpty(userEmails))
                {
                    if(userEmails.get(0)!=null&&userEmails.get(0).getUserMainId()!=null) {
                        userId = userEmails.get(0).getUserMainId();
                    }
                }
            }else if(UsernameUtils.isUsername(userLogin.getLoginUserName())) //如果输入用户名,拿到用户名对应的用户主ID
            {
                List<UserUserName> userUserNames = userUserNameService.findListByUserName(userLogin.getLoginUserName());
                if(!CollectionUtils.isEmpty(userUserNames))
                {
                    if(userUserNames.get(0)!=null&&userUserNames.get(0).getUserMainId()!=null) {
                        userId = userUserNames.get(0).getUserMainId();
                    }
                }
            }

            List<User> users = userService.findById(userId);
            if(CollectionUtils.isEmpty(users))
            {
                resultObjectVO.setCode(UserLoginConstant.NOT_REGIST);
                resultObjectVO.setMsg("请求失败,请先注册");
            }else {
                User userEntity = users.get(0);
                //判断用户启用状态
                if(userEntity.getEnableStatus().shortValue()==1) {
                    String pwdMd5 = MD5Util.md5(userLogin.getPassword());
                    //登录成功 生成token
                    if (pwdMd5.equals(userEntity.getPassword())) {
                        String loginTokenGroupKey = UserCenterLoginRedisKey.getLoginTokenGroupKey(String.valueOf(userEntity.getId()));
                        String loginTokenAppKey = UserCenterLoginRedisKey.getLoginTokenAppKey(String.valueOf(userEntity.getId()), requestJsonVO.getAppCode());
                        //判断是否已有登录token,如果有将删除掉
                        if (stringRedisTemplate.opsForHash().keys(loginTokenGroupKey) != null) {
                            long deleteRows = 0;
                            int tryCount = 0;
                            do {

                                deleteRows = stringRedisTemplate.opsForHash().delete(loginTokenGroupKey, loginTokenAppKey);
                                tryCount++;
                            } while (deleteRows <= 0 && tryCount < 50);
                        }

                        String token = UUID.randomUUID().toString().replace("-", "");
                        stringRedisTemplate.opsForHash().put(loginTokenGroupKey,
                                loginTokenAppKey, token);
                        //设置登录token1个小时超时
                        stringRedisTemplate.expire(loginTokenGroupKey,
                                UserCenterLoginRedisKey.LOGIN_TIMEOUT_SECOND, TimeUnit.SECONDS);

                        userLogin.setId(userId);
                        userLogin.setLoginToken(token);
                        userLogin.setPassword(null);

                        resultObjectVO.setData(userLogin);

                    }
                }else{
                    resultObjectVO.setCode(ResultVO.FAILD);
                    resultObjectVO.setMsg("请求失败,用户已被禁用");
                }
            }

        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请求失败,请稍后重试");
        }finally{
            redisLock.unLock(UserCenterLoginRedisKey.getLoginLockKey(userLogin.getLoginUserName()), userLogin.getLoginUserName());
        }

        return resultObjectVO;
    }






    /**
     * 判断是否在线
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/is/online",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO isOnline(@RequestBody RequestJsonVO requestVo) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        resultObjectVO.setData(false);
        if (requestVo.getEntityJson() == null) {
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请求失败,没有找到请求对象");
            return resultObjectVO;
        }
        try{
            String entityJson = requestVo.getEntityJson();
            UserLoginVO userLoginVO =JSONObject.parseObject(entityJson,UserLoginVO.class);
            if(userLoginVO.getId()==null)
            {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("请求失败,请传入用户ID");
                return resultObjectVO;
            }
            if(StringUtils.isEmpty(userLoginVO.getLoginToken()))
            {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("请求失败,请传入loginToken");
                return resultObjectVO;
            }
            String loginTokenGroupKey =UserCenterLoginRedisKey.getLoginTokenGroupKey(String.valueOf(userLoginVO.getId()));
            String loginTokenAppKey = UserCenterLoginRedisKey.getLoginTokenAppKey(String.valueOf(userLoginVO.getId()),requestVo.getAppCode());

            Object loginTokenObject = stringRedisTemplate.opsForHash().get(loginTokenGroupKey,loginTokenAppKey);
            if (loginTokenObject != null) {
                if(StringUtils.equals(userLoginVO.getLoginToken(),String.valueOf(loginTokenObject)))
                {
                    resultObjectVO.setData(true);
                }
            }

        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);

            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请求失败,请稍后重试");
        }
        return resultObjectVO;
    }






    /**
     * 查询列表分页
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/list",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO list(@RequestBody RequestJsonVO requestVo){
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        if(requestVo==null||requestVo.getEntityJson()==null)
        {
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请求失败,没有找到实体对象");
            return resultObjectVO;
        }

        try {
            UserPageInfo userPageInfo = JSONObject.parseObject(requestVo.getEntityJson(), UserPageInfo.class);

            if(StringUtils.isEmpty(requestVo.getAppCode()))
            {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("没有找到应用编码");
                return resultObjectVO;
            }

            List<Long> userMainIdLinkedList = new LinkedList();

            //手机号查询,先查询手机号子表
            if(StringUtils.isNotEmpty(userPageInfo.getMobilePhone())) {
                List<UserMobilePhone> userMobilePhones = userMobilePhoneService.findListByMobilePhone(userPageInfo.getMobilePhone());
                if (CollectionUtils.isNotEmpty(userMobilePhones)) {
                    for (int i = 0; i < userMobilePhones.size(); i++) {
                        userMainIdLinkedList.add(userMobilePhones.get(i).getUserMainId());
                    }
                }else if(userPageInfo.getUserMainId()!=null)
                {
                    userMainIdLinkedList.add(userPageInfo.getUserMainId());
                }else{ //如果没有匹配到数据,设置一个不存在的ID
                    userMainIdLinkedList.add(1L);
                }
            }

            if(userPageInfo.getUserMainId()!=null)
            {
                userMainIdLinkedList.add(userPageInfo.getUserMainId());
            }


            //设置查询条件
            if(CollectionUtils.isNotEmpty(userMainIdLinkedList))
            {
                Long[] userMainIdArray = new Long[userMainIdLinkedList.size()];
                userMainIdLinkedList.toArray(userMainIdArray);

                userPageInfo.setUserMainIdArray(userMainIdArray);
            }


            //查询用户主表
            PageInfo<UserVO> pageInfo =  userService.queryListPage(userPageInfo);
            if(CollectionUtils.isNotEmpty(pageInfo.getList()))
            {
                Long[] userIdArray = new Long[pageInfo.getList().size()];

                for(int i=0;i<pageInfo.getList().size();i++)
                {
                    UserVO userVo = pageInfo.getList().get(i);
                    if(userVo.getUserMainId()!=null)
                    {
                        userVo.setUserMainIdString(String.valueOf(userVo.getUserMainId()));
                    }
                    if(userVo.getId()!=null)
                    {
                        userVo.setUserId(String.valueOf(userVo.getId()));
                    }

                    userIdArray[i] = pageInfo.getList().get(i).getUserMainId();
                }

                //查询用户手机号关联表
                List<UserMobilePhone> userMobilePhones = userMobilePhoneService.queryListByUserId(userIdArray);
                if(CollectionUtils.isNotEmpty(userMobilePhones))
                {
                    //设置用户对象中的手机号
                    for(UserMobilePhone userMobilePhone:userMobilePhones)
                    {
                        for(UserVO userVO:pageInfo.getList())
                        {
                            if(userMobilePhone.getUserMainId().longValue()==userVO.getUserMainId().longValue())
                            {
                                userVO.setMobilePhone(userMobilePhone.getMobilePhone());
                                continue;
                            }
                        }
                    }
                }
                //查询用户用户名关联表
                List<UserUserName> userUserNames = userUserNameService.queryListByUserId(userIdArray);
                if(CollectionUtils.isNotEmpty(userUserNames))
                {
                    //设置用户用户名
                    for(UserUserName userUserName:userUserNames)
                    {
                        for(UserVO userVO:pageInfo.getList())
                        {
                            if(userUserName.getUserMainId().longValue()==userVO.getUserMainId().longValue())
                            {
                                userVO.setUsername(userUserName.getUsername());
                                continue;
                            }
                        }
                    }
                }


                //查询用户邮箱关联表
                List<UserEmail> userEmails = userEmailService.queryListByUserId(userIdArray);
                if(CollectionUtils.isNotEmpty(userEmails))
                {
                    //设置邮箱
                    for(UserEmail userEmail:userEmails)
                    {
                        for(UserVO userVO:pageInfo.getList())
                        {
                            if(userEmail.getUserMainId().longValue()==userVO.getUserMainId().longValue())
                            {
                                userVO.setEmail(userEmail.getEmail());
                                continue;
                            }
                        }
                    }
                }

                //查询用户与用户详情关联
                List<UserDetail> userDetails = userDetailService.queryListByUserId(userIdArray);
                if(CollectionUtils.isNotEmpty(userDetails))
                {
                    //设置用户详情
                    for(UserDetail userDetail:userDetails)
                    {
                        for(UserVO userVO:pageInfo.getList())
                        {
                            if(userDetail.getUserMainId().longValue()==userVO.getUserMainId().longValue())
                            {
                                userVO.setNickName(userDetail.getNickName());
                                userVO.setTrueName(userDetail.getTrueName());
                                userVO.setHeadSculpture(userDetail.getHeadSculpture());
                                userVO.setIdCard(userDetail.getIdCard());
                                userVO.setSex(userDetail.getSex());
                                userVO.setType(userDetail.getType());
                                continue;
                            }
                        }
                    }
                }

            }
            resultObjectVO.setData(pageInfo);

        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);

            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请求失败,请稍后重试");
        }
        return resultObjectVO;
    }




    /**
     * 用户绑定手机号列表
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/mobile/phone/list",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO mobilePhoneList(@RequestBody RequestJsonVO requestVo){
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        if(requestVo==null||requestVo.getEntityJson()==null)
        {
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请求失败,没有找到实体对象");
            return resultObjectVO;
        }

        try {
            UserPageInfo userPageInfo = JSONObject.parseObject(requestVo.getEntityJson(), UserPageInfo.class);

            if(StringUtils.isEmpty(requestVo.getAppCode()))
            {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("没有找到应用编码");
                return resultObjectVO;
            }
            if(userPageInfo.getUserMainId()==null)
            {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("没有找到用户主ID");
                return resultObjectVO;
            }

            Long[] userMainIdArray = new Long[1];
            userMainIdArray[0]=userPageInfo.getUserMainId();

            PageInfo<UserVO> pageInfo= new PageInfo<>();
            //查询用户手机号关联表
            List<UserMobilePhone> userMobilePhones = userMobilePhoneService.queryListByUserId(userMainIdArray);
            if(CollectionUtils.isNotEmpty(userMobilePhones))
            {
                pageInfo.setTotal((long)userMobilePhones.size());
                pageInfo.setList(new ArrayList<>());
                //设置用户对象中的手机号
                for(UserMobilePhone userMobilePhone:userMobilePhones)
                {
                    UserVO userVO = new UserVO();
                    userVO.setUserMainId(userMobilePhone.getUserMainId());
                    userVO.setMobilePhone(userMobilePhone.getMobilePhone());
                    pageInfo.getList().add(userVO);
                }
            }

            resultObjectVO.setData(pageInfo);

        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);

            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请求失败,请稍后重试");
        }
        return resultObjectVO;
    }





    /**
     * 禁用指定用户
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/disabled/id",produces = "application/json;charset=UTF-8",method = RequestMethod.DELETE)
    @ResponseBody
    public ResultObjectVO disabledById(@RequestBody RequestJsonVO requestVo){
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        if(requestVo==null||requestVo.getEntityJson()==null)
        {
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请求失败,没有找到实体对象");
            return resultObjectVO;
        }

        try {
            User entity = JSONObject.parseObject(requestVo.getEntityJson(),User.class);
            if(entity.getId()==null)
            {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("请求失败,没有找到ID");
                return resultObjectVO;
            }

            //用户主表禁用
            userService.updateEnableStatus((short)0,entity.getUserMainId());

            resultObjectVO.setData(entity);

        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);

            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请求失败,请稍后重试");
        }
        return resultObjectVO;
    }



}
