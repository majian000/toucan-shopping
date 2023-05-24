package com.toucan.shopping.cloud.order.kafka.listener.sk;

import com.alibaba.fastjson.JSONObject;
import com.toucan.shopping.modules.common.persistence.event.entity.EventProcess;
import com.toucan.shopping.modules.common.persistence.event.service.EventProcessService;
import com.toucan.shopping.cloud.order.kafka.constant.OrderMessageTopicConstant;
import com.toucan.shopping.cloud.order.message.CreateOrderMessage;
import com.toucan.shopping.modules.order.service.OrderItemService;
import com.toucan.shopping.modules.order.service.OrderService;
import com.toucan.shopping.modules.product.entity.ProductBuy;
import com.toucan.shopping.modules.product.entity.ProductSku;
import org.apache.commons.lang3.StringUtils;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * 创建订单
 */
@Component
public class CreateOrderListener {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderItemService orderItemService;


    @Autowired
    private EventProcessService eventProcessService;





    @KafkaListener(topics = {"sk_create_order"}, groupId = "sk_message_group")
    public void reciveMessage(ConsumerRecord<String, String> record) {
        System.out.println("收到创建订单消息:" + record);
        String messageJsonString = record.value();
        if (StringUtils.isEmpty(messageJsonString)) {
            throw new IllegalArgumentException("消息无效");
        }
        CreateOrderMessage kafkaMessage = JSONObject.parseObject(messageJsonString,CreateOrderMessage.class);
        String skuId =kafkaMessage.getSkuId();
        String appCode = kafkaMessage.getAppCode();
        String userId =kafkaMessage.getUserId();
        String orderNo = kafkaMessage.getOrderNo();

        try{
            //保存待处理消息
            EventProcess eventProcess = new EventProcess();
            eventProcess.setCreateDate(new Date());
            eventProcess.setBusinessId(kafkaMessage.getOrderNo());
            eventProcess.setRemark("创建订单");
            eventProcess.setTableName("sk_order");
            eventProcess.setTransactionId(kafkaMessage.getGlobalTransactionId());
            eventProcess.setPayload(messageJsonString);
            eventProcess.setStatus((short)0); //待处理
            eventProcess.setType(OrderMessageTopicConstant.sk_create_order.name());
            eventProcessService.insert(eventProcess);

            //创建订单
            //拿到所有商品
            List<ProductSku> productSkus = null;
            if(kafkaMessage.getProductSkuListJson()!=null)
            {
                productSkus = JSONObject.parseArray(kafkaMessage.getProductSkuListJson(),ProductSku.class);
            }

            //拿到购买信息
            Map<String,Object> buyMapJson = null;
            Map<String, ProductBuy> buyMap = new HashMap<String,ProductBuy>();
            if(kafkaMessage.getBuyMap()!=null)
            {
                buyMapJson = JSONObject.parseObject(kafkaMessage.getBuyMap());
                Iterator<String> keys = buyMapJson.keySet().iterator();
                while(keys.hasNext())
                {
                    String key = keys.next();
                    buyMap.put(key,JSONObject.parseObject(String.valueOf(buyMapJson.get(key)),ProductBuy.class));
                }
            }

//            Order order = orderService.createOrder(userId,orderNo,appCode,1,productSkus,buyMap);
//            if(order.getId()==null)
//            {
//                throw new IllegalArgumentException("订单创建失败");
//            }
//            logger.info("保存订单 提交本地事务{}",JSONObject.toJSONString(order));
//            List<OrderItem> orderItems = orderItemService.createOrderItem(productSkus,buyMap,order);
//            if(CollectionUtils.isEmpty(orderItems))
//            {
//                throw new IllegalArgumentException("子订单创建失败");
//            }
//            logger.info("保存子订单 提交本地事务{}", JSONArray.toJSONString(orderItems));

            //修改为已处理
//            eventProcess.setStatus((short)1); //已处理
//            eventProcessService.updateStatus(eventProcess);


        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);
        }



    }


}
