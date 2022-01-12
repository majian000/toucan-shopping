package com.toucan.shopping.modules.message.enums;

public enum MessageTypeEnum {
    NOTICE_MESSAGE("10001001","通知消息","30009"),
    HEAD_SCULPTURE("10001001","头像审核","30010"),
    TRUENAME("10001001","实名审核","30011");

    private String appCode;
    private String typeName;
    private String typeCode;

    MessageTypeEnum(String appCode,String typeName,String typeCode){
        this.appCode = appCode;
        this.typeName = typeName;
        this.typeCode = typeCode;
    }


    public String getCode()
    {
        return this.typeCode;
    }

    public String getName()
    {
        return this.typeName;
    }

    public String getAppCode()
    {
        return this.appCode;
    }

}
