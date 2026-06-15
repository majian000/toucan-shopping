package com.toucan.shopping.cloud.apps.seller.web.controller.error;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.http.HttpServletRequest;

@Controller
@RequestMapping(value = "/error")
public class ToucanErrorController implements ErrorController {


    @RequestMapping
    public String error(HttpServletRequest request) {
        return "/htmls/release/freeShop";
    }
}
