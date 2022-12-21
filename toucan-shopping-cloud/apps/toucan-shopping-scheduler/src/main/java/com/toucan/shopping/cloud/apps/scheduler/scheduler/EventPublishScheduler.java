package com.toucan.shopping.cloud.apps.scheduler.scheduler;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
//import com.toucan.shopping.modules.common.persistence.event.entity.EventPublish;
//import com.toucan.shopping.modules.common.persistence.event.service.EventPublishService;
import com.toucan.shopping.cloud.apps.scheduler.constant.PublishEventConstant;
import com.toucan.shopping.cloud.apps.scheduler.service.OrderPayTimeOutService;
import com.toucan.shopping.modules.common.generator.RequestJsonVOGenerator;
import com.toucan.shopping.modules.common.persistence.event.entity.EventPublish;
import com.toucan.shopping.modules.common.persistence.event.service.EventPublishService;
import com.toucan.shopping.modules.common.properties.Toucan;
import com.toucan.shopping.modules.common.util.DateUtils;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.cloud.product.api.feign.service.FeignProductSkuService;
import com.toucan.shopping.cloud.stock.api.feign.service.FeignProductSkuStockLockService;
import com.toucan.shopping.modules.order.vo.MainOrderVO;
import com.toucan.shopping.modules.product.vo.InventoryReductionVO;
import com.toucan.shopping.modules.stock.vo.ProductSkuStockLockVO;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * 处理所有feign调用失败的消息
 */
@Component
@EnableScheduling
public class EventPublishScheduler {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private EventPublishService eventProcessService;

    @Autowired
    private KafkaTemplate kafkaTemplate;

    @Autowired
    private FeignProductSkuService feignProductSkuService;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private FeignProductSkuStockLockService feignProductSkuStockLockService;

    @Autowired
    private Toucan toucan;

    @Autowired
    private OrderPayTimeOutService orderPayTimeOutService;

    /**
     * 每5分钟重新扫描一次本地看有没有执行失败的事件
     */
    @Scheduled(cron = "0 0/5 * * * ? ")
    public void rerun()
    {
        logger.info("处理远程调用失败的消息 开始=====================");
        List<EventPublish> eventProcesses =  eventProcessService.queryFaildListByBefore(DateUtils.advanceSecond(new Date(),60*2));
        try {
            if (!CollectionUtils.isEmpty(eventProcesses)) {
                for (EventPublish eventProcess : eventProcesses) {
                    //取消订单事件未完成
                    if (eventProcess.getType().equals(PublishEventConstant.cancel_main_order.name())) {
                        logger.info("远程服务重新调用 {} 内容 {} ", eventProcess.getType(), eventProcess.getPayload());
                        //查询商品库存
                        List<MainOrderVO> mainOrders = JSONArray.parseArray(eventProcess.getPayload(), MainOrderVO.class);

                        List<String> mainOrderNoList = mainOrders.stream().map(MainOrderVO::getOrderNo).collect(Collectors.toList());
                        ProductSkuStockLockVO productSkuStockLockVO = new ProductSkuStockLockVO();
                        productSkuStockLockVO.setMainOrderNoList(mainOrderNoList);
                        productSkuStockLockVO.setType((short) 1); //下单扣库存,付款扣库存不需要处理(因为付款扣库存是在完成订单的时候扣库存)
                        ResultObjectVO resultObjectVO = feignProductSkuStockLockService.findLockStockNumByMainOrderNos(RequestJsonVOGenerator.generator(toucan.getAppCode(), productSkuStockLockVO));
                        if (!resultObjectVO.isSuccess()) {
                            continue;
                        }
                        //完成取消订单数据事件
                        orderPayTimeOutService.finishEvent(eventProcess);

                        List<ProductSkuStockLockVO> productSkuStockLocks = resultObjectVO.formatDataList(ProductSkuStockLockVO.class);
                        resultObjectVO = orderPayTimeOutService.restoreStock(eventProcess.getTransactionId(), eventProcess, productSkuStockLocks,productSkuStockLockVO);
                        if (resultObjectVO.isSuccess()) {
                            //完成还原锁定库存事件
                            orderPayTimeOutService.finishEvent(eventProcess);
                            logger.info("删除锁定库存.....");
                            resultObjectVO = orderPayTimeOutService.deleteLockStockByMainOrderNos(eventProcess.getTransactionId(), eventProcess, productSkuStockLockVO);
                            if (resultObjectVO.isSuccess()) {
                                //删除锁定库存事件
                                orderPayTimeOutService.finishEvent(eventProcess);
                            }
                        }

                    }else if(eventProcess.getType().equals(PublishEventConstant.restart_product_lock_stock_num.name()))
                    {
                        logger.info("删除锁定库存.....");
                        Map params = JSONObject.parseObject(eventProcess.getPayload(), HashMap.class);
                        List<InventoryReductionVO> inventoryReductions = JSONArray.parseArray(JSONObject.toJSONString(params.get("inventoryReductions")), InventoryReductionVO.class);
                        ProductSkuStockLockVO productSkuStockLockVO = JSONObject.parseObject(JSONObject.toJSONString(params.get("productSkuStockLock")), ProductSkuStockLockVO.class);
                        ResultObjectVO resultObjectVO = feignProductSkuService.restoreStock(RequestJsonVOGenerator.generator(toucan.getAppCode(), inventoryReductions));
                        if(resultObjectVO.isSuccess()) {
                            //完成还原锁定库存事件
                            orderPayTimeOutService.finishEvent(eventProcess);
                            resultObjectVO = orderPayTimeOutService.deleteLockStockByMainOrderNos(eventProcess.getTransactionId(), eventProcess, productSkuStockLockVO);
                            if (resultObjectVO.isSuccess()) {
                                //删除锁定库存事件
                                orderPayTimeOutService.finishEvent(eventProcess);
                            }
                        }
                    }else if(eventProcess.getType().equals(PublishEventConstant.delete_lock_stock_num.name()))
                    {
                        ProductSkuStockLockVO productSkuStockLockVO = JSONObject.parseObject(eventProcess.getPayload(), ProductSkuStockLockVO.class);
                        ResultObjectVO resultObjectVO = feignProductSkuStockLockService.deleteLockStockByMainOrderNos(RequestJsonVOGenerator.generator(toucan.getAppCode(), productSkuStockLockVO));
                        if (resultObjectVO.isSuccess()) {
                            //删除锁定库存事件
                            orderPayTimeOutService.finishEvent(eventProcess);
                        }
                    }
                }

            }
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);
        }
        logger.info("处理远程失败的消息 结束=====================");
    }

}
