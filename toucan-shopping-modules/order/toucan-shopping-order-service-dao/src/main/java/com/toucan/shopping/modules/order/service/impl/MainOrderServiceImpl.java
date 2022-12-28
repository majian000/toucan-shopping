package com.toucan.shopping.modules.order.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.toucan.shopping.modules.common.generator.IdGenerator;
import com.toucan.shopping.modules.common.page.PageInfo;
import com.toucan.shopping.modules.order.entity.MainOrder;
import com.toucan.shopping.modules.order.entity.Order;
import com.toucan.shopping.modules.order.entity.OrderConsigneeAddress;
import com.toucan.shopping.modules.order.mapper.MainOrderMapper;
import com.toucan.shopping.modules.order.mapper.OrderConsigneeAddressMapper;
import com.toucan.shopping.modules.order.mapper.OrderItemMapper;
import com.toucan.shopping.modules.order.mapper.OrderMapper;
import com.toucan.shopping.modules.order.page.MainOrderPageInfo;
import com.toucan.shopping.modules.order.service.MainOrderService;
import com.toucan.shopping.modules.order.service.OrderItemService;
import com.toucan.shopping.modules.order.service.OrderService;
import com.toucan.shopping.modules.order.vo.CreateOrderVO;
import com.toucan.shopping.modules.order.vo.MainOrderVO;
import com.toucan.shopping.modules.order.vo.OrderConsigneeAddressVO;
import com.toucan.shopping.modules.order.vo.OrderVO;
import com.toucan.shopping.modules.product.entity.ProductBuy;
import com.toucan.shopping.modules.product.entity.ProductSku;
import com.toucan.shopping.modules.skylark.lock.service.SkylarkLock;
import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Service
public class MainOrderServiceImpl implements MainOrderService {


    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private SkylarkLock skylarkLock;

    @Autowired
    private MainOrderMapper mainOrderMapper;

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private OrderItemMapper orderItemMapper;

    @Autowired
    private IdGenerator idGenerator;

    @Autowired
    private OrderConsigneeAddressMapper orderConsigneeAddressMapper;

    @Override
    public int save(MainOrder mainOrder) {
        return mainOrderMapper.insert(mainOrder);
    }

    @Transactional
    @Override
    public int createOrder(CreateOrderVO createOrderVO) throws Exception {
        MainOrderVO mainOrderVO = createOrderVO.getMainOrder();
        //保存主订单
        int ret = mainOrderMapper.insert(mainOrderVO);
        if(ret<=0)
        {
            throw new IllegalArgumentException("保存主订单失败");
        }
        //保存子订单
        ret = orderMapper.insertByVoList(mainOrderVO.getOrders());
        if(ret!=mainOrderVO.getOrders().size())
        {
            throw new IllegalArgumentException("保存子订单失败");
        }
        List<OrderConsigneeAddress> orderConsigneeAddresss = new LinkedList<>();
        for(OrderVO orderVO:mainOrderVO.getOrders())
        {
            OrderConsigneeAddress orderConsigneeAddress = new OrderConsigneeAddress();
            BeanUtils.copyProperties(orderConsigneeAddress,createOrderVO.getConsigneeAddress());
            orderConsigneeAddress.setId(idGenerator.id());
            orderConsigneeAddress.setOrderId(orderVO.getId());
            orderConsigneeAddress.setOrderNo(orderVO.getOrderNo());
            orderConsigneeAddress.setMainOrderNo(mainOrderVO.getOrderNo());
            orderConsigneeAddress.setDeleteStatus((short)0);
            orderConsigneeAddress.setCreateDate(orderVO.getCreateDate());
            orderConsigneeAddress.setAppCode(orderVO.getAppCode());
            orderConsigneeAddresss.add(orderConsigneeAddress);
        }
        ret = orderConsigneeAddressMapper.inserts(orderConsigneeAddresss);
        if(ret!=orderConsigneeAddresss.size())
        {
            throw new IllegalArgumentException("保存收货信息失败");
        }

        for(OrderVO orderVO:mainOrderVO.getOrders())
        {
            ret = orderItemMapper.insertByVoList(orderVO.getOrderItems());
            if(ret!=orderVO.getOrderItems().size())
            {
                throw new IllegalArgumentException("保存子订单项失败");
            }
        }
        return 0;
    }

    @Override
    public MainOrder queryOneByVO(MainOrderVO mainOrderVO) {
        return mainOrderMapper.queryOneByVO(mainOrderVO);
    }



    @Transactional
    @Override
    public int cancelMainOrder(String orderNo,String userId) {
        return mainOrderMapper.cancelMainOrder(orderNo,userId);
    }


    @Transactional
    @Override
    public int cancelMainOrder(String orderNo,String userId,String cancelRemark) {
        return mainOrderMapper.cancelMainOrderAndSaveCancelRemark(orderNo,userId,cancelRemark);
    }

    @Override
    public PageInfo<MainOrderVO> queryMainOrderListByPayTimeoutPage(MainOrderPageInfo pageInfo) {
        PageInfo<MainOrderVO> pageResult = new PageInfo();
        pageInfo.setStart(pageInfo.getPage()*pageInfo.getLimit()-pageInfo.getLimit());
        pageResult.setList(mainOrderMapper.queryMainOrderListByPayTimeoutPage(pageInfo));
        pageResult.setTotal(mainOrderMapper.queryMainOrderListByPayTimeoutPageCount(pageInfo));
        return pageResult;
    }
}
