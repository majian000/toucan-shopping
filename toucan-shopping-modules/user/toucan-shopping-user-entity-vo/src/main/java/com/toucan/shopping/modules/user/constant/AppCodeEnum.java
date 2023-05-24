package com.toucan.shopping.modules.user.constant;

public enum AppCodeEnum {

    SHOPPING_WEB("10001001");

    private String appCode;

    AppCodeEnum(String appCode){
        this.appCode=appCode;
    }


    public String value()
    {
        return appCode;
    }

}
