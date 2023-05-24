package com.toucan.shopping.cloud.apps.web.controller.user.bindEmail;


import com.alibaba.fastjson.JSONObject;
import com.toucan.shopping.cloud.apps.web.controller.BaseController;
import com.toucan.shopping.cloud.apps.web.redis.*;
import com.toucan.shopping.cloud.apps.web.util.MobilePhoneVCodeUtil;
import com.toucan.shopping.cloud.apps.web.util.VCodeUtil;
import com.toucan.shopping.cloud.user.api.feign.service.FeignSmsService;
import com.toucan.shopping.cloud.user.api.feign.service.FeignUserService;
import com.toucan.shopping.modules.auth.user.UserAuth;
import com.toucan.shopping.modules.common.generator.RequestJsonVOGenerator;
import com.toucan.shopping.modules.common.properties.Toucan;
import com.toucan.shopping.modules.common.util.*;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.common.vo.ResultVO;
import com.toucan.shopping.modules.email.queue.EmailQueue;
import com.toucan.shopping.modules.image.upload.service.ImageUploadService;
import com.toucan.shopping.modules.redis.service.ToucanStringRedisService;
import com.toucan.shopping.modules.skylark.lock.service.SkylarkLock;
import com.toucan.shopping.modules.sms.constant.SmsTypeConstant;
import com.toucan.shopping.modules.user.constant.UserLoginConstant;
import com.toucan.shopping.modules.user.constant.UserRegistConstant;
import com.toucan.shopping.modules.user.constant.UserVerifyCodeConstant;
import com.toucan.shopping.modules.user.entity.UserMobilePhone;
import com.toucan.shopping.modules.user.vo.*;
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
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 绑定邮箱
 */
@RestController("apiBindEmailController")
@RequestMapping("/api/user/bind/email")
public class BindEmailController extends BaseController {


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
    private EmailQueue emailQueue;

    @Autowired
    private Toucan toucan;


    /**
     * 绑定邮箱
     * @param userBindEmailVO
     * @return
     */
    @RequestMapping(value="/bind",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO bind(@RequestBody UserBindEmailVO userBindEmailVO,HttpServletRequest request) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        if (userBindEmailVO == null) {
            resultObjectVO.setCode(UserLoginConstant.NOT_FOUND_USER);
            resultObjectVO.setMsg("没有找到账号");
            return resultObjectVO;
        }

        if(StringUtils.isEmpty(userBindEmailVO.getVcode()))
        {
            resultObjectVO.setCode(UserRegistConstant.SHOW_LOGIN_VERIFY_CODE);
            resultObjectVO.setMsg("请输入验证码");
            return resultObjectVO;
        }

        if(StringUtils.isEmpty(userBindEmailVO.getEmail()))
        {
            resultObjectVO.setCode(UserRegistConstant.PASSWORD_NOT_FOUND);
            resultObjectVO.setMsg("请输入邮箱地址");
            return resultObjectVO;
        }

        if(!EmailUtils.isEmail(userBindEmailVO.getEmail()))
        {
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            resultObjectVO.setMsg("邮箱格式有误,请重新输入");
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
            Object emailVCodeObj = toucanStringRedisService.get(UserBindEmailRedisKey.getEmailVerifyCodeKey(userMainId));
            String emailVCode=emailVCodeObj!=null?String.valueOf(emailVCodeObj):"";
            if(StringUtils.isEmpty(emailVCode))
            {
                resultObjectVO.setMsg("验证码已过期,请重新发送");
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                return resultObjectVO;
            }
            Map codeInfo = JSONObject.parseObject(emailVCode, Map.class);
            if(!userBindEmailVO.getVcode().equals(codeInfo.get("code")))
            {
                resultObjectVO.setMsg("验证码输入有误");
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                return resultObjectVO;
            }
            if(!userBindEmailVO.getEmail().equals(codeInfo.get("email")))
            {
                resultObjectVO.setMsg("邮箱输入有误,请输入接收验证码的邮箱地址");
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                return resultObjectVO;
            }

            boolean lockStatus = skylarkLock.lock(UserBindEmailRedisKey.getBindEmailKey(userMainId), "1");
            if (!lockStatus) {
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("请求超时,请稍后重试");
                return resultObjectVO;
            }
            userBindEmailVO.setUserMainId(Long.parseLong(userMainId));
            UserVO querUserVO = new UserVO();
            querUserVO.setUsername(userBindEmailVO.getEmail());
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
                resultObjectVO.setMsg("该邮箱地址已被绑定了");
                return resultObjectVO;
            }
            requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(),userBindEmailVO);
            resultObjectVO = feignUserService.updateConnectEmail(requestJsonVO);
            if(resultObjectVO.isSuccess()) {
                toucanStringRedisService.delete(UserBindEmailRedisKey.getEmailVerifyCodeKey(userMainId));
            }
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);

            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请稍后重试");
        }finally{

            //释放锁
            skylarkLock.unLock(UserBindEmailRedisKey.getBindEmailKey(userMainId), "1");
        }

        return resultObjectVO;
    }

}
