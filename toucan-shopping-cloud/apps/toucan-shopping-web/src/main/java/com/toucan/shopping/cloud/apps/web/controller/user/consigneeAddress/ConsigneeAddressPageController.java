package com.toucan.shopping.cloud.apps.web.controller.user.consigneeAddress;


import com.toucan.shopping.modules.auth.user.UserAuth;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 用户收货地址
 */
@Controller("consigneeAddressPageController")
@RequestMapping("/page/user/consigneeAddress")
public class ConsigneeAddressPageController {


    @UserAuth
    @RequestMapping("/add")
    public String indexPage()
    {
        return "user/consigneeAddress/add";
    }


}
