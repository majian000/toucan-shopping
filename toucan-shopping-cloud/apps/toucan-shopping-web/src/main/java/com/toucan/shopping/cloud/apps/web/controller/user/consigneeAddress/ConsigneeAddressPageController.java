package com.toucan.shopping.cloud.apps.web.controller.user.consigneeAddress;


import com.toucan.shopping.cloud.apps.web.service.LoginUserService;
import com.toucan.shopping.modules.auth.user.UserAuth;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * 用户收货地址
 */
@Controller("consigneeAddressPageController")
@RequestMapping("/page/user/consigneeAddress")
public class ConsigneeAddressPageController {


    @UserAuth
    @RequestMapping("/add")
    public String addPage(HttpServletRequest request)
    {
        return "user/consigneeAddress/add";
    }


}
