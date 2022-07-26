package com.toucan.shopping.modules.product.vo;

import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.ToStringSerializer;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

/**
 * 属性名/值对象
 */
@Data
public class ProductSpuAttributeKeyValueVO {


    @JsonFormat(shape=JsonFormat.Shape.STRING)
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long attributeKeyId; //属性名ID


    @JsonFormat(shape=JsonFormat.Shape.STRING)
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long attributeValueId;

    @JsonFormat(shape=JsonFormat.Shape.STRING)
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long parentAttributeKeyId; //上级属性名主键

    @JsonFormat(shape=JsonFormat.Shape.STRING)
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long categoryId; //所属类别

    private Short queryStatus; //搜索状态 1可搜索 0不可搜索
    private Short showStatus; //显示状态 1显示 0隐藏

    private String attributeName; //属性名
    private String attributeValue; //属性值
    private Long attributeSort; //排序
    private Integer type; //类型 1属性名 2:属性值


}
