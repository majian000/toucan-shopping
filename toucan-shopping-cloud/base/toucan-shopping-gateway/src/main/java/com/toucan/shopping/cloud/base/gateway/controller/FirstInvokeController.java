package com.toucan.shopping.cloud.base.gateway.controller;


import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FirstInvokeController {


    @RequestMapping(value="/invoke",produces = "application/json;charset=UTF-8")
    public void invoke(){

    }

}
