package com.toucan.shopping.web.service;

import com.toucan.shopping.order.export.entity.Order;

public interface PayService {

    /**
     * 调用第三方支付 修改订单支付状态
     * @param order
     * @return
     */
    boolean orderPay(Order order);

}
