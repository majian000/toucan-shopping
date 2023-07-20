package com.toucan.shopping.modules.order.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.toucan.shopping.modules.common.page.PageInfo;
import com.toucan.shopping.modules.order.entity.MainOrder;
import com.toucan.shopping.modules.order.entity.Order;
import com.toucan.shopping.modules.order.entity.OrderLog;
import com.toucan.shopping.modules.order.mapper.OrderMapper;
import com.toucan.shopping.modules.order.page.OrderPageInfo;
import com.toucan.shopping.modules.order.service.OrderItemService;
import com.toucan.shopping.modules.order.service.OrderLogService;
import com.toucan.shopping.modules.order.service.OrderService;
import com.toucan.shopping.modules.order.vo.OrderVO;
import com.toucan.shopping.modules.product.entity.ProductBuy;
import com.toucan.shopping.modules.product.entity.ProductSku;
import com.toucan.shopping.modules.skylark.lock.service.SkylarkLock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.*;

@Service
public class OrderServiceImpl implements OrderService {


    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private SkylarkLock skylarkLock;

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private OrderItemService orderItemService;

    @Autowired
    private OrderLogService orderLogService;



    @Override
    public int create(Order order) {
        return orderMapper.insert(order);
    }

    @Transactional
    @Override
    public int deleteByOrderNo(String orderNo) {
        return orderMapper.deleteByOrderNo(orderNo);
    }



    @Override
    public Order findByOrderNo(String orderNo) {
        return orderMapper.findByOrderNo(orderNo);
    }

    @Override
    public List<Order> findListByMainOrderNo(String mainOrderNo) {
        return orderMapper.findListByMainOrderNo(mainOrderNo);
    }


    @Override
    public List<Order> queryOrderListByPayTimeout(Order order) {
        return orderMapper.queryOrderListByPayTimeout(order);
    }

    @Override
    public Order findById(Long id) {
        return orderMapper.findById(id);
    }

    @Override
    public PageInfo<OrderVO> queryOrderListByPayTimeoutPage(OrderPageInfo pageInfo) {
        PageInfo<OrderVO> pageResult = new PageInfo();
        pageInfo.setStart(pageInfo.getPage()*pageInfo.getLimit()-pageInfo.getLimit());
        pageResult.setList(orderMapper.queryOrderListByPayTimeoutPage(pageInfo));
        pageResult.setTotal(orderMapper.queryOrderListByPayTimeoutPageCount(pageInfo));
        return pageResult;
    }

    @Override
    public PageInfo<OrderVO> queryOrderListPage(OrderPageInfo pageInfo) {
        PageInfo<OrderVO> pageResult = new PageInfo();
        pageInfo.setStart(pageInfo.getPage()*pageInfo.getLimit()-pageInfo.getLimit());
        pageResult.setList(orderMapper.queryListPage(pageInfo));
        pageResult.setTotal(orderMapper.queryListPageCount(pageInfo));

        pageResult.setSize(pageInfo.getSize());
        pageResult.setLimit(pageInfo.getLimit());
        pageResult.setPage(pageInfo.getPage());
        return pageResult;
    }

    @Override
    public int saveByVos(List<OrderVO> orders) {
        return orderMapper.insertByVoList(orders);
    }

    @Transactional
    @Override
    public int cancelNoPayOrderByMainOrderNo(String mainOrderNo, String userId) {
        return orderMapper.cancelNoPayOrderByMainOrderNo(mainOrderNo,userId);
    }

    @Transactional
    @Override
    public int cancelNoPayOrderByMainOrderNo(String mainOrderNo, String userId, String cancelRemark) {
        return orderMapper.cancelNoPayOrderByMainOrderNoAndCancelRemark(mainOrderNo,userId,cancelRemark);
    }

    @Transactional
    @Override
    public int cancelByMainOrderNo(String mainOrderNo,String appCode,String cancelRemark) {
        //锁住这些记录
        List<Order> orders = orderMapper.queryByMainOrderNo(mainOrderNo,appCode);
        return orderMapper.cancelByMainOrderNo(mainOrderNo,appCode,cancelRemark);
    }

    @Override
    public int cancelOrderByOrderNo(String orderNo, String cancelRemark) {
        //锁住这些记录
        List<Order> orders = orderMapper.queryByOrderNo(orderNo);
        return orderMapper.cancelOrderByOrderNo(orderNo,cancelRemark);
    }

    @Override
    public Order queryOneByVO(OrderVO orderVO) {
        return orderMapper.queryOneByVO(orderVO);
    }

    @Override
    public OrderVO queryOneVOByVO(OrderVO orderVO) {
        return orderMapper.queryOneVOByVO(orderVO);
    }

    @Override
    public void update(OrderVO orderVO) throws Exception {

    }
}
