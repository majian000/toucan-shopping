package com.toucan.shopping.modules.kafka.service.impl;

import com.toucan.shopping.modules.kafka.service.KafkaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaServiceImpl implements KafkaService {

    @Autowired
    private KafkaTemplate kafkaTemplate;


    @Override
    public void sendMsg(String topic, String content) {
        kafkaTemplate.send(topic,content);
    }
}
