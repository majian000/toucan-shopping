package com.toucan.shopping.cloud.apps.seller.web.controller.user;

import com.toucan.shopping.modules.common.properties.Toucan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;


/**
 * 登录页控制器
 */
@Controller("pageLoginController")
public class LoginPageController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private Toucan toucan;

    @RequestMapping("/login")
    public String index(HttpServletRequest request)
    {
        return "login";
    }



}
