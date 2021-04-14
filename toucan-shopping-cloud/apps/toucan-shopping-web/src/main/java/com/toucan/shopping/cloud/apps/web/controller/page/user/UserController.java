package com.toucan.shopping.cloud.apps.web.controller.page.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
