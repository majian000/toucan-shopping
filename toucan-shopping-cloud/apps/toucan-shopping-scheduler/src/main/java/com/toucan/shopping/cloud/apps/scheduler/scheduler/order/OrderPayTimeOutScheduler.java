package com.toucan.shopping.cloud.apps.scheduler.scheduler.order;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.toucan.shopping.cloud.apps.scheduler.constant.PublishEventConstant;
import com.toucan.shopping.cloud.apps.scheduler.service.OrderPayTimeOutService;
import com.toucan.shopping.cloud.order.api.feign.service.FeignMainOrderService;
import com.toucan.shopping.cloud.product.api.feign.service.FeignProductSkuService;
import com.toucan.shopping.modules.common.generator.IdGenerator;
import com.toucan.shopping.modules.common.generator.RequestJsonVOGenerator;
import com.toucan.shopping.modules.common.page.PageInfo;
import com.toucan.shopping.modules.common.persistence.event.entity.EventPublish;
import com.toucan.shopping.modules.common.persistence.event.service.EventPublishService;
import com.toucan.shopping.modules.common.properties.Toucan;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.cloud.order.api.feign.service.FeignOrderService;
import com.toucan.shopping.modules.order.page.MainOrderPageInfo;
import com.toucan.shopping.modules.order.vo.MainOrderVO;
import com.toucan.shopping.cloud.stock.api.feign.service.FeignProductSkuStockLockService;
import com.toucan.shopping.modules.stock.vo.ProductSkuStockLockVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;


/**
 * 处理所有支付超时的订单
 * 30分钟以上未支付将取消订单恢复商品预扣库存
 */
@Component
@EnableScheduling
public class OrderPayTimeOutScheduler {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private FeignOrderService feignOrderService;

    @Autowired
    private FeignMainOrderService feignMainOrderService;

    @Autowired
    private Toucan toucan;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;


    @Autowired
    private FeignProductSkuStockLockService feignProductSkuStockLockService;

    @Autowired
    private FeignProductSkuService feignProductSkuService;

    @Autowired
    private IdGenerator idGenerator;

    @Autowired
    private EventPublishService eventProcessService;

    @Autowired
    private OrderPayTimeOutService orderPayTimeOutService;

    /**
     * 每1分钟重新扫描
     */
    @Scheduled(cron = "0 0/1 * * * ? ")
    public void rerun()
    {
        logger.info("处理订单支付超时 开始=====================");

        try {
            int page = 1;
            int limit = 100;
            PageInfo pageInfo = null;
            ResultObjectVO resultObjectVO = null;
            MainOrderPageInfo query = new MainOrderPageInfo();
            query.setLimit(limit);
            do {
                logger.info(" 查询超时订单列表 页码:{} 每页显示 {} ", page, limit);
                query.setPage(page);
                resultObjectVO =  feignMainOrderService.batchCancelPayTimeout(RequestJsonVOGenerator.generator(toucan.getAppCode(),query));
                page++;
                if(!resultObjectVO.isSuccess())
                {
                    logger.warn("查询超时订单列表失败..... msg:{} ",resultObjectVO.getMsg());
                    continue;
                }
                pageInfo = resultObjectVO.formatData(PageInfo.class);
                if(!CollectionUtils.isEmpty(pageInfo.getList()))
                {
                    String globalTransactionId = UUID.randomUUID().toString().replace("-","");
                    List<MainOrderVO> mainOrders= JSONArray.parseArray(JSONObject.toJSONString(pageInfo.getList()),MainOrderVO.class);
                    logger.info("超时订单列表 : {} ",JSONObject.toJSON(pageInfo.getList()));
                    //保存取消订单数据事件
                    EventPublish eventProcess = orderPayTimeOutService.saveEvent(globalTransactionId,mainOrders,"取消超时订单", PublishEventConstant.cancel_main_order.name());
                    //查询商品库存
                    List<String> mainOrderNoList = mainOrders.stream().map(MainOrderVO::getOrderNo).collect(Collectors.toList());
                    ProductSkuStockLockVO productSkuStockLockVO = new ProductSkuStockLockVO();
                    productSkuStockLockVO.setMainOrderNoList(mainOrderNoList);
                    productSkuStockLockVO.setType((short)1); //下单扣库存,付款扣库存不需要处理(因为付款扣库存是在完成订单的时候扣库存)
                    resultObjectVO = feignProductSkuStockLockService.findLockStockNumByMainOrderNos(RequestJsonVOGenerator.generator(toucan.getAppCode(),productSkuStockLockVO));
                    if(!resultObjectVO.isSuccess())
                    {
                        continue;
                    }
                    //完成取消订单数据事件
                    orderPayTimeOutService.finishEvent(eventProcess);

                    List<ProductSkuStockLockVO> productSkuStockLocks = resultObjectVO.formatDataList(ProductSkuStockLockVO.class);
                    resultObjectVO = orderPayTimeOutService.restoreStock(globalTransactionId,eventProcess,productSkuStockLocks,productSkuStockLockVO);
                    if (resultObjectVO.isSuccess()) {
                        //完成还原锁定库存事件
                        orderPayTimeOutService.finishEvent(eventProcess);
                    }

                    logger.info("删除锁定库存.....");
                    orderPayTimeOutService.deleteLockStockByMainOrderNos(globalTransactionId,eventProcess,productSkuStockLockVO);
                    if(resultObjectVO.isSuccess())
                    {
                        //删除锁定库存事件
                        orderPayTimeOutService.finishEvent(eventProcess);
                    }

                }
            } while (pageInfo != null && !CollectionUtils.isEmpty(pageInfo.getList()));

        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);
        }
        logger.info("处理订单支付超时 结束=====================");
    }




}
