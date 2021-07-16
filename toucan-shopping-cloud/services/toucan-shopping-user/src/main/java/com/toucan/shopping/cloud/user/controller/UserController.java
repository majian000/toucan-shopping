package com.toucan.shopping.cloud.user.controller;


import com.alibaba.fastjson.JSONObject;
import com.toucan.shopping.cloud.user.queue.NewUserMessageQueue;
import com.toucan.shopping.modules.common.generator.IdGenerator;
import com.toucan.shopping.modules.common.page.PageInfo;
import com.toucan.shopping.modules.common.properties.Toucan;
import com.toucan.shopping.modules.common.util.*;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.common.vo.ResultVO;
import com.toucan.shopping.modules.skylark.lock.service.SkylarkLock;
import com.toucan.shopping.modules.user.constant.UserLoginConstant;
import com.toucan.shopping.modules.user.constant.UserRegistConstant;
import com.toucan.shopping.modules.user.entity.*;
import com.toucan.shopping.modules.user.es.service.UserElasticSearchService;
import com.toucan.shopping.modules.user.kafka.message.UserCreateMessage;
import com.toucan.shopping.modules.user.page.UserPageInfo;
import com.toucan.shopping.modules.user.redis.UserCenterLoginRedisKey;
import com.toucan.shopping.modules.user.redis.UserCenterRegistRedisKey;
import com.toucan.shopping.modules.user.service.*;
import com.toucan.shopping.modules.user.util.LoginTokenUtil;
import com.toucan.shopping.modules.user.vo.*;
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
    private SkylarkLock skylarkLock;

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
    private Toucan toucan;


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
            boolean lockStatus = skylarkLock.lock(UserCenterRegistRedisKey.getRegistLockKey(userRegistVO.getMobilePhone()), userRegistVO.getMobilePhone());
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
                        userDetail.setHeadSculpture(toucan.getUser().getDefaultHeadSculpture());
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
                            userCreateMessage.setType((short)0); //默认为买家

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
            skylarkLock.unLock(UserCenterRegistRedisKey.getRegistLockKey(userRegistVO.getMobilePhone()), userRegistVO.getMobilePhone());
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
            resultObjectVO.setMsg("请求失败,没有找到要重置的用户");
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
            resultObjectVO.setMsg("请求失败,没有找到要重置的用户");
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
            resultObjectVO.setMsg("请求失败,请输入密码");
            return resultObjectVO;
        }

        if(!UserRegistUtil.checkPwd(userRegistVO.getPassword()))
        {
            resultObjectVO.setCode(UserRegistConstant.PASSWORD_ERROR);
            resultObjectVO.setMsg("请求失败,请输入6至15位的密码");
            return resultObjectVO;
        }



        String userMainId = String.valueOf(userRegistVO.getUserMainId());
        try {
            boolean lockStatus = skylarkLock.lock(UserCenterRegistRedisKey.getResetPasswordLockKey(userMainId), userMainId);
            if (!lockStatus) {
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("超时重试");
                return resultObjectVO;
            }

            String password = MD5Util.md5(userRegistVO.getPassword());

            int row = userService.updatePasswordByUserMainId(userRegistVO.getUserMainId(),password);
            if(row<1)
            {
                logger.warn("修改密码失败 {}", requestJsonVO.getEntityJson());
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("请求失败,请稍后重试");
            }else{
                try {
                    List<UserElasticSearchVO> userElasticSearchVOS = userElasticSearchService.queryByUserMainId(userRegistVO.getUserMainId());
                    if (CollectionUtils.isNotEmpty(userElasticSearchVOS)) {
                        UserElasticSearchVO userElasticSearchVO = userElasticSearchVOS.get(0);
                        userElasticSearchVO.setPassword(password);
                        userElasticSearchService.update(userElasticSearchVO);
                    }
                } catch (Exception e) {
                    resultObjectVO.setCode(ResultVO.FAILD);
                    resultObjectVO.setMsg("更新用户缓存出现异常");
                    logger.warn(e.getMessage(), e);
                }
            }
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请求失败,请稍后重试");
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
            boolean lockStatus = skylarkLock.lock(UserCenterRegistRedisKey.getRegistLockKey(userRegistVO.getUsername()), userRegistVO.getUsername());
            if (!lockStatus) {
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("超时重试");
                return resultObjectVO;
            }
            //查询用户名是否已关联
            UserUserName query = new UserUserName();
            query.setUsername(userRegistVO.getUsername());
            List<UserUserName> userUserNames = userUserNameService.findListByEntity(query);
            if (!CollectionUtils.isEmpty(userUserNames)) {
                resultObjectVO.setCode(UserResultVO.FAILD);
                resultObjectVO.setMsg("关联失败，用户名已注册!");
            } else {
                query = new UserUserName();
                query.setUserMainId(userRegistVO.getUserMainId());
                userUserNames = userUserNameService.findListByEntity(query);
                if(CollectionUtils.isNotEmpty(userUserNames))
                {
                    resultObjectVO.setCode(UserResultVO.FAILD);
                    resultObjectVO.setMsg("关联失败，该用户已存在使用中用户名!");
                }else {
                    query.setUsername(userRegistVO.getUsername());
                    userUserNames = userUserNameService.findListByEntityNothingDeleteStatus(query);
                    if(CollectionUtils.isNotEmpty(userUserNames))
                    {
                        resultObjectVO.setCode(UserResultVO.FAILD);
                        resultObjectVO.setMsg("关联失败，邮箱已经关联到该用户了!");
                    }else {

                        //保存用户和用户名关联
                        UserUserName userUserName = new UserUserName();
                        userUserName.setId(idGenerator.id());
                        //设置邮箱
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
                        }else {

                            try {
                                List<UserElasticSearchVO> userElasticSearchVOS = userElasticSearchService.queryByUserMainId(userRegistVO.getUserMainId());
                                if (CollectionUtils.isNotEmpty(userElasticSearchVOS)) {
                                    UserElasticSearchVO userElasticSearchVO = userElasticSearchVOS.get(0);
                                    userElasticSearchVO.setUsername(userRegistVO.getUsername());
                                    userElasticSearchService.update(userElasticSearchVO);
                                }
                            } catch (Exception e) {
                                resultObjectVO.setCode(ResultVO.FAILD);
                                resultObjectVO.setMsg("更新用户缓存出现异常");
                                logger.warn(e.getMessage(), e);
                            }
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
            skylarkLock.unLock(UserCenterRegistRedisKey.getRegistLockKey(userRegistVO.getUsername()), userRegistVO.getUsername());
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
            boolean lockStatus = skylarkLock.lock(UserCenterRegistRedisKey.getRegistLockKey(userRegistVO.getEmail()), userRegistVO.getEmail());
            if (!lockStatus) {
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("超时重试");
                return resultObjectVO;
            }
            //查询邮箱是否已关联
            UserEmail query = new UserEmail();
            query.setEmail(userRegistVO.getEmail());
            List<UserEmail> userEmails = userEmailService.findListByEntity(query);
            if (!CollectionUtils.isEmpty(userEmails)) {
                resultObjectVO.setCode(UserResultVO.FAILD);
                resultObjectVO.setMsg("关联失败，邮箱已注册!");
            } else {
                query = new UserEmail();
                query.setUserMainId(userRegistVO.getUserMainId());
                userEmails = userEmailService.findListByEntity(query);
                if(CollectionUtils.isNotEmpty(userEmails))
                {
                    resultObjectVO.setCode(UserResultVO.FAILD);
                    resultObjectVO.setMsg("关联失败，该用户已存在使用中手机号!");
                }else {
                    query.setEmail(userRegistVO.getEmail());
                    userEmails = userEmailService.findListByEntityNothingDeleteStatus(query);
                    if(CollectionUtils.isNotEmpty(userEmails))
                    {
                        resultObjectVO.setCode(UserResultVO.FAILD);
                        resultObjectVO.setMsg("关联失败，邮箱已经关联到该用户了!");
                    }else {

                        //保存用户邮箱关联
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
                            logger.warn("关联邮箱失败 {}", requestJsonVO.getEntityJson());
                            resultObjectVO.setCode(UserResultVO.FAILD);
                            resultObjectVO.setMsg("请求失败,请重试!");
                        }else {

                            try {
                                List<UserElasticSearchVO> userElasticSearchVOS = userElasticSearchService.queryByUserMainId(userRegistVO.getUserMainId());
                                if (CollectionUtils.isNotEmpty(userElasticSearchVOS)) {
                                    UserElasticSearchVO userElasticSearchVO = userElasticSearchVOS.get(0);
                                    userElasticSearchVO.setEmail(userRegistVO.getEmail());
                                    userElasticSearchService.update(userElasticSearchVO);
                                }
                            } catch (Exception e) {
                                resultObjectVO.setCode(ResultVO.FAILD);
                                resultObjectVO.setMsg("更新用户缓存出现异常");
                                logger.warn(e.getMessage(), e);
                            }
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
            skylarkLock.unLock(UserCenterRegistRedisKey.getRegistLockKey(userRegistVO.getEmail()), userRegistVO.getEmail());
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
     * 将用户详情更新到es
     * @param userRegistVO
     * @throws Exception
     */
    public void updateDetailToElasticsearch(UserRegistVO userRegistVO) throws Exception {
        //从缓存中查询用户对象
        List<UserElasticSearchVO> userElasticSearchVOS = userElasticSearchService.queryByUserMainId(userRegistVO.getUserMainId());
        UserElasticSearchVO userElasticSearchVO = null;
        if(CollectionUtils.isNotEmpty(userElasticSearchVOS))
        {
            userElasticSearchVO = userElasticSearchVOS.get(0);
        }else{ //如果缓存不存在将重新推到缓存中
            userElasticSearchVO = new UserElasticSearchVO();

            User queryUser = new User();
            queryUser.setUserMainId(userRegistVO.getUserMainId());
            List<User> users = userService.findListByEntity(queryUser);
            if(CollectionUtils.isNotEmpty(users)) {
                User user = users.get(0);
                userElasticSearchVO.setId(user.getId());
                userElasticSearchVO.setUserMainId(user.getUserMainId());
                userElasticSearchVO.setEnableStatus(user.getEnableStatus());
                userElasticSearchVO.setDeleteStatus(user.getDeleteStatus());
            }

            //设置手机号
            UserMobilePhone queryUserMobilePhone = new UserMobilePhone();
            queryUserMobilePhone.setUserMainId(userRegistVO.getUserMainId());
            List<UserMobilePhone> userMobilePhones = userMobilePhoneService.findListByEntity(queryUserMobilePhone);
            if(CollectionUtils.isNotEmpty(userMobilePhones)) {
                userElasticSearchVO.setMobilePhone(userMobilePhones.get(0).getMobilePhone());
            }

            //设置邮箱
            UserEmail queryUserEmail = new UserEmail();
            queryUserEmail.setUserMainId(userRegistVO.getUserMainId());
            List<UserEmail> userEmails = userEmailService.findListByEntity(queryUserEmail);
            if(CollectionUtils.isNotEmpty(userEmails)) {
                userElasticSearchVO.setEmail(userEmails.get(0).getEmail());
            }

            //设置用户名
            UserUserName userUserName = new UserUserName();
            userUserName.setUserMainId(userRegistVO.getUserMainId());
            List<UserUserName> userUserNames = userUserNameService.findListByEntity(userUserName);
            if(CollectionUtils.isNotEmpty(userUserNames)) {
                userElasticSearchVO.setUsername(userUserNames.get(0).getUsername());
            }

            userElasticSearchService.save(userElasticSearchVO);
        }

        //昵称
        if(StringUtils.isNotEmpty(userRegistVO.getNickName()))
        {
            userElasticSearchVO.setNickName(userRegistVO.getNickName());
        }
        //姓名
        if(StringUtils.isNotEmpty(userRegistVO.getTrueName()))
        {
            userElasticSearchVO.setTrueName(userRegistVO.getTrueName());
        }
        //身份证
        if(StringUtils.isNotEmpty(userRegistVO.getIdCard()))
        {
            userElasticSearchVO.setIdCard(userRegistVO.getIdCard());
        }
        //头像
        if(StringUtils.isNotEmpty(userRegistVO.getHeadSculpture()))
        {
            userElasticSearchVO.setHeadSculpture(userRegistVO.getHeadSculpture());
        }
        //性别
        if(userRegistVO.getSex()!=null)
        {
            userElasticSearchVO.setSex(userRegistVO.getSex());
        }
        //用户类型
        if(userRegistVO.getType()!=null)
        {
            userElasticSearchVO.setType(userRegistVO.getType());
        }

        userElasticSearchService.update(userElasticSearchVO);
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
            resultObjectVO.setMsg("请求失败,没有找到要用户ID");
            return resultObjectVO;
        }
        try {
            boolean lockStatus = skylarkLock.lock(UserCenterRegistRedisKey.getUpdateDetailLockKey(String.valueOf(userRegistVO.getUserMainId())), String.valueOf(userRegistVO.getUserMainId()));
            if (!lockStatus) {
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("超时重试");
                return resultObjectVO;
            }
            UserDetail query = new UserDetail();
            query.setUserMainId(userRegistVO.getUserMainId());
            List<UserDetail> userDetails = userDetailService.findListByEntity(query);
            int row = 0;
            if(CollectionUtils.isNotEmpty(userDetails))
            {
                UserDetail userDetail = userDetails.get(0);
                userDetail.setNickName(userRegistVO.getNickName()); //昵称
                userDetail.setTrueName(userRegistVO.getTrueName()); //姓名
                userDetail.setIdCard(userRegistVO.getIdCard()); //身份证
                userDetail.setHeadSculpture(userRegistVO.getHeadSculpture()); //头像
                userDetail.setSex(userRegistVO.getSex()); //性别
                userDetail.setType(userRegistVO.getType()); //用户类型
                row= userDetailService.update(userDetail);
            }else{
                UserDetail userDetail = new UserDetail();
                userDetail.setId(idGenerator.id());
                userDetail.setUserMainId(userRegistVO.getUserMainId()); //用户ID
                userDetail.setNickName(userRegistVO.getNickName()); //昵称
                userDetail.setTrueName(userRegistVO.getTrueName()); //姓名
                userDetail.setIdCard(userRegistVO.getIdCard()); //身份证
                userDetail.setHeadSculpture(userRegistVO.getHeadSculpture()); //头像
                userDetail.setSex(userRegistVO.getSex()); //性别
                userDetail.setType(userRegistVO.getType()); //用户类型
                userDetail.setCreateDate(new Date());
                userDetail.setDeleteStatus((short)0);

                row= userDetailService.save(userDetail);
            }

            if(row<=0)
            {
                logger.warn("修改用户详情失败 {}", requestJsonVO.getEntityJson());
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("请求失败,请稍后重试");
            }else {

                //更新缓存
                try {
                    updateDetailToElasticsearch(userRegistVO);
                } catch (Exception e) {
                    logger.warn(e.getMessage(), e);
                    resultObjectVO.setCode(ResultVO.FAILD);
                    resultObjectVO.setMsg("更新用户缓存出现异常");
                }
            }
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请求失败,请稍后重试");
        }finally{
            skylarkLock.unLock(UserCenterRegistRedisKey.getUpdateDetailLockKey(String.valueOf(userRegistVO.getUserMainId())), String.valueOf(userRegistVO.getUserMainId()));
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
            resultObjectVO.setMsg("请求失败,没有找到要操作的用户");
            return resultObjectVO;
        }
        if(userRegistVO.getUserMainId()==null)
        {
            resultObjectVO.setCode(UserRegistConstant.NOT_FOUND_USER);
            resultObjectVO.setMsg("请求失败,没有找到要用户主ID");
            return resultObjectVO;
        }
        try {

            //从缓存中查询用户对象
            List<UserElasticSearchVO> userElasticSearchVOS = userElasticSearchService.queryByUserMainId(userRegistVO.getUserMainId());
            UserElasticSearchVO userElasticSearchVO = null;
            if(CollectionUtils.isNotEmpty(userElasticSearchVOS))
            {
                userElasticSearchVO = userElasticSearchVOS.get(0);
            }else{ //如果缓存不存在将重新推到缓存中
                userElasticSearchVO = new UserElasticSearchVO();
                //设置默认用户ID
                userElasticSearchVO.setUserMainId(-1L);

                User queryUser = new User();
                queryUser.setUserMainId(userRegistVO.getUserMainId());
                List<User> users = userService.findListByEntityNothingDeleteStatus(queryUser);
                if(CollectionUtils.isNotEmpty(users)) {
                    User user = users.get(0);
                    userElasticSearchVO.setId(user.getId());
                    userElasticSearchVO.setUserMainId(user.getUserMainId());
                }

                userElasticSearchService.save(userElasticSearchVO);
            }

            User queryUser = new User();
            queryUser.setUserMainId(userRegistVO.getUserMainId());
            List<User> users = userService.findListByEntityNothingDeleteStatus(queryUser);
            if(CollectionUtils.isNotEmpty(users)) {
                User user = users.get(0);
                userElasticSearchVO.setPassword(user.getPassword());
                userElasticSearchVO.setEnableStatus(user.getEnableStatus());
                userElasticSearchVO.setDeleteStatus(user.getDeleteStatus());
                userElasticSearchVO.setCreateDate(user.getCreateDate());
            }

            if(userElasticSearchVO.getUserMainId()==null)
            {
                resultObjectVO.setCode(UserRegistConstant.NOT_FOUND_USER);
                resultObjectVO.setMsg("刷新失败,没有找到缓存中该用户的用户ID");
                return resultObjectVO;
            }

            //设置手机号
            UserMobilePhone queryUserMobilePhone = new UserMobilePhone();
            queryUserMobilePhone.setUserMainId(userRegistVO.getUserMainId());
            List<UserMobilePhone> userMobilePhones = userMobilePhoneService.findListByEntity(queryUserMobilePhone);
            if(CollectionUtils.isNotEmpty(userMobilePhones)) {
                userElasticSearchVO.setMobilePhone(userMobilePhones.get(0).getMobilePhone());
            }

            //设置邮箱
            UserEmail queryUserEmail = new UserEmail();
            queryUserEmail.setUserMainId(userRegistVO.getUserMainId());
            List<UserEmail> userEmails = userEmailService.findListByEntity(queryUserEmail);
            if(CollectionUtils.isNotEmpty(userEmails)) {
                userElasticSearchVO.setEmail(userEmails.get(0).getEmail());
            }

            //设置用户名
            UserUserName userUserName = new UserUserName();
            userUserName.setUserMainId(userRegistVO.getUserMainId());
            List<UserUserName> userUserNames = userUserNameService.findListByEntity(userUserName);
            if(CollectionUtils.isNotEmpty(userUserNames)) {
                userElasticSearchVO.setUsername(userUserNames.get(0).getUsername());
            }

            UserDetail queryUserDetail = new UserDetail();
            queryUserDetail.setUserMainId(userRegistVO.getUserMainId());
            List<UserDetail> userDetails = userDetailService.findListByEntity(queryUserDetail);
            if(CollectionUtils.isNotEmpty(userDetails)) {
                UserDetail userDetail = userDetails.get(0);
                userElasticSearchVO.setNickName(userDetail.getNickName()); //昵称
                userElasticSearchVO.setTrueName(userDetail.getTrueName()); //姓名
                userElasticSearchVO.setIdCard(userDetail.getIdCard()); //身份证
                userElasticSearchVO.setHeadSculpture(userDetail.getHeadSculpture()); //头像
                userElasticSearchVO.setSex(userDetail.getSex()); //性别
                userElasticSearchVO.setType(userDetail.getType()); //用户类型
            }

            userElasticSearchService.update(userElasticSearchVO);

        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请求失败,请稍后重试");
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
            boolean lockStatus = skylarkLock.lock(UserCenterLoginRedisKey.getLoginLockKey(userLogin.getLoginUserName()), userLogin.getLoginUserName());
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

            List<User> users = userService.findByUserMainId(userId);
            if(CollectionUtils.isEmpty(users))
            {
                resultObjectVO.setCode(UserLoginConstant.NOT_REGIST);
                resultObjectVO.setMsg("登录失败,请检查用户名和密码");
            }else {
                User userEntity = users.get(0);
                //判断用户启用状态
                if(userEntity.getEnableStatus().shortValue()==1) {
                    String pwdMd5 = MD5Util.md5(userLogin.getPassword());
                    //登录成功 生成token
                    if (pwdMd5.equals(userEntity.getPassword())) {
                        String loginGroupKey = UserCenterLoginRedisKey.getLoginInfoGroupKey(String.valueOf(userEntity.getUserMainId()));
                        String loginTokenAppKey = UserCenterLoginRedisKey.getLoginTokenAppKey(String.valueOf(userEntity.getUserMainId()), requestJsonVO.getAppCode());
                        String loginInfoAppKey = UserCenterLoginRedisKey.getLoginInfoAppKey(String.valueOf(userEntity.getUserMainId()), requestJsonVO.getAppCode());
                        //判断是否已有登录token,如果有将删除掉
                        if (stringRedisTemplate.opsForHash().keys(loginGroupKey) != null) {
                            long deleteRows = 0;
                            int tryCount = 0;
                            do {
                                deleteRows = stringRedisTemplate.opsForHash().delete(loginGroupKey, loginTokenAppKey);
                                tryCount++;
                            } while (deleteRows <= 0 && tryCount < 50);
                        }

                        String token = LoginTokenUtil.generatorToken(userEntity.getUserMainId());
                        stringRedisTemplate.opsForHash().put(loginGroupKey,
                                loginTokenAppKey, token);

                        userLogin.setUserMainId(userId);
                        userLogin.setLoginToken(token);
                        userLogin.setPassword(null);

                        List<UserDetail> userDetails = userDetailService.findByUserMainId(userLogin.getUserMainId());
                        if(CollectionUtils.isNotEmpty(userDetails))
                        {
                            userLogin.setNickName(userDetails.get(0).getNickName());
                        }


                        stringRedisTemplate.opsForHash().put(loginGroupKey,
                                loginInfoAppKey, JSONObject.toJSONString(userLogin));
                        //设置登录token5个小时超时
                        stringRedisTemplate.expire(loginGroupKey,
                                UserCenterLoginRedisKey.LOGIN_TIMEOUT_SECOND, TimeUnit.SECONDS);

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
            skylarkLock.unLock(UserCenterLoginRedisKey.getLoginLockKey(userLogin.getLoginUserName()), userLogin.getLoginUserName());
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
            resultObjectVO.setMsg("请求失败,没有找到请求对象");
            return resultObjectVO;
        }
        try{
            String entityJson = requestVo.getEntityJson();
            UserLoginVO userLoginVO =JSONObject.parseObject(entityJson,UserLoginVO.class);
            if(userLoginVO.getUserMainId()==null)
            {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("请求失败,请传入用户ID");
                return resultObjectVO;
            }
            String loginGroupKey =UserCenterLoginRedisKey.getLoginInfoGroupKey(String.valueOf(userLoginVO.getUserMainId()));
            String loginInfoAppKey = UserCenterLoginRedisKey.getLoginInfoAppKey(String.valueOf(userLoginVO.getUserMainId()),requestVo.getAppCode());

            Object loginTokenObject = stringRedisTemplate.opsForHash().get(loginGroupKey,loginInfoAppKey);
            if(loginTokenObject!=null) {
                resultObjectVO.setData(JSONObject.parseObject(String.valueOf(loginTokenObject),UserVO.class));
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
            if(userLoginVO.getUserMainId()==null)
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
            String loginTokenGroupKey =UserCenterLoginRedisKey.getLoginInfoGroupKey(String.valueOf(userLoginVO.getUserMainId()));
            String loginTokenAppKey = UserCenterLoginRedisKey.getLoginTokenAppKey(String.valueOf(userLoginVO.getUserMainId()),requestVo.getAppCode());

            Object loginTokenObject = stringRedisTemplate.opsForHash().get(loginTokenGroupKey,loginTokenAppKey);
            if (loginTokenObject != null) {
                if(StringUtils.equals(userLoginVO.getLoginToken(),String.valueOf(loginTokenObject)))
                {
                    //登录时长重置
                    //设置登录token1个小时超时
                    stringRedisTemplate.expire(loginTokenGroupKey,UserCenterLoginRedisKey.LOGIN_TIMEOUT_SECOND, TimeUnit.SECONDS);

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
            resultObjectVO.setMsg("请求失败,没有找到请求对象");
            return resultObjectVO;
        }

        UserVO userVo = JSONObject.parseObject(requestVo.getEntityJson(),UserVO.class);
        if(userVo==null)
        {
            resultObjectVO.setCode(UserLoginConstant.NOT_FOUND_USER);
            resultObjectVO.setMsg("请求失败,没有找到账号");
            return resultObjectVO;
        }
        if(userVo.getUserMainId() == null)
        {
            resultObjectVO.setCode(UserLoginConstant.NOT_FOUND_USER);
            resultObjectVO.setMsg("请求失败,没有找到用户主ID");
            return resultObjectVO;
        }
        logger.info(" 用户登录 {} ",requestVo.getEntityJson());

        String userMainId = String.valueOf(userVo.getUserMainId());
        try {
            boolean lockStatus = skylarkLock.lock(UserCenterLoginRedisKey.getVerifyRealNameLockKey(userMainId), userMainId);
            if (!lockStatus) {
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("超时重试");
                return resultObjectVO;
            }

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
                            if(StringUtils.isNotEmpty(userDetail.getTrueName())&&StringUtils.isNotEmpty(userDetail.getIdCard()))
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
            resultObjectVO.setMsg("请求失败,请稍后重试");
        }finally{
            skylarkLock.unLock(UserCenterLoginRedisKey.getVerifyRealNameLockKey(userMainId), userMainId);
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
                List<UserMobilePhone> userMobilePhones = userMobilePhoneService.queryListByUserMainId(userIdArray);
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


            //查询用户手机号关联表
            resultObjectVO.setData(userMobilePhoneService.queryListPageNothingDeleteStatus(userPageInfo));

        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);

            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请求失败,请稍后重试");
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


            //查询用户邮箱关联表
            resultObjectVO.setData(userEmailService.queryListPageNothingDeleteStatus(userPageInfo));

        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);

            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请求失败,请稍后重试");
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


            //查询用户名关联表
            resultObjectVO.setData(userUserNameService.queryListPageNothingDeleteStatus(userPageInfo));

        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);

            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请求失败,请稍后重试");
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
            resultObjectVO.setMsg("请求失败,没有找到实体对象");
            return resultObjectVO;
        }

        try {
            User entity = JSONObject.parseObject(requestVo.getEntityJson(),User.class);
            if(entity.getUserMainId()==null)
            {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("请求失败,没有找到ID");
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
                userService.updateEnableStatus(enableStatus, entity.getUserMainId());

                try {
                    List<UserElasticSearchVO> userElasticSearchVOS = userElasticSearchService.queryByUserMainId(entity.getUserMainId());
                    if(CollectionUtils.isNotEmpty(userElasticSearchVOS))
                    {
                        UserElasticSearchVO userElasticSearchVO = userElasticSearchVOS.get(0);
                        userElasticSearchVO.setEnableStatus(enableStatus);
                        userElasticSearchService.update(userElasticSearchVO);
                    }
                }catch(Exception e)
                {
                    resultObjectVO.setCode(ResultVO.FAILD);
                    resultObjectVO.setMsg("更新用户缓存出现异常");
                    logger.warn(e.getMessage(),e);
                }
            }

            resultObjectVO.setData(entity);

        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);

            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请求失败,请稍后重试");
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
    public ResultObjectVO disabledEnabledMobilePhoneByUserMainIdAndMobilePhone(@RequestBody RequestJsonVO requestVo){
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        if(requestVo==null||requestVo.getEntityJson()==null)
        {
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请求失败,没有找到实体对象");
            return resultObjectVO;
        }

        try {
            UserVO entity = JSONObject.parseObject(requestVo.getEntityJson(),UserVO.class);
            if(entity.getUserMainId()==null)
            {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("请求失败,没有找到ID");
                return resultObjectVO;
            }
            if(StringUtils.isEmpty(entity.getMobilePhone()))
            {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("请求失败,没有找到手机号");
                return resultObjectVO;
            }

            UserMobilePhone queryUserMobile = new UserMobilePhone();
            queryUserMobile.setUserMainId(entity.getUserMainId());
            queryUserMobile.setMobilePhone(entity.getMobilePhone());

            List<UserMobilePhone> userMobilePhones = userMobilePhoneService.findListByEntityNothingDeleteStatus(queryUserMobile);
            if(CollectionUtils.isNotEmpty(userMobilePhones)) {
                short deleteStatus = 0;
                int row = 0;
                if(userMobilePhones.get(0).getDeleteStatus().shortValue()==0)
                {
                    deleteStatus=1;
                    //禁用
                    userMobilePhoneService.updateDeleteStatus((short) 1, entity.getUserMainId(),entity.getMobilePhone());
                }else{

                    //查询当前手机号关联用户列表,判断手机号是否已经有其他人使用了
                    queryUserMobile.setUserMainId(null);
                    userMobilePhones = userMobilePhoneService.findListByEntity(queryUserMobile);
                    if(CollectionUtils.isNotEmpty(userMobilePhones))
                    {
                        for(UserMobilePhone userMobilePhone:userMobilePhones)
                        {
                            if(userMobilePhone.getUserMainId()!=entity.getUserMainId())
                            {
                                resultObjectVO.setCode(ResultVO.FAILD);
                                resultObjectVO.setMsg("请求失败,手机号无法启用,已经有人在使用中了");
                                return resultObjectVO;
                            }
                        }
                    }
                    //禁用用户ID下所有关联手机号
                    userMobilePhoneService.deleteByUserMainId(entity.getUserMainId());
                    //启用
                    row = userMobilePhoneService.updateDeleteStatus((short) 0, entity.getUserMainId(),entity.getMobilePhone());
                    deleteStatus=0;
                }

                try {
                    List<UserElasticSearchVO> userElasticSearchVOS = userElasticSearchService.queryByUserMainId(entity.getUserMainId());
                    if(CollectionUtils.isNotEmpty(userElasticSearchVOS))
                    {
                        UserElasticSearchVO userElasticSearchVO = userElasticSearchVOS.get(0);
                        //删除用户手机号关联
                        if(deleteStatus==1)
                        {
                            userElasticSearchVO.setMobilePhone(null);
                        }else{
                            if(row>0) { //数据库操作成功后 设置手机号到缓存
                                userElasticSearchVO.setMobilePhone(entity.getMobilePhone());
                            }
                        }
                        userElasticSearchService.update(userElasticSearchVO);
                    }
                }catch(Exception e)
                {
                    resultObjectVO.setCode(ResultVO.FAILD);
                    resultObjectVO.setMsg("更新用户缓存出现异常");
                    logger.warn(e.getMessage(),e);
                }
            }

            resultObjectVO.setData(entity);

        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);

            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请求失败,请稍后重试");
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
    public ResultObjectVO disabledEnabledEmailByUserMainIdAndEmail(@RequestBody RequestJsonVO requestVo){
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        if(requestVo==null||requestVo.getEntityJson()==null)
        {
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请求失败,没有找到实体对象");
            return resultObjectVO;
        }

        try {
            UserVO entity = JSONObject.parseObject(requestVo.getEntityJson(),UserVO.class);
            if(entity.getUserMainId()==null)
            {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("请求失败,没有找到ID");
                return resultObjectVO;
            }
            if(StringUtils.isEmpty(entity.getEmail()))
            {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("请求失败,没有找到邮箱");
                return resultObjectVO;
            }

            UserEmail queryUserEmail = new UserEmail();
            queryUserEmail.setUserMainId(entity.getUserMainId());
            queryUserEmail.setEmail(entity.getEmail());

            List<UserEmail> userEmails = userEmailService.findListByEntityNothingDeleteStatus(queryUserEmail);
            if(CollectionUtils.isNotEmpty(userEmails)) {
                short deleteStatus = 0;
                int row = 0;
                if(userEmails.get(0).getDeleteStatus().shortValue()==0)
                {
                    deleteStatus=1;
                    //禁用
                    userEmailService.updateDeleteStatus((short) 1, entity.getUserMainId(),entity.getEmail());
                }else{

                    //查询当前邮箱关联用户列表,判断邮箱是否已经有其他人使用了
                    queryUserEmail.setUserMainId(null);
                    userEmails = userEmailService.findListByEntity(queryUserEmail);
                    if(CollectionUtils.isNotEmpty(userEmails))
                    {
                        for(UserEmail userEmail:userEmails)
                        {
                            if(userEmail.getUserMainId()!=entity.getUserMainId())
                            {
                                resultObjectVO.setCode(ResultVO.FAILD);
                                resultObjectVO.setMsg("请求失败,邮箱无法启用,已经有人在使用中了");
                                return resultObjectVO;
                            }
                        }
                    }
                    //禁用用户ID下所有关联邮箱
                    userEmailService.deleteByUserMainId(entity.getUserMainId());
                    //启用
                    row = userEmailService.updateDeleteStatus((short) 0, entity.getUserMainId(),entity.getEmail());
                    deleteStatus=0;
                }

                try {
                    List<UserElasticSearchVO> userElasticSearchVOS = userElasticSearchService.queryByUserMainId(entity.getUserMainId());
                    if(CollectionUtils.isNotEmpty(userElasticSearchVOS))
                    {
                        UserElasticSearchVO userElasticSearchVO = userElasticSearchVOS.get(0);
                        //删除用户邮箱关联
                        if(deleteStatus==1)
                        {
                            userElasticSearchVO.setEmail(null);
                        }else{
                            if(row>0) { //数据库操作成功后 设置邮箱到缓存
                                userElasticSearchVO.setEmail(entity.getEmail());
                            }
                        }
                        userElasticSearchService.update(userElasticSearchVO);
                    }
                }catch(Exception e)
                {
                    resultObjectVO.setCode(ResultVO.FAILD);
                    resultObjectVO.setMsg("更新用户缓存出现异常");
                    logger.warn(e.getMessage(),e);
                }
            }

            resultObjectVO.setData(entity);

        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);

            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请求失败,请稍后重试");
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
            resultObjectVO.setMsg("请求失败,没有找到实体对象");
            return resultObjectVO;
        }

        try {
            UserVO entity = JSONObject.parseObject(requestVo.getEntityJson(),UserVO.class);
            if(entity.getUserMainId()==null)
            {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("请求失败,没有找到ID");
                return resultObjectVO;
            }
            if(StringUtils.isEmpty(entity.getUsername()))
            {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("请求失败,没有找到用户名");
                return resultObjectVO;
            }

            UserUserName queryUsername = new UserUserName();
            queryUsername.setUserMainId(entity.getUserMainId());
            queryUsername.setUsername(entity.getUsername());

            List<UserUserName> userUserNames = userUserNameService.findListByEntityNothingDeleteStatus(queryUsername);
            if(CollectionUtils.isNotEmpty(userUserNames)) {
                short deleteStatus = 0;
                int row = 0;
                if(userUserNames.get(0).getDeleteStatus().shortValue()==0)
                {
                    deleteStatus=1;
                    //禁用
                    userUserNameService.updateDeleteStatus((short) 1, entity.getUserMainId(),entity.getUsername());
                }else{

                    //查询当前邮箱关联用户列表,判断邮箱是否已经有其他人使用了
                    queryUsername.setUserMainId(null);
                    userUserNames = userUserNameService.findListByEntity(queryUsername);
                    if(CollectionUtils.isNotEmpty(userUserNames))
                    {
                        for(UserUserName userUserName:userUserNames)
                        {
                            if(userUserName.getUserMainId()!=entity.getUserMainId())
                            {
                                resultObjectVO.setCode(ResultVO.FAILD);
                                resultObjectVO.setMsg("请求失败,用户名无法启用,已经有人在使用中了");
                                return resultObjectVO;
                            }
                        }
                    }
                    //禁用用户ID下所有关联用户名
                    userUserNameService.deleteByUserMainId(entity.getUserMainId());
                    //启用
                    row = userUserNameService.updateDeleteStatus((short) 0, entity.getUserMainId(),entity.getUsername());
                    deleteStatus=0;
                }

                try {
                    List<UserElasticSearchVO> userElasticSearchVOS = userElasticSearchService.queryByUserMainId(entity.getUserMainId());
                    if(CollectionUtils.isNotEmpty(userElasticSearchVOS))
                    {
                        UserElasticSearchVO userElasticSearchVO = userElasticSearchVOS.get(0);
                        //删除用户和用户名关联
                        if(deleteStatus==1)
                        {
                            userElasticSearchVO.setUsername(null);
                        }else{
                            if(row>0) { //数据库操作成功后 设置用户名到缓存
                                userElasticSearchVO.setUsername(entity.getUsername());
                            }
                        }
                        userElasticSearchService.update(userElasticSearchVO);
                    }
                }catch(Exception e)
                {
                    resultObjectVO.setCode(ResultVO.FAILD);
                    resultObjectVO.setMsg("更新用户缓存出现异常");
                    logger.warn(e.getMessage(),e);
                }
            }

            resultObjectVO.setData(entity);

        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);

            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请求失败,请稍后重试");
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
            resultObjectVO.setMsg("请求失败,没有找到实体对象");
            return resultObjectVO;
        }

        try {
            List<User> users = JSONObject.parseArray(requestVo.getEntityJson(),User.class);
            if(CollectionUtils.isEmpty(users))
            {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("请求失败,没有找到ID");
                return resultObjectVO;
            }
            List<ResultObjectVO> resultObjectVOList = new ArrayList<ResultObjectVO>();
            for(User user:users) {
                if(user.getUserMainId()!=null) {

                    //用户主表禁用
                    userService.updateEnableStatus((short)0,user.getUserMainId());

                }
            }
            resultObjectVO.setData(resultObjectVOList);

        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);

            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请求失败,请稍后重试");
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
            resultObjectVO.setMsg("请求失败,没有找到实体对象");
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
                userVO.setNickName(userDetail.getNickName()); //昵称
                userVO.setTrueName(userDetail.getTrueName()); //姓名
                userVO.setIdCard(userDetail.getIdCard()); //身份证
                userVO.setHeadSculpture(userDetail.getHeadSculpture()); //头像
                userVO.setSex(userDetail.getSex()); //性别
                userVO.setType(userDetail.getType()); //用户类型
            }

            resultObjectVO.setData(userVO);

        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);

            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请求失败,请稍后重试");
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
        if(StringUtils.isEmpty(userRegistVO.getMobilePhone()))
        {
            resultObjectVO.setCode(UserRegistConstant.NOT_FOUND_MOBILE);
            resultObjectVO.setMsg("请求失败,请输入手机号");
            return resultObjectVO;
        }

        if(!PhoneUtils.isPhoneLegal(userRegistVO.getMobilePhone()))
        {
            resultObjectVO.setCode(UserRegistConstant.MOBILE_ERROR);
            resultObjectVO.setMsg("请求失败,手机号格式错误");
            return resultObjectVO;
        }

        if(userRegistVO.getUserMainId()==null)
        {
            resultObjectVO.setCode(UserRegistConstant.NOT_FOUND_USER);
            resultObjectVO.setMsg("请求失败,没有找到要用户主ID");
            return resultObjectVO;
        }

        try {
            boolean lockStatus = skylarkLock.lock(UserCenterRegistRedisKey.getRegistLockKey(userRegistVO.getMobilePhone()), userRegistVO.getMobilePhone());
            if (!lockStatus) {
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("超时重试");
                return resultObjectVO;
            }
            //查询手机号是否已经关联
            UserMobilePhone query = new UserMobilePhone();
            query.setMobilePhone(userRegistVO.getMobilePhone());
            List<UserMobilePhone> userMobilePhones = userMobilePhoneService.findListByEntity(query);
            if (!CollectionUtils.isEmpty(userMobilePhones)) {
                resultObjectVO.setCode(UserResultVO.FAILD);
                resultObjectVO.setMsg("关联失败，手机号已注册!");
            } else {
                query = new UserMobilePhone();
                query.setUserMainId(userRegistVO.getUserMainId());
                userMobilePhones = userMobilePhoneService.findListByEntity(query);
                if(CollectionUtils.isNotEmpty(userMobilePhones))
                {
                    resultObjectVO.setCode(UserResultVO.FAILD);
                    resultObjectVO.setMsg("关联失败，该用户已存在使用中手机号!");
                }else {
                    query.setMobilePhone(userRegistVO.getMobilePhone());
                    userMobilePhones = userMobilePhoneService.findListByEntityNothingDeleteStatus(query);
                    if(CollectionUtils.isNotEmpty(userMobilePhones))
                    {
                        resultObjectVO.setCode(UserResultVO.FAILD);
                        resultObjectVO.setMsg("关联失败，手机号已经关联到该用户了!");
                    }else {

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
                            resultObjectVO.setMsg("请求失败,请重试!");
                        }else {

                            try {
                                List<UserElasticSearchVO> userElasticSearchVOS = userElasticSearchService.queryByUserMainId(userRegistVO.getUserMainId());
                                if (CollectionUtils.isNotEmpty(userElasticSearchVOS)) {
                                    UserElasticSearchVO userElasticSearchVO = userElasticSearchVOS.get(0);
                                    userElasticSearchVO.setMobilePhone(userRegistVO.getMobilePhone());
                                    userElasticSearchService.update(userElasticSearchVO);
                                }
                            } catch (Exception e) {
                                resultObjectVO.setCode(ResultVO.FAILD);
                                resultObjectVO.setMsg("更新用户缓存出现异常");
                                logger.warn(e.getMessage(), e);
                            }
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
            skylarkLock.unLock(UserCenterRegistRedisKey.getRegistLockKey(userRegistVO.getMobilePhone()), userRegistVO.getMobilePhone());
        }
        return resultObjectVO;
    }


}
