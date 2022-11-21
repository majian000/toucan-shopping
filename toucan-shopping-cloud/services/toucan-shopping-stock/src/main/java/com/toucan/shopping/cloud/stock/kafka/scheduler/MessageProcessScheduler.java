package com.toucan.shopping.cloud.stock.kafka.scheduler;

import com.alibaba.fastjson.JSONObject;
import com.toucan.shopping.modules.common.persistence.event.entity.EventProcess;
import com.toucan.shopping.modules.common.persistence.event.service.EventProcessService;
import com.toucan.shopping.modules.product.message.InventoryReductionMessage;
import com.toucan.shopping.modules.stock.kafka.constant.StockMessageTopicConstant;
import com.toucan.shopping.modules.stock.service.ProductSkuStockLockService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;


/**
 * 处理所有执行本地事务失败的消息
 */
@Component
@EnableScheduling
public class MessageProcessScheduler {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private EventProcessService eventProcessService;

    @Autowired
    private KafkaTemplate kafkaTemplate;

    @Autowired
    private ProductSkuStockLockService productSkuStockLockService;




    /**
     * 每1分钟重新扫描一次本地看有没有执行失败的事务
     */
    @Scheduled(cron = "0 0/1 * * * ? ")
    public void resend()
    {
        logger.info("处理执行失败的事件开始=====================");
        EventProcess query = new EventProcess();
        query.setStatus((short)0);
        List<EventProcess> eventProcesses =  eventProcessService.queryList(query);
        if(!CollectionUtils.isEmpty(eventProcesses))
        {
            for(EventProcess eventProcess : eventProcesses)
            {
                logger.info("本地事务重新执行 "+ eventProcess.getType()+" 内容:"+ eventProcess.getPayload());
                //扣库存
                if(eventProcess.getType().equals(StockMessageTopicConstant.sk_inventory_reduction.name())) {
                    try {
                        InventoryReductionMessage inventoryReductionMessage = JSONObject.parseObject(eventProcess.getPayload(), InventoryReductionMessage.class);
                        productSkuStockLockService.inventoryReduction(inventoryReductionMessage.getSkuUuid());
                        eventProcess.setStatus((short)1); //已处理
                        eventProcessService.updateStatus(eventProcess);
                    }catch(Exception e)
                    {
                        logger.warn(e.getMessage(),e);
                    }
                }
            }

        }
        logger.info("处理执行失败的事件结束=====================");
    }

}
