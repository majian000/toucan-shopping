package com.toucan.shopping.modules.product.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * 商品SKU
 *
 * @author majian
 */
@Data
public class ProductSku {
    private Long id; //主键
    private String name; //SKU名称(商品名称 + 属性值)
    private String attributes; //这个SKU的属性
    private String shopProductUuid; //店铺SPU的UUID
    private Long shopProductId; //这个店铺发布的商品的ID
    private String uuid; //SKU的UUID
    private Double price; //价格
    private String remark; //备注
    private Short status; //是否上架 0:未上架 1:已上架
    private String appCode; //所属应用
    private Long createUserId; //创建人ID
    private Long updateUserId; //创建人ID

    /**
     * 删除状态 0未删除 1已删除
     */
    private Short deleteStatus;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date createDate; //创建时间
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date updateDate; //修改时间
    private Long shopId; //店铺ID
    private Long brankId; //品牌ID

    private Integer stockNum=0; //库存
    private String productPreviewPath; //商品主图路径
    private String productUuid; //SPU的UUID
    private Long productId; //这个商品的ID

}
