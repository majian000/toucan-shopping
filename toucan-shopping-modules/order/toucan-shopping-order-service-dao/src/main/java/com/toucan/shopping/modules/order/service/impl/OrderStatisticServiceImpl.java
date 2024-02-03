package com.toucan.shopping.modules.order.service.impl;

import com.toucan.shopping.modules.common.page.PageInfo;
import com.toucan.shopping.modules.common.util.DateUtils;
import com.toucan.shopping.modules.order.mapper.OrderStatisticMapper;
import com.toucan.shopping.modules.order.page.OrderHotSellPageInfo;
import com.toucan.shopping.modules.order.service.OrderStatisticService;
import com.toucan.shopping.modules.order.vo.OrderHotSellStatisticVO;
import com.toucan.shopping.modules.order.vo.OrderStatisticVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Service
public class OrderStatisticServiceImpl implements OrderStatisticService {


    @Autowired
    private OrderStatisticMapper orderStatisticMapper;


    @Override
    public OrderStatisticVO queryTotalAndTodayAndCurrentMonthAndCurrentYear() {
        OrderStatisticVO orderStatisticVO=new OrderStatisticVO();
        BigDecimal totalMoney = orderStatisticMapper.queryTotalMoney();
        if(totalMoney!=null) {
            orderStatisticVO.setTotalMoney(totalMoney);
        }else{
            orderStatisticVO.setTotalMoney(new BigDecimal(0));
        }

        BigDecimal todayMoney = orderStatisticMapper.queryTodayMoney();
        if(todayMoney!=null) {
            orderStatisticVO.setTodayMoney(todayMoney);
        }else{
            orderStatisticVO.setTodayMoney(new BigDecimal(0));
        }

        BigDecimal curMonthMoney = orderStatisticMapper.queryCurMonthMoney();
        if(curMonthMoney!=null) {
            orderStatisticVO.setCurMonthMoney(curMonthMoney);
        }else{
            orderStatisticVO.setCurMonthMoney(new BigDecimal(0));
        }

        BigDecimal curYearMoney = orderStatisticMapper.queryCurYearMoney();
        if(curYearMoney!=null) {
            orderStatisticVO.setCurYearMoney(curYearMoney);
        }else{
            orderStatisticVO.setCurYearMoney(new BigDecimal(0));
        }
        return orderStatisticVO;
    }



    @Override
    public PageInfo<OrderHotSellStatisticVO> queryHotSellListPage(OrderHotSellPageInfo queryPageInfo) {
        queryPageInfo.setStart(queryPageInfo.getPage()*queryPageInfo.getLimit()-queryPageInfo.getLimit());
        PageInfo<OrderHotSellStatisticVO> pageInfo = new PageInfo();
        pageInfo.setList(orderStatisticMapper.queryHotSellListPage(queryPageInfo));
        pageInfo.setTotal(orderStatisticMapper.queryHotSellListPageCount(queryPageInfo));
        return pageInfo;
    }

}
