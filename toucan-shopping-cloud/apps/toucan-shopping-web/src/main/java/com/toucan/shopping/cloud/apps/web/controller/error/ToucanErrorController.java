package com.toucan.shopping.cloud.apps.web.controller.error;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/error")
public class ToucanErrorController implements org.springframework.boot.web.servlet.error.ErrorController {


    /**
     * 报错默认会跳转到首页
     * @return
     */
    @Override
    public String getErrorPath() {
        return "/htmls/release/index";
    }

    @RequestMapping
    public String error() {
        return getErrorPath();
    }
}
