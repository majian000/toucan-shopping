package com.toucan.shopping.cloud.apps.scheduler.controller;

import com.toucan.shopping.modules.kafka.service.KafkaService;
import com.toucan.shopping.modules.product.kafka.constant.ProductMessageTopicConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test/message")
public class TestKafkaMessageController {

    @Autowired
    private KafkaService kafkaService;


    @RequestMapping(value = "/postMsg",method = RequestMethod.POST)
    public void postMsg()
    {
        kafkaService.sendMsg(ProductMessageTopicConstant.PRODUCT_TOPIC,"{productId:100000}");
    }



}
