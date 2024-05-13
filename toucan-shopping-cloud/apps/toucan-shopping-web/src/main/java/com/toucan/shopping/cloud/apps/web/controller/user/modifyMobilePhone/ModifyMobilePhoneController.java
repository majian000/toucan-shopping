package com.toucan.shopping.cloud.apps.web.controller.user.modifyMobilePhone;


import com.alibaba.fastjson.JSONObject;
import com.toucan.shopping.cloud.apps.web.controller.BaseController;
import com.toucan.shopping.cloud.apps.web.redis.UserBindMobilePhoneRedisKey;
import com.toucan.shopping.cloud.apps.web.redis.UserModifyMobilePhoneRedisKey;
import com.toucan.shopping.cloud.apps.web.util.MobilePhoneVCodeUtil;
import com.toucan.shopping.cloud.user.api.feign.service.FeignSmsService;
import com.toucan.shopping.cloud.user.api.feign.service.FeignUserService;
import com.toucan.shopping.modules.common.generator.RequestJsonVOGenerator;
import com.toucan.shopping.modules.common.properties.Toucan;
import com.toucan.shopping.modules.common.util.PhoneUtils;
import com.toucan.shopping.modules.common.util.UserAuthHeaderUtil;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.common.vo.ResultVO;
import com.toucan.shopping.modules.image.upload.service.ImageUploadService;
import com.toucan.shopping.modules.redis.service.ToucanStringRedisService;
import com.toucan.shopping.modules.skylark.lock.service.SkylarkLock;
import com.toucan.shopping.modules.user.constant.UserLoginConstant;
import com.toucan.shopping.modules.user.constant.UserModifyPwdConstant;
import com.toucan.shopping.modules.user.constant.UserRegistConstant;
import com.toucan.shopping.modules.user.vo.UserBindMobilePhoneVO;
import com.toucan.shopping.modules.user.vo.UserModifyMobilePhoneVO;
import com.toucan.shopping.modules.user.vo.UserVO;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 修改手机号
 */
@RestController("apiModifyMobilePhoneController")
@RequestMapping("/api/user/modify/mobilePhone")
public class ModifyMobilePhoneController extends BaseController {


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
    private ImageUploadService imageUploadService;


    @Autowired
    private SkylarkLock skylarkLock;

    @Autowired
    private Toucan toucan;


