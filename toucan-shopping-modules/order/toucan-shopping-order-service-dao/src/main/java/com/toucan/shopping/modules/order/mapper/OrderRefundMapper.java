package com.toucan.shopping.modules.order.mapper;

import com.toucan.shopping.modules.order.entity.OrderRefund;
import com.toucan.shopping.modules.order.page.OrderRefundPageInfo;
import com.toucan.shopping.modules.order.vo.OrderRefundVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


@Mapper
public interface OrderRefundMapper {


    int insert(OrderRefund entity);

    List<OrderRefundVO> queryListPage(OrderRefundPageInfo pageInfo);

    Long queryListPageCount(OrderRefundPageInfo pageInfo);

}
