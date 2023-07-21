package com.toucan.shopping.modules.order.service.impl;

import com.toucan.shopping.modules.common.generator.IdGenerator;
import com.toucan.shopping.modules.common.page.PageInfo;
import com.toucan.shopping.modules.order.constant.OrderConstant;
import com.toucan.shopping.modules.order.entity.Order;
import com.toucan.shopping.modules.order.entity.OrderLog;
import com.toucan.shopping.modules.order.mapper.OrderLogMapper;
import com.toucan.shopping.modules.order.mapper.OrderMapper;
import com.toucan.shopping.modules.order.page.OrderPageInfo;
import com.toucan.shopping.modules.order.service.OrderItemService;
import com.toucan.shopping.modules.order.service.OrderLogService;
import com.toucan.shopping.modules.order.service.OrderService;
import com.toucan.shopping.modules.order.vo.OrderVO;
import com.toucan.shopping.modules.skylark.lock.service.SkylarkLock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class OrderLogServiceImpl implements OrderLogService {


    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private IdGenerator idGenerator;

    @Autowired
    private SkylarkLock skylarkLock;

    @Autowired
    private OrderLogMapper orderLogMapper;

    @Override
    public int save(OrderLog orderLog) {
        return orderLogMapper.insert(orderLog);
    }

    @Override
    public int save(String operateUserId, String appCode, String orderNo, String remark, Object oldObj, Object updateObj, Integer logType) {

        OrderLog orderLog = new OrderLog();
        orderLog.setOperateUserId(operateUserId);
        orderLog.setAppCode(appCode);
        orderLog.setId(idGenerator.id());
        orderLog.setCreateDate(new Date());
        orderLog.setOrderNo(orderNo);
        orderLog.setRemark("修改订单信息");
        orderLog.loadOldData(oldObj).loadUpdateData(updateObj).setDataBodyType(logType).loadDataBody();
        return this.save(orderLog);
    }
}
