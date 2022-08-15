package com.toucan.shopping.cloud.apps.web.controller.user;


import com.toucan.shopping.modules.auth.user.UserAuth;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 购物车
 */
@Controller("buyCarPageController")
@RequestMapping("/page/user/buyCar")
public class BuyCarPageController {


    @UserAuth
    @RequestMapping("/page")
    public String page()
    {
        return "user/buyCar/buy_car";
    }

}
