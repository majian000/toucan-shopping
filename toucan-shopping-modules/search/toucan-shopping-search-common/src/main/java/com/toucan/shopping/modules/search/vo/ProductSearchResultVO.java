package com.toucan.shopping.modules.search.vo;

import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.ToStringSerializer;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * 商品搜索响应对象
 */
@Data
public class ProductSearchResultVO {

    @JsonFormat(shape=JsonFormat.Shape.STRING)
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long id; //SKU ID

    @JsonFormat(shape=JsonFormat.Shape.STRING)
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long skuId; //SKU ID

    private String name; //商品名称

    private BigDecimal price; //价格

    private String brandName; //品牌名称

    @JsonFormat(shape=JsonFormat.Shape.STRING)
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long brandId; //品牌ID

    private String categoryName; //分类名称

    private List<String> categoryIds; //分类ID路径列表

    @JsonFormat(shape=JsonFormat.Shape.STRING)
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long categoryId; //分类ID

    private BigDecimal randk; //权重值

    private String attributeValueGroup; //属性值组合

    private String productPreviewPath; //商品图片预览

    private String httpProductPreviewPath; //商品图片预览

    private String createDate; //创建时间

    private String updateDate; //修改时间


}
