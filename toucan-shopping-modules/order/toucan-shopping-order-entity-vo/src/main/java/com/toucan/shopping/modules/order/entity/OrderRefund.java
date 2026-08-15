package com.toucan.shopping.modules.order.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 订单退款流水
 *
 * @author majian
 */
@Data
public class OrderRefund {

    @JsonFormat(shape=JsonFormat.Shape.STRING)
    private Long id; //主键
    private String refundNo; //内部退款流水号
    private String payNo; //关联支付流水号
    private String mainOrderNo; //主订单编号
    private String orderNo; //子订单编号
    private String userId; //用户ID
    private Integer refundType; //退款类型 1全额 2部分
    private BigDecimal refundAmount = new BigDecimal(0); //退款金额
    private Integer refundStatus; //退款状态 0退款中 1退款成功 2退款失败
    private String outerRefundNo; //第三方退款单号
    private String notifyContent; //退款回调原始报文

    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date refundDate; //退款时间

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
