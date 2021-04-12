package com.toucan.shopping.order.mapper;

import com.toucan.shopping.order.entity.Order;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;


@Mapper
public interface OrderMapper {

    int insert(Order order);


    @Select("select * from bbs_order where order_no=#{orderNo}")
    Order findByOrderNo(String orderNo);


    @Update("update bbs_order set delete_status=1 where order_no=#{orderNo} ")
    int deleteByOrderNo(String orderNo);

    int finishOrder(Order order);

    List<Order> queryOrderListByPayTimeout(Order order);

    int cancelOrder(Order order);

}
