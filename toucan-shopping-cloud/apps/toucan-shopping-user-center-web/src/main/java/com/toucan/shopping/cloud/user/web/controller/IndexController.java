package com.toucan.shopping.cloud.user.web.controller;


import com.toucan.shopping.auth.admin.Auth;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/index")
public class IndexController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Value("${toucan.app-code}")
    private String appCode;



    @Auth(verifyMethod = Auth.VERIFYMETHOD_ADMIN_AUTH,requestType = Auth.REQUEST_FORM)
    @RequestMapping(value = "/page",method = RequestMethod.GET)
    public String page()
    {
        return "index.html";
    }



    @Auth(verifyMethod = Auth.VERIFYMETHOD_ADMIN_AUTH,requestType = Auth.REQUEST_FORM)
    @RequestMapping(value = "/welcome",method = RequestMethod.GET)
    public String welcome()
    {
        return "welcome.html";
    }

}

