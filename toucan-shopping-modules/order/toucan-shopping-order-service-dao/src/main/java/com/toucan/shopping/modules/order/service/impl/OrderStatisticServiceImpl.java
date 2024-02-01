package com.toucan.shopping.modules.order.service.impl;

import com.toucan.shopping.modules.common.util.DateUtils;
import com.toucan.shopping.modules.order.mapper.OrderStatisticMapper;
import com.toucan.shopping.modules.order.service.OrderStatisticService;
import com.toucan.shopping.modules.order.vo.OrderStatisticVO;
import com.toucan.shopping.modules.product.vo.ProductSkuStatisticVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class OrderStatisticServiceImpl implements OrderStatisticService {


    @Autowired
    private OrderStatisticMapper orderStatisticMapper;


    @Override
    public OrderStatisticVO queryTotalAndTodayAndCurrentMonthAndCurrentYear() {
        OrderStatisticVO orderStatisticVO=new OrderStatisticVO();
        orderStatisticVO.setTotalMoney(orderStatisticMapper.queryTotalMoney());
        Date currentDate = new Date();
        String currentDateStr = DateUtils.FORMATTER_DD.get().format(currentDate);
        String currentMonthStr = DateUtils.FORMATTER_MON.get().format(currentDate);
        orderStatisticVO.setTodayMoney(orderStatisticMapper.queryTodayMoney(currentDateStr));
        orderStatisticVO.setCurMonthMoney(orderStatisticMapper.queryCurMonthMoney(currentMonthStr,currentDateStr));
        orderStatisticVO.setCurYearMoney(orderStatisticMapper.queryCurYearMoney());
        return orderStatisticVO;
    }

}
