package com.toucan.shopping.cloud.apps.seller.web.controller.user;


import com.alibaba.fastjson.JSONObject;
import com.toucan.shopping.cloud.apps.seller.web.controller.BaseController;
import com.toucan.shopping.cloud.apps.seller.web.queue.SellerLoginHistoryQueue;
import com.toucan.shopping.cloud.apps.seller.web.redis.UserLoginRedisKey;
import com.toucan.shopping.cloud.apps.seller.web.redis.VerifyCodeRedisKey;
import com.toucan.shopping.cloud.seller.api.feign.service.FeignSellerShopService;
import com.toucan.shopping.cloud.user.api.feign.service.FeignSmsService;
import com.toucan.shopping.cloud.user.api.feign.service.FeignUserService;
import com.toucan.shopping.modules.common.generator.RequestJsonVOGenerator;
import com.toucan.shopping.modules.common.properties.Toucan;
import com.toucan.shopping.modules.common.util.*;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.common.vo.ResultVO;
import com.toucan.shopping.modules.redis.service.ToucanStringRedisService;
import com.toucan.shopping.modules.seller.vo.SellerLoginHistoryVO;
import com.toucan.shopping.modules.skylark.lock.service.SkylarkLock;
import com.toucan.shopping.modules.user.constant.UserLoginConstant;
import com.toucan.shopping.modules.user.constant.UserRegistConstant;
import com.toucan.shopping.modules.user.entity.UserMobilePhone;
import com.toucan.shopping.modules.user.vo.UserLoginVO;
import com.toucan.shopping.modules.user.vo.UserRegistVO;
import com.toucan.shopping.modules.user.vo.UserSmsVO;
import com.toucan.shopping.modules.user.vo.UserVO;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 用户登录
 */
@RestController("apiUserController")
@RequestMapping("/api/user")
public class UserApiController extends BaseController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private SkylarkLock skylarkLock;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;


    @Autowired
    private FeignSmsService feignSmsService;

    @Autowired
    private ToucanStringRedisService toucanStringRedisService;

    @Autowired
    private FeignUserService feignUserService;

    @Autowired
    private Toucan toucan;

    @Autowired
    private SellerLoginHistoryQueue sellerLoginHistoryQueue;

    @Autowired
    private FeignSellerShopService feignSellerShopService;


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
                resultObjectVO.setMsg("登录超时,请稍后重试");
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
                userLoginVO = resultObjectVO.formatData(UserLoginVO.class);
                if(userLoginVO==null)
                {
                    resultObjectVO.setMsg("登陆失败,请重新输入");
                    resultObjectVO.setCode(UserRegistConstant.SHOW_LOGIN_VERIFY_CODE);
                }else {
                    //UID
                    Cookie uidCookie = new Cookie( "tss_uid", String.valueOf(userLoginVO.getUserMainId()));
                    uidCookie.setPath("/");
                    //1天过期
                    uidCookie.setMaxAge(86400);
                    response.addCookie(uidCookie);

                    //TOKEN
                    Cookie ltCookie = new Cookie( "tss_lt", userLoginVO.getLoginToken());
                    ltCookie.setPath("/");
                    //1天过期
                    ltCookie.setMaxAge(86400);
                    response.addCookie(ltCookie);

                    //权限Token 半小时内不再进行权限校验
                    Cookie latCookie = new Cookie( "tss_lat", userLoginVO.getLoginAuthToken());
                    latCookie.setPath("/");
                    latCookie.setMaxAge(1800); //30分钟过期
                    response.addCookie(latCookie);

                    //删除登录失败计数
                    toucanStringRedisService.delete(loginFaildCountKey);

                    //删除登录验证码
                    toucanStringRedisService.delete(vcodeRedisKey);

                    try{
                        //刷新店铺缓存
                        UserVO queryUserVO = new UserVO();
                        queryUserVO.setUserMainId(userLoginVO.getUserMainId());
                        requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(), queryUserVO);
                        feignSellerShopService.flushCache(requestJsonVO);
                    }catch(Exception e)
                    {
                        logger.warn(e.getMessage(),e);
                    }

                    try {
                        //保存登录信息
                        SellerLoginHistoryVO sellerLoginHistoryVO = new SellerLoginHistoryVO();
                        sellerLoginHistoryVO.setUserMainId(userLoginVO.getUserMainId());
                        sellerLoginHistoryVO.setIp(IPUtil.getRemoteAddr(request));
                        sellerLoginHistoryVO.setLoginSrcType(1);
                        sellerLoginHistoryVO.setCreateDate(new Date());
                        sellerLoginHistoryVO.setDeleteStatus((short) 0);
                        sellerLoginHistoryQueue.push(sellerLoginHistoryVO);
                    }catch(Exception e)
                    {
                        logger.warn(e.getMessage(),e);
                    }
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



    @RequestMapping(value="/login/info")
    @ResponseBody
    public ResultObjectVO loginInfo(HttpServletRequest httpServletRequest){
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            UserVO queryUserVO = new UserVO();
            queryUserVO.setUserMainId(Long.parseLong(UserAuthHeaderUtil.getUserMainId(httpServletRequest.getHeader(toucan.getUserAuth().getHttpToucanAuthHeader()))));
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(), queryUserVO);
            resultObjectVO = feignUserService.queryLoginInfo(requestJsonVO.sign(),requestJsonVO);
            if(resultObjectVO.isSuccess())
            {
                UserVO userVO = resultObjectVO.formatData(UserVO.class);
                resultObjectVO.setData(userVO);
            }
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请求失败,请稍后重试");
        }
        return resultObjectVO;
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
//            boolean lockStatus = skylarkLock.lock(UserCenterRedisKey.getLoginLockKey(requestJsonVO.getAppCode(),user.getMobile()), user.getMobile());
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
//            skylarkLock.unLock(UserCenterRedisKey.getLoginLockKey(requestJsonVO.getAppCode(),user.getMobile()), user.getMobile());
//
//        }catch(Exception e)
//        {
//
//            skylarkLock.unLock(UserCenterRedisKey.getLoginLockKey(requestJsonVO.getAppCode(),user.getMobile()), user.getMobile());
//            logger.warn(e.getMessage(),e);
//
//            resultObjectVO.setCode(ResultVO.FAILD);
//            resultObjectVO.setMsg("发送短信验证码失败,请稍后重试");
//        }
//
//        return resultObjectVO;
//    }





}
