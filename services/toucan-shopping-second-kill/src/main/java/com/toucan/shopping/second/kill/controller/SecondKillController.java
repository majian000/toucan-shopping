package com.toucan.shopping.second.kill.controller;


import com.alibaba.fastjson.JSONObject;
import com.toucan.shopping.common.generator.RequestJsonVOGenerator;
import com.toucan.shopping.common.util.SignUtil;
import com.toucan.shopping.common.vo.RequestJsonVO;
import com.toucan.shopping.lock.redis.RedisLock;
import com.toucan.shopping.order.export.kafka.constant.OrderMessageTopicConstant;
import com.toucan.shopping.order.export.message.CreateOrderMessage;
import com.toucan.shopping.product.export.kafka.constant.ProductMessageTopicConstant;
import com.toucan.shopping.product.export.message.InventoryReductionMessage;
import com.toucan.shopping.common.persistence.entity.EventPublish;
import com.toucan.shopping.product.export.util.ProductRedisKeyUtil;
import com.toucan.shopping.common.vo.ResultListVO;
import com.toucan.shopping.common.vo.ResultObjectVO;
import com.toucan.shopping.common.vo.ResultVO;
import com.toucan.shopping.product.api.feign.service.FeignProductSkuService;
import com.toucan.shopping.product.export.entity.ProductBuy;
import com.toucan.shopping.product.export.entity.ProductSku;
import com.toucan.shopping.second.kill.service.SecondKillService;
import com.toucan.shopping.stock.export.kafka.constant.StockMessageTopicConstant;
import com.toucan.shopping.stock.export.util.StockRedisKeyUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * 秒杀服务
 */
