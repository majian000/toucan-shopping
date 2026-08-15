package com.toucan.shopping.modules.order.service.impl;

import com.toucan.shopping.modules.common.page.PageInfo;
import com.toucan.shopping.modules.order.entity.OrderRefund;
import com.toucan.shopping.modules.order.mapper.OrderRefundMapper;
import com.toucan.shopping.modules.order.page.OrderRefundPageInfo;
import com.toucan.shopping.modules.order.service.OrderRefundService;
import com.toucan.shopping.modules.order.vo.OrderRefundVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderRefundServiceImpl implements OrderRefundService {


    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private OrderRefundMapper orderRefundMapper;

    @Override
    public int save(OrderRefund orderRefund) {
        return orderRefundMapper.insert(orderRefund);
    }

    @Override
    public PageInfo<OrderRefundVO> queryOrderRefundListPage(OrderRefundPageInfo pageInfo) {
        PageInfo<OrderRefundVO> pageResult = new PageInfo();
        pageInfo.setStart(pageInfo.getPage()*pageInfo.getLimit()-pageInfo.getLimit());
        pageResult.setList(orderRefundMapper.queryListPage(pageInfo));
        pageResult.setTotal(orderRefundMapper.queryListPageCount(pageInfo));

        pageResult.setSize(pageInfo.getSize());
        pageResult.setLimit(pageInfo.getLimit());
        pageResult.setPage(pageInfo.getPage());
        return pageResult;
    }

}
