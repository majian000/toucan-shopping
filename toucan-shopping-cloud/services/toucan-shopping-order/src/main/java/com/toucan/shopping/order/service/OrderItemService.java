package com.toucan.shopping.order.service;

import com.toucan.shopping.order.entity.Order;
import com.toucan.shopping.order.entity.OrderItem;
import com.toucan.shopping.product.export.entity.ProductBuy;
import com.toucan.shopping.product.export.entity.ProductSku;

import java.util.List;
import java.util.Map;

public interface OrderItemService {

    int create(OrderItem orderItem);

    List<OrderItem> createOrderItem(List<ProductSku> productSkuList, Map<String, ProductBuy> buyMap, Order order);

    List<OrderItem> findByOrderNo(String orderNo,String userId);

    int deleteByOrderNo(String orderNo);

}
