package com.toucan.shopping.modules.seller.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.ToStringSerializer;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 运费模板默认规则
 *
 * @author majian
 * @date 2022-9-7 15:39:24
 */
@Data
public class FreightTemplateDefaultRule {

    @JsonFormat(shape=JsonFormat.Shape.STRING)
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long id; //主键

    private String transportModel; //运送方式 1:快递 2:EMS 3:平邮

    private Double defaultWeight; //默认XXX.XX重量以内 单位KG/件

    private Double defaultWeightMoney; //默认XXX.XX重量以内金额

    private Double defaultAppendWeight; //默认增加XXX.XX重量以内 单位KG/件

    private Double defaultAppendWeightMoney; //默认增加XXX.XX重量以内金额


    /**
     * 模板ID
     */
    @JsonFormat(shape=JsonFormat.Shape.STRING)
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long templateId;


    /**
     * 所属用户ID,用该字段分库分表
     */
    @JsonFormat(shape=JsonFormat.Shape.STRING)
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long userMainId;

    /**
     * 关联店铺
     */
    @JsonFormat(shape=JsonFormat.Shape.STRING)
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long shopId;


    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date createDate; //创建时间


    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date updateDate; //创建时间

    /**
     * 删除状态 0未删除 1已删除
     */
    private Short deleteStatus;
}
