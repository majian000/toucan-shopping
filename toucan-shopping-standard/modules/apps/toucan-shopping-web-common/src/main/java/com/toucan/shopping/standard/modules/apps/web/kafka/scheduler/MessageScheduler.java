package com.toucan.shopping.standard.modules.apps.web.kafka.scheduler;

import com.toucan.shopping.modules.common.persistence.event.service.EventPublishService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


/**
 * 重发所有发送失败的消息
 */
@Component
@EnableScheduling
public class MessageScheduler {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private EventPublishService eventPublishService;

    @Autowired
    private KafkaTemplate kafkaTemplate;




    /**
     * 每两分钟重发一次消息
     */
    @Scheduled(cron = "0 0/2 * * * ? ")
    public void resend()
    {
        logger.info("消息重发 开始=====================");
        //查询五分钟之前发送失败的所有消息
//        List<EventPublish> eventPublishes =  eventPublishService.queryFaildListByBefore(DateUtils.advanceSecond(new Date(),60*5));
//        if(!CollectionUtils.isEmpty(eventPublishes))
//        {
//            for(EventPublish eventPublish : eventPublishes)
//            {
//            }
//
//        }
        logger.info("消息重发 结束=====================");
    }

}
