package com.toucan.shopping.modules.order.mapper;

import com.toucan.shopping.modules.order.entity.OrderConsigneeAddress;
import com.toucan.shopping.modules.user.entity.ConsigneeAddress;
import com.toucan.shopping.modules.user.page.ConsigneeAddressPageInfo;
import com.toucan.shopping.modules.user.vo.ConsigneeAddressVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


@Mapper
public interface OrderConsigneeAddressMapper {

    int insert(OrderConsigneeAddress entity);

    int inserts(List<OrderConsigneeAddress> orderConsigneeAddresses);

}
