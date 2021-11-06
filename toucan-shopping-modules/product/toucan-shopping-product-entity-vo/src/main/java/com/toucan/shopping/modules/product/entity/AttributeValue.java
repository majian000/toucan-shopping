package com.toucan.shopping.modules.product.entity;

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
    private Long createAdminId; //创建人ID
    private Date updateDate; //修改时间
    private Long updateAdminId; //修改人ID

}
