package com.toucan.shopping.modules.kafka.service;

public interface KafkaService {


    void sendMsg(String topic,String content);


}
