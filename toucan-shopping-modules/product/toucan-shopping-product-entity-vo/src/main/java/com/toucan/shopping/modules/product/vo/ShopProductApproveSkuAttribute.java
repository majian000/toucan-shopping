package com.toucan.shopping.modules.product.vo;

import lombok.Data;

import java.util.List;

/**
 * 店铺商品属性
 */

@Data
public class ShopProductApproveSkuAttribute {

    /**
     * 属性名
     */
    private String key;

    /**
     * 属性值
     */
    private List<String> values;

}
