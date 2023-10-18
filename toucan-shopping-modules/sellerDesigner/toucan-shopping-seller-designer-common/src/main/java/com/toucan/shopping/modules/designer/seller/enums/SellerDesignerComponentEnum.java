package com.toucan.shopping.modules.designer.seller.enums;

public enum SellerDesignerComponentEnum {

    SHOP_PAGE_CONTAINER("shopPageContainer"),
    SHOP_BANNER("shopBanner");

    private String componentType;

    SellerDesignerComponentEnum(String componentType){
        this.componentType =componentType;
    }


    public String value()
    {
        return componentType;
    }

}
