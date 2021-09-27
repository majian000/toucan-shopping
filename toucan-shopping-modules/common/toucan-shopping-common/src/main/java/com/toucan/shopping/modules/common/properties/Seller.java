package com.toucan.shopping.modules.common.properties;

import lombok.Data;

/**
 * 卖家配置
 */
@Data
public class Seller {

    /**
     * 店铺图标最大限制
     */
    private Long shopLogoMaxSize;

    /**
     * 店铺默认最大分类数量
     */
    private Integer shopCategoryMaxCount;

    /**
     * 店铺默认图标
     */
    private String defaultShopLogo = "";

}
