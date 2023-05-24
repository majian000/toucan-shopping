package com.toucan.shopping.modules.kafka.vo;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * Kafka消息
 */
@Data
public class KafkaMessage {

    private Map<String,Object> body = new HashMap<>();


    public KafkaMessage put(String name,Object value)
    {
        body.put(name,value);
        return this;
    }


}
