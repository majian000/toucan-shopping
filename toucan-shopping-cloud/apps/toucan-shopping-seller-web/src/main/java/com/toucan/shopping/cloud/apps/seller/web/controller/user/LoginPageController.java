package com.toucan.shopping.cloud.apps.seller.web.controller.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;


/**
 * 登录页控制器
 */
@Controller("pageLoginController")
public class LoginPageController {

    private final Logger logger = LoggerFactory.getLogger(getClass());


    @RequestMapping("/login")
    public String index(HttpServletRequest request)
    {
        return "login";
    }



}
