package com.toucan.shopping.modules.user.kafka.callback;

import org.apache.kafka.clients.producer.RecordMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.support.ProducerListener;
import org.springframework.stereotype.Component;

@Component
public class SendCallback implements ProducerListener<String, Object> {

    private final Logger logger = LoggerFactory.getLogger(getClass());


    @Override
    public void onSuccess(String topic, Integer partition, String key, Object value, RecordMetadata recordMetadata) {
        logger.info(" send kafka message success topic:"+topic+" msgContent:"+String.valueOf(value));
    }

    @Override
    public void onError(String topic, Integer partition, String key, Object value, Exception exception) {
        logger.warn(" send kafka message error topic:"+topic+" msgContent:"+String.valueOf(value));
    }

    @Override
    public boolean isInterestedInSuccess() {
        return true;
    }
}
