package com.toucan.shopping.modules.order.constant;

/**
 * 订单常量
 * @author majian
 */
public class OrderConstant {

    public static final long MAX_PAY_TIME = 60*30*1000; //30分钟超时

    /**
     * 支付状态
     */
    public static final int PAY_STATUS_NON_PAYMENT=0; // 未支付


    /**
     * 交易状态
     */
    public static final int TRADE_STATUS_NON_PAYMENT=0; // 未付款


    public static final int ORDER_LOG_TYPE_ORDER=1; //订单日志类型

    public static final int ORDER_LOG_TYPE_ORDER_CONSIGNEE_ADDRESS=1; //订单收货人日志类型


}
