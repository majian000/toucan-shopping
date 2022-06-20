package com.toucan.shopping.modules.product.vo;

import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.ToStringSerializer;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.List;

/**
 * 属性名/值对象 树对象
 */
@Data
public class ProductSpuAttributeKeyValueTreeVO extends ProductSpuAttributeKeyValueVO{

    private String name; //属性名
    private Integer type; //类型 1属性名 2:属性值

    private List<String> values; //属性值
    private List<ProductSpuAttributeKeyValueTreeVO> children; //子节点


}
