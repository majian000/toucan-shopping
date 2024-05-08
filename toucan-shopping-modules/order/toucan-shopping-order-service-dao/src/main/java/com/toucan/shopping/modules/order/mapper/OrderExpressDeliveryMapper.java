package com.toucan.shopping.modules.order.mapper;

import com.toucan.shopping.modules.order.entity.OrderExpressDelivery;
import com.toucan.shopping.modules.order.entity.OrderItem;
import com.toucan.shopping.modules.order.page.OrderItemPageInfo;
import com.toucan.shopping.modules.order.service.OrderExpressDeliveryService;
import com.toucan.shopping.modules.order.vo.OrderExpressDeliveryVO;
import com.toucan.shopping.modules.order.vo.OrderItemVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


@Mapper
public interface OrderExpressDeliveryMapper {

    int insert(OrderExpressDelivery entity);

    int update(OrderExpressDelivery entity);

    OrderExpressDelivery queryByOrderId(Long orderId);

    OrderExpressDeliveryVO queryVOByOrderId(Long orderId);

    int removeByOrderId(Long orderId);

}
