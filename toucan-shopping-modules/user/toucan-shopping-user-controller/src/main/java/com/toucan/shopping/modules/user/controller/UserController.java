package com.toucan.shopping.modules.user.controller;

import com.alibaba.fastjson.JSONObject;
import com.toucan.shopping.modules.user.queue.NewUserMessageQueue;
import com.toucan.shopping.modules.user.queue.UserLoginHistoryQueue;
import com.toucan.shopping.modules.user.service.UserRedisService;
import com.toucan.shopping.modules.common.generator.IdGenerator;
import com.toucan.shopping.modules.common.page.PageInfo;
import com.toucan.shopping.modules.common.properties.Toucan;
import com.toucan.shopping.modules.common.util.*;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.common.vo.ResultVO;
import com.toucan.shopping.modules.redis.service.ToucanStringRedisService;
import com.toucan.shopping.modules.skylark.lock.service.SkylarkLock;
import com.toucan.shopping.modules.user.constant.UserLoginConstant;
import com.toucan.shopping.modules.user.constant.UserRegistConstant;
import com.toucan.shopping.modules.user.entity.*;
import com.toucan.shopping.modules.user.kafka.message.UserCreateMessage;
import com.toucan.shopping.modules.user.login.cache.service.UserLoginCacheService;
import com.toucan.shopping.modules.user.page.UserPageInfo;
import com.toucan.shopping.modules.user.redis.UserCenterLoginRedisKey;
import com.toucan.shopping.modules.user.redis.UserCenterRedisKey;
import com.toucan.shopping.modules.user.redis.UserCenterRegistRedisKey;
import com.toucan.shopping.modules.user.service.*;
import com.toucan.shopping.modules.user.util.LoginTokenUtil;
import com.toucan.shopping.modules.user.vo.*;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
    private SkylarkLock skylarkLock;



    @Autowired
    private IdGenerator idGenerator;


    @Autowired
    private UserMobilePhoneService userMobilePhoneService;

    @Autowired
    private UserUserNameService userUserNameService;

    @Autowired
    private UserDetailService userDetailService;

    @Autowired
    private UserEmailService userEmailService;

    @Autowired
    private UserRedisService userRedisService;

    @Autowired
    private UserLoginCacheService userLoginCacheService;

    @Autowired
    private Toucan toucan;


    @Autowired
    private NewUserMessageQueue newUserMessageQueue;

    @Autowired
    private UserLoginHistoryQueue userLoginHistoryQueue;

    @RequestMapping(value="/find/mobile/phone",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO findByMobilePhone(@RequestBody RequestJsonVO requestJsonVO){
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        if(requestJsonVO==null)
        {
            resultObjectVO.setCode(UserRegistConstant.NOT_FOUND_USER);
            resultObjectVO.setMsg("查询失败,没有找到请求对象");
            return resultObjectVO;
        }
        if (StringUtils.isEmpty(requestJsonVO.getAppCode())) {
            resultObjectVO.setCode(UserRegistConstant.NOT_FOUND_USER);
            resultObjectVO.setMsg("查询失败,没有找到应用编码");
            return resultObjectVO;
        }
        UserRegistVO userRegistVO = JSONObject.parseObject(requestJsonVO.getEntityJson(),UserRegistVO.class);
        try {

            if(StringUtils.isEmpty(userRegistVO.getMobilePhone())||!PhoneUtils.isChinaPhoneLegal(userRegistVO.getMobilePhone()))
            {
                resultObjectVO.setCode(UserRegistConstant.MOBILE_ERROR);
                resultObjectVO.setMsg("查询失败,手机号错误");
                return resultObjectVO;
            }
            List<UserMobilePhone> userEntityList = userMobilePhoneService.findListByMobilePhone(userRegistVO.getMobilePhone());
            resultObjectVO.setData(userEntityList);
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("查询失败,请稍后重试");
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
            resultObjectVO.setMsg("没有找到要注册的用户");
            return resultObjectVO;
        }

        if (StringUtils.isEmpty(requestJsonVO.getAppCode())) {
            resultObjectVO.setCode(UserRegistConstant.NOT_FOUND_USER);
            resultObjectVO.setMsg("没有找到应用编码");
            return resultObjectVO;
        }
        UserRegistVO userRegistVO = JSONObject.parseObject(requestJsonVO.getEntityJson(),UserRegistVO.class);
        if(userRegistVO==null)
        {
            resultObjectVO.setCode(UserRegistConstant.NOT_FOUND_USER);
            resultObjectVO.setMsg("没有找到要注册的用户");
            return resultObjectVO;
        }
        if(StringUtils.isEmpty(userRegistVO.getMobilePhone()))
        {
            resultObjectVO.setCode(UserRegistConstant.NOT_FOUND_MOBILE);
            resultObjectVO.setMsg("请输入注册手机号");
            return resultObjectVO;
        }

        if(!PhoneUtils.isChinaPhoneLegal(userRegistVO.getMobilePhone()))
        {
            resultObjectVO.setCode(UserRegistConstant.MOBILE_ERROR);
            resultObjectVO.setMsg("手机号错误");
            return resultObjectVO;
        }

        if(StringUtils.isEmpty(userRegistVO.getPassword()))
        {
            resultObjectVO.setCode(UserRegistConstant.PASSWORD_NOT_FOUND);
            resultObjectVO.setMsg("请输入密码");
            return resultObjectVO;
        }

        if(!UserRegistUtil.checkPwd(userRegistVO.getPassword()))
        {
            resultObjectVO.setCode(UserRegistConstant.PASSWORD_ERROR);
            resultObjectVO.setMsg(UserRegistUtil.checkPwdFailText());
            return resultObjectVO;
        }



        try {
            boolean lockStatus = skylarkLock.lock(UserCenterRegistRedisKey.getRegistLockKey(userRegistVO.getMobilePhone()), userRegistVO.getMobilePhone());
            if (!lockStatus) {
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("请稍后重试");
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
                    resultObjectVO.setMsg("请重试!");
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
                        resultObjectVO.setMsg("手机号请重试!");
                    } else {
                        //保存用户昵称
                        UserDetail userDetail = new UserDetail();
                        userDetail.setId(idGenerator.id());
                        userDetail.setUserMainId(user.getUserMainId());
                        userDetail.setNickName("用户"+userRegistVO.getMobilePhone());
                        userDetail.setHeadSculpture(toucan.getUser().getDefaultHeadSculpture());
                        userDetail.setSex((short)1);
                        userDetail.setTrueNameStatus(0); //未实名
                        userDetail.setCreateDate(new Date());
                        userDetail.setDeleteStatus((short) 0);
                        userDetail.setIsShop((short)0); //没有店铺
                        userDetail.setIsVip((short)0); //不是会员
                        if(StringUtils.isNotEmpty(userRegistVO.getPersonalizedSignature()))
                        {
                            userDetail.setPersonalizedSignature(userRegistVO.getPersonalizedSignature());
                        }else {
                            userDetail.setPersonalizedSignature(toucan.getUser().getDefaultPersonalizedSignature()); //默认个性签名
                        }

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
                            userCreateMessage.setIsShop((short)0); //默认没有店铺
                            userCreateMessage.setIsVip((short)0); //不是会员
                            userCreateMessage.setPersonalizedSignature(userDetail.getPersonalizedSignature()); //个性签名

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
            resultObjectVO.setMsg("请稍后重试");
        }finally{
            skylarkLock.unLock(UserCenterRegistRedisKey.getRegistLockKey(userRegistVO.getMobilePhone()), userRegistVO.getMobilePhone());
        }
        return resultObjectVO;
    }


    /**
     * 更新头像
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/update/headsculpture",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO updateHeadsculpture(@RequestBody RequestJsonVO requestJsonVO)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        if(requestJsonVO==null)
        {
            logger.info("请求参数为空");
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请重试!");
            return resultObjectVO;
        }

        try {
            UserVO userVO = JSONObject.parseObject(requestJsonVO.getEntityJson(), UserVO.class);


            if(StringUtils.isEmpty(userVO.getHeadSculpture()))
            {
                logger.info("头像为空 param:"+ JSONObject.toJSONString(userVO));
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("头像不能为空!");
                return resultObjectVO;
            }


            if(userVO.getUserMainId()==null)
            {
                logger.info("用户ID为空 param:"+ JSONObject.toJSONString(userVO));
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("用户ID不能为空!");
                return resultObjectVO;
            }

            UserDetail userDetail = new UserDetail();
            userDetail.setHeadSculpture(userVO.getHeadSculpture());
            userDetail.setUserMainId(userVO.getUserMainId());

            int row = userDetailService.updateHeadSculpture(userDetail);
            if (row <=0) {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("请重试!");
                return resultObjectVO;
            }

            resultObjectVO.setData(userDetail);
            try {
                //刷新用户信息到登录缓存
                userRedisService.flushLoginCache(String.valueOf(userVO.getUserMainId()), userVO.getAppCode());
            }catch(Exception e)
            {
                logger.warn("刷新redis登录缓存失败 {}", requestJsonVO.getEntityJson());
                logger.warn(e.getMessage(),e);
            }

        }catch(Exception e)
        {
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请重试!");
            logger.warn(e.getMessage(),e);
        }
        return resultObjectVO;
    }


    @RequestMapping(value="/reset/password",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO resetPassword(@RequestBody RequestJsonVO requestJsonVO){
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        if(requestJsonVO==null)
        {
            resultObjectVO.setCode(UserRegistConstant.NOT_FOUND_USER);
            resultObjectVO.setMsg("重置失败,没有找到要重置的用户");
            return resultObjectVO;
        }

        if (StringUtils.isEmpty(requestJsonVO.getAppCode())) {
            resultObjectVO.setCode(UserRegistConstant.NOT_FOUND_USER);
            resultObjectVO.setMsg("重置失败,没有找到应用编码");
            return resultObjectVO;
        }
        UserRegistVO userRegistVO = JSONObject.parseObject(requestJsonVO.getEntityJson(),UserRegistVO.class);
        if(userRegistVO==null)
        {
            resultObjectVO.setCode(UserRegistConstant.NOT_FOUND_USER);
            resultObjectVO.setMsg("重置失败,没有找到要重置的用户");
            return resultObjectVO;
        }

        if(userRegistVO.getUserMainId()==null)
        {
            resultObjectVO.setCode(UserRegistConstant.PASSWORD_NOT_FOUND);
            resultObjectVO.setMsg("重置失败,用户ID为空");
            return resultObjectVO;
        }
        if(StringUtils.isEmpty(userRegistVO.getPassword()))
        {
            resultObjectVO.setCode(UserRegistConstant.PASSWORD_NOT_FOUND);
            resultObjectVO.setMsg("重置失败,请输入密码");
            return resultObjectVO;
        }

        if(!UserRegistUtil.checkPwd(userRegistVO.getPassword()))
        {
            resultObjectVO.setCode(UserRegistConstant.PASSWORD_ERROR);
            resultObjectVO.setMsg("重置失败,"+UserRegistUtil.checkPwdFailText());
            return resultObjectVO;
        }



        String userMainId = String.valueOf(userRegistVO.getUserMainId());
        try {
            boolean lockStatus = skylarkLock.lock(UserCenterRegistRedisKey.getResetPasswordLockKey(userMainId), userMainId);
            if (!lockStatus) {
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("请求超时,请稍后重试");
                return resultObjectVO;
            }

            String password = MD5Util.md5(userRegistVO.getPassword());

            int row = userService.updatePasswordByUserMainId(userRegistVO.getUserMainId(),password);
            if(row<1)
            {
                logger.warn("修改密码失败 {}", requestJsonVO.getEntityJson());
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("重置失败,请稍后重试");
            }
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("重置失败,请稍后重试");
        }finally{
            skylarkLock.unLock(UserCenterRegistRedisKey.getResetPasswordLockKey(userMainId), userMainId);
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
            resultObjectVO.setMsg("关联失败,没有找到参数");
            return resultObjectVO;
        }

        if (StringUtils.isEmpty(requestJsonVO.getAppCode())) {
            resultObjectVO.setCode(UserRegistConstant.NOT_FOUND_USER);
            resultObjectVO.setMsg("关联失败,没有找到应用编码");
            return resultObjectVO;
        }
        UserRegistVO userRegistVO = JSONObject.parseObject(requestJsonVO.getEntityJson(),UserRegistVO.class);
        if(userRegistVO==null)
        {
            resultObjectVO.setCode(UserRegistConstant.NOT_FOUND_USER);
            resultObjectVO.setMsg("关联失败,没有找到参数");
            return resultObjectVO;
        }
        if(StringUtils.isEmpty(userRegistVO.getUsername()))
        {
            resultObjectVO.setCode(UserRegistConstant.NOT_FOUND_MOBILE);
            resultObjectVO.setMsg("关联失败,请输入用户名");
            return resultObjectVO;
        }

        if(!UsernameUtils.isUsername(userRegistVO.getUsername()))
        {
            resultObjectVO.setCode(UserRegistConstant.MOBILE_ERROR);
            resultObjectVO.setMsg("关联失败,用户名格式错误");
            return resultObjectVO;
        }

        if(userRegistVO.getUserMainId()==null)
        {
            resultObjectVO.setCode(UserRegistConstant.NOT_FOUND_USER);
            resultObjectVO.setMsg("关联失败,没有找到要用户主ID");
            return resultObjectVO;
        }

        try {
            boolean lockStatus = skylarkLock.lock(UserCenterRegistRedisKey.getBindUserNameLock(userRegistVO.getUsername()), userRegistVO.getUsername());
            if (!lockStatus) {
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("请求超时,请稍后重试");
                return resultObjectVO;
            }
            //查询用户名是否已关联
            UserUserName query = new UserUserName();
            query.setUsername(userRegistVO.getUsername());
            List<UserUserName> userUserNames = userUserNameService.findListByEntity(query);
            if (!CollectionUtils.isEmpty(userUserNames)) {
                for(UserUserName userUserName:userUserNames)
                {
                    if(userUserName.getUserMainId().longValue()==userRegistVO.getUserMainId().longValue())
                    {
                        resultObjectVO.setCode(UserResultVO.FAILD);
                        resultObjectVO.setMsg("绑定失败，用户名已经绑定到该用户了!");
                        return resultObjectVO;
                    }
                }
                resultObjectVO.setCode(UserResultVO.FAILD);
                resultObjectVO.setMsg("绑定失败，用户名已被绑定!");
                return resultObjectVO;
            }
            query = new UserUserName();
            query.setUserMainId(userRegistVO.getUserMainId());
            userUserNames = userUserNameService.findListByEntity(query);
            if(CollectionUtils.isNotEmpty(userUserNames))
            {
                resultObjectVO.setCode(UserResultVO.FAILD);
                resultObjectVO.setMsg("绑定失败，该用户已存在使用中用户名!");
                return resultObjectVO;
            }

            //保存用户和用户名关联
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
                resultObjectVO.setMsg("关联失败,请重试!");
            }else{
                try {
                    //刷新用户信息到登录缓存
                    userRedisService.flushLoginCache(String.valueOf(userRegistVO.getUserMainId()), userRegistVO.getAppCode());
                }catch(Exception e)
                {
                    logger.warn("刷新redis登录缓存失败 {}", requestJsonVO.getEntityJson());
                    logger.warn(e.getMessage(),e);
                }
            }
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("关联失败,请稍后重试");
        }finally{
            skylarkLock.unLock(UserCenterRegistRedisKey.getBindUserNameLock(userRegistVO.getUsername()), userRegistVO.getUsername());
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
            resultObjectVO.setMsg("查询失败,没有找到要关联的用户");
            return resultObjectVO;
        }

        if (StringUtils.isEmpty(requestJsonVO.getAppCode())) {
            resultObjectVO.setCode(UserRegistConstant.NOT_FOUND_USER);
            resultObjectVO.setMsg("查询失败,没有找到应用编码");
            return resultObjectVO;
        }
        UserRegistVO userRegistVO = JSONObject.parseObject(requestJsonVO.getEntityJson(),UserRegistVO.class);
        if(userRegistVO==null)
        {
            resultObjectVO.setCode(UserRegistConstant.NOT_FOUND_USER);
            resultObjectVO.setMsg("查询失败,没有找到要注册的用户");
            return resultObjectVO;
        }
        if(StringUtils.isEmpty(userRegistVO.getUsername()))
        {
            resultObjectVO.setCode(UserRegistConstant.NOT_FOUND_MOBILE);
            resultObjectVO.setMsg("查询失败,请输入用户名");
            return resultObjectVO;
        }

        if(!UsernameUtils.isUsername(userRegistVO.getUsername()))
        {
            resultObjectVO.setCode(UserRegistConstant.MOBILE_ERROR);
            resultObjectVO.setMsg("查询失败,用户名格式错误");
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
            resultObjectVO.setMsg("查询失败,请稍后重试");
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
            resultObjectVO.setMsg("关联失败,没有找到参数");
            return resultObjectVO;
        }

        if (StringUtils.isEmpty(requestJsonVO.getAppCode())) {
            resultObjectVO.setCode(UserRegistConstant.NOT_FOUND_USER);
            resultObjectVO.setMsg("关联失败,没有找到应用编码");
            return resultObjectVO;
        }
        UserRegistVO userRegistVO = JSONObject.parseObject(requestJsonVO.getEntityJson(),UserRegistVO.class);
        if(userRegistVO==null)
        {
            resultObjectVO.setCode(UserRegistConstant.NOT_FOUND_USER);
            resultObjectVO.setMsg("关联失败,没有找到参数");
            return resultObjectVO;
        }
        if(StringUtils.isEmpty(userRegistVO.getEmail()))
        {
            resultObjectVO.setCode(UserRegistConstant.NOT_FOUND_MOBILE);
            resultObjectVO.setMsg("关联失败,请输入邮箱");
            return resultObjectVO;
        }

        if(!EmailUtils.isEmail(userRegistVO.getEmail()))
        {
            resultObjectVO.setCode(UserRegistConstant.MOBILE_ERROR);
            resultObjectVO.setMsg("关联失败,邮箱格式错误");
            return resultObjectVO;
        }

        if(userRegistVO.getUserMainId()==null)
        {
            resultObjectVO.setCode(UserRegistConstant.NOT_FOUND_USER);
            resultObjectVO.setMsg("关联失败,没有找到要用户主ID");
            return resultObjectVO;
        }

        try {
            boolean lockStatus = skylarkLock.lock(UserCenterRegistRedisKey.getBindEmailLock(String.valueOf(userRegistVO.getUserMainId())), String.valueOf(userRegistVO.getUserMainId()));
            if (!lockStatus) {
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("请求超时,请稍后重试");
                return resultObjectVO;
            }
            //查询邮箱是否已关联
            UserEmail query = new UserEmail();
            query.setEmail(userRegistVO.getEmail());
            List<UserEmail> userEmails = userEmailService.findListByEntity(query);
            if (!CollectionUtils.isEmpty(userEmails)) {
                for(UserEmail userEmail:userEmails)
                {
                    if(userEmail.getUserMainId().longValue()==userRegistVO.getUserMainId().longValue())
                    {
                        resultObjectVO.setCode(UserResultVO.FAILD);
                        resultObjectVO.setMsg("绑定失败，邮箱已经绑定到该用户了!");
                        return resultObjectVO;
                    }
                }
                resultObjectVO.setCode(UserResultVO.FAILD);
                resultObjectVO.setMsg("绑定失败，邮箱已被绑定!");
                return resultObjectVO;
            }
            query = new UserEmail();
            query.setUserMainId(userRegistVO.getUserMainId());
            userEmails = userEmailService.findListByEntity(query);
            if(CollectionUtils.isNotEmpty(userEmails))
            {
                resultObjectVO.setCode(UserResultVO.FAILD);
                resultObjectVO.setMsg("绑定失败，该用户已存在使用中邮箱!");
                return resultObjectVO;
            }

            //保存用户邮箱绑定
            UserEmail userEmail = new UserEmail();
            userEmail.setId(idGenerator.id());
            //设置邮箱
            userEmail.setEmail(userRegistVO.getEmail());
            //设置用户主表ID
            userEmail.setUserMainId(userRegistVO.getUserMainId());
            userEmail.setCreateDate(new Date());
            userEmail.setDeleteStatus((short) 0);

            int row = userEmailService.save(userEmail);
            if (row < 1) {
                logger.warn("绑定邮箱失败 {}", requestJsonVO.getEntityJson());
                resultObjectVO.setCode(UserResultVO.FAILD);
                resultObjectVO.setMsg("绑定失败,请重试!");
            }else{
                try {
                    //刷新用户信息到登录缓存
                    userRedisService.flushLoginCache(String.valueOf(userRegistVO.getUserMainId()), userRegistVO.getAppCode());
                }catch(Exception e)
                {
                    logger.warn("刷新redis登录缓存失败 {}", requestJsonVO.getEntityJson());
                    logger.warn(e.getMessage(),e);
                }
            }

        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("绑定失败,请稍后重试");
        }finally{
            skylarkLock.unLock(UserCenterRegistRedisKey.getBindEmailLock(String.valueOf(userRegistVO.getUserMainId())), String.valueOf(userRegistVO.getUserMainId()));
        }
        return resultObjectVO;
    }


    /**
     * 更新关联邮箱
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/update/connect/email",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO updateConnectEmail(@RequestBody RequestJsonVO requestJsonVO){
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        if(requestJsonVO==null)
        {
            resultObjectVO.setCode(UserRegistConstant.NOT_FOUND_USER);
            resultObjectVO.setMsg("关联失败,没有找到参数");
            return resultObjectVO;
        }

        if (StringUtils.isEmpty(requestJsonVO.getAppCode())) {
            resultObjectVO.setCode(UserRegistConstant.NOT_FOUND_USER);
            resultObjectVO.setMsg("关联失败,没有找到应用编码");
            return resultObjectVO;
        }
        UserBindEmailVO userBindEmailVO = JSONObject.parseObject(requestJsonVO.getEntityJson(),UserBindEmailVO.class);
        if(userBindEmailVO==null)
        {
            resultObjectVO.setCode(UserRegistConstant.NOT_FOUND_USER);
            resultObjectVO.setMsg("关联失败,没有找到参数");
            return resultObjectVO;
        }
        if(StringUtils.isEmpty(userBindEmailVO.getEmail()))
        {
            resultObjectVO.setCode(UserRegistConstant.NOT_FOUND_MOBILE);
            resultObjectVO.setMsg("关联失败,请输入邮箱");
            return resultObjectVO;
        }

        if(!EmailUtils.isEmail(userBindEmailVO.getEmail()))
        {
            resultObjectVO.setCode(UserRegistConstant.MOBILE_ERROR);
            resultObjectVO.setMsg("关联失败,邮箱格式错误");
            return resultObjectVO;
        }

        if(userBindEmailVO.getUserMainId()==null)
        {
            resultObjectVO.setCode(UserRegistConstant.NOT_FOUND_USER);
            resultObjectVO.setMsg("关联失败,没有找到要用户主ID");
            return resultObjectVO;
        }

        try {
            boolean lockStatus = skylarkLock.lock(UserCenterRegistRedisKey.getBindEmailLock(String.valueOf(userBindEmailVO.getUserMainId())), String.valueOf(userBindEmailVO.getUserMainId()));
            if (!lockStatus) {
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("请求超时,请稍后重试");
                return resultObjectVO;
            }

            List<UserEmail> userEmails = userEmailService.findListByEmail(userBindEmailVO.getEmail());
            if (!CollectionUtils.isEmpty(userEmails)) {
                for(UserEmail userEmail:userEmails)
                {
                    if(userEmail.getUserMainId().longValue()==userBindEmailVO.getUserMainId().longValue())
                    {
                        resultObjectVO.setCode(UserResultVO.FAILD);
                        resultObjectVO.setMsg("绑定失败，邮箱已经绑定到该用户了!");
                        return resultObjectVO;
                    }
                }
                resultObjectVO.setCode(UserResultVO.FAILD);
                resultObjectVO.setMsg("绑定失败，邮箱已被绑定!");
                return resultObjectVO;
            }

            //清空当前绑定的邮箱
            userEmailService.deleteByUserMainId(userBindEmailVO.getUserMainId());

            //保存用户邮箱绑定
            UserEmail userEmail = new UserEmail();
            userEmail.setId(idGenerator.id());
            //设置邮箱
            userEmail.setEmail(userBindEmailVO.getEmail());
            //设置用户主表ID
            userEmail.setUserMainId(userBindEmailVO.getUserMainId());
            userEmail.setCreateDate(new Date());
            userEmail.setDeleteStatus((short) 0);

            int row = userEmailService.save(userEmail);
            if (row < 1) {
                logger.warn("绑定邮箱失败 {}", requestJsonVO.getEntityJson());
                resultObjectVO.setCode(UserResultVO.FAILD);
                resultObjectVO.setMsg("绑定失败,请稍后重试!");
            }else{
                try {
                    //刷新用户信息到登录缓存
                    userRedisService.flushLoginCache(String.valueOf(userBindEmailVO.getUserMainId()), userBindEmailVO.getAppCode());
                }catch(Exception e)
                {
                    logger.warn("刷新redis登录缓存失败 {}", requestJsonVO.getEntityJson());
                    logger.warn(e.getMessage(),e);
                }
            }

        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("绑定失败,请稍后重试");
        }finally{
            skylarkLock.unLock(UserCenterRegistRedisKey.getBindEmailLock(String.valueOf(userBindEmailVO.getUserMainId())), String.valueOf(userBindEmailVO.getUserMainId()));
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
            resultObjectVO.setMsg("查询失败,没有找到要关联的用户");
            return resultObjectVO;
        }

        if (StringUtils.isEmpty(requestJsonVO.getAppCode())) {
            resultObjectVO.setCode(UserRegistConstant.NOT_FOUND_USER);
            resultObjectVO.setMsg("查询失败,没有找到应用编码");
            return resultObjectVO;
        }
        UserRegistVO userRegistVO = JSONObject.parseObject(requestJsonVO.getEntityJson(),UserRegistVO.class);
        if(userRegistVO==null)
        {
            resultObjectVO.setCode(UserRegistConstant.NOT_FOUND_USER);
            resultObjectVO.setMsg("查询失败,没有找到要注册的用户");
            return resultObjectVO;
        }
        if(StringUtils.isEmpty(userRegistVO.getEmail()))
        {
            resultObjectVO.setCode(UserRegistConstant.NOT_FOUND_MOBILE);
            resultObjectVO.setMsg("查询失败,请输入邮箱");
            return resultObjectVO;
        }

        if(!EmailUtils.isEmail(userRegistVO.getEmail()))
        {
            resultObjectVO.setCode(UserRegistConstant.MOBILE_ERROR);
            resultObjectVO.setMsg("查询失败,邮箱格式错误");
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
            resultObjectVO.setMsg("查询失败,请稍后重试");
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
            resultObjectVO.setMsg("修改失败,没有找到要操作的用户");
            return resultObjectVO;
        }

        if (StringUtils.isEmpty(requestJsonVO.getAppCode())) {
            resultObjectVO.setCode(UserRegistConstant.NOT_FOUND_USER);
            resultObjectVO.setMsg("修改失败,没有找到应用编码");
            return resultObjectVO;
        }
        UserVO userVO = JSONObject.parseObject(requestJsonVO.getEntityJson(),UserVO.class);
        if(userVO==null)
        {
            resultObjectVO.setCode(UserRegistConstant.NOT_FOUND_USER);
            resultObjectVO.setMsg("修改失败,没有找到要修改的用户");
            return resultObjectVO;
        }
        if(userVO.getUserMainId()==null)
        {
            resultObjectVO.setCode(UserRegistConstant.NOT_FOUND_USER);
            resultObjectVO.setMsg("修改失败,没有找到要用户ID");
            return resultObjectVO;
        }
        try {
            boolean lockStatus = skylarkLock.lock(UserCenterRegistRedisKey.getUpdateDetailLockKey(String.valueOf(userVO.getUserMainId())), String.valueOf(userVO.getUserMainId()));
            if (!lockStatus) {
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("请求超时,请稍后重试");
                return resultObjectVO;
            }
            //判断这个昵称是否被占用,如果出现这个昵称两个人使用的情况就判断这两个人如果有一个人是这个用户,还允许使用
            UserDetail query = new UserDetail();
            List<UserDetail> userDetails = null;
            if(StringUtils.isNotEmpty(userVO.getNickName())) {
                query.setNickName(userVO.getNickName());
                userDetails = userDetailService.findListByEntity(query);
                if (CollectionUtils.isNotEmpty(userDetails)) {
                    if (userDetails.size() == 1) {
                        if (userVO.getUserMainId().longValue() != userDetails.get(0).getUserMainId().longValue()) {
                            skylarkLock.unLock(UserCenterRegistRedisKey.getUpdateDetailLockKey(String.valueOf(userVO.getUserMainId())), String.valueOf(userVO.getUserMainId()));
                            resultObjectVO.setCode(ResultObjectVO.FAILD);
                            resultObjectVO.setMsg("该昵称已被占用");
                            return resultObjectVO;
                        }
                    } else {
                        //判断如果出现了一个昵称两个人使用的话,当前用户如果使用了这个昵称,可以接着使用
                        boolean currentUserIsUseNickName = false;
                        for (UserDetail userDetail : userDetails) {
                            if (userDetail.getUserMainId().longValue() == userVO.getUserMainId().longValue()) {
                                currentUserIsUseNickName = true;
                            }
                        }
                        if (!currentUserIsUseNickName) {
                            skylarkLock.unLock(UserCenterRegistRedisKey.getUpdateDetailLockKey(String.valueOf(userVO.getUserMainId())), String.valueOf(userVO.getUserMainId()));
                            resultObjectVO.setCode(ResultObjectVO.FAILD);
                            resultObjectVO.setMsg("该昵称已被占用");
                            return resultObjectVO;
                        }
                    }
                }
            }
            query = new UserDetail();
            query.setUserMainId(userVO.getUserMainId());
            userDetails = userDetailService.findListByEntity(query);
            int row = 0;
            if(CollectionUtils.isNotEmpty(userDetails))
            {
                UserDetail userDetail = userDetails.get(0);
                BeanUtils.copyProperties(userDetail,userVO);
                row= userDetailService.update(userDetail);
            }else{
                UserDetail userDetail = new UserDetail();
                BeanUtils.copyProperties(userDetail,userVO);
                userDetail.setId(idGenerator.id());
                userDetail.setCreateDate(new Date());
                userDetail.setDeleteStatus((short)0);

                row= userDetailService.save(userDetail);
            }

            if(row<=0)
            {
                logger.warn("修改用户详情失败 {}", requestJsonVO.getEntityJson());
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("修改失败,请稍后重试");
            }else{
                try {
                    //刷新用户信息到登录缓存
                    userRedisService.flushLoginCache(String.valueOf(userVO.getUserMainId()), userVO.getAppCode());
                }catch(Exception e)
                {
                    logger.warn("刷新redis登录缓存失败 {}", requestJsonVO.getEntityJson());
                    logger.warn(e.getMessage(),e);
                }
            }
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("修改失败,请稍后重试");
        }finally{
            skylarkLock.unLock(UserCenterRegistRedisKey.getUpdateDetailLockKey(String.valueOf(userVO.getUserMainId())), String.valueOf(userVO.getUserMainId()));
        }
        return resultObjectVO;
    }


    /**
     * 字段值过滤
     * @param userVO
     * @throws Exception
     */
    void executeFieldValueFilter(UserVO userVO) throws Exception
    {
        //过滤空格
        BlankSpaceUtils.replaceBlankSpace(userVO);
    }


    /**
     * 修改用户信息
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/edit/info",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO editInfo(@RequestBody RequestJsonVO requestJsonVO){
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        if(requestJsonVO==null)
        {
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            resultObjectVO.setMsg("修改失败,没有找到要操作的用户");
            return resultObjectVO;
        }

        if (StringUtils.isEmpty(requestJsonVO.getAppCode())) {
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            resultObjectVO.setMsg("修改失败,没有找到应用编码");
            return resultObjectVO;
        }
        UserVO userVO = JSONObject.parseObject(requestJsonVO.getEntityJson(),UserVO.class);
        if(userVO==null)
        {
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            resultObjectVO.setMsg("修改失败,没有找到要修改的用户");
            return resultObjectVO;
        }
        if(userVO.getUserMainId()==null)
        {
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            resultObjectVO.setMsg("修改失败,没有找到要用户ID");
            return resultObjectVO;
        }
        if(StringUtils.isEmpty(userVO.getNickName()))
        {
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            resultObjectVO.setMsg("修改失败,昵称不能为空");
            return resultObjectVO;
        }
        if(!NicknameUtils.isNickname(userVO.getNickName()))
        {
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            resultObjectVO.setMsg("修改失败,昵称不合法,不能以数字开头,可以是汉字、字母下划线组合");
            return resultObjectVO;
        }
        try {
            boolean lockStatus = skylarkLock.lock(UserCenterRedisKey.getEditInfoLockKey(String.valueOf(userVO.getUserMainId())), String.valueOf(userVO.getUserMainId()));
            if (!lockStatus) {
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("请求超时,请稍后重试");
                return resultObjectVO;
            }
            //过滤字段非法值
            this.executeFieldValueFilter(userVO);

            //判断这个昵称是否被占用,如果出现这个昵称两个人使用的情况就判断这两个人如果有一个人是这个用户,还允许使用
            UserDetail query = new UserDetail();
            query.setNickName(userVO.getNickName());
            List<UserDetail> userDetails = userDetailService.findListByEntity(query);
            if(CollectionUtils.isNotEmpty(userDetails))
            {
                if(userDetails.size()==1)
                {
                    if(userVO.getUserMainId().longValue()!=userDetails.get(0).getUserMainId().longValue())
                    {
                        skylarkLock.unLock(UserCenterRedisKey.getEditInfoLockKey(String.valueOf(userVO.getUserMainId())), String.valueOf(userVO.getUserMainId()));
                        resultObjectVO.setCode(ResultObjectVO.FAILD);
                        resultObjectVO.setMsg("该昵称已被占用");
                        return resultObjectVO;
                    }
                }else {
                    //判断如果出现了一个昵称两个人使用的话,当前用户如果使用了这个昵称,可以接着使用
                    boolean currentUserIsUseNickName=false;
                    for (UserDetail userDetail : userDetails) {
                        if(userDetail.getUserMainId().longValue()==userVO.getUserMainId().longValue())
                        {
                            currentUserIsUseNickName=true;
                        }
                    }
                    if(!currentUserIsUseNickName)
                    {
                        skylarkLock.unLock(UserCenterRedisKey.getEditInfoLockKey(String.valueOf(userVO.getUserMainId())), String.valueOf(userVO.getUserMainId()));
                        resultObjectVO.setCode(ResultObjectVO.FAILD);
                        resultObjectVO.setMsg("该昵称已被占用");
                        return resultObjectVO;
                    }
                }
            }
            query = new UserDetail();
            query.setUserMainId(userVO.getUserMainId());
            userDetails = userDetailService.findListByEntity(query);
            int row = 0;
            if(CollectionUtils.isNotEmpty(userDetails))
            {
                UserDetail userDetail = userDetails.get(0);
                userDetail.setNickName(userVO.getNickName()); //昵称
                userDetail.setSex(userVO.getSex()); //性别
                userDetail.setPersonalizedSignature(userVO.getPersonalizedSignature()); //个性签名
                row= userDetailService.updateInfo(userDetail);
            }else{
                UserDetail userDetail = new UserDetail();
                userDetail.setId(idGenerator.id());
                userDetail.setUserMainId(userVO.getUserMainId()); //用户ID
                userDetail.setNickName(userVO.getNickName()); //昵称
                userDetail.setSex(userVO.getSex()); //性别
                userDetail.setPersonalizedSignature(toucan.getUser().getDefaultPersonalizedSignature());
                userDetail.setCreateDate(new Date());
                userDetail.setDeleteStatus((short)0);
                userDetail.setIsShop((short)0);
                userDetail.setIsVip((short)0);


                row= userDetailService.save(userDetail);
            }

            if(row<=0)
            {
                logger.warn("修改用户详情失败 {}", requestJsonVO.getEntityJson());
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("修改失败,请稍后重试");
            }else{
                try {
                    //刷新用户信息到登录缓存
                    userRedisService.flushLoginCache(String.valueOf(userVO.getUserMainId()), userVO.getAppCode());
                }catch(Exception e)
                {
                    logger.warn("刷新redis登录缓存失败 {}", requestJsonVO.getEntityJson());
                    logger.warn(e.getMessage(),e);
                }
            }
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("修改失败,请稍后重试");
        }finally{
            skylarkLock.unLock(UserCenterRedisKey.getEditInfoLockKey(String.valueOf(userVO.getUserMainId())), String.valueOf(userVO.getUserMainId()));
        }
        return resultObjectVO;
    }





    /**
     * 修改用户是否存在店铺
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/update/is/shop",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO updateIsShop(@RequestBody RequestJsonVO requestJsonVO){
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        if(requestJsonVO==null)
        {
            resultObjectVO.setCode(UserRegistConstant.NOT_FOUND_USER);
            resultObjectVO.setMsg("修改失败,没有找到要操作的用户");
            return resultObjectVO;
        }

        if (StringUtils.isEmpty(requestJsonVO.getAppCode())) {
            resultObjectVO.setCode(UserRegistConstant.NOT_FOUND_USER);
            resultObjectVO.setMsg("修改失败,没有找到应用编码");
            return resultObjectVO;
        }
        UserVO userVO = JSONObject.parseObject(requestJsonVO.getEntityJson(),UserVO.class);
        if(userVO==null)
        {
            resultObjectVO.setCode(UserRegistConstant.NOT_FOUND_USER);
            resultObjectVO.setMsg("修改失败,没有找到要修改的用户");
            return resultObjectVO;
        }
        if(userVO.getUserMainId()==null)
        {
            resultObjectVO.setCode(UserRegistConstant.NOT_FOUND_USER);
            resultObjectVO.setMsg("修改失败,没有找到要用户ID");
            return resultObjectVO;
        }
        try {
            boolean lockStatus = skylarkLock.lock(UserCenterRedisKey.getEditInfoLockKey(String.valueOf(userVO.getUserMainId())), String.valueOf(userVO.getUserMainId()));
            if (!lockStatus) {
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("请求超时,请稍后重试");
                return resultObjectVO;
            }

            UserDetail query = new UserDetail();
            query.setUserMainId(userVO.getUserMainId());
            List<UserDetail> userDetails = userDetailService.findListByEntity(query);
            int row = 0;
            if(CollectionUtils.isNotEmpty(userDetails))
            {
                UserDetail userDetail = userDetails.get(0);
                userDetail.setIsShop(userVO.getIsShop());
                row= userDetailService.updateIsShop(userDetail);
            }

            if(row<=0)
            {
                logger.warn("修改用户是否有店铺失败 {}", requestJsonVO.getEntityJson());
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("修改失败,请稍后重试");
            }else{
                try {
                    //刷新用户信息到登录缓存
                    userRedisService.flushLoginCache(String.valueOf(userVO.getUserMainId()), userVO.getAppCode());
                }catch(Exception e)
                {
                    logger.warn("刷新redis登录缓存失败 {}", requestJsonVO.getEntityJson());
                    logger.warn(e.getMessage(),e);
                }
            }
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("修改失败,请稍后重试");
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
            resultObjectVO.setMsg("登录失败,没有找到要登录的用户");
            return resultObjectVO;
        }
        if (StringUtils.isEmpty(requestJsonVO.getAppCode())) {
            resultObjectVO.setCode(UserLoginConstant.NOT_FOUND_USER);
            resultObjectVO.setMsg("登录失败,没有找到应用编码");
            return resultObjectVO;
        }

        UserLoginVO userLogin = JSONObject.parseObject(requestJsonVO.getEntityJson(),UserLoginVO.class);
        if(userLogin==null)
        {
            resultObjectVO.setCode(UserLoginConstant.NOT_FOUND_USER);
            resultObjectVO.setMsg("登录失败,没有找到账号");
            return resultObjectVO;
        }
        logger.info(" 用户登录 {} ",requestJsonVO.getEntityJson());

        if (StringUtils.isEmpty(userLogin.getPassword())) {
            resultObjectVO.setCode(UserLoginConstant.PASSWORD_NOT_FOUND);
            resultObjectVO.setMsg("登录失败,请输入密码");
            return resultObjectVO;
        }
        if(StringUtils.isEmpty(userLogin.getLoginUserName()))
        {
            resultObjectVO.setCode(UserLoginConstant.USERNAME_NOT_FOUND);
            resultObjectVO.setMsg("登录失败,请输入账号");
            return resultObjectVO;
        }

        try {
            boolean lockStatus = skylarkLock.lock(UserCenterLoginRedisKey.getLoginLockKey(userLogin.getLoginUserName()), userLogin.getLoginUserName());
            if (!lockStatus) {
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("请求超时,请稍后重试");
                return resultObjectVO;
            }

            //如果当前输入的是手机号判断手机号是否存在
            Long userId = 0L;

            User userEntity=null;
            if(PhoneUtils.isChinaPhoneLegal(userLogin.getLoginUserName()))
            {
                List<UserMobilePhone> userMobilePhones = userMobilePhoneService.findListByMobilePhone(userLogin.getLoginUserName());
                if (!CollectionUtils.isEmpty(userMobilePhones)) {
                    if (userMobilePhones.get(0) != null && userMobilePhones.get(0).getUserMainId() != null) {
                        userId = userMobilePhones.get(0).getUserMainId();
                    }
                }

            }else if(EmailUtils.isEmail(userLogin.getLoginUserName())) //如果输入邮箱,拿到邮箱对应的用户主ID
            {
                List<UserEmail> userEmails = userEmailService.findListByEmail(userLogin.getLoginUserName());
                if (!CollectionUtils.isEmpty(userEmails)) {
                    if (userEmails.get(0) != null && userEmails.get(0).getUserMainId() != null) {
                        userId = userEmails.get(0).getUserMainId();
                    }
                }
            }else if(UsernameUtils.isUsername(userLogin.getLoginUserName())) //如果输入用户名,拿到用户名对应的用户主ID
            {
                List<UserUserName> userUserNames = userUserNameService.findListByUserName(userLogin.getLoginUserName());
                if (!CollectionUtils.isEmpty(userUserNames)) {
                    if (userUserNames.get(0) != null && userUserNames.get(0).getUserMainId() != null) {
                        userId = userUserNames.get(0).getUserMainId();
                    }
                }
            }

            List<User> users = userService.findByUserMainId(userId);
            if (CollectionUtils.isEmpty(users)) {
                resultObjectVO.setCode(UserLoginConstant.NOT_REGIST);
                resultObjectVO.setMsg("登录失败,没有找到该账号");

                //释放锁
                skylarkLock.unLock(UserCenterLoginRedisKey.getLoginLockKey(userLogin.getLoginUserName()), userLogin.getLoginUserName());
                return resultObjectVO;
            } else {
                userEntity = users.get(0);
            }



            //判断用户启用状态
            if (userEntity.getEnableStatus().shortValue() == 1) {
                String pwdMd5 = MD5Util.md5(userLogin.getPassword());
                //登录成功 生成token
                if (pwdMd5.equals(userEntity.getPassword())) {
                    String loginGroupKey = UserCenterLoginRedisKey.getLoginInfoGroupKey(String.valueOf(userEntity.getUserMainId()));
                    String loginTokenAppKey = UserCenterLoginRedisKey.getLoginTokenAppKey(String.valueOf(userEntity.getUserMainId()), requestJsonVO.getAppCode());
                    String loginInfoAppKey = UserCenterLoginRedisKey.getLoginInfoAppKey(String.valueOf(userEntity.getUserMainId()), requestJsonVO.getAppCode());

                    UserVO userVO = new UserVO();
                    userVO.setUserMainId(userEntity.getUserMainId());
                    ToucanStringRedisService toucanStringRedisService = userLoginCacheService.routeToucanRedisService(userVO);
                    //判断是否已有登录token,如果有将删除掉
                    if (toucanStringRedisService.keys(loginGroupKey) != null) {
                        long deleteRows = 0;
                        int tryCount = 0;
                        do {
                            //只删除这个应用的会话
                            deleteRows = toucanStringRedisService.delete(loginGroupKey, loginTokenAppKey);
                            tryCount++;
                        } while (deleteRows <= 0 && tryCount < 5);
                    }
                    //判断是否有登录信息,如果有就删除掉
                    if (toucanStringRedisService.keys(loginGroupKey) != null) {
                        long deleteRows = 0;
                        int tryCount = 0;
                        do {
                            //只删除这个应用的登录信息缓存
                            deleteRows = toucanStringRedisService.delete(loginGroupKey, loginInfoAppKey);
                            tryCount++;
                        } while (deleteRows <= 0 && tryCount < 5);
                    }

                    String token = LoginTokenUtil.generatorToken(userEntity.getUserMainId());
                    toucanStringRedisService.put(loginGroupKey,loginTokenAppKey, token);

                    //设置登录token超时
                    toucanStringRedisService.expire(loginGroupKey,
                            UserCenterLoginRedisKey.LOGIN_TIMEOUT_SECOND, TimeUnit.SECONDS);

                    //刷新用户信息到登录缓存
                    userRedisService.flushLoginCache(String.valueOf(userEntity.getUserMainId()),requestJsonVO.getAppCode());


                    userLogin.setPassword(null);
                    userLogin.setLoginToken(token);
                    userLogin.setLoginAuthToken(LoginTokenUtil.generatorTokenByString(token));
                    userLogin.setUserMainId(userEntity.getUserMainId());

                    resultObjectVO.setData(userLogin);

                    try{

                        //保存登录信息
                        UserLoginHistoryVO userLoginHistoryVO = new UserLoginHistoryVO();
                        userLoginHistoryVO.setId(idGenerator.id());
                        userLoginHistoryVO.setUserMainId(userEntity.getUserMainId());
                        userLoginHistoryVO.setIp(userLogin.getLoginIp());
                        userLoginHistoryVO.setLoginSrcType(userLogin.getSrcType());
                        userLoginHistoryVO.setCreateDate(new Date());
                        userLoginHistoryVO.setDeleteStatus((short)0);
                        userLoginHistoryQueue.push(userLoginHistoryVO);
                    }catch(Exception e)
                    {
                        logger.warn(e.getMessage(),e);
                    }

                } else {
                    resultObjectVO.setCode(ResultObjectVO.FAILD);
                    resultObjectVO.setMsg("登录失败,密码输入有误");
                }
            } else {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("登录失败,用户已被禁用");
            }
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("登录失败,请稍后重试");
        }finally{
            skylarkLock.unLock(UserCenterLoginRedisKey.getLoginLockKey(userLogin.getLoginUserName()), userLogin.getLoginUserName());
        }

        return resultObjectVO;
    }





    /**
     * 根据用户名查询用户信息
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/find/by/username",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO findByUsername(@RequestBody RequestJsonVO requestJsonVO) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        if (requestJsonVO == null) {
            resultObjectVO.setCode(UserLoginConstant.NOT_FOUND_USER);
            resultObjectVO.setMsg("查询失败,没有找到要查询的用户");
            return resultObjectVO;
        }
        if (StringUtils.isEmpty(requestJsonVO.getAppCode())) {
            resultObjectVO.setCode(UserLoginConstant.NOT_FOUND_USER);
            resultObjectVO.setMsg("查询失败,没有找到应用编码");
            return resultObjectVO;
        }

        UserVO user = JSONObject.parseObject(requestJsonVO.getEntityJson(),UserVO.class);
        if(user==null)
        {
            resultObjectVO.setCode(UserLoginConstant.NOT_FOUND_USER);
            resultObjectVO.setMsg("登录失败,没有找到账号");
            return resultObjectVO;
        }
        logger.info(" 用户查询 {} ",requestJsonVO.getEntityJson());

        if(StringUtils.isEmpty(user.getUsername()))
        {
            resultObjectVO.setCode(UserLoginConstant.USERNAME_NOT_FOUND);
            resultObjectVO.setMsg("查询失败,请输入账号");
            return resultObjectVO;
        }

        try {

            //如果当前输入的是手机号判断手机号是否存在
            Long userId = 0L;

            User userEntity=null;
            if(PhoneUtils.isChinaPhoneLegal(user.getUsername()))
            {
                List<UserMobilePhone> userMobilePhones = userMobilePhoneService.findListByMobilePhone(user.getUsername());
                if (!CollectionUtils.isEmpty(userMobilePhones)) {
                    if (userMobilePhones.get(0) != null && userMobilePhones.get(0).getUserMainId() != null) {
                        userId = userMobilePhones.get(0).getUserMainId();
                    }
                }

            }else if(EmailUtils.isEmail(user.getUsername())) //如果输入邮箱,拿到邮箱对应的用户主ID
            {
                List<UserEmail> userEmails = userEmailService.findListByEmail(user.getUsername());
                if (!CollectionUtils.isEmpty(userEmails)) {
                    if (userEmails.get(0) != null && userEmails.get(0).getUserMainId() != null) {
                        userId = userEmails.get(0).getUserMainId();
                    }
                }
            }else if(UsernameUtils.isUsername(user.getUsername())) //如果输入用户名,拿到用户名对应的用户主ID
            {
                List<UserUserName> userUserNames = userUserNameService.findListByUserName(user.getUsername());
                if (!CollectionUtils.isEmpty(userUserNames)) {
                    if (userUserNames.get(0) != null && userUserNames.get(0).getUserMainId() != null) {
                        userId = userUserNames.get(0).getUserMainId();
                    }
                }
            }

            List<User> users = userService.findByUserMainId(userId);
            if (CollectionUtils.isEmpty(users)) {
                resultObjectVO.setData(null);
                return resultObjectVO;
            } else {
                userEntity = users.get(0);
            }

            UserVO userVO = new UserVO();
            BeanUtils.copyProperties(userVO,userEntity);

            //设置用户详情
            UserDetail queryUserDetail = new UserDetail();
            queryUserDetail.setUserMainId(userVO.getUserMainId());
            List<UserDetail> userDetails = userDetailService.findListByEntity(queryUserDetail);
            if(CollectionUtils.isNotEmpty(userDetails))
            {
                UserDetail userDetail = userDetails.get(0);
                BeanUtils.copyProperties(userVO,userDetail);
            }
            //查询手机号
            UserMobilePhone userMobilePhone = userMobilePhoneService.findByUserMainId(userVO.getUserMainId());
            if(userMobilePhone!=null)
            {
                userVO.setMobilePhone(userMobilePhone.getMobilePhone());
            }
            //查询邮箱
            UserEmail userEmail = userEmailService.findByUserMainId(userVO.getUserMainId());
            if(userEmail!=null)
            {
                userVO.setEmail(userEmail.getEmail());
            }
            resultObjectVO.setData(userVO);
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("查询失败,请稍后重试");
        }

        return resultObjectVO;
    }





    /**
     * 判断是否在线
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/query/login/info",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO queryLoginInfo(@RequestBody RequestJsonVO requestVo) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        if (requestVo.getEntityJson() == null) {
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("查询失败,没有找到请求对象");
            return resultObjectVO;
        }
        try{
            String entityJson = requestVo.getEntityJson();
            UserLoginVO userLoginVO =JSONObject.parseObject(entityJson,UserLoginVO.class);
            if(userLoginVO.getUserMainId()==null)
            {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("查询失败,请传入用户ID");
                return resultObjectVO;
            }
            String loginGroupKey =UserCenterLoginRedisKey.getLoginInfoGroupKey(String.valueOf(userLoginVO.getUserMainId()));
            String loginInfoAppKey = UserCenterLoginRedisKey.getLoginInfoAppKey(String.valueOf(userLoginVO.getUserMainId()),requestVo.getAppCode());


            UserVO userVO = new UserVO();
            userVO.setUserMainId(userLoginVO.getUserMainId());
            ToucanStringRedisService toucanStringRedisService = userLoginCacheService.routeToucanRedisService(userVO);
            Object loginTokenObject = toucanStringRedisService.get(loginGroupKey,loginInfoAppKey);
            if(loginTokenObject!=null) {
                resultObjectVO.setData(JSONObject.parseObject(String.valueOf(loginTokenObject),UserVO.class));
            }

        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);

            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("查询失败,请稍后重试");
        }
        return resultObjectVO;
    }

    /**
     * 退出登录
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/logout",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO logout(@RequestBody RequestJsonVO requestVo) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        if (requestVo.getEntityJson() == null) {
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("退出登录失败,没有找到请求对象");
            return resultObjectVO;
        }
        try{
            String entityJson = requestVo.getEntityJson();
            UserLoginVO userLoginVO =JSONObject.parseObject(entityJson,UserLoginVO.class);
            if(userLoginVO.getUserMainId()==null)
            {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("退出登录失败,请传入用户ID");
                return resultObjectVO;
            }
            String loginGroupKey =UserCenterLoginRedisKey.getLoginInfoGroupKey(String.valueOf(userLoginVO.getUserMainId()));

            ToucanStringRedisService toucanStringRedisService = userLoginCacheService.routeToucanRedisServiceByUserMainId(userLoginVO.getUserMainId());
            //删除登录信息
            toucanStringRedisService.delete(loginGroupKey);

        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);

            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("退出登录失败,请稍后重试");
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
            resultObjectVO.setMsg("查询失败,没有找到请求对象");
            return resultObjectVO;
        }
        try{
            String entityJson = requestVo.getEntityJson();
            UserLoginVO userLoginVO =JSONObject.parseObject(entityJson,UserLoginVO.class);
            if(userLoginVO.getUserMainId()==null)
            {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("查询失败,请传入用户ID");
                return resultObjectVO;
            }
            if(StringUtils.isEmpty(userLoginVO.getLoginToken()))
            {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("查询失败,请传入loginToken");
                return resultObjectVO;
            }
            String loginTokenGroupKey =UserCenterLoginRedisKey.getLoginInfoGroupKey(String.valueOf(userLoginVO.getUserMainId()));
            String loginTokenAppKey = UserCenterLoginRedisKey.getLoginTokenAppKey(String.valueOf(userLoginVO.getUserMainId()),requestVo.getAppCode());

            ToucanStringRedisService toucanStringRedisService = userLoginCacheService.routeToucanRedisServiceByUserMainId(userLoginVO.getUserMainId());
            Object loginTokenObject = toucanStringRedisService.get(loginTokenGroupKey,loginTokenAppKey);
            if (loginTokenObject != null) {
                if(StringUtils.equals(userLoginVO.getLoginToken(),String.valueOf(loginTokenObject)))
                {
                    //登录时长重置
                    //设置登录token超时
                    toucanStringRedisService.expire(loginTokenGroupKey,UserCenterLoginRedisKey.LOGIN_TIMEOUT_SECOND, TimeUnit.SECONDS);

                    resultObjectVO.setData(true);
                }
            }

        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);

            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("查询失败,请稍后重试");
        }
        return resultObjectVO;
    }


    /**
     * 判断userId和tokne是否一致
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/verify/login/token",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO verifyLoginToken(@RequestBody RequestJsonVO requestVo) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        if (requestVo.getEntityJson() == null) {
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("查询失败,没有找到请求对象");
            return resultObjectVO;
        }
        try{
            String entityJson = requestVo.getEntityJson();
            UserLoginVO userLoginVO =JSONObject.parseObject(entityJson,UserLoginVO.class);
            if(userLoginVO.getUserMainId()==null)
            {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("查询失败,请传入用户ID");
                return resultObjectVO;
            }
            if(StringUtils.isEmpty(userLoginVO.getLoginToken()))
            {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("查询失败,请传入loginToken");
                return resultObjectVO;
            }
            String loginTokenGroupKey =UserCenterLoginRedisKey.getLoginInfoGroupKey(String.valueOf(userLoginVO.getUserMainId()));
            String loginTokenAppKey = UserCenterLoginRedisKey.getLoginTokenAppKey(String.valueOf(userLoginVO.getUserMainId()),requestVo.getAppCode());

            ToucanStringRedisService toucanStringRedisService = userLoginCacheService.routeToucanRedisServiceByUserMainId(userLoginVO.getUserMainId());
            Object loginTokenObject = toucanStringRedisService.get(loginTokenGroupKey,loginTokenAppKey);
            if (loginTokenObject != null) {
                if(!StringUtils.equals(userLoginVO.getLoginToken(),String.valueOf(loginTokenObject)))
                {
                    //当前传过来的token和缓存中的token不一致
                    resultObjectVO.setCode(ResultVO.FAILD);
                }
            }

        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);

            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("查询失败,请稍后重试");
        }
        return resultObjectVO;
    }




    /**
     * 校验用户token以及判断登录会话
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/verify/login/token/is/online",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO verifyLoginTokenAndIsOnline(@RequestBody RequestJsonVO requestVo) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        if (requestVo.getEntityJson() == null) {
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("查询失败,没有找到请求对象");
            return resultObjectVO;
        }
        try{
            String entityJson = requestVo.getEntityJson();
            UserLoginVO userLoginVO =JSONObject.parseObject(entityJson,UserLoginVO.class);
            if(userLoginVO.getUserMainId()==null)
            {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("查询失败,请传入用户ID");
                return resultObjectVO;
            }
            if(StringUtils.isEmpty(userLoginVO.getLoginToken()))
            {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("查询失败,请传入loginToken");
                return resultObjectVO;
            }
            String loginTokenGroupKey =UserCenterLoginRedisKey.getLoginInfoGroupKey(String.valueOf(userLoginVO.getUserMainId()));
            String loginTokenAppKey = UserCenterLoginRedisKey.getLoginTokenAppKey(String.valueOf(userLoginVO.getUserMainId()),requestVo.getAppCode());

            //在这里进行缓存的分片处理,用户登录的会话通过UserMainId路由到不同的集群
            ToucanStringRedisService toucanStringRedisService = userLoginCacheService.routeToucanRedisServiceByUserMainId(userLoginVO.getUserMainId());
            Object loginTokenObject = toucanStringRedisService.get(loginTokenGroupKey,loginTokenAppKey);
            if (loginTokenObject != null) {
                if(!StringUtils.equals(userLoginVO.getLoginToken(),String.valueOf(loginTokenObject)))
                {
                    //当前传过来的token和缓存中的token不一致
                    resultObjectVO.setCode(ResultVO.FAILD);
                    return resultObjectVO;
                }

                //登录时长重置
                //延长登录token超时
                toucanStringRedisService.expire(loginTokenGroupKey,UserCenterLoginRedisKey.LOGIN_TIMEOUT_SECOND, TimeUnit.SECONDS);
                resultObjectVO.setCode(ResultVO.SUCCESS);

            }else{
                resultObjectVO.setCode(ResultVO.FAILD);
            }


        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);

            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("查询失败,请稍后重试");
        }
        return resultObjectVO;
    }


    /**
     * 判断是否实名
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/verify/real/name",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO verifyRealName(@RequestBody RequestJsonVO requestVo) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        resultObjectVO.setData(false);
        if (requestVo.getEntityJson() == null) {
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("查询失败,没有找到请求对象");
            return resultObjectVO;
        }

        UserVO userVo = JSONObject.parseObject(requestVo.getEntityJson(),UserVO.class);
        if(userVo==null)
        {
            resultObjectVO.setCode(UserLoginConstant.NOT_FOUND_USER);
            resultObjectVO.setMsg("查询失败,没有找到账号");
            return resultObjectVO;
        }
        if(userVo.getUserMainId() == null)
        {
            resultObjectVO.setCode(UserLoginConstant.NOT_FOUND_USER);
            resultObjectVO.setMsg("查询失败,没有找到用户主ID");
            return resultObjectVO;
        }
        logger.info(" 用户登录 {} ",requestVo.getEntityJson());

        try {

            List<User> users = userService.findByUserMainId(userVo.getUserMainId());
            if(CollectionUtils.isEmpty(users))
            {
                resultObjectVO.setCode(UserLoginConstant.NOT_REGIST);
                resultObjectVO.setMsg("查询失败,没有找到该用户");
            }else {
                User userEntity = users.get(0);
                //判断用户启用状态
                if(userEntity.getEnableStatus().shortValue()==1) {
                    List<UserDetail> userDetails = userDetailService.findByUserMainId(userEntity.getUserMainId());
                    if(CollectionUtils.isEmpty(userDetails))
                    {
                        resultObjectVO.setData(false);
                    }else{
                        for(UserDetail userDetail:userDetails)
                        {
                            //如果输入了真实姓名和身份证账号
                            if(userDetail.getTrueNameStatus()!=null&&userDetail.getTrueNameStatus().intValue()==1)
                            {
                                resultObjectVO.setData(true);
                                break;
                            }
                        }
                    }
                }else{

                    resultObjectVO.setCode(UserLoginConstant.NOT_REGIST);
                    resultObjectVO.setMsg("查询失败,该用户被禁用");
                }
            }

        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("查询失败,请稍后重试");
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
            resultObjectVO.setMsg("查询失败,没有找到实体对象");
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
            boolean findEntity = true;

            //根据用户ID查询
            if(userPageInfo.getUserMainId()!=null)
            {
                userMainIdLinkedList.add(userPageInfo.getUserMainId());
            }

            //手机号查询,先查询手机号子表
            if(StringUtils.isNotEmpty(userPageInfo.getMobilePhone())&&findEntity) {
                List<UserMobilePhone> entitys = userMobilePhoneService.findListByMobilePhone(userPageInfo.getMobilePhone(),userMainIdLinkedList);
                if (CollectionUtils.isNotEmpty(entitys)) {
                    for (int i = 0; i < entitys.size(); i++) {
                        userMainIdLinkedList.add(entitys.get(i).getUserMainId());
                    }
                }else{ //如果没有匹配到数据,设置一个不存在的ID
                    userMainIdLinkedList.clear();
                    userMainIdLinkedList.add(1L);
                    findEntity=false;
                }
            }


            //用户名查询,先查询用户名子表
            if(StringUtils.isNotEmpty(userPageInfo.getUsername())&&findEntity) {
                List<UserUserName> entitys = userUserNameService.findListByUserName(userPageInfo.getUsername(),userMainIdLinkedList);
                if (CollectionUtils.isNotEmpty(entitys)) {
                    for (int i = 0; i < entitys.size(); i++) {
                        userMainIdLinkedList.add(entitys.get(i).getUserMainId());
                    }
                }else{ //如果没有匹配到数据,设置一个不存在的ID
                    userMainIdLinkedList.clear();
                    userMainIdLinkedList.add(1L);
                    findEntity=false;
                }
            }


            //邮箱查询,先查询邮箱子表
            if(StringUtils.isNotEmpty(userPageInfo.getEmail())&&findEntity) {
                List<UserEmail> entitys = userEmailService.findListByEmail(userPageInfo.getEmail(),userMainIdLinkedList);
                if (CollectionUtils.isNotEmpty(entitys)) {
                    for (int i = 0; i < entitys.size(); i++) {
                        userMainIdLinkedList.add(entitys.get(i).getUserMainId());
                    }
                }else{ //如果没有匹配到数据,设置一个不存在的ID
                    userMainIdLinkedList.clear();
                    userMainIdLinkedList.add(1L);
                    findEntity=false;
                }
            }


            PageInfo<UserVO> pageInfo = null;
            if(findEntity) {
                //设置查询条件
                if (CollectionUtils.isNotEmpty(userMainIdLinkedList)) {
                    Long[] userMainIdArray = new Long[userMainIdLinkedList.size()];
                    userMainIdLinkedList.toArray(userMainIdArray);

                    userPageInfo.setUserMainIdArray(userMainIdArray);
                }


                //查询用户主表
                pageInfo = userService.queryListPage(userPageInfo);
                if (CollectionUtils.isNotEmpty(pageInfo.getList())) {
                    Long[] userIdArray = new Long[pageInfo.getList().size()];

                    for (int i = 0; i < pageInfo.getList().size(); i++) {
                        UserVO userVo = pageInfo.getList().get(i);
                        if (userVo.getUserMainId() != null) {
                            userVo.setUserMainIdString(String.valueOf(userVo.getUserMainId()));
                        }
                        if (userVo.getId() != null) {
                            userVo.setUserId(String.valueOf(userVo.getId()));
                        }

                        userIdArray[i] = pageInfo.getList().get(i).getUserMainId();
                    }

                    //查询用户手机号关联表
                    List<UserMobilePhone> userMobilePhones = userMobilePhoneService.queryListByUserMainId(userIdArray);
                    if (CollectionUtils.isNotEmpty(userMobilePhones)) {
                        //设置用户对象中的手机号
                        for (UserMobilePhone userMobilePhone : userMobilePhones) {
                            for (UserVO userVO : pageInfo.getList()) {
                                if (userMobilePhone.getUserMainId().longValue() == userVO.getUserMainId().longValue()) {
                                    userVO.setMobilePhone(userMobilePhone.getMobilePhone());
                                    continue;
                                }
                            }
                        }
                    }
                    //查询用户用户名关联表
                    List<UserUserName> userUserNames = userUserNameService.queryListByUserId(userIdArray);
                    if (CollectionUtils.isNotEmpty(userUserNames)) {
                        //设置用户用户名
                        for (UserUserName userUserName : userUserNames) {
                            for (UserVO userVO : pageInfo.getList()) {
                                if (userUserName.getUserMainId().longValue() == userVO.getUserMainId().longValue()) {
                                    userVO.setUsername(userUserName.getUsername());
                                    continue;
                                }
                            }
                        }
                    }


                    //查询用户邮箱关联表
                    List<UserEmail> userEmails = userEmailService.queryListByUserId(userIdArray);
                    if (CollectionUtils.isNotEmpty(userEmails)) {
                        //设置邮箱
                        for (UserEmail userEmail : userEmails) {
                            for (UserVO userVO : pageInfo.getList()) {
                                if (userEmail.getUserMainId().longValue() == userVO.getUserMainId().longValue()) {
                                    userVO.setEmail(userEmail.getEmail());
                                    continue;
                                }
                            }
                        }
                    }

                    //查询用户与用户详情关联
                    List<UserDetail> userDetails = userDetailService.queryListByUserId(userIdArray);
                    if (CollectionUtils.isNotEmpty(userDetails)) {
                        //设置用户详情
                        for (UserDetail userDetail : userDetails) {
                            for (UserVO userVO : pageInfo.getList()) {
                                if (userDetail.getUserMainId().longValue() == userVO.getUserMainId().longValue()) {
                                    BeanUtils.copyProperties(userVO, userDetail);
                                    continue;
                                }
                            }
                        }
                    }

                }
            }else{
                pageInfo = new PageInfo<UserVO>();
            }

            resultObjectVO.setData(pageInfo);
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);

            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("查询失败,请稍后重试");
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
            resultObjectVO.setMsg("查询失败,没有找到实体对象");
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


            //查询用户手机号关联表
            resultObjectVO.setData(userMobilePhoneService.queryListPageNothingDeleteStatus(userPageInfo));

        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);

            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("查询失败,请稍后重试");
        }
        return resultObjectVO;
    }


    /**
     * 用户绑定邮箱列表
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/email/list",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO emailList(@RequestBody RequestJsonVO requestVo){
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        if(requestVo==null||requestVo.getEntityJson()==null)
        {
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("查询失败,没有找到实体对象");
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


            //查询用户邮箱关联表
            resultObjectVO.setData(userEmailService.queryListPageNothingDeleteStatus(userPageInfo));

        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);

            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("查询失败,请稍后重试");
        }
        return resultObjectVO;
    }


    /**
     * 用户名列表
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/username/list",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO usernameList(@RequestBody RequestJsonVO requestVo){
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        if(requestVo==null||requestVo.getEntityJson()==null)
        {
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("查询失败,没有找到实体对象");
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


            //查询用户名关联表
            resultObjectVO.setData(userUserNameService.queryListPageNothingDeleteStatus(userPageInfo));

        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);

            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("查询失败,请稍后重试");
        }
        return resultObjectVO;
    }

    /**
     * 禁用启用指定用户
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/disabled/enabled/id",produces = "application/json;charset=UTF-8",method = RequestMethod.DELETE)
    @ResponseBody
    public ResultObjectVO disabledEnabledById(@RequestBody RequestJsonVO requestVo){
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        if(requestVo==null||requestVo.getEntityJson()==null)
        {
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("操作失败,没有找到实体对象");
            return resultObjectVO;
        }

        try {
            UserVO entity = JSONObject.parseObject(requestVo.getEntityJson(),UserVO.class);
            if(entity.getUserMainId()==null)
            {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("操作失败,没有找到ID");
                return resultObjectVO;
            }

            List<User> users = userService.findListByUserMainId(entity.getUserMainId());
            if(CollectionUtils.isNotEmpty(users)) {
                short enableStatus=0;
                if(users.get(0).getEnableStatus().shortValue()==0)
                {
                    //用户主表启用
                    enableStatus=1;
                }else{
                    //用户主表禁用
                    enableStatus=0;
                }
                int ret = userService.updateEnableStatus(enableStatus, entity.getUserMainId());
                if(ret>0)
                {
                    try {
                        //清空用户登录信息
                        userRedisService.clearLoginCache(String.valueOf(entity.getUserMainId()),requestVo.getAppCode());
                    }catch(Exception e)
                    {
                        logger.warn("清空redis登录缓存失败 {}", requestVo.getEntityJson());
                        logger.warn(e.getMessage(),e);
                    }
                }
            }

            resultObjectVO.setData(entity);

        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);

            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("操作失败,请稍后重试");
        }
        return resultObjectVO;
    }






    /**
     * 禁用启用指定用户手机号
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/mobile/phone/disabled/enabled",produces = "application/json;charset=UTF-8",method = RequestMethod.DELETE)
    @ResponseBody
    public ResultObjectVO disabledEnabledMobilePhone(@RequestBody RequestJsonVO requestVo){
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        if(requestVo==null||requestVo.getEntityJson()==null)
        {
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("操作失败,没有找到实体对象");
            return resultObjectVO;
        }

        try {
            UserMobilePhoneVO queryUserMobile = JSONObject.parseObject(requestVo.getEntityJson(),UserMobilePhoneVO.class);
            if(queryUserMobile.getUserMainId()==null)
            {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("操作失败,没有找到ID");
                return resultObjectVO;
            }
            if(StringUtils.isEmpty(queryUserMobile.getMobilePhone()))
            {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("操作失败,没有找到手机号");
                return resultObjectVO;
            }

            List<UserMobilePhone> userMobilePhones = userMobilePhoneService.findListByEntityNothingDeleteStatus(queryUserMobile);
            if(CollectionUtils.isNotEmpty(userMobilePhones)) {
                short deleteStatus = 0;
                int row = 0;
                if(userMobilePhones.get(0).getDeleteStatus().shortValue()==0)
                {
                    //禁用
                    userMobilePhoneService.updateDeleteStatusById((short) 1, queryUserMobile.getUserMainId(),queryUserMobile.getMobilePhone(),queryUserMobile.getId());
                }else{

                    //查询当前手机号关联用户列表,判断手机号是否已经有其他人使用了
                    UserMobilePhone queryExists = new UserMobilePhone();
                    queryExists.setMobilePhone(queryUserMobile.getMobilePhone());
                    userMobilePhones = userMobilePhoneService.findListByEntity(queryExists);
                    if(CollectionUtils.isNotEmpty(userMobilePhones))
                    {
                        for(UserMobilePhone userMobilePhone:userMobilePhones)
                        {
                            if(userMobilePhone.getUserMainId().longValue()!=queryUserMobile.getUserMainId().longValue())
                            {
                                resultObjectVO.setCode(ResultVO.FAILD);
                                resultObjectVO.setMsg("操作失败,手机号无法启用,已经有人在使用中了");
                                return resultObjectVO;
                            }
                        }
                    }
                    //禁用用户ID下所有关联手机号
                    userMobilePhoneService.deleteByUserMainId(queryUserMobile.getUserMainId());
                    //启用
                    row = userMobilePhoneService.updateDeleteStatusById((short) 0, queryUserMobile.getUserMainId(),queryUserMobile.getMobilePhone(),queryUserMobile.getId());

                    try {
                        //刷新用户信息到登录缓存
                        userRedisService.flushLoginCache(String.valueOf(queryUserMobile.getUserMainId()), queryUserMobile.getAppCode());
                    }catch(Exception e)
                    {
                        logger.warn("刷新redis登录缓存失败 {}", requestVo.getEntityJson());
                        logger.warn(e.getMessage(),e);
                    }
                }

            }

            resultObjectVO.setData(queryUserMobile);

        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);

            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("操作失败,请稍后重试");
        }
        return resultObjectVO;
    }





    /**
     * 禁用启用指定用户邮箱
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/email/disabled/enabled",produces = "application/json;charset=UTF-8",method = RequestMethod.DELETE)
    @ResponseBody
    public ResultObjectVO disabledEnabledEmail(@RequestBody RequestJsonVO requestVo){
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        if(requestVo==null||requestVo.getEntityJson()==null)
        {
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("操作失败,没有找到实体对象");
            return resultObjectVO;
        }

        try {
            UserEmailVO queryUserEmail = JSONObject.parseObject(requestVo.getEntityJson(),UserEmailVO.class);
            if(queryUserEmail.getUserMainId()==null)
            {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("操作失败,没有找到ID");
                return resultObjectVO;
            }
            if(StringUtils.isEmpty(queryUserEmail.getEmail()))
            {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("操作失败,没有找到邮箱");
                return resultObjectVO;
            }


            List<UserEmail> userEmails = userEmailService.findListByEntityNothingDeleteStatus(queryUserEmail);
            if(CollectionUtils.isNotEmpty(userEmails)) {
                int row = 0;
                if(userEmails.get(0).getDeleteStatus().shortValue()==0)
                {
                    //禁用
                    userEmailService.updateDeleteStatusById((short) 1, queryUserEmail.getUserMainId(),queryUserEmail.getEmail(),queryUserEmail.getId());
                }else{

                    //查询当前邮箱关联用户列表,判断邮箱是否已经有其他人使用了
                    UserEmail queryExists = new UserEmail();
                    queryExists.setEmail(queryUserEmail.getEmail());
                    userEmails = userEmailService.findListByEntity(queryExists);
                    if(CollectionUtils.isNotEmpty(userEmails))
                    {
                        for(UserEmail userEmail:userEmails)
                        {
                            if(userEmail.getUserMainId().longValue()!=queryUserEmail.getUserMainId().longValue())
                            {
                                resultObjectVO.setCode(ResultVO.FAILD);
                                resultObjectVO.setMsg("操作失败,邮箱无法启用,已经有人在使用中了");
                                return resultObjectVO;
                            }
                        }
                    }
                    //禁用用户ID下所有关联邮箱
                    userEmailService.deleteByUserMainId(queryUserEmail.getUserMainId());
                    //启用
                    row = userEmailService.updateDeleteStatusById((short) 0, queryUserEmail.getUserMainId(),queryUserEmail.getEmail(),queryUserEmail.getId());

                    try {
                        //刷新用户信息到登录缓存
                        userRedisService.flushLoginCache(String.valueOf(queryUserEmail.getUserMainId()), queryUserEmail.getAppCode());
                    }catch(Exception e)
                    {
                        logger.warn("刷新redis登录缓存失败 {}", requestVo.getEntityJson());
                        logger.warn(e.getMessage(),e);
                    }
                }

            }


            resultObjectVO.setData(queryUserEmail);

        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);

            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("操作失败,请稍后重试");
        }
        return resultObjectVO;
    }





    /**
     * 禁用启用指定用户名
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/username/disabled/enabled",produces = "application/json;charset=UTF-8",method = RequestMethod.DELETE)
    @ResponseBody
    public ResultObjectVO disabledEnabledUsernameByUserMainIdAndUsername(@RequestBody RequestJsonVO requestVo){
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        if(requestVo==null||requestVo.getEntityJson()==null)
        {
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("操作失败,没有找到实体对象");
            return resultObjectVO;
        }

        try {
            UserUserNameVO queryUsername = JSONObject.parseObject(requestVo.getEntityJson(),UserUserNameVO.class);
            if(queryUsername.getUserMainId()==null)
            {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("操作失败,没有找到ID");
                return resultObjectVO;
            }
            if(StringUtils.isEmpty(queryUsername.getUsername()))
            {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("操作失败,没有找到用户名");
                return resultObjectVO;
            }


            List<UserUserName> userUserNames = userUserNameService.findListByEntityNothingDeleteStatus(queryUsername);
            if(CollectionUtils.isNotEmpty(userUserNames)) {
                short deleteStatus = 0;
                int row = 0;
                if(userUserNames.get(0).getDeleteStatus().shortValue()==0)
                {
                    //禁用
                    userUserNameService.updateDeleteStatusById((short) 1, queryUsername.getUserMainId(),queryUsername.getUsername(),queryUsername.getId());
                }else{

                    //查询当前邮箱关联用户列表,判断邮箱是否已经有其他人使用了
                    UserUserName queryExists = new UserUserName();
                    queryExists.setUsername(queryUsername.getUsername());
                    userUserNames = userUserNameService.findListByEntity(queryExists);
                    if(CollectionUtils.isNotEmpty(userUserNames))
                    {
                        for(UserUserName userUserName:userUserNames)
                        {
                            if(userUserName.getUserMainId().longValue()!=queryUsername.getUserMainId().longValue())
                            {
                                resultObjectVO.setCode(ResultVO.FAILD);
                                resultObjectVO.setMsg("操作失败,用户名无法启用,已经有人在使用中了");
                                return resultObjectVO;
                            }
                        }
                    }
                    //禁用用户ID下所有关联用户名
                    userUserNameService.deleteByUserMainId(queryUsername.getUserMainId());
                    //启用
                    row = userUserNameService.updateDeleteStatusById((short) 0, queryUsername.getUserMainId(),queryUsername.getUsername(),queryUsername.getId());

                    try {
                        //刷新用户信息到登录缓存
                        userRedisService.flushLoginCache(String.valueOf(queryUsername.getUserMainId()), queryUsername.getAppCode());
                    }catch(Exception e)
                    {
                        logger.warn("刷新redis登录缓存失败 {}", requestVo.getEntityJson());
                        logger.warn(e.getMessage(),e);
                    }
                }

            }

            resultObjectVO.setData(queryUsername);

        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);

            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("操作失败,请稍后重试");
        }
        return resultObjectVO;
    }

    /**
     * 批量删除
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/disabled/ids",produces = "application/json;charset=UTF-8",method = RequestMethod.DELETE)
    @ResponseBody
    public ResultObjectVO disabledByIds(@RequestBody RequestJsonVO requestVo){
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        if(requestVo==null||requestVo.getEntityJson()==null)
        {
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("没有找到实体对象");
            return resultObjectVO;
        }

        try {
            List<UserVO> users = JSONObject.parseArray(requestVo.getEntityJson(),UserVO.class);
            if(CollectionUtils.isEmpty(users))
            {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("没有找到ID");
                return resultObjectVO;
            }
            List<ResultObjectVO> resultObjectVOList = new ArrayList<ResultObjectVO>();
            for(UserVO user:users) {
                if(user.getUserMainId()!=null) {
                    //用户主表禁用
                    userService.updateEnableStatus((short)0,user.getUserMainId());

                    try {
                        //刷新用户信息到登录缓存
                        userRedisService.flushLoginCache(String.valueOf(user.getUserMainId()), user.getAppCode());
                    }catch(Exception e)
                    {
                        logger.warn("刷新redis登录缓存失败 {}", requestVo.getEntityJson());
                        logger.warn(e.getMessage(),e);
                    }
                }
            }
            resultObjectVO.setData(resultObjectVOList);

        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);

            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请稍后重试");
        }
        return resultObjectVO;
    }







    /**
     * 根据用户主ID查询
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/find/by/user/main/id",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO findByUserMainId(@RequestBody RequestJsonVO requestVo){
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        if(requestVo==null||requestVo.getEntityJson()==null)
        {
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("查询失败,没有找到实体对象");
            return resultObjectVO;
        }

        try {
            UserVO userVO = JSONObject.parseObject(requestVo.getEntityJson(), UserVO.class);

            if(StringUtils.isEmpty(requestVo.getAppCode()))
            {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("没有找到应用编码");
                return resultObjectVO;
            }


            List<User> users = userService.findListByUserMainId(userVO.getUserMainId());
            if(CollectionUtils.isNotEmpty(users))
            {
                User user = users.get(0);
                userVO.setId(user.getId());
                userVO.setEnableStatus(user.getEnableStatus());
            }

            //查询手机号
            UserMobilePhone queryUserMobilePhone = new UserMobilePhone();
            queryUserMobilePhone.setUserMainId(userVO.getUserMainId());
            List<UserMobilePhone> userMobilePhones = userMobilePhoneService.findListByEntity(queryUserMobilePhone);
            if(CollectionUtils.isNotEmpty(userMobilePhones))
            {
                userVO.setMobilePhone(userMobilePhones.get(0).getMobilePhone());
            }

            //设置用户名
            List<UserUserName> userUserNames = userUserNameService.findListByUserMainId(userVO.getUserMainId());
            if(CollectionUtils.isNotEmpty(userUserNames))
            {
                userVO.setUsername(userUserNames.get(0).getUsername());
            }

            //设置邮箱
            List<UserEmail> userEmails = userEmailService.findListByUserMainId(userVO.getUserMainId());
            if(CollectionUtils.isNotEmpty(userEmails))
            {
                userVO.setEmail(userEmails.get(0).getEmail());
            }

            //设置用户详情
            UserDetail queryUserDetail = new UserDetail();
            queryUserDetail.setUserMainId(userVO.getUserMainId());
            List<UserDetail> userDetails = userDetailService.findListByEntity(queryUserDetail);
            if(CollectionUtils.isNotEmpty(userDetails))
            {
                UserDetail userDetail = userDetails.get(0);
                BeanUtils.copyProperties(userVO,userDetail);
            }

            resultObjectVO.setData(userVO);

        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);

            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("查询失败,请稍后重试");
        }
        return resultObjectVO;
    }





    /**
     * 根据用户主ID查询
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/find/by/user/main/id/for/cache/db",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO findByUserMainIdForCacheOrDB(@RequestBody RequestJsonVO requestVo){
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        if(requestVo==null||requestVo.getEntityJson()==null)
        {
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("查询失败,没有找到实体对象");
            return resultObjectVO;
        }

        try {
            UserVO userVO = JSONObject.parseObject(requestVo.getEntityJson(), UserVO.class);

            if(StringUtils.isEmpty(requestVo.getAppCode()))
            {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("没有找到应用编码");
                return resultObjectVO;
            }

            List<UserElasticSearchVO> userElasticSearchVOS = null;

            if(CollectionUtils.isNotEmpty(userElasticSearchVOS)) {
                UserElasticSearchVO userElasticSearchVO = userElasticSearchVOS.get(0);
                try{
                    BeanUtils.copyProperties(userVO,userElasticSearchVO);
                }catch(Exception e)
                {
                    logger.warn(e.getMessage(),e);
                }

            }

            if (userVO.getUserMainId() == null ||
                    userVO.getEnableStatus() == null ||
                    userVO.getPassword() == null ||
                    userVO.getNickName()==null||
                    userVO.getSex()==null){

                //查询数据库
                List<User> users = userService.findListByUserMainId(userVO.getUserMainId());
                if(CollectionUtils.isNotEmpty(users))
                {
                    User user = users.get(0);
                    userVO.setId(user.getId());
                    userVO.setEnableStatus(user.getEnableStatus());
                }

                //查询手机号
                UserMobilePhone queryUserMobilePhone = new UserMobilePhone();
                queryUserMobilePhone.setUserMainId(userVO.getUserMainId());
                List<UserMobilePhone> userMobilePhones = userMobilePhoneService.findListByEntity(queryUserMobilePhone);
                if(CollectionUtils.isNotEmpty(userMobilePhones))
                {
                    userVO.setMobilePhone(userMobilePhones.get(0).getMobilePhone());
                }

                //设置用户名
                List<UserUserName> userUserNames = userUserNameService.findListByUserMainId(userVO.getUserMainId());
                if(CollectionUtils.isNotEmpty(userUserNames))
                {
                    userVO.setUsername(userUserNames.get(0).getUsername());
                }

                //设置邮箱
                List<UserEmail> userEmails = userEmailService.findListByUserMainId(userVO.getUserMainId());
                if(CollectionUtils.isNotEmpty(userEmails))
                {
                    userVO.setEmail(userEmails.get(0).getEmail());
                }

                //设置用户详情
                UserDetail queryUserDetail = new UserDetail();
                queryUserDetail.setUserMainId(userVO.getUserMainId());
                List<UserDetail> userDetails = userDetailService.findListByEntity(queryUserDetail);
                if(CollectionUtils.isNotEmpty(userDetails))
                {
                    UserDetail userDetail = userDetails.get(0);
                    userVO.setNickName(userDetail.getNickName()); //昵称
                    userVO.setTrueName(userDetail.getTrueName()); //姓名
                    userVO.setIdCard(userDetail.getIdCard()); //身份证
                    userVO.setTrueNameStatus(userDetail.getTrueNameStatus()); //实名状态
                    userVO.setIdcardType(userDetail.getIdcardType()); //身份证类型
                    userVO.setHeadSculpture(userDetail.getHeadSculpture()); //头像
                    userVO.setSex(userDetail.getSex()); //性别
                    userVO.setIsShop(userDetail.getIsShop()); //是否有店铺
                }

            }

            resultObjectVO.setData(userVO);

        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);

            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("查询失败,请稍后重试");
        }
        return resultObjectVO;
    }

    /**
     * 关联手机号
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/connect/mobile/phone",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO connectMobilePhone(@RequestBody RequestJsonVO requestJsonVO){
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        if(requestJsonVO==null)
        {
            resultObjectVO.setCode(UserRegistConstant.NOT_FOUND_USER);
            resultObjectVO.setMsg("关联失败,没有找到参数");
            return resultObjectVO;
        }

        if (StringUtils.isEmpty(requestJsonVO.getAppCode())) {
            resultObjectVO.setCode(UserRegistConstant.NOT_FOUND_USER);
            resultObjectVO.setMsg("关联失败,没有找到应用编码");
            return resultObjectVO;
        }
        UserRegistVO userRegistVO = JSONObject.parseObject(requestJsonVO.getEntityJson(),UserRegistVO.class);
        if(userRegistVO==null)
        {
            resultObjectVO.setCode(UserRegistConstant.NOT_FOUND_USER);
            resultObjectVO.setMsg("关联失败,没有找到参数");
            return resultObjectVO;
        }
        if(StringUtils.isEmpty(userRegistVO.getMobilePhone()))
        {
            resultObjectVO.setCode(UserRegistConstant.NOT_FOUND_MOBILE);
            resultObjectVO.setMsg("关联失败,请输入手机号");
            return resultObjectVO;
        }

        if(!PhoneUtils.isPhoneLegal(userRegistVO.getMobilePhone()))
        {
            resultObjectVO.setCode(UserRegistConstant.MOBILE_ERROR);
            resultObjectVO.setMsg("关联失败,手机号格式错误");
            return resultObjectVO;
        }

        if(userRegistVO.getUserMainId()==null)
        {
            resultObjectVO.setCode(UserRegistConstant.NOT_FOUND_USER);
            resultObjectVO.setMsg("关联失败,没有找到要用户主ID");
            return resultObjectVO;
        }

        try {
            boolean lockStatus = skylarkLock.lock(UserCenterRegistRedisKey.getBindMobilePhoneLock(userRegistVO.getMobilePhone()), userRegistVO.getMobilePhone());
            if (!lockStatus) {
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("请求超时,请稍后重试");
                return resultObjectVO;
            }
            //查询邮箱是否已关联
            UserMobilePhone query = new UserMobilePhone();
            query.setMobilePhone(userRegistVO.getMobilePhone());
            List<UserMobilePhone> userMobilePhones = userMobilePhoneService.findListByEntity(query);
            if (!CollectionUtils.isEmpty(userMobilePhones)) {
                for(UserMobilePhone userMobilePhone:userMobilePhones)
                {
                    if(userMobilePhone.getUserMainId().longValue()==userRegistVO.getUserMainId().longValue())
                    {
                        resultObjectVO.setCode(UserResultVO.FAILD);
                        resultObjectVO.setMsg("绑定失败，手机号已经绑定到该用户了!");
                        return resultObjectVO;
                    }
                }
                resultObjectVO.setCode(UserResultVO.FAILD);
                resultObjectVO.setMsg("绑定失败，手机号已被绑定!");
                return resultObjectVO;
            }
            query = new UserMobilePhone();
            query.setUserMainId(userRegistVO.getUserMainId());
            userMobilePhones = userMobilePhoneService.findListByEntity(query);
            if(CollectionUtils.isNotEmpty(userMobilePhones))
            {
                resultObjectVO.setCode(UserResultVO.FAILD);
                resultObjectVO.setMsg("绑定失败，该用户已存在使用中手机号!");
                return resultObjectVO;
            }

            //保存用户手机号关联
            UserMobilePhone userMobilePhone = new UserMobilePhone();
            userMobilePhone.setId(idGenerator.id());
            //设置手机号
            userMobilePhone.setMobilePhone(userRegistVO.getMobilePhone());
            //设置用户主表ID
            userMobilePhone.setUserMainId(userRegistVO.getUserMainId());
            userMobilePhone.setCreateDate(new Date());
            userMobilePhone.setDeleteStatus((short) 0);

            int row = userMobilePhoneService.save(userMobilePhone);
            if (row < 1) {
                logger.warn("关联手机号失败 {}", requestJsonVO.getEntityJson());
                resultObjectVO.setCode(UserResultVO.FAILD);
                resultObjectVO.setMsg("关联失败,请重试!");
            }else{
                try {
                    //刷新用户信息到登录缓存
                    userRedisService.flushLoginCache(String.valueOf(userRegistVO.getUserMainId()), userRegistVO.getAppCode());
                }catch(Exception e)
                {
                    logger.warn("刷新redis登录缓存失败 {}", requestJsonVO.getEntityJson());
                    logger.warn(e.getMessage(),e);
                }
            }
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("关联失败,请稍后重试");
        }finally{
            skylarkLock.unLock(UserCenterRegistRedisKey.getBindMobilePhoneLock(userRegistVO.getMobilePhone()), userRegistVO.getMobilePhone());
        }
        return resultObjectVO;
    }






    /**
     * 刷新缓存
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/flush/cache",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO flushCache(@RequestBody RequestJsonVO requestJsonVO){
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        if(requestJsonVO==null)
        {
            resultObjectVO.setCode(UserRegistConstant.NOT_FOUND_USER);
            resultObjectVO.setMsg("刷新失败,没有找到要操作的用户");
            return resultObjectVO;
        }

        if (StringUtils.isEmpty(requestJsonVO.getAppCode())) {
            resultObjectVO.setCode(UserRegistConstant.NOT_FOUND_USER);
            resultObjectVO.setMsg("刷新失败,没有找到应用编码");
            return resultObjectVO;
        }
        UserRegistVO userRegistVO = JSONObject.parseObject(requestJsonVO.getEntityJson(),UserRegistVO.class);
        if(userRegistVO==null)
        {
            resultObjectVO.setCode(UserRegistConstant.NOT_FOUND_USER);
            resultObjectVO.setMsg("刷新失败,没有找到要操作的用户");
            return resultObjectVO;
        }
        if(userRegistVO.getUserMainId()==null)
        {
            resultObjectVO.setCode(UserRegistConstant.NOT_FOUND_USER);
            resultObjectVO.setMsg("刷新失败,没有找到要用户主ID");
            return resultObjectVO;
        }
        try {

            //刷新用户信息到登录缓存
            userRedisService.flushLoginCache(String.valueOf(userRegistVO.getUserMainId()),userRegistVO.getAppCode());

        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("刷新失败,请稍后重试");
        }
        return resultObjectVO;
    }

}
