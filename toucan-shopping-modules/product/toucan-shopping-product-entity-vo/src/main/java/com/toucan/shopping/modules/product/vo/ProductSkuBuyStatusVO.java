package com.toucan.shopping.modules.product.vo;

import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.ToStringSerializer;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

/**
 * 商品购买状态
 */
@Data
public class ProductSkuBuyStatusVO {

    @JsonFormat(shape=JsonFormat.Shape.STRING)
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long id; //主键

    private String attributeValueGroup; //这个SKU的属性值组合,多个用_分割

    private Short status; //是否上架 0:未上架 1:已上架

    private Integer stockNum=0; //库存
}
