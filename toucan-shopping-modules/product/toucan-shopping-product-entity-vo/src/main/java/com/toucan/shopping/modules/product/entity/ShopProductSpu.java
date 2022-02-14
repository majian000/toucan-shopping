package com.toucan.shopping.modules.product.entity;

import lombok.Data;

import java.util.Date;

/**
 * 店铺维度的SPU (关联到平台维度的SPU)
 *
 * @author majian
 */
@Data
public class ShopProductSpu {
    private Long id; //主键
    private Integer categoryId; //所属类别
    private String uuid; //SPU UUID
    private String name; //商品名称
    private String attributes; //这个店铺的商品所有属性
    private String productUuid; //平台维度的SPU的UUID
    private Long productId; //平台维度的SPU的ID
    private Short status; //是否上架 0:未上架 1:已上架
    private Long shopId; //店铺ID
    private Date createDate; //创建时间
    private String appCode; //所属应用
    private Long createUserId; //创建人ID
    private Integer deleteStatus; //删除状态 0未删除 1已删除





}
