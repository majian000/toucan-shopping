package com.toucan.shopping.cloud.apps.scheduler.canal.kafka.listener;

import com.alibaba.fastjson.JSON;
import com.toucan.shopping.cloud.apps.scheduler.vo.CanalMessage;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class CanalListener {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @KafkaListener(topics = "canal_topic",groupId = "canal_topic_group",  concurrency = "5")
    public void consumer(ConsumerRecord<String, String> record){
        logger.info("接收canal消息 {} ",record.value());
    }
}
