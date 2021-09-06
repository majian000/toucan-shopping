package com.toucan.shopping.standard.apps.web.controller.api.user;


import com.alibaba.fastjson.JSONObject;
import com.toucan.shopping.modules.common.generator.IdGenerator;
import com.toucan.shopping.modules.common.lock.redis.RedisLock;
import com.toucan.shopping.modules.common.util.*;
import com.toucan.shopping.modules.common.generator.RequestJsonVOGenerator;
import com.toucan.shopping.modules.common.properties.Toucan;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.common.vo.ResultVO;
import com.toucan.shopping.modules.user.constant.SmsTypeConstant;
import com.toucan.shopping.modules.user.constant.UserLoginConstant;
import com.toucan.shopping.modules.user.constant.UserRegistConstant;
import com.toucan.shopping.modules.user.entity.User;
import com.toucan.shopping.modules.user.entity.UserDetail;
import com.toucan.shopping.modules.user.entity.UserMobilePhone;
import com.toucan.shopping.modules.user.kafka.message.UserCreateMessage;
import com.toucan.shopping.modules.user.redis.UserCenterLoginRedisKey;
import com.toucan.shopping.modules.user.service.*;
import com.toucan.shopping.modules.user.vo.UserLoginVO;
import com.toucan.shopping.modules.user.vo.UserRegistVO;
import com.toucan.shopping.modules.user.vo.UserResultVO;
import com.toucan.shopping.modules.user.vo.UserSmsVO;
import com.toucan.shopping.standard.apps.web.controller.BaseController;
import com.toucan.shopping.standard.apps.web.redis.UserRegistRedisKey;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * 用户注册、用户登录
 */
@RestController("apiUserController")
@RequestMapping("/api/user")
public class UserApiController extends BaseController {


    private final Logger logger = LoggerFactory.getLogger(getClass());


    @Autowired
    private IdGenerator idGenerator;

    @Autowired
    private RedisLock redisLock;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private UserMobilePhoneService userMobilePhoneService;

    @Autowired
    private UserUserNameService userUserNameService;

    @Autowired
    private UserDetailService userDetailService;

    @Autowired
    private SmsService smsService;


    @Autowired
    private UserService userService;

    @Autowired
    private Toucan toucan;


    /**
     * 发送注册验证码
     * @return
     */
    @RequestMapping("/sendRegistVerifyCode")
    @ResponseBody
    public ResultObjectVO sendRegistVerifyCode(String mobilePhone)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        if(StringUtils.isEmpty(mobilePhone)||!PhoneUtils.isChinaPhoneLegal(mobilePhone))
        {
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            resultObjectVO.setMsg("发送失败,手机号错误");
            return resultObjectVO;
        }

