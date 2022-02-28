package com.toucan.shopping.modules.product.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.ToStringSerializer;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * 店铺维度的商品 (关联到平台维度的SPU)
 *
 * @author majian
 */
@Data
public class ShopProduct {


    @JsonFormat(shape=JsonFormat.Shape.STRING)
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long id; //主键

    private String uuid; //SPU UUID

    @JsonFormat(shape=JsonFormat.Shape.STRING)
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long categoryId; //所属类别

    private String productUuid; //平台维度的SPU的UUID

    @JsonFormat(shape=JsonFormat.Shape.STRING)
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long productId; //平台维度的SPU的ID

    private String name; //商品名称

    private String attributes; //这个店铺的商品所有属性

    private Integer deleteStatus; //删除状态 0未删除 1已删除

    private String appCode; //所属应用

    private Date createDate; //创建时间

    @JsonFormat(shape=JsonFormat.Shape.STRING)
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long createUserId; //创建人ID

    private Short status; //是否上架 0:未上架 1:已上架

    @JsonFormat(shape=JsonFormat.Shape.STRING)
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long shopId; //店铺ID

    private Short approveStatus; //1审核中 2审核通过 3审核驳回

    private String rejectText; //驳回原因

    private String sellerNo; //卖家编码

    private Short payMethod; //付款方式 1:一口价(普通交易模式)

    private Short buckleInventoryMethod; //库存计数 1:买家拍下减库存 2:买家付款减库存

    private Short giveInvoice; //1:提供发票 0:不提供

    private Short changeOrReturn; //退换货承诺 1:承诺 0:不承诺

    private String articleNumber; //货号

    @JsonFormat(shape=JsonFormat.Shape.STRING)
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long brandId; //品牌ID

    @JsonFormat(shape=JsonFormat.Shape.STRING)
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long shopCategoryId; //店铺内的所属分类

    private String etractMethod; //提取方式(多选用,分割) 1:用物流配送


}
