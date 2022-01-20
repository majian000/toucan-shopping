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
    private String attributeValue; //属性值
    private Integer attributeSort; //排序
    private Date createDate; //创建时间
    private String createAdminId; //创建人ID
    private Date updateDate; //修改时间
    private String updateAdminId; //修改人ID

}
