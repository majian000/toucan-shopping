package com.toucan.shopping.modules.order.service.impl;

import com.toucan.shopping.modules.common.page.PageInfo;
import com.toucan.shopping.modules.order.entity.OrderPay;
import com.toucan.shopping.modules.order.mapper.OrderPayMapper;
import com.toucan.shopping.modules.order.page.OrderPayPageInfo;
import com.toucan.shopping.modules.order.service.OrderPayService;
import com.toucan.shopping.modules.order.vo.OrderPayVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderPayServiceImpl implements OrderPayService {


    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private OrderPayMapper orderPayMapper;

    @Override
    public int save(OrderPay orderPay) {
        return orderPayMapper.insert(orderPay);
    }

    @Override
    public PageInfo<OrderPayVO> queryOrderPayListPage(OrderPayPageInfo pageInfo) {
        PageInfo<OrderPayVO> pageResult = new PageInfo();
        pageInfo.setStart(pageInfo.getPage()*pageInfo.getLimit()-pageInfo.getLimit());
        pageResult.setList(orderPayMapper.queryListPage(pageInfo));
        pageResult.setTotal(orderPayMapper.queryListPageCount(pageInfo));

        pageResult.setSize(pageInfo.getSize());
        pageResult.setLimit(pageInfo.getLimit());
        pageResult.setPage(pageInfo.getPage());
        return pageResult;
    }

}
