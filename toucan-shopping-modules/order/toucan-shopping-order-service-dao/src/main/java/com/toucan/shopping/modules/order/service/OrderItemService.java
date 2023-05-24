package com.toucan.shopping.modules.order.service;

import com.toucan.shopping.modules.common.page.PageInfo;
import com.toucan.shopping.modules.order.entity.Order;
import com.toucan.shopping.modules.order.entity.OrderItem;
import com.toucan.shopping.modules.order.page.OrderItemPageInfo;
import com.toucan.shopping.modules.order.vo.OrderItemVO;
import com.toucan.shopping.modules.product.entity.ProductBuy;
import com.toucan.shopping.modules.product.entity.ProductSku;

import java.util.List;
import java.util.Map;

public interface OrderItemService {

    int create(OrderItem orderItem);

    List<OrderItemVO> findByOrderNo(String orderNo, String userId);

    List<OrderItemVO> findByOrderNo(String orderNo);

    List<OrderItemVO> findByOrderNos(List<String> orderNos);


    /**
     * 查询订单列表页
     * @param pageInfo
     * @return
     */
    PageInfo<OrderItemVO> queryOrderListPage(OrderItemPageInfo pageInfo);

}
