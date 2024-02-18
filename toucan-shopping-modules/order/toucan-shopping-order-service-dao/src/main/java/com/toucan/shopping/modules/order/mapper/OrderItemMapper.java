package com.toucan.shopping.modules.order.mapper;

import com.toucan.shopping.modules.order.entity.Order;
import com.toucan.shopping.modules.order.entity.OrderItem;
import com.toucan.shopping.modules.order.page.OrderItemPageInfo;
import com.toucan.shopping.modules.order.vo.OrderItemVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;


@Mapper
public interface OrderItemMapper {

    int insert(OrderItem orderItem);

    int insertByVoList(List<OrderItemVO> entitys);

    List<OrderItemVO> findByOrderNoAndUserId(String orderNo,String userId);

    List<OrderItemVO> findByOrderNo(String orderNo);

    List<OrderItemVO> findByOrderId(Long orderId);

    List<OrderItemVO> findByOrderNos(List<String> orderNos);

    int updatesFromOrderList(OrderItemVO orderItemVO);


    List<OrderItemVO> queryListPage(OrderItemPageInfo pageInfo);

    Long queryListPageCount(OrderItemPageInfo pageInfo);

    OrderItemVO findById(Long id);

}
