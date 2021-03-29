package com.toucan.shopping.product.entity;

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
    private Date createDate; //创建时间
    private String appCode; //所属应用
    private Long createUserId; //创建人ID

    /**
     * 删除状态 0未删除 1已删除
     */
    private Short deleteStatus;
}
