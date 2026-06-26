package com.toucan.shopping.cloud.apps.scheduler.scheduler.user;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.toucan.shopping.cloud.apps.scheduler.constant.PublishEventConstant;
import com.toucan.shopping.cloud.apps.scheduler.service.OrderPayTimeOutService;
import com.toucan.shopping.cloud.order.api.MainOrderServiceAPI;
import com.toucan.shopping.cloud.order.api.OrderServiceAPI;
import com.toucan.shopping.cloud.product.api.ProductSkuServiceAPI;
import com.toucan.shopping.cloud.stock.api.ProductSkuStockLockServiceAPI;
import com.toucan.shopping.cloud.user.api.UserStatisticServiceAPI;
import com.toucan.shopping.modules.common.generator.IdGenerator;
import com.toucan.shopping.modules.common.generator.RequestJsonVOGenerator;
import com.toucan.shopping.modules.common.page.PageInfo;
import com.toucan.shopping.modules.common.persistence.event.entity.EventPublish;
import com.toucan.shopping.modules.common.persistence.event.service.EventPublishService;
import com.toucan.shopping.modules.common.properties.Toucan;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.order.page.MainOrderPageInfo;
import com.toucan.shopping.modules.order.vo.MainOrderVO;
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
 * 每天凌晨同步一次用户总数
 */
@Component
@EnableScheduling
public class UserStatisticScheduler {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private OrderServiceAPI orderService;


    @Autowired
    private Toucan toucan;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private UserStatisticServiceAPI userStatisticService;

    /**
     * 每天0:10触发一次
     */
    @Scheduled(cron = "0 10 0 * * ? ")
    public void rerun()
    {
        logger.info("处理刷新用户总数 开始=====================");
        try {
            userStatisticService.refershTotal(RequestJsonVOGenerator.generator(toucan.getAppCode(),null));
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);
        }
        logger.info("处理刷新用户总数 结束=====================");
    }




}
