package com.toucan.shopping.modules.sller.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.alibaba.fastjson.serializer.ToStringSerializer;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 卖家店铺信息
 */
@Data
public class SllerShop {
    /**
     * 主键 雪花算法生成
     */
    @JsonFormat(shape=JsonFormat.Shape.STRING)
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long id;

    /**
     * 所属用户ID,用该字段分库分表
     */
    @JsonFormat(shape=JsonFormat.Shape.STRING)
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long userMainId;

    /**
     * 公开店铺ID
     */
    private String publicShopId;

    /**
     * 店铺名称
     */
    private String name;


    /**
     * 审核状态 1审核中 2审核通过 3审核驳回
     */
    private Integer approveStatus;

    /**
     * 店铺类型 1个人 2企业
     */
    private Integer type;

    /**
     * 店铺备注
     */
    private String remark;


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
