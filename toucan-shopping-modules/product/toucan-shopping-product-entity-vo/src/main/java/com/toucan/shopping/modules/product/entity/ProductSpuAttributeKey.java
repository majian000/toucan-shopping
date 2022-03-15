package com.toucan.shopping.modules.product.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.ToStringSerializer;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 平台商品公共属性名(属性的快照)
 * @author majian
 */
@Data
public class ProductSpuAttributeKey {
    @JsonFormat(shape=JsonFormat.Shape.STRING)
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long id; //主键


    @JsonFormat(shape=JsonFormat.Shape.STRING)
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long categoryId; //所属类别

    private Short queryStatus; //搜索状态 1可搜索 0不可搜索

    @JsonFormat(shape=JsonFormat.Shape.STRING)
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long attributeKeyId; //属性名ID

    private String attributeName; //属性名
    private Long attributeSort; //排序
    private Short showStatus; //显示状态 1显示 0隐藏

    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date updateDate; //创建时间
    private String createAdminId; //创建人ID
    private String updateAdminId; //修改人ID



    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date createDate; //创建时间


    @JsonFormat(shape=JsonFormat.Shape.STRING)
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long parentId; //上级属性名主键


    /**
     * 备注
     */
    private String remark;

    /**
     * 删除状态 0未删除 1已删除
     */
    private Short deleteStatus;


    @JsonFormat(shape=JsonFormat.Shape.STRING)
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long productSpuId; //平台商品SPU ID


}
