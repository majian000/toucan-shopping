package com.toucan.shopping.cloud.apps.web.controller;

import com.toucan.shopping.modules.common.properties.Toucan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

public abstract class BaseController {

    @Value("${toucan.app-code}")
    private String appCode;

    @Autowired
    private Toucan toucan;


    public String getAppCode()
    {
        return appCode;
    }

    public Toucan getToucan()
    {
        return toucan;
    }
}
