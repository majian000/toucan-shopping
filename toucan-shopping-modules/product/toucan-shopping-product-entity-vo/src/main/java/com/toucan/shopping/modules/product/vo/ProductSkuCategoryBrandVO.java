package com.toucan.shopping.modules.product.vo;

import com.alibaba.fastjson2.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

/**
 * 商品分类品牌
 */
@Data
public class ProductSkuCategoryBrandVO {

    @JsonFormat(shape=JsonFormat.Shape.STRING)
    private Long id; //ID

    private String name; //名称

    private int type; //类型 1:分类 2:品牌


}
