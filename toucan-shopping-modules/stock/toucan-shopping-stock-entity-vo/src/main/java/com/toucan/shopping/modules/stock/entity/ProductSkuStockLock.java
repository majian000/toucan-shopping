package com.toucan.shopping.modules.stock.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.ToStringSerializer;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * 商品SKU库存锁定表
 *
 * @author majian
 */
@Data
public class ProductSkuStockLock {

    @JsonFormat(shape=JsonFormat.Shape.STRING)
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long id; //主键

    @JsonFormat(shape=JsonFormat.Shape.STRING)
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long productSkuId;  //SKU ID

    @JsonFormat(shape=JsonFormat.Shape.STRING)
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long userMainId; //用户ID
    private String mainOrderNo; //主订单编号
    private String orderNo; //子订单编号
    private Integer stockNum; //锁定库存数量
    private Short type; //库存计数 1:买家拍下减库存 2:买家付款减库存
    private String remark; //备注
    private String appCode; //所属应用
    private Short payStatus; //支付状态 0未支付 1已支付
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date orderCreateDate; //订单创建时间

    /**
     * 删除状态 0未删除 1已删除
     */
    private Short deleteStatus;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date createDate; //创建时间

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date updateDate; //修改时间

}
