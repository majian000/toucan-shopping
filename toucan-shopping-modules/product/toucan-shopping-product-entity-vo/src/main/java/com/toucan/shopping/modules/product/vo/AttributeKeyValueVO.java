package com.toucan.shopping.modules.product.vo;

import lombok.Data;

import java.util.List;

/**
 * 属性
 */
@Data
public class AttributeKeyValueVO {

    private Long productSpuId; //SPU ID

    private Long categoryId; //分类ID

    private List<String> attributeKeyList; //属性名列表

    private List<String> attributeValueList; //属性值列表

}
