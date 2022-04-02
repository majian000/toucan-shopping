package com.toucan.shopping.modules.product.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.ToStringSerializer;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 店铺商品审核记录
 */
@Data
public class ShopProductApproveRecord {


    /**
     * 主键 雪花算法生成
     */
    @JsonFormat(shape=JsonFormat.Shape.STRING)
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long id;


    /**
     * 审核ID 店铺商品表ID
     */
    @JsonFormat(shape=JsonFormat.Shape.STRING)
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long approveId;



    /**
     * 审核状态 1通过 2驳回
     */
    private Integer approveStatus;

    /**
     * 驳回原因
     */
    private String approveText;

    /**
     * 审核人
     */
    private String createAdminId;

    /**
     * 创建时间
     */
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date createDate;



    /**
     * 删除状态 0未删除 1已删除
     */
    private Short deleteStatus;



}
