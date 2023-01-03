package com.toucan.shopping.modules.order.mapper;

import com.toucan.shopping.modules.order.entity.Order;
import com.toucan.shopping.modules.order.page.OrderPageInfo;
import com.toucan.shopping.modules.order.vo.OrderVO;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;


@Mapper
public interface OrderMapper {

    int insert(Order order);

    int insertByVoList(List<OrderVO> entitys);

    @Select("select * from bbs_order where order_no=#{orderNo}")
    Order findByOrderNo(String orderNo);


    @Update("update bbs_order set delete_status=1 where order_no=#{orderNo} ")
    int deleteByOrderNo(String orderNo);

    int finishOrder(Order order);

    List<Order> queryOrderListByPayTimeout(Order order);

    int cancelOrder(Order order);

    int cancelNoPayOrderByMainOrderNo(String mainOrderNo,String userId);

    int cancelNoPayOrderByMainOrderNoAndCancelRemark(String mainOrderNo,String userId,String cancelRemark);


    List<OrderVO> queryOrderListByPayTimeoutPage(OrderPageInfo pageInfo);

    Long queryOrderListByPayTimeoutPageCount(OrderPageInfo pageInfo);


    List<OrderVO> queryListPage(OrderPageInfo pageInfo);

    Long queryListPageCount(OrderPageInfo pageInfo);

    List<Order> findListByMainOrderNo(String mainOrderNo);

    int cancelByMainOrderNo(String mainOrderNo,String appCode,String cancelRemark);

    List<Order> queryByMainOrderNo(String mainOrderNo,String appCode);

}
