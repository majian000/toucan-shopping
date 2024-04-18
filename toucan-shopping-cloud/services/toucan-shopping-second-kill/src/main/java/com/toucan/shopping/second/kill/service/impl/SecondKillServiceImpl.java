package com.toucan.shopping.second.kill.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.toucan.shopping.modules.common.persistence.event.entity.EventPublish;
import com.toucan.shopping.modules.common.persistence.event.service.EventPublishService;
import com.toucan.shopping.modules.order.kafka.constant.OrderMessageTopicConstant;
import com.toucan.shopping.modules.order.message.CreateOrderMessage;
import com.toucan.shopping.modules.product.entity.ProductSku;
import com.toucan.shopping.modules.product.message.InventoryReductionMessage;
import com.toucan.shopping.modules.stock.kafka.constant.StockMessageTopicConstant;
import com.toucan.shopping.second.kill.service.SecondKillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class SecondKillServiceImpl implements SecondKillService {


    @Autowired
    private StringRedisTemplate redisTemplate;


    @Autowired
    private EventPublishService eventPublishService;

    @Autowired
    private KafkaTemplate kafkaTemplate;


    @Transactional
    @Override
    public EventPublish saveInventoryReductionMessagePublishEvent(InventoryReductionMessage inventoryReductionMessage, String orderNo, String globalTransactionId, String skuId, List<ProductSku> productSkuList) {

        //保存发消息记录到数据库
        EventPublish inventorReductionMessagePersistence = new EventPublish();
        inventorReductionMessagePersistence.setCreateDate(new Date());
        inventorReductionMessagePersistence.setBusinessId(orderNo);
        inventorReductionMessagePersistence.setRemark("扣库存");
        inventorReductionMessagePersistence.setTransactionId(globalTransactionId);
        inventorReductionMessagePersistence.setPayload(JSONObject.toJSONString(inventoryReductionMessage));
        inventorReductionMessagePersistence.setStatus((short)0); //待发送
        inventorReductionMessagePersistence.setType(StockMessageTopicConstant.sk_inventory_reduction.name());
        eventPublishService.insert(inventorReductionMessagePersistence);

        return inventorReductionMessagePersistence;
    }

    @Transactional
    @Override
    public EventPublish saveCreateOrderMessagePublishEvent(CreateOrderMessage createOrderMessage, String orderNo, String globalTransactionId) {
        //保存发布事件记录到数据库
        EventPublish orderMessagePersistence = new EventPublish();
        orderMessagePersistence.setCreateDate(new Date());
        orderMessagePersistence.setBusinessId(orderNo);
        orderMessagePersistence.setRemark("创建订单");
        orderMessagePersistence.setTransactionId(globalTransactionId);
        orderMessagePersistence.setPayload(JSONObject.toJSONString(createOrderMessage));
        orderMessagePersistence.setStatus((short)0); //待发送
        orderMessagePersistence.setType(OrderMessageTopicConstant.sk_create_order.name());
        eventPublishService.insert(orderMessagePersistence);

        return orderMessagePersistence;
    }

}
