package com.toucan.shopping.cloud.apps.web.controller.error;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/error")
public class ToucanErrorController implements ErrorController {


    @RequestMapping
    public String error() {
        return "/htmls/release/index";
    }
}
