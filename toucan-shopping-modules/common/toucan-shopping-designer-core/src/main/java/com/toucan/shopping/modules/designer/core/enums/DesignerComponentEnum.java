package com.toucan.shopping.modules.designer.core.enums;

public enum DesignerComponentEnum {

    PAGE_CONTAINER("pageContainer");

    private String componentType;

    DesignerComponentEnum(String componentType){
        this.componentType =componentType;
    }


    public String value()
    {
        return componentType;
    }

}
