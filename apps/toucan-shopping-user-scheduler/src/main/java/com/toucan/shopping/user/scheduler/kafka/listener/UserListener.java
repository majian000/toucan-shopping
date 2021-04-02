package com.toucan.shopping.user.scheduler.kafka.listener;

import com.alibaba.fastjson.JSONObject;
import com.toucan.shopping.center.user.export.kafka.message.UserCreateMessage;
import com.toucan.shopping.center.user.export.kafka.message.UserDetailModifyMessage;
import com.toucan.shopping.center.user.export.vo.UserElasticSearchVO;
import com.toucan.shopping.center.user.service.UserElasticSearchService;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 用户消息
 */
@Component
public class UserListener {

    private final Logger logger = LoggerFactory.getLogger(getClass());


    @Autowired
    private UserElasticSearchService esUserService;

    @KafkaListener(topics = {"user_detail_modify"}, groupId = "user_group")
    public void reciveUserDetailModifyMessage(ConsumerRecord<String, String> record) {
        logger.info("收到用户修改消息:{}",record);
        try{
            String messageJsonString = record.value();
            if (StringUtils.isEmpty(messageJsonString)) {
                throw new IllegalArgumentException("消息无效");
            }
            UserDetailModifyMessage userDetailModifyMessage = JSONObject.parseObject(messageJsonString, UserDetailModifyMessage.class);
            UserElasticSearchVO userElasticSearchVO = new UserElasticSearchVO();
            BeanUtils.copyProperties(userElasticSearchVO, userDetailModifyMessage);

            //设置用户主表ID到主键中
            userElasticSearchVO.setId(userDetailModifyMessage.getUserMainId());
            //更新ES用户的缓存
            esUserService.update(userElasticSearchVO);

        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);
        }
    }



    @KafkaListener(topics = {"user_create"}, groupId = "user_group")
    public void reciveUserCreateMessage(ConsumerRecord<String, String> record) {
        logger.info("收到用户创建消息:{}",record);
        try{
            String messageJsonString = record.value();
            if (StringUtils.isEmpty(messageJsonString)) {
                throw new IllegalArgumentException("消息无效");
            }
            UserCreateMessage userCreateMessage = JSONObject.parseObject(messageJsonString, UserCreateMessage.class);
            UserElasticSearchVO userElasticSearchVO = new UserElasticSearchVO();
            BeanUtils.copyProperties(userElasticSearchVO, userCreateMessage);

            //更新ES用户的缓存
            List<UserElasticSearchVO> userElasticSearchVOS = esUserService.queryById(userElasticSearchVO.getId());
            //如果缓存不存在用户将缓存起来
            if (CollectionUtils.isEmpty(userElasticSearchVOS)) {
                esUserService.save(userElasticSearchVO);
            }

        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);
        }
    }
}
