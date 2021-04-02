package com.toucan.shopping.user.scheduler.kafka.listener;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.toucan.shopping.product.export.entity.ProductBuy;
import com.toucan.shopping.product.export.entity.ProductSku;
import org.apache.commons.lang.StringUtils;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.*;

/**
 * 用户消息
 */
@Component
public class UserListener {

    private final Logger logger = LoggerFactory.getLogger(getClass());



    @KafkaListener(topics = {"user_modify"}, groupId = "user_group")
    public void reciveMessage(ConsumerRecord<String, String> record) {
        System.out.println("收到用户修改消息:" + record);

        try{

        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);
        }



    }


}
