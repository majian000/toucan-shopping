package com.toucan.shopping.modules.order.mapper;

import com.toucan.shopping.modules.order.entity.Order;
import com.toucan.shopping.modules.order.entity.OrderItem;
import com.toucan.shopping.modules.order.vo.OrderItemVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;


@Mapper
public interface OrderItemMapper {

    int insert(OrderItem orderItem);

    int insertByVoList(List<OrderItemVO> entitys);

    @Select("select * from bbs_order_item where order_no=#{orderNo} and user_id=#{userId} and delete_status =0 ")
    public List<OrderItem> findByOrderNo(String orderNo,String userId);

    @Update("update bbs_order_item set delete_status=1 where order_no=#{orderNo} ")
    int deleteByOrderNo(String orderNo);

    int finishOrder(Order order);

}
