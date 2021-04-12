package com.toucan.shopping.product.entity;

import lombok.Data;

import java.util.Date;

/**
 * 属性键
 *
 * @author majian
 */
@Data
public class AttributeKey {
    private Long id; //主键
    private Integer categoryId; //所属类别
    private String attributeName; //属性名
    private Long attributeSort; //排序
    private Date createDate; //创建时间
    private String appCode; //所属应用
    private Long createUserId; //创建人ID

    /**
     * 删除状态 0未删除 1已删除
     */
    private Short deleteStatus;
}
