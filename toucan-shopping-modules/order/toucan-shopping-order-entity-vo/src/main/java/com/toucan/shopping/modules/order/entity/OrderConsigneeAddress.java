package com.toucan.shopping.modules.order.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.ToStringSerializer;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 订单收货人
 */
@Data
public class OrderConsigneeAddress {
    /**
     * 主键 雪花算法生成
     */
    @JsonFormat(shape=JsonFormat.Shape.STRING)
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long id;

    /**
     * 所属用户ID
     */
    @JsonFormat(shape=JsonFormat.Shape.STRING)
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long userMainId;

    @JsonFormat(shape=JsonFormat.Shape.STRING)
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long orderId; //订单ID

    private String orderNo; //订单编号

    private String mainOrderNo; //主订单编号

    /**
     * 收货人姓名
     */
    private String name;

    /**
     * 收货人地址
     */
    private String address;


    /**
     * 收货人电话
     */
    private String phone;

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


    /**
     * 创建时间
     */
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date createDate;

    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date shardingDate; //分片日期

    /**
     * 修改时间
     */
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date updateDate;

    /**
     * 删除状态 0未删除 1已删除
     */
    private Short deleteStatus;


    /**
     * 应用编码
     */
    private String appCode;

}
