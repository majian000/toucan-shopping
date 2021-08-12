package com.toucan.shopping.cloud.apps.seller.web.controller.user;

import com.toucan.shopping.cloud.apps.seller.web.controller.BaseController;
import com.toucan.shopping.cloud.apps.seller.web.redis.UserLoginRedisKey;
import com.toucan.shopping.cloud.user.api.feign.service.FeignUserService;
import com.toucan.shopping.modules.auth.user.UserAuth;
import com.toucan.shopping.modules.common.generator.RequestJsonVOGenerator;
import com.toucan.shopping.modules.common.properties.Toucan;
import com.toucan.shopping.modules.common.util.IPUtil;
import com.toucan.shopping.modules.common.util.UserAuthHeaderUtil;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.redis.service.ToucanStringRedisService;
import com.toucan.shopping.modules.user.vo.UserLoginVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

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



    @RequestMapping("/regist")
    public void regist(HttpServletResponse response)
    {
        try {
            response.sendRedirect(toucan.getShoppingPC().getRegistPage());
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);
        }
    }

    @RequestMapping("/login")
    public String login(HttpServletRequest request)
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


    @UserAuth(requestType = UserAuth.REQUEST_FORM)
    @RequestMapping("/info")
    public String info(HttpServletRequest request)
    {
        return "user/info";
    }


    @UserAuth(requestType = UserAuth.REQUEST_FORM)
    @RequestMapping(value="/logout")
    public void logout(HttpServletRequest request, HttpServletResponse response) {
        try{
            String uid = UserAuthHeaderUtil.getUserMainId(request.getHeader(toucan.getUserAuth().getHttpToucanAuthHeader()));
            UserLoginVO userLoginVO = new UserLoginVO();
            userLoginVO.setUserMainId(Long.parseLong(uid));
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(),userLoginVO);
            //删除缓存中的token 以及用户信息
            feignUserService.logout(requestJsonVO.sign(),requestJsonVO);
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
        try {
            response.sendRedirect(toucan.getUserAuth().getLoginPage());
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);
        }
    }

}
