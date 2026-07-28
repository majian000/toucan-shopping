package com.toucan.shopping.modules.product.vo;


import com.alibaba.fastjson2.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 分类商品SKU统计
 * @author majian
 */
@Data
public class CategoryProductSkuStatisticVO {

    @JsonFormat(shape=JsonFormat.Shape.STRING)
    private Long count; //总数


    @JsonFormat(shape=JsonFormat.Shape.STRING)
    private Long categoryId; //分类ID

    @JsonFormat(shape=JsonFormat.Shape.STRING)
    private Long parentCategoryId; //上级分类ID

    private String categoryName; //分类名称



}
