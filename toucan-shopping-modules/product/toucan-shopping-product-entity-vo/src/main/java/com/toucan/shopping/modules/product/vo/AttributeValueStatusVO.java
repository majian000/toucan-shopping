package com.toucan.shopping.modules.product.vo;

import lombok.Data;

/**
 * 属性是否禁用
 */
@Data
public class AttributeValueStatusVO {

    private String valuePath; //值路径

    private String value; //属性值

    private Integer status = 0; //0:禁用 1:启用

}