@RestController
@RequestMapping("/sk")
public class SecondKillController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Value("${toucan.app-code}")
    private String appCode;

    @Autowired
    private FeignProductSkuService feignProductSkuService;

    @Autowired
    private RedisLock redisLock;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private KafkaTemplate kafkaTemplate;

    @Autowired
    private SecondKillService secondKillService;

    /**
     * 初始化
     * @return
     */
    @RequestMapping(method = RequestMethod.POST,value="/init",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO init()
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generatorByUser(appCode,"","");
            ResultListVO  resultListVO = feignProductSkuService.queryShelvesList(SignUtil.sign(appCode,requestJsonVO.getEntityJson()),requestJsonVO);
            if(resultListVO.getCode().intValue()==ResultVO.FAILD)
            {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("查询上架商品失败");
                return resultObjectVO;
            }

            if(resultListVO.getData()==null)
            {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("没有找到要初始化的商品列表");
            }
            List<ProductSku> products = JSONObject.parseArray(JSONObject.toJSONString(resultListVO.getData()),ProductSku.class);
            if (!CollectionUtils.isEmpty(products)) {
                for (ProductSku productSku : products) {
                    if(productSku!=null) {
                        if(productSku.getStockNum().intValue()>0) {
                            //设置库存
                            redisTemplate.opsForValue().set(StockRedisKeyUtil.getStockKey(appCode, productSku.getUuid()), String.valueOf(productSku.getStockNum()));
                            //设置商品活动开始状态
                            redisTemplate.opsForValue().set(ProductRedisKeyUtil.getProductActivityKey(appCode, String.valueOf(productSku.getId())), "1");
                            //设置商品
                            redisTemplate.opsForValue().set(ProductRedisKeyUtil.getProductKey(appCode, String.valueOf(productSku.getId())), JSONObject.toJSONString(productSku));
                        }
                    }
                }
            }
        }catch(Exception e)
        {
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("初始化秒杀模块失败");
            logger.warn(e.getMessage(),e);
        }


        return resultObjectVO;
    }



    /**
     * 点击秒杀
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(method = RequestMethod.POST,value="/sk",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO sk(@RequestBody RequestJsonVO requestJsonVO)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO(ResultVO.FAILD,"请重试");
        if(requestJsonVO!=null) {
            logger.info("点击秒杀 param : "+ JSONObject.toJSONString(requestJsonVO));
            ProductSku skProductSku = JSONObject.parseObject(requestJsonVO.getEntityJson(),ProductSku.class);
            if(skProductSku.getId()==null)
            {
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("没有找到商品");
                return resultObjectVO;
            }
            if(requestJsonVO.getUserId()==null)
            {
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("没有找到用户");
                return resultObjectVO;
            }
            String skuUuid = skProductSku.getUuid();
            String userId=String.valueOf(requestJsonVO.getUserId());
            String lockKey = ProductRedisKeyUtil.getGlobalSecondKillKey(this.appCode,skuUuid);
            String stockKey = StockRedisKeyUtil.getStockKey(this.appCode,skuUuid);
            String orderNo = UUID.randomUUID().toString().replace("-","");
            String productSkuJson = redisTemplate.opsForValue().get(ProductRedisKeyUtil.getProductKey(this.appCode,skuUuid));

            if(StringUtils.isEmpty(productSkuJson))
            {
                resultObjectVO.setCode(ResultObjectVO.SUCCESS);
                resultObjectVO.setMsg("下单失败,请重试");
                return resultObjectVO;
            }

            //TODO:控制访问人数,比如只允许1000人访问,其余所有人将不执行后面代码,限流部分放在网关做判断


            //判断全局状态
            String productActivityKey = ProductRedisKeyUtil.getProductActivityKey(this.appCode,skuUuid);

            Object productActivityValueObject = redisTemplate.opsForValue().get(productActivityKey);
            if(productActivityValueObject==null)
            {
                resultObjectVO.setCode(ResultObjectVO.SUCCESS);
                resultObjectVO.setMsg("活动已结束");
                return resultObjectVO;
            }
            String productActivityValue = String.valueOf(productActivityValueObject);
            if("0".equals(productActivityValue))
            {
                resultObjectVO.setCode(ResultObjectVO.SUCCESS);
                resultObjectVO.setMsg("商品已售罄");
                return resultObjectVO;
            }

            boolean lockStatus = redisLock.lock(lockKey,userId);
            if(!lockStatus)
            {
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("超时重试");
                return resultObjectVO;
            }

            try {
                //初始化库存
                if (redisTemplate.opsForValue().get(stockKey) == null) {
                    resultObjectVO = init();
                }

                Integer stock = Integer.parseInt(String.valueOf(redisTemplate.opsForValue().get(stockKey)));
                if (stock <= 0) {
                    redisLock.unLock(lockKey, userId);

                    //设置结束状态
                    redisTemplate.opsForValue().set(productActivityKey,"0");

                    resultObjectVO.setCode(ResultObjectVO.SUCCESS);
                    resultObjectVO.setMsg("商品已售罄");
                    return resultObjectVO;
                }


                //扣redis库存
                redisTemplate.opsForValue().set(stockKey, String.valueOf(stock.longValue() - 1));

                logger.info("get product userId:"+userId+ " skuUuid:"+skuUuid);
                String globalTransactionId = UUID.randomUUID().toString().replace("-","");

                //异步发送消息 数据入库

                //设置商品列表
                List<ProductSku> productSkuList = new ArrayList<ProductSku>();
                ProductSku productSku = JSONObject.parseObject(productSkuJson,ProductSku.class);
                productSkuList.add(productSku);

                //发送扣库存消息
                InventoryReductionMessage inventoryReductionMessage = new InventoryReductionMessage();
                inventoryReductionMessage.setAppCode(appCode);
                inventoryReductionMessage.setUserId(userId);
                inventoryReductionMessage.setOrderNo(orderNo);
                //保存全局事务
                inventoryReductionMessage.setGlobalTransactionId(globalTransactionId);
                inventoryReductionMessage.setSkuId(skuUuid);
                inventoryReductionMessage.setProductSkuListJson(JSONObject.toJSONString(productSkuList));

                //保存扣库存发布事件记录
                EventPublish inventorReductionMessagePersistence = secondKillService.saveInventoryReductionMessagePublishEvent(inventoryReductionMessage, orderNo,
                        globalTransactionId, skuUuid, productSkuList);
                inventoryReductionMessage.setLocalEventPublishId(String.valueOf(inventorReductionMessagePersistence.getId()));


                //保存创建订单发布事件
                //购买信息对象
                Map<String, String> buyMap=new LinkedHashMap<String,String>();
                ProductBuy productBuy=new ProductBuy();
                productBuy.setSkuId(productSku.getId());
                productBuy.setBuyNum(1);
                buyMap.put(String.valueOf(productSku.getId()),JSONObject.toJSONString(productBuy));

                CreateOrderMessage createOrderMessage = new CreateOrderMessage();
                createOrderMessage.setAppCode(appCode);
                createOrderMessage.setUserId(userId);
                createOrderMessage.setOrderNo(orderNo);
                //保存全局事务
                createOrderMessage.setGlobalTransactionId(globalTransactionId);
                createOrderMessage.setSkuId(String.valueOf(skProductSku.getId()));
                createOrderMessage.setSkuUuid(skuUuid);
                createOrderMessage.setProductSkuListJson(JSONObject.toJSONString(productSkuList));
                createOrderMessage.setBuyMap(JSONObject.toJSONString(buyMap));

                //保存创建订单事件
                EventPublish orderMessagePersistence = secondKillService.saveCreateOrderMessagePublishEvent(createOrderMessage, orderNo,
                        globalTransactionId);
                createOrderMessage.setLocalEventPublishId(String.valueOf(orderMessagePersistence.getId()));



                //发送扣库存消息
                kafkaTemplate.send(StockMessageTopicConstant.sk_inventory_reduction.name(),JSONObject.toJSONString(inventoryReductionMessage));

                //发送创建订单消息
                kafkaTemplate.send(OrderMessageTopicConstant.sk_create_order.name(),JSONObject.toJSONString(createOrderMessage));




                redisLock.unLock(lockKey, userId);
                resultObjectVO.setMsg("恭喜抢到了");
            }catch (Exception e)
            {
                redisLock.unLock(lockKey, userId);
                logger.warn(e.getMessage(),e);
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("请重试");
            }
        }
        return resultObjectVO;
    }

//
//    /**
//     * 下单支付功能
//     * @param queryMap
//     * @return
//     */
//    @RequestMapping(method = RequestMethod.POST,value="/placeOrder",produces = "application/json;charset=UTF-8")
//    @ResponseBody
//    public ResultObjectVO placeOrder(@RequestBody Map<String,Object> queryMap)
//    {
//        ResultObjectVO resultObjectVO = new ResultObjectVO();
//        if(queryMap!=null) {
//            if(queryMap.get("skuId")==null)
//            {
//                resultObjectVO.setCode(ResultObjectVO.FAILD);
//                resultObjectVO.setMsg("没有找到商品");
//                return resultObjectVO;
//            }
//        }
//        return resultObjectVO;
//    }

}
