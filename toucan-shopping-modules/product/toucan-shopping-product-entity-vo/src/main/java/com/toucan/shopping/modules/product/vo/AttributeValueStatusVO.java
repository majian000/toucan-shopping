package com.toucan.shopping.modules.product.vo;

import lombok.Data;

import java.util.List;

/**
 * 属性是否禁用
 */
@Data
public class AttributeValueStatusVO {

    private String valuePath; //值路径

    private String value; //属性值

    private Integer status = 0; //0:禁用 1:启用

    private Integer statusCode = 0; //1:已售罄 2:已下架

    private Integer disabledChildCount=0; //禁用子节点数量

    private String parentValuePath; //父属性路径

    private List<AttributeValueStatusVO> children; //子属性

}
