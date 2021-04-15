package com.toucan.shopping.modules.order.service;

import com.toucan.shopping.modules.order.entity.Order;
import com.toucan.shopping.modules.order.entity.OrderItem;
import com.toucan.shopping.modules.product.entity.ProductBuy;
import com.toucan.shopping.modules.product.entity.ProductSku;

import java.util.List;
import java.util.Map;

public interface OrderItemService {

    int create(OrderItem orderItem);

    List<OrderItem> createOrderItem(List<ProductSku> productSkuList, Map<String, ProductBuy> buyMap, Order order);

    List<OrderItem> findByOrderNo(String orderNo,String userId);

    int deleteByOrderNo(String orderNo);

}
