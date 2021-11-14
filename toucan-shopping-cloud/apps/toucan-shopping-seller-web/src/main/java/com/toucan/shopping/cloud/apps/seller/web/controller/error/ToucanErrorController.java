package com.toucan.shopping.cloud.apps.seller.web.controller.error;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping(value = "/error")
public class ToucanErrorController implements ErrorController {


    /**
     * 报错默认会跳转到免费开店页面
     * @return
     */
    @Override
    public String getErrorPath() {
        return "/htmls/release/freeShop";
    }

    @RequestMapping
    public String error(HttpServletRequest request) {
        return getErrorPath();
    }
}
