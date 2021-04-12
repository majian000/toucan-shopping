package com.toucan.shopping.cloud.order.kafka.scheduler;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.toucan.shopping.modules.common.persistence.event.entity.EventProcess;
import com.toucan.shopping.modules.common.persistence.event.service.EventProcessService;
import com.toucan.shopping.modules.order.entity.Order;
import com.toucan.shopping.modules.order.entity.OrderItem;
import com.toucan.shopping.modules.order.kafka.constant.OrderMessageTopicConstant;
import com.toucan.shopping.modules.order.message.CreateOrderMessage;
import com.toucan.shopping.modules.order.service.OrderItemService;
import com.toucan.shopping.modules.order.service.OrderService;
import com.toucan.shopping.modules.product.entity.ProductBuy;
import com.toucan.shopping.modules.product.entity.ProductSku;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


/**
 * 处理所有执行本地事务失败的消息
 */
@Component
@EnableScheduling
public class MessageProcessScheduler {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private EventProcessService eventProcessService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderItemService orderItemService;




    /**
     * 每1分钟重新扫描一次本地看有没有执行失败的事务
     */
    @Scheduled(cron = "0 0/1 * * * ? ")
    public void resend()
    {
        logger.info("处理执行失败的事件开始=====================");
        EventProcess query = new EventProcess();
        query.setStatus((short)0);
        List<EventProcess> eventProcesses =  eventProcessService.queryList(query);
        if(!CollectionUtils.isEmpty(eventProcesses))
        {
            for(EventProcess eventProcess : eventProcesses)
            {
                logger.info("本地事务重新执行 "+ eventProcess.getType()+" 内容:"+ eventProcess.getPayload());
                //扣库存
                if(eventProcess.getType().equals(OrderMessageTopicConstant.sk_create_order.name())) {
                    try {
                        CreateOrderMessage createOrderMessage = JSONObject.parseObject(eventProcess.getPayload(), CreateOrderMessage.class);
                        //创建订单
                        String skuId =createOrderMessage.getSkuId();
                        String appCode = createOrderMessage.getAppCode();
                        String userId =createOrderMessage.getUserId();
                        String orderNo = createOrderMessage.getOrderNo();
                        //拿到所有商品
                        List<ProductSku> productSkus = null;
                        if(createOrderMessage.getProductSkuListJson()!=null)
                        {
                            productSkus = JSONObject.parseArray(createOrderMessage.getProductSkuListJson(),ProductSku.class);
                        }

                        //拿到购买信息
                        Map<String,Object> buyMapJson = null;
                        Map<String, ProductBuy> buyMap = new HashMap<String,ProductBuy>();
                        if(createOrderMessage.getBuyMap()!=null)
                        {
                            buyMapJson = JSONObject.parseObject(createOrderMessage.getBuyMap());
                            Iterator<String> keys = buyMapJson.keySet().iterator();
                            while(keys.hasNext())
                            {
                                String key = keys.next();
                                buyMap.put(key,JSONObject.parseObject(String.valueOf(buyMapJson.get(key)),ProductBuy.class));
                            }
                        }

                        Order order = orderService.createOrder(userId,orderNo,appCode,1,productSkus,buyMap);
                        logger.info("保存订单 提交本地事务{}", JSONObject.toJSON(order));
                        List<OrderItem> orderItems = orderItemService.createOrderItem(productSkus,buyMap, order);
                        logger.info("保存子订单 提交本地事务{}", JSONArray.toJSONString(orderItems));

                        eventProcess.setStatus((short)1); //已处理
                        eventProcessService.updateStatus(eventProcess);
                    }catch(Exception e)
                    {
                        logger.warn(e.getMessage(),e);
                    }
                }
            }

        }
        logger.info("处理执行失败的事件结束=====================");
    }

}
