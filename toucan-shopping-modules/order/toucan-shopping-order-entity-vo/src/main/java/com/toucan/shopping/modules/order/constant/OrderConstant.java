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


    //====================交易状态=========================
    public static final int TRADE_STATUS_NON_PAYMENT=0; // 未付款
    public static final int TRADE_STATUS_CALCEL=2; //已取消
    public static final int TRADE_STATUS_FINISH=3; //已完成
    public static final int TRADE_STATUS_WAIT_DELIVERY=4; //待发货
    public static final int TRADE_STATUS_WAIT_RECEIVER=1; //待收货
    //=============================================

    /**
     * 订单日志类型
     */
    public static final int ORDER_LOG_TYPE_CREATE_ORDER=0; //创建订单
    public static final int ORDER_LOG_TYPE_ORDER=1; //修改订单主表类型
    public static final int ORDER_LOG_TYPE_ORDER_CONSIGNEE_ADDRESS=2; //修改订单收货人类型
    public static final int ORDER_LOG_TYPE_ORDER_ITEMS=3; //修改订单项类型
    public static final int ORDER_LOG_TYPE_EXPRESS_DELIVERY=4; //修改订单快递信息
    public static final int ORDER_LOG_TYPE_UPDATE_ORDER_TRADE_STATUS=5; //修改订单交易状态
    public static final int ORDER_LOG_TYPE_DELETE_EXPRESS_DELIVERY=6; //删除订单快递信息


}