        try {
            UserRegistVO userRegistVO = new UserRegistVO();
            userRegistVO.setMobilePhone(mobilePhone);
            List<UserMobilePhone> userEntityList = userMobilePhoneService.findListByMobilePhone(userRegistVO.getMobilePhone());
            if(CollectionUtils.isNotEmpty(userEntityList))
            {
                List<UserMobilePhone> userMobilePhoneList = JSONObject.parseArray(JSONObject.toJSONString(resultObjectVO.getData()), UserMobilePhone.class);
                if(CollectionUtils.isNotEmpty(userMobilePhoneList))
                {
                    resultObjectVO.setCode(ResultObjectVO.FAILD);
                    resultObjectVO.setMsg("手机号已经注册");
                    return resultObjectVO;
                }
            }

            if(StringUtils.isNotEmpty(stringRedisTemplate.opsForValue().get(UserRegistRedisKey.getVerifyCodeKey(mobilePhone))))
            {
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("已发送,请在1分钟后重新获取");
                return resultObjectVO;
            }

            UserSmsVO userSmsVO = new UserSmsVO();
            userSmsVO.setMobilePhone(mobilePhone);;
            userSmsVO.setType(SmsTypeConstant.USER_REGIST_TYPE);
            boolean lockStatus = redisLock.lock(UserRegistRedisKey.getVerifyCodeLockKey(mobilePhone), mobilePhone);
            if (!lockStatus) {
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("请求超时,请稍后重试");
                return resultObjectVO;
            }


            //保存生成验证码到缓存
            String code = NumberUtil.random(6);
            userSmsVO.setMsg("[犀鸟电商]您于"+ DateUtils.format(DateUtils.currentDate(), DateUtils.FORMATTER_DD_CN)+"申请了手机号码注册,验证码是"+code);

            int ret = smsService.send(userSmsVO);
            if(ret==1)
            {
                //将验证码保存到缓存
                stringRedisTemplate.opsForValue().set(UserRegistRedisKey.getVerifyCodeKey(userSmsVO.getMobilePhone()),code);
                //默认1分钟过期
                stringRedisTemplate.expire(UserRegistRedisKey.getVerifyCodeKey(userSmsVO.getMobilePhone()),60*1, TimeUnit.SECONDS);
            }
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            resultObjectVO.setMsg("发送失败,请稍候重试");
        }finally{
            redisLock.unLock(UserRegistRedisKey.getVerifyCodeLockKey(mobilePhone), mobilePhone);
        }
        return resultObjectVO;


    }






    @RequestMapping(value="/regist")
    @ResponseBody
    public ResultObjectVO regist(UserRegistVO userRegistVO){
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        if(userRegistVO==null)
        {
            resultObjectVO.setCode(UserRegistConstant.NOT_FOUND_USER);
            resultObjectVO.setMsg("注册失败,没有找到要注册的用户");
            return resultObjectVO;
        }
        if(StringUtils.isEmpty(userRegistVO.getMobilePhone()))
        {
            resultObjectVO.setCode(UserRegistConstant.NOT_FOUND_MOBILE);
            resultObjectVO.setMsg("注册失败,请输入注册手机号");
            return resultObjectVO;
        }

        if(StringUtils.isEmpty(userRegistVO.getVcode()))
        {
            resultObjectVO.setCode(UserRegistConstant.NOT_FOUND_VCODE);
            resultObjectVO.setMsg("注册失败,请输入验证码");
            return resultObjectVO;
        }
        if(!PhoneUtils.isChinaPhoneLegal(userRegistVO.getMobilePhone()))
        {
            resultObjectVO.setCode(UserRegistConstant.MOBILE_ERROR);
            resultObjectVO.setMsg("注册失败,手机号错误");
            return resultObjectVO;
        }

        if(StringUtils.isEmpty(userRegistVO.getPassword()))
        {
            resultObjectVO.setCode(UserRegistConstant.PASSWORD_NOT_FOUND);
            resultObjectVO.setMsg("注册失败,请输入密码");
            return resultObjectVO;
        }
        if(!StringUtils.equals(userRegistVO.getPassword(),userRegistVO.getConfirmPassword()))
        {
            resultObjectVO.setCode(UserRegistConstant.PASSWORD_NOT_FOUND);
            resultObjectVO.setMsg("注册失败,密码与确认密码不一致");
            return resultObjectVO;
        }

        if(!UserRegistUtil.checkPwd(userRegistVO.getPassword()))
        {
            resultObjectVO.setCode(UserRegistConstant.PASSWORD_ERROR);
            resultObjectVO.setMsg("注册失败,请输入6至15位的密码");
            return resultObjectVO;
        }



        try {
            boolean lockStatus = redisLock.lock(UserRegistRedisKey.getRegistLockKey(userRegistVO.getMobilePhone()), userRegistVO.getMobilePhone());
            if (!lockStatus) {
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("请求超时,请稍后重试");
                return resultObjectVO;
            }
            //判断验证码

            String vcodeObject = stringRedisTemplate.opsForValue().get(UserRegistRedisKey.getVerifyCodeKey(userRegistVO.getMobilePhone()));
            if(vcodeObject==null)
            {
                //释放注册锁
                redisLock.unLock(UserRegistRedisKey.getRegistLockKey(userRegistVO.getMobilePhone()), userRegistVO.getMobilePhone());
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("注册失败,请发送验证码");
                return resultObjectVO;
            }
            if(!userRegistVO.getVcode().equals(vcodeObject))
            {
                //释放注册锁
                redisLock.unLock(UserRegistRedisKey.getRegistLockKey(userRegistVO.getMobilePhone()), userRegistVO.getMobilePhone());
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("注册失败,验证码输入有误");
                return resultObjectVO;
            }



            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(getAppCode(),userRegistVO);

            logger.info(" 用户注册 {} ", userRegistVO.getMobilePhone());

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
//                            newUserMessageQueue.push(userCreateMessage);

                            //删除验证码
                            stringRedisTemplate.opsForValue().getOperations().delete(UserRegistRedisKey.getVerifyCodeKey(userRegistVO.getMobilePhone()));

                        }
                    }
                }
            }

            resultObjectVO.setData(null);
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("注册失败,请稍后重试");
        }finally{
            redisLock.unLock(UserRegistRedisKey.getRegistLockKey(userRegistVO.getMobilePhone()), userRegistVO.getMobilePhone());
        }
        return resultObjectVO;
    }








    /**
     * 用户密码登录
     * @param userLoginVO
     * @return
     */
    @RequestMapping(value="/login/password",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO loginByPassword(@RequestBody UserLoginVO userLoginVO, HttpServletResponse response, HttpServletRequest request) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        if (userLoginVO == null) {
            resultObjectVO.setCode(UserLoginConstant.NOT_FOUND_USER);
            resultObjectVO.setMsg("登录失败,没有找到要登录的用户");
            return resultObjectVO;
        }

        if(StringUtils.isEmpty(userLoginVO.getLoginUserName()))
        {
            resultObjectVO.setCode(UserLoginConstant.USERNAME_NOT_FOUND);
            resultObjectVO.setMsg("登录失败,请输入账号");
            return resultObjectVO;
        }

        if (StringUtils.isEmpty(userLoginVO.getPassword())) {
            resultObjectVO.setCode(UserLoginConstant.PASSWORD_NOT_FOUND);
            resultObjectVO.setMsg("登录失败,请输入密码");
            return resultObjectVO;
        }

        if(!UserRegistUtil.checkPwd(userLoginVO.getPassword()))
        {
            resultObjectVO.setCode(UserRegistConstant.PASSWORD_ERROR);
            resultObjectVO.setMsg("注册失败,请输入6至15位的密码");
            return resultObjectVO;
        }
        try {
            boolean lockStatus = redisLock.lock(UserCenterLoginRedisKey.getLoginLockKey(userLoginVO.getLoginUserName()), userLoginVO.getLoginUserName());
            if (!lockStatus) {
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("请求超时,请稍后重试");
                return resultObjectVO;
            }

            //如果当前输入的是手机号判断手机号是否存在
            Long userId = 0L;
            if(PhoneUtils.isChinaPhoneLegal(userLoginVO.getLoginUserName()))
            {
                List<UserMobilePhone> userMobilePhones = userMobilePhoneService.findListByMobilePhone(userLoginVO.getLoginUserName());
                if(!CollectionUtils.isEmpty(userMobilePhones))
                {
                    if(userMobilePhones.get(0)!=null&&userMobilePhones.get(0).getUserMainId()!=null) {
                        userId = userMobilePhones.get(0).getUserMainId();
                    }
                }

            }else if(EmailUtils.isEmail(userLoginVO.getLoginUserName()))
            {

            }

            List<User> users = userService.findById(userId);
            if(CollectionUtils.isEmpty(users))
            {
                resultObjectVO.setCode(UserLoginConstant.NOT_REGIST);
                resultObjectVO.setMsg("登录失败,请先注册");
            }else {
                User userEntity = users.get(0);
                //判断用户启用状态
                if (userEntity.getEnableStatus().shortValue() == 1) {
                    String pwdMd5 = MD5Util.md5(userLoginVO.getPassword());
                    //登录成功 生成token
                    if (pwdMd5.equals(userEntity.getPassword())) {
                        String loginTokenGroupKey = UserCenterLoginRedisKey.getLoginInfoGroupKey(String.valueOf(userEntity.getId()));
                        String loginTokenAppKey = UserCenterLoginRedisKey.getLoginTokenAppKey(String.valueOf(userEntity.getId()), toucan.getAppCode());
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

                        userLoginVO.setId(userId);
                        userLoginVO.setLoginToken(token);
                        userLoginVO.setPassword(null);

                        resultObjectVO.setData(userLoginVO);

                    }
                }
            }

        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("登录失败,请稍后重试");
        }finally{
            redisLock.unLock(UserCenterLoginRedisKey.getLoginLockKey(userLoginVO.getLoginUserName()), userLoginVO.getLoginUserName());
        }

        return resultObjectVO;
    }




