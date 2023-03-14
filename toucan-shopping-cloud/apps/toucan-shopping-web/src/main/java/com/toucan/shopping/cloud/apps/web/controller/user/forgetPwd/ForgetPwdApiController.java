package com.toucan.shopping.cloud.apps.web.controller.user.forgetPwd;


import com.alibaba.fastjson.JSONObject;
import com.toucan.shopping.cloud.apps.web.controller.BaseController;
import com.toucan.shopping.cloud.apps.web.redis.*;
import com.toucan.shopping.cloud.apps.web.util.EmailForgetPwdUtil;
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
import com.toucan.shopping.modules.common.vo.email.Email;
import com.toucan.shopping.modules.common.vo.email.EmailConfig;
import com.toucan.shopping.modules.common.vo.email.Receiver;
import com.toucan.shopping.modules.email.helper.EmailConfigHelper;
import com.toucan.shopping.modules.email.message.EmailMessage;
import com.toucan.shopping.modules.email.queue.EmailQueue;
import com.toucan.shopping.modules.image.upload.service.ImageUploadService;
import com.toucan.shopping.modules.redis.service.ToucanStringRedisService;
import com.toucan.shopping.modules.skylark.lock.service.SkylarkLock;
import com.toucan.shopping.modules.sms.constant.SmsTypeConstant;
import com.toucan.shopping.modules.user.constant.UserForgetPwdConstant;
import com.toucan.shopping.modules.user.constant.UserLoginConstant;
import com.toucan.shopping.modules.user.constant.UserRegistConstant;
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
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * 用户找回密码
 */
