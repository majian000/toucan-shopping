package com.toucan.shopping.cloud.apps.web.service;


import com.toucan.shopping.modules.order.entity.Order;

public interface PayService {

    /**
     * 调用第三方支付 修改订单支付状态
     * @param order
     * @return
     */
    boolean orderPay(Order order);

}
