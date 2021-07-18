package com.toucan.shopping.cloud.apps.web.controller.user;


import com.alibaba.fastjson.JSONObject;
import com.toucan.shopping.cloud.apps.web.redis.UserLoginRedisKey;
import com.toucan.shopping.cloud.apps.web.redis.VerifyCodeRedisKey;
import com.toucan.shopping.cloud.apps.web.util.VCodeUtil;
import com.toucan.shopping.cloud.user.api.feign.service.FeignSmsService;
import com.toucan.shopping.cloud.user.api.feign.service.FeignUserService;
import com.toucan.shopping.modules.common.util.*;
import com.toucan.shopping.modules.redis.service.ToucanStringRedisService;
import com.toucan.shopping.modules.skylark.lock.service.SkylarkLock;
import com.toucan.shopping.modules.user.constant.SmsTypeConstant;
import com.toucan.shopping.modules.user.constant.UserLoginConstant;
import com.toucan.shopping.modules.user.constant.UserRegistConstant;
import com.toucan.shopping.modules.user.entity.UserMobilePhone;
import com.toucan.shopping.modules.user.redis.UserCenterLoginRedisKey;
import com.toucan.shopping.modules.user.vo.UserLoginVO;
import com.toucan.shopping.modules.user.vo.UserRegistVO;
import com.toucan.shopping.modules.user.vo.UserSmsVO;
import com.toucan.shopping.modules.common.generator.RequestJsonVOGenerator;
import com.toucan.shopping.modules.common.properties.Toucan;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.common.vo.ResultVO;
import com.toucan.shopping.cloud.apps.web.controller.BaseController;
import com.toucan.shopping.cloud.apps.web.redis.UserRegistRedisKey;
import com.toucan.shopping.modules.user.vo.UserVO;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 用户注册、用户登录
 */
@RestController("apiUserController")
@RequestMapping("/api/user")
public class UserApiController extends BaseController {


    private final Logger logger = LoggerFactory.getLogger(getClass());


    @Autowired
    private SkylarkLock redisLock;

    @Autowired
    private ToucanStringRedisService toucanStringRedisService;


    @Autowired
    private FeignSmsService feignSmsService;


    @Autowired
    private FeignUserService feignUserService;


