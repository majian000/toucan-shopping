package com.toucan.shopping.standard.apps.web.kafka.callback;

import com.toucan.shopping.modules.common.persistence.event.service.EventPublishService;
import com.toucan.shopping.modules.stock.kafka.constant.StockMessageTopicConstant;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.support.ProducerListener;
import org.springframework.stereotype.Component;

@Component
public class SendCallback implements ProducerListener<String, Object> {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private EventPublishService eventPublishService;

    @Override
    public void onSuccess(String topic, Integer partition, String key, Object value, RecordMetadata recordMetadata) {
        logger.info(" send kafka message success topic:"+topic+" msgContent:"+String.valueOf(value));
    }

    @Override
    public void onError(String topic, Integer partition, String key, Object value, Exception exception) {
        logger.warn(" send kafka message error topic:"+topic+" msgContent:"+String.valueOf(value));

        //扣库存失败,记录失败消息
        if(topic.equals(StockMessageTopicConstant.sk_inventory_reduction.name()))
        {
            logger.warn("resend kafka message  topic:"+topic+" msgContent:"+String.valueOf(value));
        }
    }

    @Override
    public boolean isInterestedInSuccess() {
        return true;
    }
}
