package com.toucan.shopping.modules.message.enums;

public enum MessageAppEnum {

    SHOPPING_WEB("10001001");

    private String appCode;

    MessageAppEnum(String appCode){
        this.appCode=appCode;
    }


    public String value()
    {
        return appCode;
    }


}
