package com.toucan.shopping.modules.order.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.ToStringSerializer;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 子订单
 *
 * @author majian
 */
@Data
public class Order {

    @JsonFormat(shape=JsonFormat.Shape.STRING)
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long id; //主键
    private String orderNo; //订单编号
    private String mainOrderNo; //主订单编号
    private String userId; //用户ID
    private BigDecimal orderAmount = new BigDecimal(0); //订单金额(折扣算完)
    private BigDecimal payAmount = new BigDecimal(0); //付款金额
    private BigDecimal freightAmount = new BigDecimal(0); //运费总金额
    private BigDecimal redPackageAmount = new BigDecimal(0); //红包金额
    private BigDecimal couponAmount = new BigDecimal(0); //优惠券金额
    private BigDecimal totalAmount; //订单金额(不算折扣)
    private Integer payStatus; //支付状态 0未支付 1已支付  4取消支付
    private Integer tradeStatus; //交易状态 0:待付款 4:待发货 1:待收货 2:已取消 3:已完成
    private Integer payMethod;  //支付方式 1线上支付 2线下支付
    private Integer payType; //交易类型 -1未确定 0微信 1支付宝
    private String outerTradeNo; //交易订单号(微信支付宝交易流水号)
    private Integer srcType; //用户购买渠道 1:pc 2:app

    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date paymentDeadlineTime; //支付截止时间(超时时间)

    @JsonFormat(shape=JsonFormat.Shape.STRING)
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long shopId; //店铺ID

    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date cancelDate; //取消订单时间

    private String cancelRemark; //取消订单备注


    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date bestDate; //最佳送货时间

    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date payDate; //订单支付时间

    private String remark; //订单备注 买家填写

    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date createDate; //创建时间

    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date shardingDate; //分片日期

    private String appCode; //所属应用

    /**
     * 删除状态 0未删除 1已删除
     */
    private Short deleteStatus;
}
