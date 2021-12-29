package com.toucan.shopping.modules.message.enums;

public enum MessageTypeEnum {

    HEAD_SCULPTURE("头像审核","30010");

    private String typeCode;
    private String typeName;

    MessageTypeEnum(String typeName,String typeCode){
        this.typeName = typeName;
        this.typeCode = typeCode;
    }


    public String getCode()
    {
        return typeCode;
    }

    public String getName()
    {
        return typeName;
    }


}
