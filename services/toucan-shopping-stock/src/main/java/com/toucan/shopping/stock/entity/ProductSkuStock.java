package com.toucan.shopping.stock.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * 商品SKU库存表
 *
 * @author majian
 */
@Data
public class ProductSkuStock {
    private Long id; //主键
    private String productUuid; //所属商品spu UUID
    private String skuUuid; //所属商品sku UUID
    private String uuid; //UUID
    private Integer stockNum; //库存数量
    private String remark; //备注
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

}
