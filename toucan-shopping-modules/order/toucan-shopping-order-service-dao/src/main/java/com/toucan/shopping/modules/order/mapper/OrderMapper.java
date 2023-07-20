package com.toucan.shopping.modules.order.mapper;

import com.toucan.shopping.modules.order.entity.Order;
import com.toucan.shopping.modules.order.page.OrderPageInfo;
import com.toucan.shopping.modules.order.vo.OrderVO;
import org.apache.ibatis.annotations.*;

import java.util.List;


@Mapper
public interface OrderMapper {

    int insert(Order order);

    int insertByVoList(List<OrderVO> entitys);

    @Select("select * from t_order where order_no=#{orderNo}")
    @ResultMap("com.toucan.shopping.modules.order.mapper.OrderMapper.orderMap")
    Order findByOrderNo(String orderNo);


    @Update("update t_order set delete_status=1 where order_no=#{orderNo} ")
    int deleteByOrderNo(String orderNo);

    int finishOrder(Order order);

    Order findById(Long id);

    List<Order> queryOrderListByPayTimeout(Order order);

    int cancelOrder(Order order);

    int cancelNoPayOrderByMainOrderNo(String mainOrderNo,String userId);

    int cancelNoPayOrderByMainOrderNoAndCancelRemark(String mainOrderNo,String userId,String cancelRemark);

    int cancelOrderByOrderNo(String orderNo,String cancelRemark);


    List<OrderVO> queryOrderListByPayTimeoutPage(OrderPageInfo pageInfo);

    Long queryOrderListByPayTimeoutPageCount(OrderPageInfo pageInfo);


    List<OrderVO> queryListPage(OrderPageInfo pageInfo);

    Long queryListPageCount(OrderPageInfo pageInfo);

    List<Order> findListByMainOrderNo(String mainOrderNo);

    int cancelByMainOrderNo(String mainOrderNo,String appCode,String cancelRemark);

    List<Order> queryByMainOrderNo(String mainOrderNo,String appCode);

    List<Order> queryByOrderNo(String orderNo);

    Order queryOneByVO(OrderVO orderVO);

    OrderVO queryOneVOByVO(OrderVO orderVO);

    int updateById(OrderVO orderVO);

}
