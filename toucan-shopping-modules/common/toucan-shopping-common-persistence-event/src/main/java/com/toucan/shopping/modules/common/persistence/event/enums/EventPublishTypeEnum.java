package com.toucan.shopping.modules.common.persistence.event.enums;

public enum EventPublishTypeEnum {

    USER_TRUESCULPTURE_MESSAGE("实名认证消息","70010");

    private String name;
    private String code;

    EventPublishTypeEnum(String name,String code)
    {
        this.name=name;
        this.code=code;
    }

    public String getCode()
    {
        return this.code;
    }

    public String getName()
    {
        return this.name;
    }
}
