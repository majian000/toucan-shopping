package com.toucan.shopping.modules.order.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 订单支付流水
 *
 * @author majian
 */
@Data
public class OrderPay {

    @JsonFormat(shape=JsonFormat.Shape.STRING)
    private Long id; //主键
    private String payNo; //内部支付流水号
    private String mainOrderNo; //主订单编号
    private String userId; //用户ID
    private Integer payType; //交易类型 -1未确定 0微信 1支付宝
    private String tradeType; //支付场景 JSAPI/NATIVE/APP/H5
    private Integer payMethod; //支付方式 1线上 2线下
    private BigDecimal payAmount = new BigDecimal(0); //交易金额
    private Integer tradeStatus; //流水状态 0待支付 1支付成功 2支付失败 3已关闭
    private String payerId; //第三方付款人标识(openid/buyer_id)
    private String prepayId; //第三方预支付单号
    private String outerTradeNo; //第三方交易流水号
    private String notifyId; //第三方通知唯一ID
    private Integer verifyStatus; //验签结果 0未验 1通过 2失败

    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date notifyTime; //回调通知时间

    private String notifyContent; //回调原始报文

    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date payDate; //实际支付时间

    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date expireTime; //支付过期时间

    private String appCode; //所属应用

    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date createDate; //记录创建时间

    /**
     * 删除状态 0未删除 1已删除
     */
    private Short deleteStatus;

    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date shardingDate; //分片日期(取订单创建时间)
}
