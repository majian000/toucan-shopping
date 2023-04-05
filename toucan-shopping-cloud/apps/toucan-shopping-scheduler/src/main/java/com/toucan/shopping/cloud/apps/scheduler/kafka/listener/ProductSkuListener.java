package com.toucan.shopping.cloud.apps.scheduler.kafka.listener;

import com.toucan.shopping.modules.search.service.ProductSearchService;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;


/**
 * 同步修改的商品到ES中
 */
@Component
public class ProductSkuListener {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private ProductSearchService productSearchService;

    @KafkaListener(topics = {"sku_topics"}, groupId = "sku_group")
    public void listenSkuTopics(ConsumerRecord<String, String> record) {
        logger.info("收到商品同步消息: {} " , record);
        String messageJsonString = record.value();
    }

}
