package com.toucan.shopping.modules.order.mapper;

import com.toucan.shopping.modules.order.entity.Order;
import com.toucan.shopping.modules.order.entity.OrderLog;
import com.toucan.shopping.modules.order.page.OrderLogPageInfo;
import com.toucan.shopping.modules.order.page.OrderPageInfo;
import com.toucan.shopping.modules.order.vo.OrderLogVO;
import com.toucan.shopping.modules.order.vo.OrderVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;


@Mapper
public interface OrderLogMapper {


    int insert(OrderLog entity);

    int inserts(List<OrderLog> entitys);

    List<OrderLogVO> queryListPage(OrderLogPageInfo pageInfo);

    Long queryListPageCount(OrderLogPageInfo pageInfo);

}
