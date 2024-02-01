package com.toucan.shopping.modules.order.service.impl;

import com.toucan.shopping.modules.common.page.PageInfo;
import com.toucan.shopping.modules.order.entity.OrderConsigneeAddress;
import com.toucan.shopping.modules.order.mapper.OrderConsigneeAddressMapper;
import com.toucan.shopping.modules.order.service.OrderConsigneeAddressService;
import com.toucan.shopping.modules.order.vo.OrderConsigneeAddressVO;
import com.toucan.shopping.modules.user.entity.ConsigneeAddress;
import com.toucan.shopping.modules.user.page.ConsigneeAddressPageInfo;
import com.toucan.shopping.modules.user.vo.ConsigneeAddressVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 订单收货地址服务
 * @author majian
 * @date 2022-12-28 11:09:16
 */
@Service
public class OrderConsigneeAddressServiceImpl implements OrderConsigneeAddressService {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private OrderConsigneeAddressMapper orderConsigneeAddressMapper;


    @Override
    public int save(OrderConsigneeAddress entity) {
        entity.setShardingDate(entity.getCreateDate());
        return orderConsigneeAddressMapper.insert(entity);
    }

    @Override
    public List<OrderConsigneeAddressVO> queryListByOrderNos(List<String> orderNos) {
        return orderConsigneeAddressMapper.findByOrderNos(orderNos);
    }

    @Override
    public OrderConsigneeAddressVO queryOneByOrderNo(String orderNo) {
        return orderConsigneeAddressMapper.queryOneByOrderNo(orderNo);
    }

    @Override
    public int updateByOrderNo(OrderConsigneeAddressVO orderConsigneeAddressVO) {
        return orderConsigneeAddressMapper.updateByOrderNo(orderConsigneeAddressVO);
    }

}
