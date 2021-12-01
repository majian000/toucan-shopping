package com.toucan.shopping.cloud.apps.web.controller.user;

import com.toucan.shopping.cloud.apps.web.controller.BaseController;
import com.toucan.shopping.cloud.apps.web.redis.UserLoginRedisKey;
import com.toucan.shopping.cloud.user.api.feign.service.FeignUserService;
import com.toucan.shopping.modules.auth.user.UserAuth;
import com.toucan.shopping.modules.common.generator.RequestJsonVOGenerator;
import com.toucan.shopping.modules.common.properties.Toucan;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


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



    @RequestMapping("/regist")
    public String regist()
    {
        return "user/regist";
    }


    /**
     * 找回密码
     * @return
     */
    @RequestMapping("/forget/pwd")
    public String forgetPwd()
    {
        return "user/forgetPwd/forget_pwd";
    }



    /**
     * 找回密码 步骤2
     * @return
     */
    @RequestMapping("/forget/pwd/step2")
    public String forgetPwdByStep2()
    {
        return "user/forgetPwd/forget_pwd_step2";
    }



    /**
     * 找回密码 步骤3
     * @return
     */
    @RequestMapping("/forget/pwd/step3")
    public String forgetPwdByStep3()
    {
        return "user/forgetPwd/forget_pwd_step3";
    }

    /**
     * 找回密码 步骤3
     * @return
     */
    @RequestMapping("/forget/pwd/success")
    public String forgetPwdBySuccess()
    {
        return "user/forgetPwd/forget_pwd_success";
    }


    @RequestMapping("/login")
    public String login(HttpServletRequest request, HttpServletResponse response)
    {
        try {
            //查询登录次数,失败3次要求输入验证码
            String loginFaildCountKey = UserLoginRedisKey.getLoginFaildCountKey(IPUtil.getRemoteAddr(request));
            Object loginFaildCountValueObject = toucanStringRedisService.get(loginFaildCountKey);
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

    void setAttributeUser(HttpServletRequest request)
    {
        try {
            UserVO queryUserVO = new UserVO();
            queryUserVO.setUserMainId(Long.parseLong(UserAuthHeaderUtil.getUserMainId( request.getHeader(this.getToucan().getUserAuth().getHttpToucanAuthHeader()))));
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(), queryUserVO);
            ResultObjectVO resultObjectVO = feignUserService.queryLoginInfo(requestJsonVO.sign(),requestJsonVO);
            if(resultObjectVO.isSuccess())
            {
                UserVO userVO = resultObjectVO.formatData(UserVO.class);
                if(userVO!=null&& StringUtils.isNotEmpty(userVO.getHeadSculpture())) {
                    userVO.setHttpHeadSculpture(imageUploadService.getImageHttpPrefix()+"/"+userVO.getHeadSculpture());
                }else{
                    userVO.setHttpHeadSculpture(imageUploadService.getImageHttpPrefix()+"/"+toucan.getUser().getDefaultHeadSculpture());
                }
                request.setAttribute("userVO",userVO);
            }else{
                request.setAttribute("userVO",new UserVO());
            }
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);
            request.setAttribute("userVO",new UserVO());
        }
    }

    @UserAuth(requestType = UserAuth.REQUEST_FORM)
    @RequestMapping("/info")
    public String info(HttpServletRequest httpServletRequest)
    {
        this.setAttributeUser(httpServletRequest);
        return "user/info";
    }


    @UserAuth(requestType = UserAuth.REQUEST_FORM)
    @RequestMapping("/editInfo")
    public String editInfo(HttpServletRequest httpServletRequest)
    {
        this.setAttributeUser(httpServletRequest);
        return "user/edit_info";
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
