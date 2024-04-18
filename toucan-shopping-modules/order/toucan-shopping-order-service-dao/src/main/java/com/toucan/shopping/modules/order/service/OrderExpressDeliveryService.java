package com.toucan.shopping.modules.order.service;

import com.toucan.shopping.modules.common.page.PageInfo;
import com.toucan.shopping.modules.order.entity.OrderExpressDelivery;
import com.toucan.shopping.modules.order.entity.OrderItem;
import com.toucan.shopping.modules.order.page.OrderItemPageInfo;
import com.toucan.shopping.modules.order.vo.OrderItemVO;

import java.util.List;

/**
 * 订单快递信息
 */
public interface OrderExpressDeliveryService {


    int save(OrderExpressDelivery entity);

    int update(OrderExpressDelivery entity);

    OrderExpressDelivery queryByOrderId(Long orderId);

}
