package com.toucan.shopping.modules.designer.seller.enums;

public enum SellerDesignerComponentEnum {

    PAGE_CONTAINER("pageContainer");

    private String componentType;

    SellerDesignerComponentEnum(String appCode){
        this.componentType =appCode;
    }


    public String value()
    {
        return componentType;
    }

}
