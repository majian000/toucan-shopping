package com.toucan.shopping.modules.seller.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.alibaba.fastjson.serializer.ToStringSerializer;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 商户店铺信息
 */
@Data
public class SellerShop {
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
     * 店铺介绍
     */
    private String introduce;

    /**
     * 店铺图标
     */
    private String logo;

    /**
     * 省
     */
    private String province;

    /**
     * 省编码
     */
    private String provinceCode;
    /**
     * 市
     */
    private String city;
    /**
     * 市编码
     */
    private String cityCode;

    /**
     * 区县
     */
    private String area;
    /**
     * 区县编码
     */
    private String areaCode;

    /**
     * 详细地址
     */
    private String detailAddress;

    /**
     * 审核状态 1审核中 2审核通过 3审核驳回 (个人店铺直接通过)
     */
    private Integer approveStatus;

    /**
     * 启用状态 0:禁用 1启用
     */
    private Short enableStatus;

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
     * 创建人
     */
    private String createAdminId;


    /**
     * 修改时间
     */
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date updateDate;

    /**
     * 修改人
     */
    private String updateAdminId;


    /**
     * 删除状态 0未删除 1已删除
     */
    private Short deleteStatus;

}
