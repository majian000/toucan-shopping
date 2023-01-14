package com.toucan.shopping.modules.order.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.toucan.shopping.modules.common.page.PageInfo;
import com.toucan.shopping.modules.order.entity.Order;
import com.toucan.shopping.modules.order.entity.OrderItem;
import com.toucan.shopping.modules.order.mapper.OrderItemMapper;
import com.toucan.shopping.modules.order.page.OrderItemPageInfo;
import com.toucan.shopping.modules.order.service.OrderItemService;
import com.toucan.shopping.modules.order.vo.OrderItemVO;
import com.toucan.shopping.modules.product.entity.ProductBuy;
import com.toucan.shopping.modules.product.entity.ProductSku;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.*;

@Service
public class OrderItemServiceImpl implements OrderItemService {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private OrderItemMapper orderItemMapper;


    public List<OrderItemVO> findByOrderNo(String orderNo, String userId){
        return orderItemMapper.findByOrderNoAndUserId(orderNo,userId);
    }

    @Override
    public List<OrderItemVO> findByOrderNo(String orderNo) {
        return orderItemMapper.findByOrderNo(orderNo);
    }

    @Override
    public List<OrderItemVO> findByOrderNos(List<String> orderNos) {
        if(CollectionUtils.isEmpty(orderNos))
        {
            return new LinkedList<>();
        }
        return orderItemMapper.findByOrderNos(orderNos);
    }
    @Override
    public int create(OrderItem orderItem) {
        return orderItemMapper.insert(orderItem);
    }


    @Override
    public PageInfo<OrderItemVO> queryOrderListPage(OrderItemPageInfo pageInfo) {
        PageInfo<OrderItemVO> pageResult = new PageInfo();
        pageInfo.setStart(pageInfo.getPage()*pageInfo.getLimit()-pageInfo.getLimit());
        pageResult.setList(orderItemMapper.queryListPage(pageInfo));
        pageResult.setTotal(orderItemMapper.queryListPageCount(pageInfo));

        pageResult.setSize(pageInfo.getSize());
        pageResult.setLimit(pageInfo.getLimit());
        pageResult.setPage(pageInfo.getPage());
        return pageResult;
    }
}