    @Autowired
    private SkylarkLock skylarkLock;



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
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(), userRegistVO);
            resultObjectVO = feignUserService.findByMobilePhone(SignUtil.sign(requestJsonVO),requestJsonVO);
            if(resultObjectVO.SUCCESS.intValue()==ResultObjectVO.FAILD.intValue())
            {
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("发送失败,请稍候重试");
                return resultObjectVO;
            }
            if(resultObjectVO.getData()!=null)
            {
                List<UserMobilePhone> userMobilePhoneList = JSONObject.parseArray(JSONObject.toJSONString(resultObjectVO.getData()), UserMobilePhone.class);
                if(CollectionUtils.isNotEmpty(userMobilePhoneList))
                {
                    resultObjectVO.setCode(ResultObjectVO.FAILD);
                    resultObjectVO.setMsg("手机号已经注册");
                    return resultObjectVO;
                }
            }

            Object mobilePhoneValue = toucanStringRedisService.get(UserRegistRedisKey.getVerifyCodeKey(mobilePhone));
            if(mobilePhoneValue==null||StringUtils.isNotEmpty(String.valueOf(mobilePhoneValue)))
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
                resultObjectVO.setMsg("超时重试");
                return resultObjectVO;
            }


            //保存生成验证码到缓存
            String code = NumberUtil.random(6);
            userSmsVO.setMsg("[犀鸟电商]您于"+ DateUtils.format(DateUtils.currentDate(), DateUtils.FORMATTER_DD_CN)+"申请了手机号码注册,验证码是"+code);
            requestJsonVO = RequestJsonVOGenerator.generator(this.getAppCode(),userSmsVO);

            resultObjectVO = feignSmsService.send(SignUtil.sign(requestJsonVO),requestJsonVO);
            if(resultObjectVO.getCode().intValue()== ResultObjectVO.SUCCESS.intValue())
            {
                //将验证码保存到缓存
                toucanStringRedisService.set(UserRegistRedisKey.getVerifyCodeKey(userSmsVO.getMobilePhone()),code);
                //默认1分钟过期
                toucanStringRedisService.expire(UserRegistRedisKey.getVerifyCodeKey(userSmsVO.getMobilePhone()),60*1, TimeUnit.SECONDS);
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
    public ResultObjectVO regist(UserRegistVO user){
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        if(user==null)
        {
            resultObjectVO.setCode(UserRegistConstant.NOT_FOUND_USER);
            resultObjectVO.setMsg("注册失败,没有找到要注册的用户");
            return resultObjectVO;
        }
        if(StringUtils.isEmpty(user.getMobilePhone()))
        {
            resultObjectVO.setCode(UserRegistConstant.NOT_FOUND_MOBILE);
            resultObjectVO.setMsg("注册失败,请输入注册手机号");
            return resultObjectVO;
        }

        if(StringUtils.isEmpty(user.getVcode()))
        {
            resultObjectVO.setCode(UserRegistConstant.NOT_FOUND_VCODE);
            resultObjectVO.setMsg("注册失败,请输入验证码");
            return resultObjectVO;
        }
        if(!PhoneUtils.isChinaPhoneLegal(user.getMobilePhone()))
        {
            resultObjectVO.setCode(UserRegistConstant.MOBILE_ERROR);
            resultObjectVO.setMsg("注册失败,手机号错误");
            return resultObjectVO;
        }

        if(StringUtils.isEmpty(user.getPassword()))
        {
            resultObjectVO.setCode(UserRegistConstant.PASSWORD_NOT_FOUND);
            resultObjectVO.setMsg("注册失败,请输入密码");
            return resultObjectVO;
        }
        if(!StringUtils.equals(user.getPassword(),user.getConfirmPassword()))
        {
            resultObjectVO.setCode(UserRegistConstant.PASSWORD_NOT_FOUND);
            resultObjectVO.setMsg("注册失败,密码与确认密码不一致");
            return resultObjectVO;
        }

        if(!UserRegistUtil.checkPwd(user.getPassword()))
        {
            resultObjectVO.setCode(UserRegistConstant.PASSWORD_ERROR);
            resultObjectVO.setMsg("注册失败,请输入6至15位的密码");
            return resultObjectVO;
        }



        try {
            boolean lockStatus = redisLock.lock(UserRegistRedisKey.getRegistLockKey(user.getMobilePhone()), user.getMobilePhone());
            if (!lockStatus) {
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("超时重试");
                return resultObjectVO;
            }
            //判断验证码

            Object vcodeObject = toucanStringRedisService.get(UserRegistRedisKey.getVerifyCodeKey(user.getMobilePhone()));
            if(vcodeObject==null)
            {
                //释放注册锁
                redisLock.unLock(UserRegistRedisKey.getRegistLockKey(user.getMobilePhone()), user.getMobilePhone());
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("注册失败,请发送验证码");
                return resultObjectVO;
            }
            if(!user.getVcode().equals(String.valueOf(vcodeObject)))
            {
                //释放注册锁
                redisLock.unLock(UserRegistRedisKey.getRegistLockKey(user.getMobilePhone()), user.getMobilePhone());
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("注册失败,验证码输入有误");
                return resultObjectVO;
            }



            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(getAppCode(),user);

            logger.info(" 用户注册 {} ", user.getMobilePhone());

            resultObjectVO = feignUserService.registByMobilePhone(SignUtil.sign(requestJsonVO),requestJsonVO);
            if(resultObjectVO.getCode().intValue()==ResultObjectVO.SUCCESS.intValue())
            {
                //删除验证码
                toucanStringRedisService.delete(UserRegistRedisKey.getVerifyCodeKey(user.getMobilePhone()));
            }

            resultObjectVO.setData(null);
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("注册失败,请稍后重试");
        }finally{
            redisLock.unLock(UserRegistRedisKey.getRegistLockKey(user.getMobilePhone()), user.getMobilePhone());
        }
        return resultObjectVO;
    }

    @RequestMapping(value="/info")
    @ResponseBody
    public ResultObjectVO info(HttpServletRequest httpServletRequest){

        ResultObjectVO resultObjectVO = new ResultObjectVO();

        try {
            UserVO userVO = new UserVO();
            userVO.setUserMainId(Long.parseLong(UserAuthHeaderUtil.getUserMainId(toucan.getAppCode(), httpServletRequest.getHeader(this.getToucan().getUserAuth().getHttpToucanAuthHeader()))));
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(), userVO);
            resultObjectVO = feignUserService.queryLoginInfo(requestJsonVO.sign(),requestJsonVO);
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请求失败,请稍候重试");
        }
        return resultObjectVO;
    }




    @RequestMapping(value="/is/online")
    @ResponseBody
    public ResultObjectVO isOnline(HttpServletRequest httpServletRequest){

        ResultObjectVO resultObjectVO = new ResultObjectVO();
        //在这里调用用户中心 判断登录
        UserLoginVO queryUserLogin = new UserLoginVO();
        try {
            String uid = UserAuthHeaderUtil.getUserMainId(toucan.getAppCode(),httpServletRequest.getHeader(this.getToucan().getUserAuth().getHttpToucanAuthHeader()));
            String lt =  UserAuthHeaderUtil.getToken(toucan.getAppCode(),httpServletRequest.getHeader(this.getToucan().getUserAuth().getHttpToucanAuthHeader()));
            queryUserLogin.setUserMainId(Long.parseLong(uid));
            queryUserLogin.setLoginToken(lt);
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generatorByUser(toucan.getAppCode(),uid,queryUserLogin);
            resultObjectVO = feignUserService.isOnline(SignUtil.sign(requestJsonVO),requestJsonVO);
            return resultObjectVO;
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请求失败,请稍候重试");
        }
        resultObjectVO.setData(false);
        return resultObjectVO;
    }



    /**
     * 用户密码登录
     * @param userLoginVO
     * @return
     */
    @RequestMapping(value="/login/password",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO loginByPassword(@RequestBody UserLoginVO userLoginVO,HttpServletResponse response, HttpServletRequest request) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        if (userLoginVO == null) {
            resultObjectVO.setCode(UserLoginConstant.NOT_FOUND_USER);
            resultObjectVO.setMsg("登录失败,没有找到要登录的用户");
            return resultObjectVO;
        }

        if(StringUtils.isEmpty(userLoginVO.getLoginUserName()))
        {
            resultObjectVO.setCode(UserLoginConstant.USERNAME_NOT_FOUND);
            resultObjectVO.setMsg("登录失败,请输入用户名");
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
            resultObjectVO.setMsg("登录失败,请输入6至15位的密码");
            return resultObjectVO;
        }

        try {

            boolean lockStatus = skylarkLock.lock(UserLoginRedisKey.getLoginKey(userLoginVO.getLoginUserName()), userLoginVO.getLoginUserName());
            if (!lockStatus) {
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("登录超时,请稍候重试");
                return resultObjectVO;
            }

            //查询登录次数,失败3次要求输入验证码
            String loginFaildCountKey = UserLoginRedisKey.getLoginFaildCountKey(IPUtil.getRemoteAddr(request));
            String vcodeRedisKey = VerifyCodeRedisKey.getLoginFaildVerifyCodeKey(this.getAppCode(),IPUtil.getRemoteAddr(request));
            Object loginFaildCountValueObject = toucanStringRedisService.get(loginFaildCountKey);
            Integer faildCount = 0;
            if(loginFaildCountValueObject!=null)
            {
                faildCount = Integer.parseInt(String.valueOf(loginFaildCountValueObject));
                //判断验证码
                if(faildCount>=3)
                {
                    if(StringUtils.isEmpty(userLoginVO.getVcode()))
                    {
                        resultObjectVO.setCode(UserRegistConstant.SHOW_LOGIN_VERIFY_CODE);
                        resultObjectVO.setMsg("登录失败,请输入验证码");
                        //释放锁
                        skylarkLock.unLock(UserLoginRedisKey.getLoginKey(userLoginVO.getLoginUserName()), userLoginVO.getLoginUserName());
                        return resultObjectVO;
                    }


                    Object vCodeObject = toucanStringRedisService.get(vcodeRedisKey);
                    if(vCodeObject==null)
                    {
                        resultObjectVO.setMsg("登录失败,验证码过期,请刷新");
                        resultObjectVO.setCode(UserRegistConstant.SHOW_LOGIN_VERIFY_CODE);
                        //释放锁
                        skylarkLock.unLock(UserLoginRedisKey.getLoginKey(userLoginVO.getLoginUserName()), userLoginVO.getLoginUserName());
                        return resultObjectVO;
                    }
                    if(!StringUtils.equals(userLoginVO.getVcode().toUpperCase(),String.valueOf(vCodeObject).toUpperCase()))
                    {
                        resultObjectVO.setMsg("登录失败,验证码输入有误");
                        resultObjectVO.setCode(UserRegistConstant.LOGIN_VERIFY_CODE_FAILD);
                        //释放锁
                        skylarkLock.unLock(UserLoginRedisKey.getLoginKey(userLoginVO.getLoginUserName()), userLoginVO.getLoginUserName());

                        return resultObjectVO;
                    }
                    //校验通过,删除验证码
                    toucanStringRedisService.delete(vcodeRedisKey);
                }
            }


            userLoginVO.setAppCode(this.getAppCode());
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(this.getAppCode(),userLoginVO);
            resultObjectVO = feignUserService.loginByPassword(SignUtil.sign(requestJsonVO),requestJsonVO);
            if(resultObjectVO.isSuccess())
            {
                userLoginVO = (UserLoginVO)resultObjectVO.formatData(UserLoginVO.class);
                if(userLoginVO==null)
                {
                    resultObjectVO.setMsg("登陆失败,请重新输入");
                    resultObjectVO.setCode(UserRegistConstant.SHOW_LOGIN_VERIFY_CODE);
                }else {
                    //UID
                    Cookie uidCookie = new Cookie(toucan.getAppCode() + "_uid", String.valueOf(userLoginVO.getUserMainId()));
                    uidCookie.setPath("/");
                    //1天时间不过期,如果设置为永不过期的话 客户端浏览器不能删除cookie
                    uidCookie.setMaxAge(86400);
                    response.addCookie(uidCookie);

                    //TOKEN
                    Cookie ltCookie = new Cookie(toucan.getAppCode() + "_lt", userLoginVO.getLoginToken());
                    ltCookie.setPath("/");
                    //1天时间不过期,如果设置为永不过期的话 客户端浏览器不能删除cookie
                    ltCookie.setMaxAge(86400);
                    response.addCookie(ltCookie);

                    //删除登录失败计数
                    toucanStringRedisService.delete(loginFaildCountKey);

                    //删除登录验证码
                    toucanStringRedisService.delete(vcodeRedisKey);
                }
            }else{
                if(loginFaildCountValueObject==null)
                {
                    toucanStringRedisService.set(loginFaildCountKey,1);
                }else {
                    toucanStringRedisService.set(loginFaildCountKey, faildCount + 1);

                    if(faildCount+1>3)
                    {
                        //输入失败3次,需要输入验证码
                        resultObjectVO.setCode(UserRegistConstant.SHOW_LOGIN_VERIFY_CODE);
                    }
                }
                //10分钟之内 输入错误3次 要求验证码
                toucanStringRedisService.expire(loginFaildCountKey,60*10, TimeUnit.SECONDS);

            }
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);

            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("登录失败,请稍后重试");
        }finally{

            //释放锁
            skylarkLock.unLock(UserLoginRedisKey.getLoginKey(userLoginVO.getLoginUserName()), userLoginVO.getLoginUserName());
        }

        return resultObjectVO;
    }


    @RequestMapping(value="/login/faild/vcode", method = RequestMethod.GET)
    public void loginFaildVerifyCode(HttpServletRequest request,HttpServletResponse response) {
        OutputStream outputStream = null;
        try {
            outputStream = response.getOutputStream();
            int w = 200, h = 80;
            //生成4位验证码
            String code = VerifyCodeUtil.generateVerifyCode(4);


            //生成客户端验证码ID
            String vcodeRedisKey = VerifyCodeRedisKey.getLoginFaildVerifyCodeKey(this.getAppCode(), IPUtil.getRemoteAddr(request));
            toucanStringRedisService.set(vcodeRedisKey,code);
            toucanStringRedisService.expire(vcodeRedisKey,60, TimeUnit.SECONDS);


            VerifyCodeUtil.outputImage(w, h, outputStream, code);
        } catch (IOException e) {
            logger.warn(e.getMessage(),e);
        }finally{
            if(outputStream!=null)
            {
                try {
                    outputStream.flush();
                    outputStream.close();
                }catch(Exception e)
                {
                    logger.warn(e.getMessage(),e);
                }
            }
        }
    }


    @RequestMapping(value="/vcode", method = RequestMethod.GET)
    public void verifyCode(HttpServletResponse response) {
        OutputStream outputStream = null;
        try {
            outputStream = response.getOutputStream();
            int w = 200, h = 80;
            //生成4位验证码
            String code = VerifyCodeUtil.generateVerifyCode(4);


            //生成客户端验证码ID
            String vcodeUuid = GlobalUUID.uuid();
            String vcodeRedisKey = VerifyCodeRedisKey.getVerifyCodeKey(this.getAppCode(),vcodeUuid);
            toucanStringRedisService.set(vcodeRedisKey,code);
            toucanStringRedisService.expire(vcodeRedisKey,60, TimeUnit.SECONDS);

            Cookie clientVCodeId = new Cookie("clientVCodeId",vcodeUuid);
            clientVCodeId.setPath("/");
            //60秒过期
            clientVCodeId.setMaxAge(60);
            response.addCookie(clientVCodeId);

            VerifyCodeUtil.outputImage(w, h, outputStream, code);
        } catch (IOException e) {
            logger.warn(e.getMessage(),e);
        }finally{
            if(outputStream!=null)
            {
                try {
                    outputStream.flush();
                    outputStream.close();
                }catch(Exception e)
                {
                    logger.warn(e.getMessage(),e);
                }
            }
        }
    }


//
//
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
//                resultObjectVO.setMsg("超时重试");
//                return resultObjectVO;
//            }
//
//            //调用第三方短信接口
//
//            String code = NumberUtil.random(6);
//            toucanStringRedisService.opsForValue().set(UserCenterRedisKey.getLoginLockKey(requestJsonVO.getAppCode(),user.getMobile()),code);
//            //默认3分钟过期
//            toucanStringRedisService.expire(UserCenterRedisKey.getLoginLockKey(requestJsonVO.getAppCode(),user.getMobile()),60*3, TimeUnit.SECONDS);
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
