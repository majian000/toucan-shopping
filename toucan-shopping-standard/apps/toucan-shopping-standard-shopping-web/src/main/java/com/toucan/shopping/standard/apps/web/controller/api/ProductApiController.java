package com.toucan.shopping.standard.apps.web.controller.api;

import com.toucan.shopping.modules.common.lock.redis.RedisLock;
import com.toucan.shopping.modules.common.persistence.event.service.EventProcessService;
import com.toucan.shopping.modules.common.properties.Toucan;
import com.toucan.shopping.modules.order.no.OrderNoService;
import com.toucan.shopping.modules.order.service.OrderService;
import com.toucan.shopping.modules.product.service.ProductSkuService;
import com.toucan.shopping.modules.stock.service.ProductSkuStockLockService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/product")
public class ProductApiController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private Toucan toucan;

    @Autowired
    private ProductSkuService productSkuService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private ProductSkuStockLockService productSkuStockLockService;

    @Autowired
    private RedisLock redisLock;

    @Autowired
    private EventProcessService eventProcessService;

    @Autowired
    private OrderNoService orderNoService;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;


//    /**
//     * TODO:订单30分钟未支付 恢复预扣库存数量,支付宝回调刷新订单状态 如果失败创建本地事件以便事务补偿回滚
//     * @param buyVo
//     * @return
//     */
//    @RequestMapping(value="/buy",produces = "application/json;charset=UTF-8")
//    @ResponseBody
//    public ResultObjectVO buy(@RequestBody BuyVo buyVo){
//        ResultObjectVO resultObjectVO = new ResultObjectVO();
//        logger.info("购买商品: param:"+ JSONObject.toJSONString(buyVo));
//        if(buyVo==null||buyVo.getUserId()==null)
//        {
//            logger.info("没有找到用户: param:"+ JSONObject.toJSONString(buyVo));
//            resultObjectVO.setCode(ResultVO.FAILD);
//            resultObjectVO.setMsg("没有找到用户!");
//            return resultObjectVO;
//        }
//        if(buyVo==null|| CollectionUtils.isEmpty(buyVo.getProductSkuList()))
//        {
//            logger.info("没有找到要购买的商品: param:"+ JSONObject.toJSONString(buyVo));
//            resultObjectVO.setCode(ResultVO.FAILD);
//            resultObjectVO.setMsg("没有找到要购买的商品!");
//            return resultObjectVO;
//        }
//        //锁住当前购买所有商品
//        List<String> lockKeys= new ArrayList<String>();
//
//        System.out.println(DateUtils.currentDate().getTime());
//        String userId = buyVo.getUserId();
//        //订单号
//        String orderNo= orderNoService.generateOrderNo();
//        String globalTransactionId = UUID.randomUUID().toString().replace("-","");
//        String appCode = toucan.getAppCode();
//
//        try {
//            for(ProductSku productSku : buyVo.getProductSkuList())
//            {
//                if(productSku!=null) {
//                    if (StringUtils.isEmpty(productSku.getUuid())) {
//                        logger.warn("sku uuid is null params: {} ", JSONObject.toJSONString(productSku));
//                        throw new IllegalArgumentException("没有找到商品");
//                    }
//                    String productBuyKey = ProductRedisKeyUtil.getProductBuyKey(appCode, productSku.getUuid());
//                    boolean lockStatus = redisLock.lock(productBuyKey, userId);
//                    while (!lockStatus) {
//                        lockStatus = redisLock.lock(productBuyKey, userId);
//                    }
//                    lockKeys.add(productBuyKey);
//                }
//            }
//
//            //可购买商品列表
//            List<ProductSku> releaseProductSkuList = new ArrayList<ProductSku>();
//            //库存不够商品列表
//            List<ProductSku> faildProductSkuList = new ArrayList<ProductSku>();
//
//            //查询商品
//            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generatorByUser(appCode,userId,buyVo.getProductSkuList());
//            resultObjectVO = productSkuService.queryByIdList(SignUtil.sign(appCode,requestJsonVO.getEntityJson()),requestJsonVO);
//            if(resultObjectVO.getCode().intValue()==ResultVO.SUCCESS.intValue()) {
//                List<ProductSku> queryProductSkuList = JSONArray.parseArray(JSONObject.toJSONString(resultObjectVO.getData()),ProductSku.class);
//
//                //调用库存服务查询库存
//                if(!CollectionUtils.isEmpty(queryProductSkuList))
//                {
//                    requestJsonVO = RequestJsonVOGenerator.generatorByUser(appCode,userId,queryProductSkuList);
//                    resultObjectVO = productSkuStockLockService.queryStockCacheBySkuUuidList(SignUtil.sign(appCode,requestJsonVO.getEntityJson()),requestJsonVO);
//                    //如果查询库存缓存失败 商品数量全部设置为0
//                    if(resultObjectVO.getCode().intValue()==ResultObjectVO.FAILD.intValue())
//                    {
//                        for (ProductSku productSku : queryProductSkuList) {
//                            productSku.setStockNum(0);
//                        }
//                    }else{
//                        List<ProductSkuStockLock> productSkuStocks = JSONArray.parseArray(JSONObject.toJSONString(resultObjectVO.getData()), ProductSkuStockLock.class);
//                        if(!CollectionUtils.isEmpty(productSkuStocks))
//                        {
//                            //设置预扣库存
//                            for(ProductSkuStockLock productSkuStock:productSkuStocks)
//                            {
//                                if(productSkuStock!=null&&StringUtils.isNotEmpty(productSkuStock.getSkuUuid()))
//                                {
//                                    for (ProductSku productSku : queryProductSkuList) {
//                                        if(productSku!=null&&StringUtils.isNotEmpty(productSku.getUuid()))
//                                        {
//                                            if(productSku.getUuid().equals(productSkuStock.getSkuUuid()))
//                                            {
//                                                productSku.setStockNum(productSkuStock.getStockNum());
//                                            }
//                                        }
//                                    }
//                                }
//                            }
//                        }
//                    }
//
//                }
//                //判断商品数量
//                for (ProductSku productSku : queryProductSkuList) {
//                    if (productSku.getStockNum().intValue() <= 0) {
//                        faildProductSkuList.add(productSku);
//                        continue;
//                    }
//                    releaseProductSkuList.add(productSku);
//                }
//                //设置最终可购买的所以商品
//                buyVo.setProductSkuList(releaseProductSkuList);
//
//                //如果当前要购买的商品全部都有库存
//                if (!CollectionUtils.isEmpty(buyVo.getProductSkuList())) {
//                    //预扣库存对象
//
//                    InventoryReductionVo inventoryReductionVo = new InventoryReductionVo();
//                    inventoryReductionVo.setAppCode(appCode);
//                    inventoryReductionVo.setUserId(userId);
//                    inventoryReductionVo.setProductSkuList(buyVo.getProductSkuList());
//
//                    //预扣库存
//                    logger.info("开始预扣库存 {}",JSONObject.toJSONString(requestJsonVO));
//                    requestJsonVO = RequestJsonVOGenerator.generatorByUser(appCode,userId,buyVo.getProductSkuList());
//                    resultObjectVO = productSkuStockLockService.deductCacheStock(SignUtil.sign(appCode,requestJsonVO.getEntityJson()),requestJsonVO);
//                    if(resultObjectVO.getCode().intValue()==ResultObjectVO.SUCCESS.intValue()) {
//
//                        List<EventProcess> restoreStock = new ArrayList<EventProcess>();
//                        //创建本地补偿事务消息
//                        for (ProductSku productSku : buyVo.getProductSkuList()) {
//
//                            //还原库存对象
//                            requestJsonVO = RequestJsonVOGenerator.generatorByUser(appCode, userId, inventoryReductionVo);
//                            RestoreStockVO restoreStockVo = new RestoreStockVO();
//                            restoreStockVo.setAppCode(appCode);
//                            restoreStockVo.setUserId(userId);
//                            List<ProductSku> productSkuList = new ArrayList<ProductSku>();
//                            productSkuList.add(productSku);
//                            restoreStockVo.setProductSkuList(productSkuList);
//                            requestJsonVO.setEntityJson(JSONObject.toJSONString(restoreStockVo));
//
//                            EventProcess eventProcess = new EventProcess();
//                            eventProcess.setCreateDate(new Date());
//                            eventProcess.setBusinessId(String.valueOf(productSku.getId()));
//                            eventProcess.setRemark("还原预扣库存 skuId:" + productSku.getId());
//                            eventProcess.setTableName(null);
//                            eventProcess.setTransactionId(globalTransactionId);
//                            eventProcess.setPayload(JSONObject.toJSONString(requestJsonVO));
//                            eventProcess.setStatus((short) 0); //待处理
//                            eventProcess.setType(StockMessageTopicConstant.restore_redis_stock.name());
//                            eventProcessService.insert(eventProcess);
//
//                            restoreStock.add(eventProcess);
//                        }
//
//
//                        //扣库存成功后创建订单
//                        requestJsonVO = RequestJsonVOGenerator.generatorByUser(appCode, userId, inventoryReductionVo);
//                        CreateOrderVO createOrderVo = new CreateOrderVO();
//                        createOrderVo.setAppCode(appCode);
//                        createOrderVo.setUserId(userId);
//                        createOrderVo.setOrderNo(orderNo);
//                        createOrderVo.setPayMethod(1);
//                        createOrderVo.setProductSkuList(buyVo.getProductSkuList());
//                        createOrderVo.setBuyMap(buyVo.getBuyMap());
//                        requestJsonVO.setEntityJson(JSONObject.toJSONString(createOrderVo));
//
//                        resultObjectVO = orderService.create(SignUtil.sign(appCode, requestJsonVO.getEntityJson()), requestJsonVO);
//
//                        //如果订单创建成功 将上面回滚事件取消掉
//                        if (resultObjectVO.getCode().intValue() == ResultVO.SUCCESS.intValue()) {
//                            for (EventProcess eventProcess : restoreStock) {
//                                eventProcess.setStatus((short) 1);
//                                eventProcessService.updateStatus(eventProcess);
//                            }
//                        }
//                        //订单创建失败 将回滚库存
//                        if (resultObjectVO.getCode().intValue() == ResultVO.FAILD.intValue()) {
//
//                            for (EventProcess eventProcess : restoreStock) {
//                                ResultObjectVO restoreStockResultObjectVO = productSkuStockLockService.restoreStock(JSONObject.parseObject(eventProcess.getPayload(), RequestJsonVO.class));
//                                if (restoreStockResultObjectVO.getCode().intValue() == ResultVO.SUCCESS.intValue()) {
//                                    eventProcess.setStatus((short) 1);
//                                    eventProcessService.updateStatus(eventProcess);
//                                }
//                            }
//                        }
//                        if (!CollectionUtils.isEmpty(faildProductSkuList)) {
//                            resultObjectVO.setCode(BuyResultVo.BUY_SOME_NOT_STOCK);
//                            resultObjectVO.setMsg("对不起,您购买的部分商品已售罄!");
//                            resultObjectVO.setData(faildProductSkuList);
//                        }
//                    }else{
//                        resultObjectVO.setCode(BuyResultVo.BUY_ALL_NOT_STOCK);
//                        resultObjectVO.setMsg("对不起,您购买的商品已售罄!");
//                        resultObjectVO.setData(faildProductSkuList);
//                    }
//                } else { //购买商品没有库存了
//                    resultObjectVO.setCode(BuyResultVo.BUY_ALL_NOT_STOCK);
//                    resultObjectVO.setMsg("对不起,您购买的商品已售罄!");
//                    resultObjectVO.setData(faildProductSkuList);
//
//                }
//            }
//
//        }catch(Exception e)
//        {
//            logger.warn(e.getMessage(),e);
//
//            resultObjectVO.setCode(ResultVO.FAILD);
//            resultObjectVO.setMsg("购买失败,请重试!");
//        }finally{
//            //释放所有商品锁
//            for(String lockKey:lockKeys) {
//                redisLock.unLock(lockKey, userId);
//            }
//        }
//
//        return resultObjectVO;
//    }



}
