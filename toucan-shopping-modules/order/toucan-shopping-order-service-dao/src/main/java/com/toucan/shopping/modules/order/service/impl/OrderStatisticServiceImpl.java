package com.toucan.shopping.modules.order.service.impl;

import com.toucan.shopping.modules.order.mapper.OrderStatisticMapper;
import com.toucan.shopping.modules.order.service.OrderStatisticService;
import com.toucan.shopping.modules.order.vo.OrderStatisticVO;
import com.toucan.shopping.modules.product.vo.ProductSkuStatisticVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderStatisticServiceImpl implements OrderStatisticService {


    @Autowired
    private OrderStatisticMapper orderStatisticMapper;


    @Override
    public OrderStatisticVO queryTotalAndTodayAndCurrentMonthAndCurrentYear() {
        OrderStatisticVO orderStatisticVO=new OrderStatisticVO();
        return orderStatisticVO;
    }

}
