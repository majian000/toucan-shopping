package com.toucan.shopping.cloud.apps.web.controller.user;

import com.toucan.shopping.cloud.apps.web.controller.BaseController;
import com.toucan.shopping.cloud.apps.web.redis.UserLoginRedisKey;
import com.toucan.shopping.modules.auth.user.UserAuth;
import com.toucan.shopping.modules.common.util.IPUtil;
import com.toucan.shopping.modules.redis.service.ToucanStringRedisService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;


/**
 * 用户控制器
 */
@Controller("pageUserController")
@RequestMapping("/page/user")
public class UserPageController extends BaseController {

    private final Logger logger = LoggerFactory.getLogger(getClass());


    @Autowired
    private ToucanStringRedisService toucanStringRedisService;

    @RequestMapping("/regist")
    public String regist()
    {
        return "user/regist";
    }

    @RequestMapping("/login")
    public String login(HttpServletRequest request)
    {
        //查询登录次数,失败3次要求输入验证码
        String loginFaildCountKey = UserLoginRedisKey.getLoginFaildCountKey(IPUtil.getRemoteAddr(request));
        Object loginFaildCountValueObject = toucanStringRedisService.get(loginFaildCountKey);
        if(loginFaildCountValueObject!=null) {
            Integer faildCount = Integer.parseInt(String.valueOf(loginFaildCountValueObject));
            if(faildCount>=3)
            {
                request.setAttribute("isShowInputVcode",true);
            }else{
                request.setAttribute("isShowInputVcode",false);
            }
        }else{
            request.setAttribute("isShowInputVcode",false);
        }
        return "user/login";
    }


    @UserAuth(requestType = UserAuth.REQUEST_FORM)
    @RequestMapping("/info")
    public String center()
    {
        return "user/info";
    }



}
