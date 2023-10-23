package com.toucan.shopping.modules.designer.seller.enums;

public enum SellerViewEnum {

    SHOP_PAGE_VIEW("shopPageView"),
    SHOP_BANNER_VIEW("shopBannerView");

    private String componentType;

    SellerViewEnum(String componentType){
        this.componentType =componentType;
    }


    public String value()
    {
        return componentType;
    }

}
