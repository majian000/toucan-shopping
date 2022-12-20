package com.toucan.shopping.cloud.apps.scheduler.scheduler.order;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.toucan.shopping.cloud.order.api.feign.service.FeignMainOrderService;
import com.toucan.shopping.cloud.product.api.feign.service.FeignProductSkuService;
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
import com.toucan.shopping.modules.product.vo.InventoryReductionVO;
import com.toucan.shopping.modules.stock.vo.ProductSkuStockLockVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
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
                    List<MainOrderVO> mainOrders= JSONArray.parseArray(JSONObject.toJSONString(pageInfo.getList()),MainOrderVO.class);
                    logger.info("超时订单列表 : {} ",JSONObject.toJSON(pageInfo.getList()));
                    //查询商品库存
                    List<String> mainOrderNoList = mainOrders.stream().map(MainOrderVO::getOrderNo).collect(Collectors.toList());
                    ProductSkuStockLockVO productSkuStockLockVO = new ProductSkuStockLockVO();
                    productSkuStockLockVO.setMainOrderNoList(mainOrderNoList);
                    productSkuStockLockVO.setType((short)1); //下单扣库存,付款扣库存不需要处理(因为付款扣库存是在完成订单的时候扣库存)
                    resultObjectVO = feignProductSkuStockLockService.findLockStockNumByMainOrderNos(RequestJsonVOGenerator.generator(toucan.getAppCode(),productSkuStockLockVO));
                    List<ProductSkuStockLockVO> productSkuStockLocks = resultObjectVO.formatDataList(ProductSkuStockLockVO.class);
                    if(!CollectionUtils.isEmpty(productSkuStockLocks))
                    {
                        List<InventoryReductionVO> inventoryReductions= new LinkedList<>();
                        for(ProductSkuStockLockVO pssl:productSkuStockLocks)
                        {
                            InventoryReductionVO inventoryReductionVO = new InventoryReductionVO();
                            inventoryReductionVO.setProductSkuId(pssl.getProductSkuId());
                            inventoryReductionVO.setStockNum(pssl.getStockNum());
                            inventoryReductions.add(inventoryReductionVO);
                        }
                        if(!CollectionUtils.isEmpty(inventoryReductions)) {
                            resultObjectVO = feignProductSkuService.restoreStock(RequestJsonVOGenerator.generator(toucan.getAppCode(), inventoryReductions));
                            if (resultObjectVO.isSuccess()) {
                                logger.info("删除锁定库存.....");
                                feignProductSkuStockLockService.deleteLockStockByMainOrderNos(RequestJsonVOGenerator.generator(toucan.getAppCode(), productSkuStockLockVO));
                            }
                        }
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
