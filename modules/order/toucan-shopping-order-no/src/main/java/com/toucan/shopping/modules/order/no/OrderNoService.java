package com.toucan.shopping.modules.order.no;

/**
 * 订单编号生成
 */
public interface OrderNoService {

    /**
     * 生成订单编号
     * @return
     */
    String generateOrderNo();
}
