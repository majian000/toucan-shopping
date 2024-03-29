package com.toucan.shopping.cloud.apps.web.controller.user;

import com.toucan.shopping.cloud.apps.web.controller.BaseController;
import com.toucan.shopping.cloud.apps.web.redis.UserLoginRedisKey;
import com.toucan.shopping.cloud.apps.web.service.LoginUserService;
import com.toucan.shopping.cloud.user.api.feign.service.FeignUserService;
import com.toucan.shopping.modules.auth.user.UserAuth;
import com.toucan.shopping.modules.common.generator.RequestJsonVOGenerator;
import com.toucan.shopping.modules.common.properties.Toucan;
import com.toucan.shopping.modules.common.util.DateUtils;
import com.toucan.shopping.modules.common.util.IPUtil;
import com.toucan.shopping.modules.common.util.UserAuthHeaderUtil;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.common.vo.ResultVO;
import com.toucan.shopping.modules.image.upload.service.ImageUploadService;
import com.toucan.shopping.modules.redis.service.ToucanStringRedisService;
import com.toucan.shopping.modules.user.vo.UserForgetPasswordVO;
import com.toucan.shopping.modules.user.vo.UserLoginVO;
import com.toucan.shopping.modules.user.vo.UserVO;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;


/**
 * 用户控制器
 */
@Controller("pageUserController")
@RequestMapping("/page/user")
public class UserPageController extends BaseController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private Toucan toucan;

    @Autowired
    private ToucanStringRedisService toucanStringRedisService;

    @Autowired
    private FeignUserService feignUserService;

    @Autowired
    private ImageUploadService imageUploadService;

    @Autowired
    private LoginUserService loginUserService;



    @RequestMapping("/regist")
    public String regist()
    {
        return "user/regist";
    }


    /**
     * 找回密码页面
     * @return
     */
    @RequestMapping("/forget/pwd")
    public String forgetPwd()
    {
        return "user/forgetPwd/forget_pwd";
    }




    @RequestMapping("/login")
    public String login(HttpServletRequest request, HttpServletResponse response)
    {
        try {
            //查询登录次数,失败3次要求输入验证码
            String loginFaildCountKey = UserLoginRedisKey.getLoginFaildCountKey(IPUtil.getRemoteAddr(request));
            Object loginFaildCountValueObject = toucanStringRedisService.get(loginFaildCountKey);
            String redirectUrl = request.getParameter("redirectUrl");
            if(StringUtils.isEmpty(redirectUrl))
            {
                redirectUrl = request.getAttribute("redirectUrl")!=null?String.valueOf(request.getAttribute("redirectUrl")):null;
            }
            request.setAttribute("redirectUrl",redirectUrl);
            if (loginFaildCountValueObject != null) {
                Integer faildCount = Integer.parseInt(String.valueOf(loginFaildCountValueObject));
                if (faildCount >= 3) {
                    request.setAttribute("isShowInputVcode", true);
                } else {
                    request.setAttribute("isShowInputVcode", false);
                }
            } else {
                request.setAttribute("isShowInputVcode", false);
            }
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);
            request.setAttribute("isShowInputVcode", false);
        }
        return "user/login";
    }



    @UserAuth(requestType = UserAuth.REQUEST_FORM,responseType = UserAuth.RESPONSE_FORM)
    @RequestMapping("/info")
    public String info(HttpServletRequest httpServletRequest)
    {
        Date currentDate = DateUtils.currentDate();
        String welcomeText = "欢迎回来～";
        try {
            String hours = DateUtils.format(currentDate, DateUtils.FORMATTER_HH.get());
            int hoursInteger = Integer.parseInt(hours);
            if(hoursInteger>=9&&hoursInteger<11){
                welcomeText="早上好～";
            }else if(hoursInteger>=11&&hoursInteger<13){
                welcomeText="中午好～";
            }else if(hoursInteger>=13&&hoursInteger<18){
                welcomeText="下午好～";
            }else{
                welcomeText="晚上好～";
            }
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);
        }
        httpServletRequest.setAttribute("welcomeText",welcomeText);
        loginUserService.setAttributeUser(httpServletRequest);
        return "user/info";
    }


    @UserAuth(requestType = UserAuth.REQUEST_FORM,responseType = UserAuth.RESPONSE_FORM)
    @RequestMapping("/editInfo")
    public String editInfo(HttpServletRequest httpServletRequest)
    {
        loginUserService.setAttributeUser(httpServletRequest);
        return "user/info/edit_info";
    }


    @UserAuth(requestType = UserAuth.REQUEST_FORM,responseType = UserAuth.RESPONSE_FORM)
    @RequestMapping("/modifyPwd")
    public String modifyPwd(HttpServletRequest httpServletRequest)
    {
        loginUserService.setAttributeUser(httpServletRequest);
        return "user/modifyPwd/modify_pwd";
    }


    @UserAuth(requestType = UserAuth.REQUEST_FORM,responseType = UserAuth.RESPONSE_FORM)
    @RequestMapping("/bindEmail")
    public String bindEmail(HttpServletRequest httpServletRequest)
    {
        loginUserService.setAttributeUser(httpServletRequest);
        return "user/bindEmail/bind_email";
    }


    @UserAuth(requestType = UserAuth.REQUEST_FORM,responseType = UserAuth.RESPONSE_FORM)
    @RequestMapping("/bindMobilePhone")
    public String bindMobilePhone(HttpServletRequest httpServletRequest)
    {
        loginUserService.setAttributeUser(httpServletRequest);
        UserVO userVO = (UserVO)httpServletRequest.getAttribute("userVO");
        //如果是修改手机号的话,需要先进行实名认证
        if(StringUtils.isNotEmpty(userVO.getMobilePhone())) {
            if (userVO.getTrueNameStatus().intValue() == 0) {
                httpServletRequest.setAttribute("msg", "请您先进行实名");
                return "user/bindMobilePhone/bind_mobile_phone_msg";
            }
        }
        return "user/bindMobilePhone/bind_mobile_phone";
    }


    @RequestMapping(value="/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        try{
            String uid = UserAuthHeaderUtil.getUserMainId(request.getHeader(this.getToucan().getUserAuth().getHttpToucanAuthHeader()));
            UserLoginVO userLoginVO = new UserLoginVO();
            userLoginVO.setUserMainId(Long.parseLong(uid));
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(),userLoginVO);
            //删除缓存中的token 以及用户信息
            ResultObjectVO resultObjectVO  = feignUserService.logout(requestJsonVO.sign(),requestJsonVO);
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);
        }finally{
            //UID
            Cookie uidCookie = new Cookie("tss_uid","-1");
            uidCookie.setPath("/");
            //立刻清除
            uidCookie.setMaxAge(0);
            response.addCookie(uidCookie);

            //TOKEN
            Cookie ltCookie = new Cookie("tss_lt", "-1");
            ltCookie.setPath("/");
            //立刻清除
            ltCookie.setMaxAge(0);
            response.addCookie(ltCookie);

            request.setAttribute("isShowInputVcode", false);

        }

        return login(request,response);
    }

}
