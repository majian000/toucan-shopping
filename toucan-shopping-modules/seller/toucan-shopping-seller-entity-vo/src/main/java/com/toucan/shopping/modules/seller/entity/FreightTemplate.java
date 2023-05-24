package com.toucan.shopping.modules.seller.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.ToStringSerializer;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 运费模板
 *
 * @author majian
 * @date 2022-9-7 15:39:24
 */
@Data
public class FreightTemplate {

    @JsonFormat(shape=JsonFormat.Shape.STRING)
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long id; //主键

    private String name; //模板名称

    private Short freightStatus; //运费状态 1:自定义运费 2:包邮


    private String deliverProvinceCode; //发货地省份编码

    private String deliverCityCode; //发货地地市编码

    private String deliverAreaCode; //发货地区县编码

    private String deliverProvinceName; //发货地省份名称

    private String deliverCityName; //发货地地市名称

    private String deliverAreaName; //发货地区县名称

    private String transportModel; //运送方式 1:快递 2:EMS 3:平邮 (多个用,分割)

    private Short valuationMethod; //计价方式 1:按件数 2:按重量 3:按体积

    private Long templateSort; //模板排序

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
    private String remark; //备注

    /**
     * 删除状态 0未删除 1已删除
     */
    private Short deleteStatus;
}
