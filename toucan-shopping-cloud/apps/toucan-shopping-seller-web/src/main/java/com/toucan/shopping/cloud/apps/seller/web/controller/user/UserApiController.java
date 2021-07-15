package com.toucan.shopping.cloud.apps.seller.web.controller.user;


import com.alibaba.fastjson.JSONObject;
import com.toucan.shopping.cloud.apps.seller.web.controller.BaseController;
import com.toucan.shopping.cloud.user.api.feign.service.FeignSmsService;
import com.toucan.shopping.cloud.user.api.feign.service.FeignUserService;
import com.toucan.shopping.modules.common.generator.RequestJsonVOGenerator;
import com.toucan.shopping.modules.common.properties.Toucan;
import com.toucan.shopping.modules.common.util.*;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.common.vo.ResultVO;
import com.toucan.shopping.modules.skylark.lock.service.SkylarkLock;
import com.toucan.shopping.modules.user.constant.SmsTypeConstant;
import com.toucan.shopping.modules.user.constant.UserLoginConstant;
import com.toucan.shopping.modules.user.constant.UserRegistConstant;
import com.toucan.shopping.modules.user.entity.UserMobilePhone;
import com.toucan.shopping.modules.user.vo.UserLoginVO;
import com.toucan.shopping.modules.user.vo.UserRegistVO;
import com.toucan.shopping.modules.user.vo.UserSmsVO;
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
    private FeignUserService feignUserService;

    @Autowired
    private Toucan toucan;





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
            userLoginVO.setAppCode(this.getAppCode());
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(this.getAppCode(),userLoginVO);
            resultObjectVO = feignUserService.loginByPassword(SignUtil.sign(requestJsonVO),requestJsonVO);
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);

            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("登录失败,请稍后重试");
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
//                resultObjectVO.setMsg("超时重试");
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
