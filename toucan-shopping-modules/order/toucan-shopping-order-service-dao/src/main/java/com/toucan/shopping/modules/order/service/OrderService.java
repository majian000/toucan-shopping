package com.toucan.shopping.modules.order.service;

import com.toucan.shopping.modules.order.entity.Order;
import com.toucan.shopping.modules.product.entity.ProductBuy;
import com.toucan.shopping.modules.product.entity.ProductSku;

import java.util.List;
import java.util.Map;

public interface OrderService {

    int create(Order order);

    int deleteByOrderNo(String orderNo);

    Order createOrder(String userId, String orderNo, String appCode, Integer payMethod, List<ProductSku> productSkuList, Map<String, ProductBuy> buyMap);

    Order findByOrderNo(String orderNo);

    int finishOrder(Order order);

    int cancelOrder(Order order);

    List<Order> queryOrderListByPayTimeout(Order order);

}
