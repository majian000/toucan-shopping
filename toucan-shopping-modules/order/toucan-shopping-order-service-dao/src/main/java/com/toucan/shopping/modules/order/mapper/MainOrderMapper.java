package com.toucan.shopping.modules.order.mapper;

import com.toucan.shopping.modules.order.entity.MainOrder;
import com.toucan.shopping.modules.order.vo.MainOrderVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;


@Mapper
public interface MainOrderMapper {

    int insert(MainOrder order);


    @Select("select * from bbs_order where order_no=#{orderNo}")
    MainOrder findByMainOrderNo(String orderNo);


    @Update("update bbs_order set delete_status=1 where order_no=#{orderNo} ")
    int deleteByMainOrderNo(String orderNo);

    int finishMainOrder(MainOrder order);

    List<MainOrder> queryMainOrderListByPayTimeout(MainOrder order);

    int cancelMainOrder(MainOrder order);

    MainOrder queryOneByVO(MainOrderVO mainOrderVO);

}
