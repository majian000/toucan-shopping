package com.toucan.shopping.modules.common.properties.seller;

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
     * 运费模板最大数量
     */
    private Integer freightTemplateMaxCount;

    /**
     * 店铺默认图标
     */
    private String defaultShopLogo = "";

}