    /**
     * 验证修改手机号的验证码
     * @param userModifyMobilePhoneVO
     * @return
     */
    @RequestMapping(value="/valid/verify/code",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO validVerifyCode(@RequestBody UserModifyMobilePhoneVO userModifyMobilePhoneVO,HttpServletRequest request) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        if (userModifyMobilePhoneVO == null) {
            resultObjectVO.setCode(UserLoginConstant.NOT_FOUND_USER);
            resultObjectVO.setMsg("没有找到账号");
            return resultObjectVO;
        }

        if(StringUtils.isEmpty(userModifyMobilePhoneVO.getVcode()))
        {
            resultObjectVO.setCode(UserRegistConstant.SHOW_LOGIN_VERIFY_CODE);
            resultObjectVO.setMsg("请输入验证码");
            return resultObjectVO;
        }

        String userMainId ="-1";
        try {
            userMainId = UserAuthHeaderUtil.getUserMainId(request.getHeader(this.getToucan().getUserAuth().getHttpToucanAuthHeader()));
            if("-1".equals(userMainId))
            {
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("登录超时,请稍后重试");
                return resultObjectVO;
            }
            Object emailVCodeObj = toucanStringRedisService.get(UserModifyMobilePhoneRedisKey.getEmailVerifyCodeKey(userMainId));
            String emailVCode=emailVCodeObj!=null?String.valueOf(emailVCodeObj):"";
            Object mobilePhoneVCodeObj = toucanStringRedisService.get(UserModifyMobilePhoneRedisKey.getMobileVerifyCodeKey(userMainId));
            String mobilePhoneVCode=mobilePhoneVCodeObj!=null?String.valueOf(mobilePhoneVCodeObj):"";
            if(StringUtils.isEmpty(emailVCode)&&StringUtils.isEmpty(mobilePhoneVCode))
            {
                resultObjectVO.setMsg("验证码已过期,请重新发送");
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                return resultObjectVO;
            }
            Map emailCodeInfo = new HashMap();
            if(StringUtils.isNotEmpty(emailVCode)) {
                emailCodeInfo = JSONObject.parseObject(emailVCode, Map.class);
            }
            Map mobileCodeInfo = null;
            if(StringUtils.isNotEmpty(mobilePhoneVCode)) {
                mobileCodeInfo = JSONObject.parseObject(mobilePhoneVCode, Map.class);
            }
            if(!userModifyMobilePhoneVO.getVcode().equals(emailCodeInfo.get("code"))
                    &&!userModifyMobilePhoneVO.getVcode().equals(mobileCodeInfo.get("code")))
            {
                resultObjectVO.setMsg("验证码输入有误");
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                return resultObjectVO;
            }

            String securityCode = MobilePhoneVCodeUtil.genCode(6);
            toucanStringRedisService.set(UserModifyMobilePhoneRedisKey.getSecurityCodeKey(userMainId),securityCode, UserModifyPwdConstant.MAX_MODIFY_PWD_VCODE_MAX_AGE, TimeUnit.SECONDS);
            resultObjectVO.setData(securityCode);
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);

            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请稍后重试");
        }finally{

            //释放锁
            skylarkLock.unLock(UserBindMobilePhoneRedisKey.getMobilePhoneVerifyCodeKey(userMainId), "1");
        }
        return resultObjectVO;
    }

    /**
     * 修改手机号
     * @param userModifyMobilePhoneVO
     * @return
     */
    @RequestMapping(value="/modify",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO modify(@RequestBody UserModifyMobilePhoneVO userModifyMobilePhoneVO, HttpServletRequest request) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        if (userModifyMobilePhoneVO == null) {
            resultObjectVO.setCode(UserLoginConstant.NOT_FOUND_USER);
            resultObjectVO.setMsg("没有找到账号");
            return resultObjectVO;
        }

        if(StringUtils.isEmpty(userModifyMobilePhoneVO.getVcode()))
        {
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            resultObjectVO.setMsg("请输入验证码");
            return resultObjectVO;
        }

        if(StringUtils.isEmpty(userModifyMobilePhoneVO.getMobilePhone()))
        {
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            resultObjectVO.setMsg("请输入手机号");
            return resultObjectVO;
        }


        String userMainId ="-1";
        try {
            userMainId = UserAuthHeaderUtil.getUserMainId(request.getHeader(this.getToucan().getUserAuth().getHttpToucanAuthHeader()));
            if("-1".equals(userMainId))
            {
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("登录超时,请稍后重试");
                return resultObjectVO;
            }
            Object mobileVCodeObj = toucanStringRedisService.get(UserModifyMobilePhoneRedisKey.getMobileVerifyCodeKey(userMainId));
            String mobileVCode=mobileVCodeObj!=null?String.valueOf(mobileVCodeObj):"";
            if(StringUtils.isEmpty(mobileVCode))
            {
                resultObjectVO.setMsg("验证码已过期,请重新发送");
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                return resultObjectVO;
            }
            Map codeInfo = JSONObject.parseObject(mobileVCode, Map.class);
            if(!userModifyMobilePhoneVO.getVcode().equals(codeInfo.get("code")))
            {
                resultObjectVO.setMsg("验证码输入有误");
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                return resultObjectVO;
            }

            if(!userModifyMobilePhoneVO.getMobilePhone().equals(codeInfo.get("mobilePhone")))
            {
                resultObjectVO.setMsg("验证码已过期,请重新发送");
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                return resultObjectVO;
            }
            Object securityCodeObj = toucanStringRedisService.get(UserModifyMobilePhoneRedisKey.getSecurityCodeKey(userMainId));
            String securityCode = securityCodeObj!=null?String.valueOf(securityCodeObj):"";
            if(!securityCode.equals(userModifyMobilePhoneVO.getSecurityCode())){
                resultObjectVO.setMsg("验证码已过期,请返回上一个页面重新发送");
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                return resultObjectVO;
            }

            boolean lockStatus = skylarkLock.lock(UserModifyMobilePhoneRedisKey.getMobilePhoneVerifyCodeLockKey(userMainId), "1");
            if (!lockStatus) {
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("请求超时,请稍后重试");
                return resultObjectVO;
            }
            userModifyMobilePhoneVO.setUserMainId(Long.parseLong(userMainId));
            UserVO querUserVO = new UserVO();
            querUserVO.setUsername(userModifyMobilePhoneVO.getMobilePhone());
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(),querUserVO);
            resultObjectVO = feignUserService.findByUsername(requestJsonVO);
            if(!resultObjectVO.isSuccess())
            {
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("请求超时,请稍后重试");
                return resultObjectVO;
            }
            if(resultObjectVO.getData()!=null)
            {
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("该手机号已被绑定了");
                return resultObjectVO;
            }
            requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(),userModifyMobilePhoneVO);
            resultObjectVO = feignUserService.updateConnectMobilePhone(requestJsonVO);
            if(resultObjectVO.isSuccess()) {
                toucanStringRedisService.delete(UserModifyMobilePhoneRedisKey.getSecurityCodeKey(userMainId));
                toucanStringRedisService.delete(UserBindMobilePhoneRedisKey.getMobilePhoneVerifyCodeKey(userMainId));
            }
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);

            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请稍后重试");
        }finally{

            //释放锁
            skylarkLock.unLock(UserModifyMobilePhoneRedisKey.getMobilePhoneVerifyCodeLockKey(userMainId), "1");
        }

        return resultObjectVO;
    }

}
