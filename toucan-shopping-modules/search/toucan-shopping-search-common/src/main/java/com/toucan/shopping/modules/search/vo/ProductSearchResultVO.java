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

    private Long brandId; //品牌ID

    private String categoryName; //分类名称

    private String categoryIdPath; //分类ID路径 格式 .根节点.一级节点.二级节点

    private Long categoryId; //分类ID

    private BigDecimal randk; //权重值

    private String attributeValueGroup; //属性值组合

    private String productPreviewPath; //商品图片预览

    private String httpProductPreviewPath; //商品图片预览

}