//    /**
//     * 用户短信验证码登录
//     * @param requestJsonVO
//     * @return
//     */
//    @RequestMapping(value="/login/vcode",produces = "application/json;charset=UTF-8")
//    @ResponseBody
//    public ResultObjectVO loginByVCode(@RequestBody RequestJsonVO requestJsonVO) {
//        ResultObjectVO resultObjectVO = new ResultObjectVO();
//        if (requestJsonVO == null) {
//            resultObjectVO.setCode(UserResultVO.NOT_FOUND_USER);
//            resultObjectVO.setMsg("登录失败,没有找到要登录的用户");
//            return resultObjectVO;
//        }
//
//        if (StringUtils.isEmpty(requestJsonVO.getAppCode())) {
//            resultObjectVO.setCode(UserResultVO.NOT_FOUND_USER);
//            resultObjectVO.setMsg("登录失败,没有找到应用编码");
//            return resultObjectVO;
//        }
//        User user = JSONObject.parseObject(requestJsonVO.getEntityJson(),User.class);
//        if (CollectionUtils.isEmpty(user.getUserApps())) {
//            resultObjectVO.setCode(UserResultVO.NOT_FOUND_APP);
//            resultObjectVO.setMsg("登录失败,未找到所属应用");
//            return resultObjectVO;
//        }
//
//        if (!PhoneUtils.isChinaPhoneLegal(user.getMobile())) {
//            resultObjectVO.setCode(UserResultVO.NOT_FOUND_APP);
//            resultObjectVO.setMsg("登录失败,手机号错误");
//            return resultObjectVO;
//        }
//
//        try {
//            boolean lockStatus = redisLock.lock(UserCenterRedisKey.getLoginLockKey(requestJsonVO.getAppCode(),user.getMobile()), user.getMobile());
//            if (!lockStatus) {
//                resultObjectVO.setCode(ResultObjectVO.FAILD);
//                resultObjectVO.setMsg("请求超时,请稍后重试");
//                return resultObjectVO;
//            }
//
//            //调用第三方短信接口
//
//            String code = NumberUtil.random(6);
//            stringRedisTemplate.opsForValue().set(UserCenterRedisKey.getLoginLockKey(requestJsonVO.getAppCode(),user.getMobile()),code);
//            //默认3分钟过期
//            stringRedisTemplate.expire(UserCenterRedisKey.getLoginLockKey(requestJsonVO.getAppCode(),user.getMobile()),60*3, TimeUnit.SECONDS);
//
//            redisLock.unLock(UserCenterRedisKey.getLoginLockKey(requestJsonVO.getAppCode(),user.getMobile()), user.getMobile());
//
//        }catch(Exception e)
//        {
//
//            redisLock.unLock(UserCenterRedisKey.getLoginLockKey(requestJsonVO.getAppCode(),user.getMobile()), user.getMobile());
//            logger.warn(e.getMessage(),e);
//
//            resultObjectVO.setCode(ResultVO.FAILD);
//            resultObjectVO.setMsg("发送短信验证码失败,请稍后重试");
//        }
//
//        return resultObjectVO;
//    }





}
