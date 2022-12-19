package com.toucan.shopping.cloud.apps.scheduler.scheduler.order;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.toucan.shopping.cloud.order.api.feign.service.FeignMainOrderService;
import com.toucan.shopping.modules.common.generator.RequestJsonVOGenerator;
import com.toucan.shopping.modules.common.properties.Toucan;
import com.toucan.shopping.modules.common.util.DateUtils;
import com.toucan.shopping.modules.common.util.SignUtil;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.cloud.order.api.feign.service.FeignOrderService;
import com.toucan.shopping.modules.order.entity.Order;
import com.toucan.shopping.modules.product.entity.ProductSku;
import com.toucan.shopping.cloud.stock.api.feign.service.FeignProductSkuStockLockService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Date;
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
    private FeignMainOrderService feignMainOrderService;

    @Autowired
    private Toucan toucan;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;


    @Autowired
    private FeignProductSkuStockLockService feignProductSkuStockLockService;

    /**
     * 每1分钟重新扫描
     */
    @Scheduled(cron = "0 0/1 * * * ? ")
    public void rerun()
    {
        logger.info("处理订单支付超时 开始=====================");

        //获得30分钟前的时间戳
        try {
            Order order = new Order();
            order.setCreateDate(new Date());
            order.setAppCode(toucan.getAppCode());
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generatorByUser(toucan.getAppCode(), null, order);
            ResultObjectVO resultObjectVO = feignOrderService.queryOrderByPayTimeOut(SignUtil.sign(requestJsonVO.getAppCode(),requestJsonVO.getEntityJson()),requestJsonVO);
            if(resultObjectVO.isSuccess())
            {

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
