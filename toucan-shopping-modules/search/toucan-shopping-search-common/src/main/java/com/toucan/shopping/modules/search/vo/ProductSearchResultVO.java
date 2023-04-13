package com.toucan.shopping.modules.search.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 商品搜索响应对象
 */
@Data
public class ProductSearchResultVO {

    private Long id; //SKU ID

    private Long skuId; //SKU ID

    private String name; //商品名称

    private BigDecimal price; //价格

    private String brandName; //品牌名称

    private Short status; //是否上架 0:未上架 1:已上架

    private String categoryName; //分类名称

    private BigDecimal randk; //权重值

    private String productPreviewPath; //商品图片预览

}
