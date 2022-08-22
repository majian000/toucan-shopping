package com.toucan.shopping.cloud.apps.web.controller.user.buyCar;


import com.toucan.shopping.modules.auth.user.UserAuth;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 用户购物车
 */
@Controller("buyCarPageController")
@RequestMapping("/page/user/buyCar")
public class UserBuyCarPageController {


    @UserAuth
    @RequestMapping("/index")
    public String indexPage()
    {
        return "user/buyCar/user_buy_car";
    }


    @UserAuth
    @RequestMapping("/confirm")
    public String confirmPage()
    {
        return "user/buyCar/user_buy_confirm";
    }

}
