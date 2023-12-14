package com.toucan.shopping.modules.designer.seller.enums;

public enum SellerComponentViewEnum {

    SHOP_PAGE_VIEW("shopPageView"),
    SHOP_BANNER_VIEW("shopBannerView"),
    IMAGE_VIEW("imageView");

    private String componentType;

    SellerComponentViewEnum(String componentType){
        this.componentType =componentType;
    }


    public String value()
    {
        return componentType;
    }

}
