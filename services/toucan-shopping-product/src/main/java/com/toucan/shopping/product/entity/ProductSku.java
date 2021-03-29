package com.toucan.shopping.product.entity;

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
    private String attributes; //商品所有属性
    private String productUuid; //SPU的UUID
    private String uuid; //SKU的UUID
    private Double price; //价格
    private String remark; //备注
    private Short status; //是否上架 0:未上架 1:已上架
    private String appCode; //所属应用
    private Long createUserId; //创建人ID

    /**
     * 删除状态 0未删除 1已删除
     */
    private Short deleteStatus;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date createDate; //创建时间

}
