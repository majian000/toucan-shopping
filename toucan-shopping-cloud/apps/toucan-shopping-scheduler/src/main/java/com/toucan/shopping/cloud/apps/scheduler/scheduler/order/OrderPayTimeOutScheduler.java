package com.toucan.shopping.cloud.apps.scheduler.scheduler.order;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.toucan.shopping.cloud.order.api.feign.service.FeignMainOrderService;
import com.toucan.shopping.modules.common.generator.RequestJsonVOGenerator;
import com.toucan.shopping.modules.common.page.PageInfo;
import com.toucan.shopping.modules.common.properties.Toucan;
import com.toucan.shopping.modules.common.util.DateUtils;
import com.toucan.shopping.modules.common.util.SignUtil;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.cloud.order.api.feign.service.FeignOrderService;
import com.toucan.shopping.modules.order.entity.Order;
import com.toucan.shopping.modules.order.page.MainOrderPageInfo;
import com.toucan.shopping.modules.order.vo.MainOrderVO;
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
                if(!resultObjectVO.isSuccess())
                {
                    logger.warn("查询超时订单列表失败.....");
                }
                pageInfo = resultObjectVO.formatData(PageInfo.class);
                page++;
            } while (pageInfo != null && !CollectionUtils.isEmpty(pageInfo.getList()));

        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);
        }
        logger.info("处理订单支付超时 结束=====================");
    }

}
