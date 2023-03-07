package com.toucan.shopping.cloud.apps.web.controller.verifyCode;

import com.toucan.shopping.cloud.apps.web.controller.BaseController;
import com.toucan.shopping.cloud.apps.web.redis.UserModifyPwdRedisKey;
import com.toucan.shopping.cloud.apps.web.redis.VerifyCodeRedisKey;
import com.toucan.shopping.cloud.apps.web.util.EmailModifyPwdUtil;
import com.toucan.shopping.cloud.user.api.feign.service.FeignUserService;
import com.toucan.shopping.modules.auth.user.UserAuth;
import com.toucan.shopping.modules.common.generator.RequestJsonVOGenerator;
import com.toucan.shopping.modules.common.properties.Toucan;
import com.toucan.shopping.modules.common.util.DateUtils;
import com.toucan.shopping.modules.common.util.GlobalUUID;
import com.toucan.shopping.modules.common.util.UserAuthHeaderUtil;
import com.toucan.shopping.modules.common.util.VerifyCodeUtil;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.common.vo.email.Email;
import com.toucan.shopping.modules.common.vo.email.EmailConfig;
import com.toucan.shopping.modules.common.vo.email.Receiver;
import com.toucan.shopping.modules.email.helper.EmailConfigHelper;
import com.toucan.shopping.modules.email.message.EmailMessage;
import com.toucan.shopping.modules.email.queue.EmailQueue;
import com.toucan.shopping.modules.redis.service.ToucanStringRedisService;
import com.toucan.shopping.modules.user.constant.UserModifyPwdConstant;
import com.toucan.shopping.modules.user.vo.UserVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@RestController("apiTextVerifyCodeController")
@RequestMapping("/api/vcode/text")
public class TextVerifyCodeController extends BaseController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private ToucanStringRedisService toucanStringRedisService;

    @Autowired
    private FeignUserService feignUserService;

    @Autowired
    private Toucan toucan;

    @Autowired
    private EmailQueue emailQueue;

    /**
     * 修改密码 验证码(手机)
     * @param request
     * @param response
     */
    @UserAuth
    @RequestMapping(value="/mobile/user/modifyPwd", method = RequestMethod.POST)
    public ResultObjectVO mobileModifyPwdVerifyCode(HttpServletRequest request, HttpServletResponse response) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        String userMainId ="-1";
        try {
            userMainId = UserAuthHeaderUtil.getUserMainId( request.getHeader(this.getToucan().getUserAuth().getHttpToucanAuthHeader()));
            if("-1".equals(userMainId))
            {
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("登录超时,请稍后重试");
                return resultObjectVO;
            }
            String vcode = VerifyCodeUtil.generateVerifyCode(6,"123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz");
            //TODO:手机验证码这个版本固定写死
            vcode="1234";
            toucanStringRedisService.set(UserModifyPwdRedisKey.getMobileVerifyCodeKey(userMainId),vcode,UserModifyPwdConstant.MAX_MODIFY_PWD_VCODE_MAX_AGE, TimeUnit.SECONDS);
        } catch (Exception e) {
            logger.warn(e.getMessage(),e);
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            resultObjectVO.setMsg("验证码生成失败,请稍后重试");
        }
        return resultObjectVO;
    }


    /**
     * 修改密码 验证码(邮箱)
     * @param request
     * @param response
     */
    @UserAuth
    @RequestMapping(value="/email/user/modifyPwd", method = RequestMethod.POST)
    public ResultObjectVO emailModifyPwdVerifyCode(HttpServletRequest request, HttpServletResponse response) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        String userMainId ="-1";
        try {
            userMainId = UserAuthHeaderUtil.getUserMainId( request.getHeader(this.getToucan().getUserAuth().getHttpToucanAuthHeader()));
            if("-1".equals(userMainId))
            {
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("登录超时,请稍后重试");
                return resultObjectVO;
            }
            UserVO userVO = new UserVO();
            userVO.setUserMainId(Long.parseLong(userMainId));
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(),userVO);
            ResultObjectVO resultObectVO = feignUserService.findByUserMainIdForCacheOrDB(requestJsonVO.sign(),requestJsonVO);
            if(resultObectVO.isSuccess()) {
                userVO = resultObectVO.formatData(UserVO.class);
                String vcode = VerifyCodeUtil.generateVerifyCode(6, "123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz");
                toucanStringRedisService.set(UserModifyPwdRedisKey.getEmailVerifyCodeKey(userMainId), vcode, UserModifyPwdConstant.MAX_MODIFY_PWD_VCODE_MAX_AGE, TimeUnit.SECONDS);
                Email email = new Email();
                EmailConfig emailConfig=EmailConfigHelper.getEmailConfig("犀鸟商城——修改密码");
                if(emailConfig!=null) {
                    email.setEmailConfig(emailConfig);
                    email.setSubject("犀鸟商城——修改密码");
                    email.setContent(EmailModifyPwdUtil.getEmailContent(vcode, userVO.getNickName(), UserModifyPwdConstant.MAX_MODIFY_PWD_VCODE_MAX_AGE, DateUtils.format(new Date(), DateUtils.FORMATTER_DD.get())));

                    Receiver receiver = new Receiver();
                    receiver.setEmail(userVO.getEmail());
                    receiver.setName(userVO.getNickName());
                    List<Receiver> receiverList = new LinkedList<>();
                    receiverList.add(receiver);
                    emailConfig.setReceivers(receiverList);
                    EmailMessage EmailMessage = new EmailMessage();
                    EmailMessage.setEmail(email);
                    emailQueue.push(EmailMessage);
                }else{
                    logger.warn("修改密码发送邮件功能已被禁用...");
                }
            }
        } catch (Exception e) {
            logger.warn(e.getMessage(),e);
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            resultObjectVO.setMsg("验证码生成失败,请稍后重试");
        }
        return resultObjectVO;
    }
}