@RestController("forgetPwdApiController")
@RequestMapping("/api/user/forget/pwd")
public class ForgetPwdApiController extends BaseController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private SkylarkLock skylarkLock;

    @Autowired
    private EmailQueue emailQueue;

    @Autowired
    private ToucanStringRedisService toucanStringRedisService;


    @Autowired
    private FeignUserService feignUserService;

    @Autowired
    private Toucan toucan;

    /**
     * 检查用户状态
     * @return
     */
    @RequestMapping(value = "/check/username", method = RequestMethod.POST)
    public ResultObjectVO checkUserName(HttpServletRequest request,@RequestBody UserForgetPasswordVO userForgetPasswordVO)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();

        if(StringUtils.isEmpty(userForgetPasswordVO.getUsername()))
        {
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            resultObjectVO.setMsg("请输入用户名");
            return resultObjectVO;
        }


        try{

            boolean lockStatus = skylarkLock.lock(UserForgetPasswordRedisKey.getForgetPasswordLockKey(userForgetPasswordVO.getUsername()), userForgetPasswordVO.getUsername());
            if (!lockStatus) {
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("操作超时,请稍后重试");
                return resultObjectVO;
            }


            if(StringUtils.isEmpty(userForgetPasswordVO.getVcode()))
            {
                resultObjectVO.setCode(UserRegistConstant.SHOW_LOGIN_VERIFY_CODE);
                resultObjectVO.setMsg("请输入验证码");
                //释放锁
                skylarkLock.unLock(UserForgetPasswordRedisKey.getForgetPasswordLockKey(userForgetPasswordVO.getUsername()), userForgetPasswordVO.getUsername());
                return resultObjectVO;
            }

            String vcodeRedisKey = VerifyCodeRedisKey.getForgetVerifyCodeKey(this.getAppCode(), IPUtil.getRemoteAddr(request));
            Object vCodeObject = toucanStringRedisService.get(vcodeRedisKey);
            if(vCodeObject==null)
            {
                resultObjectVO.setMsg("验证码过期,请刷新");
                resultObjectVO.setCode(UserRegistConstant.SHOW_LOGIN_VERIFY_CODE);
                //释放锁
                skylarkLock.unLock(UserForgetPasswordRedisKey.getForgetPasswordLockKey(userForgetPasswordVO.getUsername()), userForgetPasswordVO.getUsername());
                return resultObjectVO;
            }
            if(!StringUtils.equals(userForgetPasswordVO.getVcode().toUpperCase(),String.valueOf(vCodeObject).toUpperCase()))
            {
                resultObjectVO.setMsg("验证码输入有误");
                resultObjectVO.setCode(UserRegistConstant.LOGIN_VERIFY_CODE_FAILD);
                //释放锁
                skylarkLock.unLock(UserForgetPasswordRedisKey.getForgetPasswordLockKey(userForgetPasswordVO.getUsername()), userForgetPasswordVO.getUsername());

                return resultObjectVO;
            }
            //校验通过,删除验证码
            toucanStringRedisService.delete(vcodeRedisKey);


            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(),userForgetPasswordVO);
            resultObjectVO = feignUserService.findByUsername(requestJsonVO);
            if(resultObjectVO.isSuccess()) {
                if (resultObjectVO.getData() != null) {
                    UserVO userVO = resultObjectVO.formatData(UserVO.class);
                    if(userVO.getEnableStatus().intValue()==1) {
                        logger.info("找回密码 用户 {}", JSONObject.toJSONString(userVO));
                        resultObjectVO.setCode(ResultObjectVO.SUCCESS);

                        Map<String,Object> retVO = new HashMap();
                        retVO.put("mobileMethod", 0);
                        retVO.put("emailMethod", 0);
                        retVO.put("idcardMethod", 0);

                        if (StringUtils.isNotEmpty(userVO.getMobilePhone())) {
                            retVO.put("mobileMethod", 1);
                        }
                        if (StringUtils.isNotEmpty(userVO.getEmail())) {
                            retVO.put("emailMethod", 1);
                        }
                        if(userVO.getTrueNameStatus()!=null&&userVO.getTrueNameStatus().intValue()==1)
                        {
                            retVO.put("idcardMethod", 1);
                        }
                        resultObjectVO.setData(retVO);
                    }else{
                        resultObjectVO.setCode(ResultObjectVO.FAILD);
                        resultObjectVO.setMsg("该账号已被禁用");
                    }
                }else{
                    resultObjectVO.setCode(ResultObjectVO.FAILD);
                    resultObjectVO.setMsg("账号不存在");
                }
            }
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);
        }finally{
            //释放锁
            skylarkLock.unLock(UserForgetPasswordRedisKey.getForgetPasswordLockKey(userForgetPasswordVO.getUsername()), userForgetPasswordVO.getUsername());
        }
        return resultObjectVO;
    }

    /**
     * 找回密码(发送验证码)
     * @param request
     */
    @RequestMapping(value="/send/vcode", method = RequestMethod.POST)
    public ResultObjectVO sendForgetPwdVerifyCode(HttpServletRequest request,@RequestBody UserForgetPasswordVO userForgetPasswordVO) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(),userForgetPasswordVO);
            resultObjectVO = feignUserService.findByUsername(requestJsonVO);
            if(resultObjectVO.isSuccess()) {
                UserVO userVO = resultObjectVO.formatData(UserVO.class);
                if(userVO==null||userVO.getUserMainId()==null)
                {
                    resultObjectVO.setCode(ResultObjectVO.FAILD);
                    resultObjectVO.setMsg("账号不存在");
                    return resultObjectVO;
                }
                if(userVO.getEnableStatus().intValue()==0) {
                    resultObjectVO.setCode(ResultObjectVO.FAILD);
                    resultObjectVO.setMsg("账号已被禁用");
                    return resultObjectVO;
                }
                String userMainId = String.valueOf(userVO.getUserMainId());
                if(userForgetPasswordVO.getVerifyMethod()==0) //手机号接收验证码
                {
                    String vcode = MobilePhoneVCodeUtil.genCode(6);
                    toucanStringRedisService.set(UserForgetPwdRedisKey.getMobileVerifyCodeKey(userMainId),vcode, UserForgetPwdConstant.MAX_FORGET_PWD_VCODE_MAX_AGE, TimeUnit.SECONDS);

                }else if(userForgetPasswordVO.getVerifyMethod()==1) //邮箱接收验证码
                {
                    String vcode = VerifyCodeUtil.generateVerifyCode(6, "123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz");
                    toucanStringRedisService.set(UserForgetPwdRedisKey.getEmailVerifyCodeKey(userMainId), vcode, UserForgetPwdConstant.MAX_FORGET_PWD_VCODE_MAX_AGE, TimeUnit.SECONDS);
                    Email email = new Email();
                    EmailConfig emailConfig= EmailConfigHelper.getEmailConfig("犀鸟商城——找回密码");
                    if(emailConfig!=null) {
                        email.setEmailConfig(emailConfig);
                        email.setSubject("犀鸟商城——找回密码");
                        email.setContent(EmailForgetPwdUtil.getEmailContent(vcode, userVO.getNickName(), (UserForgetPwdConstant.MAX_FORGET_PWD_VCODE_MAX_AGE/60), DateUtils.format(new Date(), DateUtils.FORMATTER_DD.get())));

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
                }else if(userForgetPasswordVO.getVerifyMethod()==2) //身份证信息验证
                {
                    if(userVO.getTrueNameStatus()==null||userVO.getTrueNameStatus().intValue()==0)
                    {
                        resultObjectVO.setCode(ResultObjectVO.FAILD);
                        resultObjectVO.setMsg("该账号未进行实名认证");
                        return resultObjectVO;
                    }
                }
            }else{
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("验证码生成失败,请稍后重试");
            }
        } catch (Exception e) {
            logger.warn(e.getMessage(),e);
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            resultObjectVO.setMsg("验证码生成失败,请稍后重试");
        }
        return resultObjectVO;
    }

    /**
     * 忘记密码 验证码
     * @param request
     * @param response
     */
    @RequestMapping(value="/vcode", method = RequestMethod.GET)
    public void forgetVerifyCode(HttpServletRequest request,HttpServletResponse response) {
        OutputStream outputStream = null;
        try {
            outputStream = response.getOutputStream();
            int w = 200, h = 80;
            //生成4位验证码
            String code = VerifyCodeUtil.generateVerifyCode(4);


            //生成客户端验证码ID
            String vcodeRedisKey = VerifyCodeRedisKey.getForgetVerifyCodeKey(this.getAppCode(), IPUtil.getRemoteAddr(request));
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





}
