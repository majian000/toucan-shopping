package com.toucan.shopping.cloud.apps.web.controller.user.bindMobilePhone;


import com.alibaba.fastjson.JSONObject;
import com.toucan.shopping.cloud.apps.web.controller.BaseController;
import com.toucan.shopping.cloud.apps.web.redis.UserBindEmailRedisKey;
import com.toucan.shopping.cloud.apps.web.redis.UserBindMobilePhoneRedisKey;
import com.toucan.shopping.cloud.user.api.SmsServiceAPI;
import com.toucan.shopping.cloud.user.api.UserServiceAPI;
import com.toucan.shopping.modules.common.generator.RequestJsonVOGenerator;
import com.toucan.shopping.modules.common.properties.Toucan;
import com.toucan.shopping.modules.common.util.EmailUtils;
import com.toucan.shopping.modules.common.util.PhoneUtils;
import com.toucan.shopping.modules.common.util.UserAuthHeaderUtil;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.common.vo.ResultVO;
import com.toucan.shopping.modules.email.queue.EmailQueue;
import com.toucan.shopping.modules.image.upload.service.ImageUploadService;
import com.toucan.shopping.modules.redis.service.ToucanStringRedisService;
import com.toucan.shopping.modules.skylark.lock.service.SkylarkLock;
import com.toucan.shopping.modules.user.constant.UserLoginConstant;
import com.toucan.shopping.modules.user.constant.UserRegistConstant;
import com.toucan.shopping.modules.user.vo.UserBindEmailVO;
import com.toucan.shopping.modules.user.vo.UserBindMobilePhoneVO;
import com.toucan.shopping.modules.user.vo.UserVO;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * 绑定手机号
 */
@RestController("apiBindMobilePhoneController")
@RequestMapping("/api/user/bind/mobilePhone")
public class BindMobilePhoneApiController extends BaseController {


    private final Logger logger = LoggerFactory.getLogger(getClass());


    @Autowired
    private SkylarkLock redisLock;

    @Autowired
    private ToucanStringRedisService toucanStringRedisService;


    @Autowired
    private SmsServiceAPI smsService;


    @Autowired
    private UserServiceAPI userService;

    @Autowired
    private ImageUploadService imageUploadService;


    @Autowired
    private SkylarkLock skylarkLock;

    @Autowired
    private EmailQueue emailQueue;

    @Autowired
    private Toucan toucan;


    /**
     * 绑定手机号
     * @param userBindMobilePhoneVO
     * @return
     */
    @RequestMapping(value="/bind",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO bind(@RequestBody UserBindMobilePhoneVO userBindMobilePhoneVO, HttpServletRequest request) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        if (userBindMobilePhoneVO == null) {
            resultObjectVO.setCode(UserLoginConstant.NOT_FOUND_USER);
            resultObjectVO.setMsg("没有找到账号");
            return resultObjectVO;
        }

        if(StringUtils.isEmpty(userBindMobilePhoneVO.getVcode()))
        {
            resultObjectVO.setCode(UserRegistConstant.SHOW_LOGIN_VERIFY_CODE);
            resultObjectVO.setMsg("请输入验证码");
            return resultObjectVO;
        }

        if(StringUtils.isEmpty(userBindMobilePhoneVO.getMobilePhone()))
        {
            resultObjectVO.setCode(UserRegistConstant.PASSWORD_NOT_FOUND);
            resultObjectVO.setMsg("请输入手机号");
            return resultObjectVO;
        }

        if(!PhoneUtils.isChinaPhoneLegal(userBindMobilePhoneVO.getMobilePhone()))
        {
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            resultObjectVO.setMsg("手机号格式有误,请重新输入");
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
            Object emailVCodeObj = toucanStringRedisService.get(UserBindMobilePhoneRedisKey.getMobilePhoneVerifyCodeKey(userMainId));
            String emailVCode=emailVCodeObj!=null?String.valueOf(emailVCodeObj):"";
            if(StringUtils.isEmpty(emailVCode))
            {
                resultObjectVO.setMsg("验证码已过期,请重新发送");
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                return resultObjectVO;
            }
            Map codeInfo = JSONObject.parseObject(emailVCode, Map.class);
            if(!userBindMobilePhoneVO.getVcode().equals(codeInfo.get("code")))
            {
                resultObjectVO.setMsg("验证码输入有误");
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                return resultObjectVO;
            }
            if(!userBindMobilePhoneVO.getMobilePhone().equals(codeInfo.get("mobilePhone")))
            {
                resultObjectVO.setMsg("手机号输入有误,请输入接收验证码的手机号");
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                return resultObjectVO;
            }

            boolean lockStatus = skylarkLock.lock(UserBindMobilePhoneRedisKey.getBindMobilePhoneLockKey(userMainId), "1");
            if (!lockStatus) {
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("请求超时,请稍后重试");
                return resultObjectVO;
            }
            userBindMobilePhoneVO.setUserMainId(Long.parseLong(userMainId));
            UserVO querUserVO = new UserVO();
            querUserVO.setUsername(userBindMobilePhoneVO.getMobilePhone());
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(),querUserVO);
            resultObjectVO = userService.findByUsername(requestJsonVO);
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
            requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(),userBindMobilePhoneVO);
            resultObjectVO = userService.updateConnectMobilePhone(requestJsonVO);
            if(resultObjectVO.isSuccess()) {
                toucanStringRedisService.delete(UserBindMobilePhoneRedisKey.getMobilePhoneVerifyCodeKey(userMainId));
            }
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);

            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请稍后重试");
        }finally{

            //释放锁
            skylarkLock.unLock(UserBindMobilePhoneRedisKey.getBindMobilePhoneLockKey(userMainId), "1");
        }

        return resultObjectVO;
    }

}
