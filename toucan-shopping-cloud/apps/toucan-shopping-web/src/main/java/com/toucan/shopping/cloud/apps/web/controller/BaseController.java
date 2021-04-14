package com.toucan.shopping.cloud.apps.web.controller;

import org.springframework.beans.factory.annotation.Value;

public abstract class BaseController {

    @Value("${toucan.app-code}")
    private String appCode;



    public String getAppCode()
    {
        return appCode;
    }

}
