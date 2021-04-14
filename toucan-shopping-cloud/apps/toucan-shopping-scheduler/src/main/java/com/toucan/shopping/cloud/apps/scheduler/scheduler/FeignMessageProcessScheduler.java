package com.toucan.shopping.cloud.apps.scheduler.scheduler;

import com.alibaba.fastjson.JSONObject;
import com.toucan.shopping.modules.common.persistence.event.entity.EventProcess;
import com.toucan.shopping.modules.common.persistence.event.service.EventProcessService;
import com.toucan.shopping.modules.common.util.DateUtils;
import com.toucan.shopping.modules.common.util.SignUtil;
import com.toucan.shopping.modules.product.util.ProductRedisKeyUtil;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.cloud.product.api.feign.service.FeignProductSkuService;
import com.toucan.shopping.modules.product.entity.ProductSku;
import com.toucan.shopping.cloud.stock.api.feign.service.FeignProductSkuStockService;
import com.toucan.shopping.modules.stock.kafka.constant.StockMessageTopicConstant;
import com.toucan.shopping.modules.stock.util.StockRedisKeyUtil;
import com.toucan.shopping.modules.stock.vo.RestoreStockVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.List;


/**
 * 处理所有feign调用失败的消息
 */
@Component
@EnableScheduling
public class FeignMessageProcessScheduler {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private EventProcessService eventProcessService;

    @Autowired
    private KafkaTemplate kafkaTemplate;

    @Autowired
    private FeignProductSkuService feignProductSkuService;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private FeignProductSkuStockService feignProductSkuStockService;


    /**
     * 每5分钟重新扫描一次本地看有没有执行失败的事件
     */
    @Scheduled(cron = "0 0/5 * * * ? ")
    public void rerun()
    {
        logger.info("处理远程调用失败的消息 开始=====================");
        List<EventProcess> eventProcesses =  eventProcessService.queryFaildListByBefore(DateUtils.advanceSecond(new Date(),60*5));
        if(!CollectionUtils.isEmpty(eventProcesses))
        {
            for(EventProcess eventProcess : eventProcesses)
            {
                //恢复库存
                if(eventProcess.getType().equals(StockMessageTopicConstant.restore_stock.name())) {
                    logger.info("远程服务重新调用 "+ eventProcess.getType()+" 内容:"+ eventProcess.getPayload());
                    ResultObjectVO resultObjectVO = feignProductSkuStockService.restoreStock(JSONObject.parseObject(eventProcess.getPayload(), RequestJsonVO.class));
                    if(resultObjectVO.getCode().intValue()==ResultObjectVO.SUCCESS.intValue())
                    {
                        eventProcess.setStatus((short)1);
                        eventProcessService.updateStatus(eventProcess);
                    }
                }else if(eventProcess.getType().equals(StockMessageTopicConstant.restore_redis_stock.name())) {  //恢复预扣库存
                    try {
                        RequestJsonVO requestJsonVO = JSONObject.parseObject(eventProcess.getPayload(), RequestJsonVO.class);
                        ResultObjectVO resultObjectVO = feignProductSkuStockService.restoreCacheStock(SignUtil.sign(requestJsonVO.getAppCode(), requestJsonVO.getEntityJson()), requestJsonVO);
                        if (resultObjectVO.getCode().intValue()==ResultObjectVO.SUCCESS.intValue()) {
                            eventProcess.setStatus((short) 1);
                            eventProcessService.updateStatus(eventProcess);
                        }
                    }catch(Exception e){
                        logger.warn(e.getMessage(),e);
                    }
                }


            }

        }
        logger.info("处理远程失败的消息 结束=====================");
    }

}
