package com.toucan.shopping.modules.pay.vo;

import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

/**
 * 支付对象
 *
 * @author majian
 */
@Data
public class PayCallbackVO {


    private final Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * 订单号
     */
    private String orderNo;

    /**
     * 支付用户ID
     */
    private String userId;


    private Integer payType; //交易类型 -1未确定 0微信 1支付宝

    private String outerTradeNo; //交易订单号(微信支付宝交易流水号)



}
