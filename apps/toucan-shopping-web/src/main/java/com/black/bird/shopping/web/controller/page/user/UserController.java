package com.toucan.shopping.web.controller.page.user;

import com.toucan.shopping.area.api.feign.service.FeignAdminAreaService;
import com.toucan.shopping.auth.user.Auth;
import com.toucan.shopping.center.user.api.feign.service.FeignSmsService;
import com.toucan.shopping.center.user.export.vo.UserSmsVO;
import com.toucan.shopping.common.vo.ResultObjectVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;


/**
 * 用户控制器
 */
@Controller("pageUserController")
@RequestMapping("/page/user")
public class UserController {

    private final Logger logger = LoggerFactory.getLogger(getClass());


    @RequestMapping("/regist")
    public String regist()
    {
        return "user/regist";
    }

    @RequestMapping("/login")
    public String login()
    {
        return "user/login";
    }


    @RequestMapping("/info")
    public String center()
    {
        return "user/info";
    }

}
