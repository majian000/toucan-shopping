package com.toucan.shopping.modules.product.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.ToStringSerializer;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 属性值
 *
 * @author majian
 */
@Data
public class AttributeValue {

    @JsonFormat(shape=JsonFormat.Shape.STRING)
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long id; //主键

    @JsonFormat(shape=JsonFormat.Shape.STRING)
    @JSONField(serializeUsing = ToStringSerializer.class)

    private Long attributeKeyId;
    private String attributeValue; //属性值
    private Integer attributeSort; //排序
    private Short attributeType; //属性类型 1:普通属性 2:颜色属性
    private String attributeValueExtend1; //属性值扩展字段
    private String attributeValueExtend2; //属性值扩展字段
    private String attributeValueExtend3; //属性值扩展字段


    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date createDate; //创建时间
    private String createAdminId; //创建人ID

    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date updateDate; //修改时间
    private String updateAdminId; //修改人ID
    private String remark; //备注
    /**
     * 删除状态 0未删除 1已删除
     */
    private Short deleteStatus;

}
