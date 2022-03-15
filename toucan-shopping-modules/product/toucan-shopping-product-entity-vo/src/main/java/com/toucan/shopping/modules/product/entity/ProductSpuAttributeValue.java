package com.toucan.shopping.modules.product.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.ToStringSerializer;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 平台商品公共属性值(属性的快照)
 *
 * @author majian
 */
@Data
public class ProductSpuAttributeValue {

    @JsonFormat(shape=JsonFormat.Shape.STRING)
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long id; //主键

    @JsonFormat(shape=JsonFormat.Shape.STRING)
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long productSpuAttributeKeyId;

    @JsonFormat(shape=JsonFormat.Shape.STRING)
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long attributeKeyId;

    @JsonFormat(shape=JsonFormat.Shape.STRING)
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long attributeValueId;

    private String attributeValue; //属性值
    private Integer attributeSort; //排序
    private String attributeValueExtend1; //属性值扩展字段
    private String attributeValueExtend2; //属性值扩展字段
    private String attributeValueExtend3; //属性值扩展字段
    private Short showStatus; //显示状态 1显示 0隐藏
    private Short queryStatus; //搜索状态 1可搜索 0不可搜索


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

    @JsonFormat(shape=JsonFormat.Shape.STRING)
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long productSpuId; //平台商品SPU ID


}
