package com.toucan.shopping.modules.user.kafka.callback;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.support.ProducerListener;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

@Component
public class SendCallback implements ProducerListener<String, Object> {

    private final Logger logger = LoggerFactory.getLogger(getClass());


    @Override
    public void onSuccess(ProducerRecord<String, Object> producerRecord, RecordMetadata recordMetadata) {
        logger.info(" send kafka message success topic:"+producerRecord.topic()+" msgContent:"+String.valueOf(producerRecord.value()));
    }

    @Override
    public void onError(ProducerRecord<String, Object> producerRecord, @Nullable RecordMetadata recordMetadata, Exception exception) {
        logger.warn(" send kafka message error topic:"+producerRecord.topic()+" msgContent:"+String.valueOf(producerRecord.value()));
    }
}
