package com.toucan.shopping.modules.seller.vo;


import lombok.Data;

/**
 * 店铺总览
 */
@Data
public class ShopOverviewVO {

    private String shopLogo; //店铺图标

    private String httpShopLogo; //店铺图标地址

    private Long shelvesProductCount = 0L; //上架商品数量

    private Long collectShopCount = 0L; //收藏店铺数量

    private Long waitCommentsOrderCount = 0L; //待评价订单数量

    private Long waitApproveProductCount = 0L; //待审核商品数量

}
