package com.toucan.shopping.modules.order.service.impl;

import com.toucan.shopping.modules.common.page.PageInfo;
import com.toucan.shopping.modules.order.entity.OrderExpressDelivery;
import com.toucan.shopping.modules.order.entity.OrderItem;
import com.toucan.shopping.modules.order.mapper.OrderExpressDeliveryMapper;
import com.toucan.shopping.modules.order.mapper.OrderItemMapper;
import com.toucan.shopping.modules.order.page.OrderItemPageInfo;
import com.toucan.shopping.modules.order.service.OrderExpressDeliveryService;
import com.toucan.shopping.modules.order.service.OrderItemService;
import com.toucan.shopping.modules.order.vo.OrderItemVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.LinkedList;
import java.util.List;

@Service
public class OrderExpressDeliveryServiceImpl implements OrderExpressDeliveryService {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private OrderExpressDeliveryMapper orderExpressDeliveryMapper;


    @Override
    public int save(OrderExpressDelivery entity) {
        return orderExpressDeliveryMapper.insert(entity);
    }

    @Override
    public int update(OrderExpressDelivery entity) {
        return orderExpressDeliveryMapper.update(entity);
    }

    @Override
    public OrderExpressDelivery queryByOrderId(Long orderId) {
        return orderExpressDeliveryMapper.queryByOrderId(orderId);
    }
}
