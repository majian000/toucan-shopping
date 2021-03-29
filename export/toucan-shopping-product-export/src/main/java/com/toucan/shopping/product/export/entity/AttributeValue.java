package com.toucan.shopping.product.export.entity;

import lombok.Data;

import java.util.Date;

/**
 * 属性值
 *
 * @author majian
 */
@Data
public class AttributeValue {
    private Long id; //主键
    private Long attributeKeyId;
    private Integer categoryId; //所属类别
    private String attributeValue; //属性值
    private Integer attributeSort; //排序
    private String createDate; //创建时间
    private String appCode; //所属应用
    private Long createUserId; //创建人ID

}
