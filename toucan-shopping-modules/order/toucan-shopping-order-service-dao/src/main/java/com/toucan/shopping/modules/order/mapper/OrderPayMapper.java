package com.toucan.shopping.modules.order.mapper;

import com.toucan.shopping.modules.order.entity.OrderPay;
import com.toucan.shopping.modules.order.page.OrderPayPageInfo;
import com.toucan.shopping.modules.order.vo.OrderPayVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


@Mapper
public interface OrderPayMapper {


    int insert(OrderPay entity);

    List<OrderPayVO> queryListPage(OrderPayPageInfo pageInfo);

    Long queryListPageCount(OrderPayPageInfo pageInfo);

}
