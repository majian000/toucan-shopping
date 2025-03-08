package com.toucan.shopping.modules.order.service.impl;

import com.toucan.shopping.modules.common.generator.IdGenerator;
import com.toucan.shopping.modules.common.page.PageInfo;
import com.toucan.shopping.modules.common.util.GlobalUUID;
import com.toucan.shopping.modules.order.constant.OrderConstant;
import com.toucan.shopping.modules.order.entity.Order;
import com.toucan.shopping.modules.order.entity.OrderLog;
import com.toucan.shopping.modules.order.mapper.OrderLogMapper;
import com.toucan.shopping.modules.order.mapper.OrderMapper;
import com.toucan.shopping.modules.order.page.OrderLogPageInfo;
import com.toucan.shopping.modules.order.page.OrderPageInfo;
import com.toucan.shopping.modules.order.service.OrderItemService;
import com.toucan.shopping.modules.order.service.OrderLogService;
import com.toucan.shopping.modules.order.service.OrderService;
import com.toucan.shopping.modules.order.vo.OrderLogVO;
import com.toucan.shopping.modules.order.vo.OrderVO;
import com.toucan.shopping.modules.skylark.lock.service.SkylarkLock;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.LinkedList;
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
    public int save(String batchId,String operateUserId, String appCode, String orderNo, String remark, Object oldObj, Object updateObj, Integer logType) {

        OrderLog orderLog = new OrderLog();
        orderLog.setBatchId(batchId);
        orderLog.setOperateUserId(operateUserId);
        orderLog.setAppCode(appCode);
        orderLog.setId(idGenerator.id());
        orderLog.setCreateDate(new Date());
        orderLog.setShardingDate(orderLog.getCreateDate());
        orderLog.setType(logType);
        orderLog.setOrderNo(orderNo);
        orderLog.setRemark(remark);
        orderLog.loadOldData(oldObj).loadUpdateData(updateObj).setDataBodyType(logType).loadDataBody();
        return this.save(orderLog);
    }

    @Override
    public int save(String operateUserId, String appCode, String orderNo, String remark, Object oldObj, Object updateObj, Integer logType) {

        OrderLog orderLog = new OrderLog();
        orderLog.setBatchId(GlobalUUID.uuid());
        orderLog.setOperateUserId(operateUserId);
        orderLog.setAppCode(appCode);
        orderLog.setId(idGenerator.id());
        orderLog.setCreateDate(new Date());
        orderLog.setType(logType);
        orderLog.setShardingDate(orderLog.getCreateDate());
        orderLog.setOrderNo(orderNo);
        orderLog.setRemark(remark);
        orderLog.loadOldData(oldObj).loadUpdateData(updateObj).setDataBodyType(logType).loadDataBody();
        return this.save(orderLog);
    }

    @Override
    public int saves(String operateUserId,List<OrderVO> orderVOS,String appCode,String remark,Integer logType,Date shardingDate) {
        List<OrderLog> orderLogs = new LinkedList<>();
        if(CollectionUtils.isNotEmpty(orderVOS)){
            String batchId = GlobalUUID.uuid();
            Date createDate = new Date();
            for(OrderVO orderVO:orderVOS) {
                OrderLog orderLog = new OrderLog();
                orderLog.setBatchId(batchId);
                orderLog.setOperateUserId(operateUserId);
                orderLog.setType(logType);
                orderLog.setAppCode(appCode);
                orderLog.setId(idGenerator.id());
                orderLog.setCreateDate(createDate);
                orderLog.setShardingDate(shardingDate);
                orderLog.setOrderNo(orderVO.getOrderNo());
                orderLog.setRemark(remark);
                orderLog.loadOldData(null).loadUpdateData(null).setDataBodyType(logType).loadDataBody();
                orderLogs.add(orderLog);
            }
        }
        return orderLogMapper.inserts(orderLogs);
    }

    @Override
    public PageInfo<OrderLogVO> queryOrderListPage(OrderLogPageInfo pageInfo) {
        PageInfo<OrderLogVO> pageResult = new PageInfo();
        pageInfo.setStart(pageInfo.getPage()*pageInfo.getLimit()-pageInfo.getLimit());
        pageResult.setList(orderLogMapper.queryListPage(pageInfo));
        pageResult.setTotal(orderLogMapper.queryListPageCount(pageInfo));

        pageResult.setSize(pageInfo.getSize());
        pageResult.setLimit(pageInfo.getLimit());
        pageResult.setPage(pageInfo.getPage());
        return pageResult;
    }

}
