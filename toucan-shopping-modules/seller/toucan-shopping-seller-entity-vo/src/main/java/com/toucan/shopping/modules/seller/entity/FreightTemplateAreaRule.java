package com.toucan.shopping.modules.seller.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.ToStringSerializer;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 运费模板地区规则
 *
 * @author majian
 * @date 2022-9-7 15:39:24
 */
@Data
public class FreightTemplateAreaRule {

    @JsonFormat(shape=JsonFormat.Shape.STRING)
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long id; //主键

    /**
     * 所在省份编码
     */
    private String provinceCode;

    /**
     * 所在地市编码
     */
    private String cityCode;

    /**
     * 所在区县编码
     */
    private String areaCode;


    /**
     * 所在省份名称
     */
    private String provinceName;

    /**
     * 所在地市名称
     */
    private String cityName;

    /**
     * 所在区县名称
     */
    private String areaName;

    private String transportModel; //运送方式 1:快递 2:EMS 3:平邮

    private Double firstWeight; //首重量 单位KG/件

    private Double firstWeightMoney; //首重金额

    private Double appendWeight; //续重量 单位KG/件

    private Double appendWeightMoney; //续重金额

    @JsonFormat(shape=JsonFormat.Shape.STRING)
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long groupId; //分组ID



    /**
     * 默认规则ID
     */
    @JsonFormat(shape=JsonFormat.Shape.STRING)
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long defaultRuleId;

    /**
     * 模板ID
     */
    @JsonFormat(shape=JsonFormat.Shape.STRING)
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long templateId;


    /**
     * 所属用户ID
     */
    @JsonFormat(shape=JsonFormat.Shape.STRING)
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long userMainId;

    /**
     * 关联店铺,用该字段分库分表
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
