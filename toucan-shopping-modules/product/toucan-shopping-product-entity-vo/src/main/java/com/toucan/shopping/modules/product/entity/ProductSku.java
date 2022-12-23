package com.toucan.shopping.modules.product.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.ToStringSerializer;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 商品SKU
 *
 * @author majian
 */
@Data
public class ProductSku {

    @JsonFormat(shape=JsonFormat.Shape.STRING)
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long id; //主键

    private String name; //SKU名称(商品名称 + 属性值)

    private String onlyName; //SKU名称(商品名称)

    private String attributes; //这个SKU的属性

    private String attributeValueGroup; //这个SKU的属性值组合,多个用_分割

    private String shopProductUuid; //店铺SPU的UUID

    @JsonFormat(shape=JsonFormat.Shape.STRING)
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long shopProductId; //这个店铺发布的商品的ID

    private String uuid; //SKU的UUID

    private BigDecimal price; //价格

    private String remark; //备注

    private Short status; //是否上架 0:未上架 1:已上架

    private String appCode; //所属应用


    @JsonFormat(shape=JsonFormat.Shape.STRING)
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long createUserId; //创建人ID


    @JsonFormat(shape=JsonFormat.Shape.STRING)
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long updateUserId; //创建人ID

    /**
     * 删除状态 0未删除 1已删除
     */
    private Short deleteStatus;

    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date createDate; //创建时间

    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date updateDate; //修改时间


    @JsonFormat(shape=JsonFormat.Shape.STRING)
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long shopId; //店铺ID


    @JsonFormat(shape=JsonFormat.Shape.STRING)
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long brankId; //品牌ID

    private Integer stockNum=0; //库存

    private String productPreviewPath; //商品主图路径

    private String httpDescriptionImgPath; //商品介绍图HTTP路径

    private String productUuid; //SPU的UUID


    @JsonFormat(shape=JsonFormat.Shape.STRING)
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long productId; //这个商品的ID


    @JsonFormat(shape=JsonFormat.Shape.STRING)
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long categoryId; //商品分类ID

    @JsonFormat(shape=JsonFormat.Shape.STRING)
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long shopCategoryId; //店铺商品分类ID

    private String productNo; //商品编号

    private BigDecimal roughWeight; //毛重

    private BigDecimal suttle; //净重

    private String lastRestoreTransactionId; //最后恢复库存的事务ID

}
