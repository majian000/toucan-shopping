package com.toucan.shopping.scheduler.scheduler;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.toucan.shopping.common.generator.RequestJsonVOGenerator;
import com.toucan.shopping.common.properties.Toucan;
import com.toucan.shopping.common.util.DateUtils;
import com.toucan.shopping.common.util.SignUtil;
import com.toucan.shopping.common.vo.RequestJsonVO;
import com.toucan.shopping.common.vo.ResultObjectVO;
import com.toucan.shopping.order.api.feign.service.FeignOrderService;
import com.toucan.shopping.order.export.entity.Order;
import com.toucan.shopping.product.export.entity.ProductSku;
import com.toucan.shopping.stock.api.feign.service.FeignProductSkuStockService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;


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
    private Toucan toucan;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;


    @Autowired
    private FeignProductSkuStockService feignProductSkuStockService;

    /**
     * 每1分钟重新扫描一次本地看有没有执行失败的事件
     */
    @Scheduled(cron = "0 0/1 * * * ? ")
    public void rerun()
    {
        logger.info("处理订单支付超时 开始=====================");

        //获得30分钟前的时间戳
        try {
            Order order = new Order();
            order.setCreateDate(DateUtils.advanceSecond(DateUtils.currentDate(), 60 * 30));
            order.setAppCode(toucan.getAppCode());
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generatorByUser(toucan.getAppCode(), null, order);
            ResultObjectVO resultObjectVO = feignOrderService.queryOrderByPayTimeOut(SignUtil.sign(requestJsonVO.getAppCode(),requestJsonVO.getEntityJson()),requestJsonVO);
            if(resultObjectVO.getCode().intValue()==ResultObjectVO.SUCCESS.intValue())
            {
                List<Order> orders = JSONArray.parseArray(JSONObject.toJSONString(resultObjectVO.getData()),Order.class);
                if(!CollectionUtils.isEmpty(orders))
                {
                    for(Order faildOrder:orders)
                    {
                        faildOrder.setAppCode(toucan.getAppCode());
                        requestJsonVO = RequestJsonVOGenerator.generatorByUser(toucan.getAppCode(), null, faildOrder);
                        //取消订单
                        resultObjectVO = feignOrderService.cancel(SignUtil.sign(requestJsonVO.getAppCode(),requestJsonVO.getEntityJson()),requestJsonVO);
                        if(resultObjectVO.getCode().intValue()==ResultObjectVO.SUCCESS.intValue())
                        {
                            //恢复预扣库存
                            requestJsonVO = RequestJsonVOGenerator.generatorByUser(toucan.getAppCode(), faildOrder.getUserId(), faildOrder);
                            resultObjectVO = feignOrderService.querySkuUuidsByOrderNo(SignUtil.sign(requestJsonVO.getAppCode(),requestJsonVO.getEntityJson()),requestJsonVO);
                            if(resultObjectVO.getCode().intValue()==ResultObjectVO.SUCCESS.intValue())
                            {
                                List<ProductSku> productSkus = JSONArray.parseArray(JSONObject.toJSONString(resultObjectVO.getData()),ProductSku.class);
                                requestJsonVO = RequestJsonVOGenerator.generatorByUser(toucan.getAppCode(), faildOrder.getUserId(), productSkus);
                                resultObjectVO = feignProductSkuStockService.restoreCacheStock(SignUtil.sign(requestJsonVO.getAppCode(), requestJsonVO.getEntityJson()), requestJsonVO);
                                if(resultObjectVO.getCode().intValue()==ResultObjectVO.FAILD.intValue())
                                {
                                    logger.warn("查询订单的商品sku失败 order :{}",JSONObject.toJSONString(faildOrder));
                                }

                            }else{
                                logger.warn("查询订单的商品sku失败 order :{}",JSONObject.toJSONString(faildOrder));
                            }
                        }else{
                            logger.warn("取消订单失败 order :{}",JSONObject.toJSONString(faildOrder));
                        }
                    }

                }
            }else{
                logger.warn("查询支付超时订单列表失败 param {}",requestJsonVO.getEntityJson());
            }
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);
        }
        logger.info("处理订单支付超时 结束=====================");
    }

}
