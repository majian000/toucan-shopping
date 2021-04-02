package com.toucan.shopping.user.scheduler.kafka.listener;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.toucan.shopping.center.user.export.entity.UserDetail;
import com.toucan.shopping.center.user.export.kafka.message.UserDetailMessage;
import com.toucan.shopping.center.user.export.vo.UserElasticSearchVO;
import com.toucan.shopping.center.user.service.UserElasticSearchService;
import com.toucan.shopping.product.export.entity.ProductBuy;
import com.toucan.shopping.product.export.entity.ProductSku;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
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


    @Autowired
    private UserElasticSearchService esUserService;

    @KafkaListener(topics = {"user_detail_modify"}, groupId = "user_group")
    public void reciveMessage(ConsumerRecord<String, String> record) {
        logger.info("收到用户修改消息:{}",record);
        try{
            String messageJsonString = record.value();
            if (StringUtils.isEmpty(messageJsonString)) {
                throw new IllegalArgumentException("消息无效");
            }
            UserDetailMessage userDetailMessage = JSONObject.parseObject(messageJsonString,UserDetailMessage.class);
            UserElasticSearchVO userElasticSearchVO = new UserElasticSearchVO();
            BeanUtils.copyProperties(userElasticSearchVO,userDetailMessage);

            //设置用户主表ID到主键中
            userElasticSearchVO.setId(userDetailMessage.getUserMainId());
            //更新ES用户的缓存
            esUserService.update(userElasticSearchVO);

        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);
        }
    }


}
