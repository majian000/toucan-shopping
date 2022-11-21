package com.toucan.shopping.cloud.apps.web.controller.order;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.toucan.shopping.cloud.apps.web.service.PayService;
import com.toucan.shopping.cloud.apps.web.vo.BuyVo;
import com.toucan.shopping.cloud.apps.web.vo.PayVo;
import com.toucan.shopping.cloud.order.api.feign.service.FeignOrderService;
import com.toucan.shopping.cloud.product.api.feign.service.FeignProductSkuService;
import com.toucan.shopping.cloud.stock.api.feign.service.FeignProductSkuStockLockService;
import com.toucan.shopping.cloud.user.api.feign.service.FeignUserBuyCarService;
import com.toucan.shopping.modules.auth.user.UserAuth;
import com.toucan.shopping.modules.common.generator.IdGenerator;
import com.toucan.shopping.modules.common.generator.RequestJsonVOGenerator;
import com.toucan.shopping.modules.common.persistence.event.entity.EventProcess;
import com.toucan.shopping.modules.common.persistence.event.service.EventProcessService;
import com.toucan.shopping.modules.common.properties.Toucan;
import com.toucan.shopping.modules.common.util.HttpParamUtil;
import com.toucan.shopping.modules.common.util.SignUtil;
import com.toucan.shopping.modules.common.util.UserAuthHeaderUtil;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.common.vo.ResultVO;
import com.toucan.shopping.modules.order.entity.Order;
import com.toucan.shopping.modules.order.no.OrderNoService;
import com.toucan.shopping.modules.order.vo.QueryOrderVo;
import com.toucan.shopping.modules.product.entity.ProductSku;
import com.toucan.shopping.modules.product.util.ProductRedisKeyUtil;
import com.toucan.shopping.modules.product.vo.InventoryReductionVO;
import com.toucan.shopping.modules.product.vo.ProductSkuVO;
import com.toucan.shopping.modules.product.vo.RestoreStockVO;
import com.toucan.shopping.modules.skylark.lock.service.SkylarkLock;
import com.toucan.shopping.modules.stock.kafka.constant.StockMessageTopicConstant;
import com.toucan.shopping.modules.user.vo.UserBuyCarItemVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;


/**
 * 订单服务
 * @author majian
 * @date 2022-11-17 09:41:58
 */
@RestController
@RequestMapping("/api/order")
public class OrderApiController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private Toucan toucan;

    @Autowired
    private FeignOrderService feignOrderService;

    @Autowired
    private FeignProductSkuStockLockService feignProductSkuStockLockService;

    @Autowired
    private SkylarkLock skylarkLock;

    @Autowired
    private EventProcessService eventProcessService;

    @Autowired
    private OrderNoService orderNoService;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private FeignProductSkuService feignProductSkuService;

    @Autowired
    private FeignUserBuyCarService feignUserBuyCarService;

    @Autowired
    private PayService payService;

    @Autowired
    private IdGenerator idGenerator;

    /**
     * 创建订单
     * TODO:订单30分钟未支付 恢复预扣库存数量,支付宝回调刷新订单状态 如果失败创建本地事件以便事务补偿回滚
     * @param buyVo
     * @return
     */
    @UserAuth(requestType = UserAuth.REQUEST_AJAX)
    @RequestMapping(value = "/create",method = RequestMethod.POST)
    @ResponseBody
    public ResultObjectVO create(HttpServletRequest request, @RequestBody BuyVo buyVo){
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        logger.info("购买商品: param:"+ JSONObject.toJSONString(buyVo));
        if(buyVo==null)
        {
            logger.info("没有找到要购买的商品: param:"+ JSONObject.toJSONString(buyVo));
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("没有找到要购买的商品!");
            return resultObjectVO;
        }
        //锁住当前购买所有商品
        List<String> lockKeys= new ArrayList<String>();
        String userId = "-1";
        try {
            userId = UserAuthHeaderUtil.getUserMainId(request.getHeader(toucan.getUserAuth().getHttpToucanAuthHeader()));
            if("-1".equals(userId))
            {
                throw new IllegalArgumentException("登录认证失败");
            }
            //订单号
            String orderNo= orderNoService.generateOrderNo();
            String globalTransactionId = UUID.randomUUID().toString().replace("-","");
            String appCode = toucan.getAppCode();
            for(UserBuyCarItemVO buyCarItemVO : buyVo.getBuyCarItems())
            {
                if(buyCarItemVO!=null) {
                    if (buyCarItemVO.getShopProductSkuId()==null) {
                        logger.warn("sku id is null params: {} ", JSONObject.toJSONString(buyCarItemVO));
                        throw new IllegalArgumentException("没有找到商品");
                    }
                    String productBuyKey = ProductRedisKeyUtil.getProductBuyKey(appCode, String.valueOf(buyCarItemVO.getShopProductSkuId()));
                    boolean lockStatus = skylarkLock.lock(productBuyKey, userId);
                    if (!lockStatus) {
                        //释放加的所有锁
                        if(!CollectionUtils.isEmpty(lockKeys))
                        {
                            for(String lockKey:lockKeys)
                            {
                                skylarkLock.unLock(lockKey,userId);
                            }
                        }
                        resultObjectVO.setCode(ResultObjectVO.FAILD);
                        resultObjectVO.setMsg("请稍后重试");
                        return resultObjectVO;
                    }
                    lockKeys.add(productBuyKey);
                }
            }


            //===================================查询当前库中的所有购物车项
            resultObjectVO = this.queryBuyCarItems(buyVo,Long.parseLong(userId));
            if(!resultObjectVO.isSuccess())
            {
                return resultObjectVO;
            }

            //实扣库存
            List<InventoryReductionVO> realInventoryReductions = new LinkedList<>();
            //预扣库存
            List<InventoryReductionVO> predictInventoryReductions = new LinkedList<>();

            //==============================查询商品
            List<ProductSkuVO> productSkuVOS = new LinkedList<>();
            for(UserBuyCarItemVO userBuyCarItemVO:buyVo.getBuyCarItems())
            {
                ProductSkuVO productSkuVO = new ProductSkuVO();
                productSkuVO.setId(userBuyCarItemVO.getShopProductSkuId());
                productSkuVOS.add(productSkuVO);
            }
            if(CollectionUtils.isEmpty(productSkuVOS))
            {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("请稍后重试!");
                return resultObjectVO;
            }
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generatorByUser(appCode,userId,productSkuVOS);
            resultObjectVO = feignProductSkuService.queryByIdList(SignUtil.sign(appCode,requestJsonVO.getEntityJson()),requestJsonVO);
            if(!resultObjectVO.isSuccess())
            {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("请稍后重试!");
                return resultObjectVO;
            }

            List<ProductSkuVO> queryProductSkuList = resultObjectVO.formatDataList(ProductSkuVO.class);
            if(CollectionUtils.isEmpty(queryProductSkuList))
            {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("请稍后重试!");
                return resultObjectVO;
            }

            //判断商品下架
            for (ProductSkuVO productSku : queryProductSkuList) {
                if(productSku.getStatus().intValue()==0) {
                    resultObjectVO.setCode(ResultVO.FAILD);
                    resultObjectVO.setMsg(productSku.getName() + " 已下架");
                    return resultObjectVO;
                }
            }

            //判断商品数量
            for (ProductSkuVO productSku : queryProductSkuList) {
                if (productSku.getStockNum().intValue() <= 0) {
                    resultObjectVO.setCode(ResultVO.FAILD);
                    resultObjectVO.setMsg(productSku.getName()+" 库存不足");
                    return resultObjectVO;
                }
                for(UserBuyCarItemVO userBuyCarItemVO:buyVo.getBuyCarItems())
                {
                    if(productSku.getId().longValue()==userBuyCarItemVO.getShopProductSkuId().longValue())
                    {
                        //购买数量大于库存数量
                        if(userBuyCarItemVO.getBuyCount().intValue()>productSku.getStockNum().intValue()) {
                            resultObjectVO.setCode(ResultVO.FAILD);
                            resultObjectVO.setMsg(productSku.getName()+" 库存不足");
                            return resultObjectVO;
                        }

                        //减库存对象
                        InventoryReductionVO inventoryReductionProduct = new InventoryReductionVO();
                        inventoryReductionProduct.setAppCode(toucan.getAppCode());
                        inventoryReductionProduct.setProductSkuId(userBuyCarItemVO.getShopProductSkuId());
                        inventoryReductionProduct.setUserId(userId);
                        inventoryReductionProduct.setStockNum(userBuyCarItemVO.getBuyCount()); //减库存数量

                        //拍下减库存
                        if(productSku.getBuckleInventoryMethod().intValue()==1) {
                            inventoryReductionProduct.setType((short)2);
                            realInventoryReductions.add(inventoryReductionProduct); //实扣库存
                        }else{ //支付减库存
                            inventoryReductionProduct.setType((short)1);
                            predictInventoryReductions.add(inventoryReductionProduct); //预扣库存
                        }
                        break;
                    }

                }
            }


            this.recalculateProductPrice(buyVo);

            List<EventProcess> restoreStock = new ArrayList<EventProcess>();
            //创建本地补偿事务消息

            //实际扣库存
            for (InventoryReductionVO inventoryReductionVO : realInventoryReductions) {

                //还原库存对象
                RestoreStockVO restoreStockVo = new RestoreStockVO();
                restoreStockVo.setAppCode(appCode);
                restoreStockVo.setUserId(userId);
                restoreStockVo.setInventoryReductionVO(inventoryReductionVO);

                EventProcess eventProcess = new EventProcess();
                eventProcess.setId(idGenerator.id());
                eventProcess.setCreateDate(new Date());
                eventProcess.setBusinessId("");
                eventProcess.setRemark("还原实扣库存");
                eventProcess.setTableName(null);
                eventProcess.setTransactionId(globalTransactionId);
                eventProcess.setPayload(JSONObject.toJSONString(restoreStockVo));
                eventProcess.setStatus((short) 0); //待处理
                eventProcess.setType(StockMessageTopicConstant.restore_stock.name());
                eventProcessService.insert(eventProcess);

                restoreStock.add(eventProcess);
            }

            //预计扣库存
            for (InventoryReductionVO inventoryReductionVO : predictInventoryReductions) {

                //还原库存对象
                RestoreStockVO restoreStockVo = new RestoreStockVO();
                restoreStockVo.setAppCode(appCode);
                restoreStockVo.setUserId(userId);
                restoreStockVo.setInventoryReductionVO(inventoryReductionVO);

                EventProcess eventProcess = new EventProcess();
                eventProcess.setId(idGenerator.id());
                eventProcess.setCreateDate(new Date());
                eventProcess.setBusinessId("");
                eventProcess.setRemark("还原预扣库存");
                eventProcess.setTableName(null);
                eventProcess.setTransactionId(globalTransactionId);
                eventProcess.setPayload(JSONObject.toJSONString(restoreStockVo));
                eventProcess.setStatus((short) 0); //待处理
                eventProcess.setType(StockMessageTopicConstant.restore_redis_stock.name());
                eventProcessService.insert(eventProcess);

                restoreStock.add(eventProcess);
            }

            //预扣库存
            requestJsonVO = RequestJsonVOGenerator.generatorByUser(appCode,userId,realInventoryReductions);
            logger.info("开始预扣库存 {}",JSONObject.toJSONString(requestJsonVO));
//            resultObjectVO = feignProductSkuStockLockService.deductCacheStock(SignUtil.sign(appCode,requestJsonVO.getEntityJson()),requestJsonVO);
//
//
//
//            //扣库存成功后创建订单
//            requestJsonVO = RequestJsonVOGenerator.generatorByUser(appCode, userId, inventoryReductionVo);
//            CreateOrderVo createOrderVo = new CreateOrderVo();
//            createOrderVo.setAppCode(appCode);
//            createOrderVo.setUserId(userId);
//            createOrderVo.setOrderNo(orderNo);
//            createOrderVo.setPayMethod(1);
//            createOrderVo.setProductSkuList(buyVo.getProductSkuList());
//            createOrderVo.setBuyMap(buyVo.getBuyMap());
//            requestJsonVO.setEntityJson(JSONObject.toJSONString(createOrderVo));
//
//            resultObjectVO = feignOrderService.create(SignUtil.sign(appCode, requestJsonVO.getEntityJson()), requestJsonVO);
//
//            //如果订单创建成功 将上面回滚事件取消掉
//            if (resultObjectVO.getCode().intValue() == ResultVO.SUCCESS.intValue()) {
//                for (EventProcess eventProcess : restoreStock) {
//                    eventProcess.setStatus((short) 1);
//                    eventProcessService.updateStatus(eventProcess);
//                }
//            }

//            //订单创建失败 将回滚库存
//            if (resultObjectVO.getCode().intValue() == ResultVO.FAILD.intValue()) {
//
//                for (EventProcess eventProcess : restoreStock) {
//                    ResultObjectVO restoreStockResultObjectVO = feignProductSkuStockLockService.restoreStock(JSONObject.parseObject(eventProcess.getPayload(), RequestJsonVO.class));
//                    if (restoreStockResultObjectVO.getCode().intValue() == ResultVO.SUCCESS.intValue()) {
//                        eventProcess.setStatus((short) 1);
//                        eventProcessService.updateStatus(eventProcess);
//                    }
//                }
//            }

        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);
            //TODO:回滚数据

            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("购买失败,请重试!");
        }finally{   //上面做return 不会影响到这个锁的释放
            //释放所有商品锁
            for(String lockKey:lockKeys) {
                skylarkLock.unLock(lockKey, userId);
            }
        }

        return resultObjectVO;
    }

    /**
     * 查询库中所有购物车项
     * @param buyVo
     * @return
     */
    private ResultObjectVO queryBuyCarItems(BuyVo buyVo,Long userId)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try{
            UserBuyCarItemVO userBuyCarVO = new UserBuyCarItemVO();
            userBuyCarVO.setUserMainId(userId);
            resultObjectVO = feignUserBuyCarService.listByUserMainId(RequestJsonVOGenerator.generator(toucan.getAppCode(),userBuyCarVO));
            if(!resultObjectVO.isSuccess())
            {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("请稍后重试!");
                return resultObjectVO;
            }
            //当前购物车的所有项
            List<UserBuyCarItemVO> userBuyCarVOList = resultObjectVO.formatDataList(UserBuyCarItemVO.class);
            if(CollectionUtils.isEmpty(userBuyCarVOList))
            {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("请稍后重试!");
                return resultObjectVO;
            }

            //以前端的购买数量为准
            for(UserBuyCarItemVO userBuyCarItemVO:userBuyCarVOList)
            {
                for(UserBuyCarItemVO frontBuyCarItem : buyVo.getBuyCarItems())
                {
                    if(userBuyCarItemVO.getId().longValue()==frontBuyCarItem.getId().longValue())
                    {
                        userBuyCarItemVO.setBuyCount(frontBuyCarItem.getBuyCount());
                    }
                }
            }
            //将数据库中的购物车项设置进去
            buyVo.setBuyCarItems(userBuyCarVOList);
        }catch(Exception e)
        {
            resultObjectVO.setMsg("请稍后重试!");
            resultObjectVO.setCode(ResultVO.FAILD);
        }
        return resultObjectVO;
    }

    /**
     * 重新计算商品价格
     */
    private void recalculateProductPrice(BuyVo buyVo)
    {

    }


    /**
     * 调用支付宝
     * @param payVo
     * @return
     */
    @RequestMapping(value="/alipay",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO pay(@RequestBody PayVo payVo){
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        if(payVo==null)
        {
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("支付失败,请重试!");
            return resultObjectVO;
        }
        logger.info("支付订单: param:"+ JSONObject.toJSONString(payVo));

        try {
            String appCode = toucan.getAppCode();
            //调用第三方,传进去callback接口
            payService.orderPay(null);
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);

            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("支付失败,请重试!");
        }

        return resultObjectVO;
    }


    /**
     * 支付宝回调刷新本地订单状态 实扣库存等
     * @param request
     * @return
     */
    @RequestMapping(value="/alipay/callback")
    @ResponseBody
    public ResultObjectVO buy(HttpServletRequest request){
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        Map<String, String> params = HttpParamUtil.convertRequestParamsToMap(request); // 将异步通知中收到的待验证所有参数都存放到map中
        String paramsJson = JSON.toJSONString(params);
        logger.info("支付宝支付回调: param {}",paramsJson);

        try {
//            AlipayConfig alipayConfig = new AlipayConfig();// 支付宝配置
//            // 调用SDK验证签名
//            boolean signVerified = AlipaySignature.rsaCheckV1(params, alipayConfig.getAlipay_public_key(),
//                    alipayConfig.getCharset(), alipayConfig.getSigntype());
//            if (signVerified) {
//                logger.info("支付宝回调签名认证成功");
//                // 按照支付结果异步通知中的描述，对支付结果中的业务内容进行1\2\3\4二次校验，校验成功后在response中返回success，校验失败返回failure
//                this.check(params);
//                // 另起线程处理业务
//                executorService.execute(new Runnable() {
//                    @Override
//                    public void run() {
//                        AlipayNotifyParam param = buildAlipayNotifyParam(params);
//                        String trade_status = param.getTradeStatus();
//                        // 支付成功
//                        if (trade_status.equals(AlipayTradeStatus.TRADE_SUCCESS.getStatus())
//                                || trade_status.equals(AlipayTradeStatus.TRADE_FINISHED.getStatus())) {
//                            // 处理支付成功逻辑
//                            try {
            PayVo payVo = JSONObject.parseObject(paramsJson,PayVo.class);
            Order order = new Order();
            order.setOrderNo(payVo.getOrderNo());
            order.setUserId(payVo.getUserId());
            order.setPayDate(new Date());
            order.setPayStatus(1);
            order.setTradeStatus(3);
            order.setOuterTradeNo("支付宝交易流水号");
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generatorByUser(toucan.getAppCode(),payVo.getUserId(),order);
            //完成订单
            resultObjectVO = feignOrderService.finish(SignUtil.sign(toucan.getAppCode(),requestJsonVO.getEntityJson()),requestJsonVO);

            if(resultObjectVO.getCode().intValue()== ResultVO.SUCCESS.intValue()) {
                String appCode = toucan.getAppCode();
                QueryOrderVo queryOrderVo = new QueryOrderVo();
                queryOrderVo.setUserId(payVo.getUserId());
                queryOrderVo.setOrderNo(payVo.getOrderNo());

                requestJsonVO = RequestJsonVOGenerator.generatorByUser(toucan.getAppCode(), payVo.getUserId(), queryOrderVo);
                resultObjectVO = feignOrderService.querySkuUuidsByOrderNo(SignUtil.sign(toucan.getAppCode(),requestJsonVO.getEntityJson()),requestJsonVO);

                if (resultObjectVO.getCode().intValue() == ResultVO.SUCCESS.intValue()) {
                    //实扣库存对象
                    InventoryReductionVO inventoryReductionVo = new InventoryReductionVO();
                    inventoryReductionVo.setAppCode(appCode);
                    inventoryReductionVo.setUserId(payVo.getUserId());
                    List<ProductSku> productSkus = JSONArray.parseArray(JSONArray.toJSONString(resultObjectVO.getData()), ProductSku.class);
//                    inventoryReductionVo.setProductSkuList(productSkus);

                    requestJsonVO = RequestJsonVOGenerator.generatorByUser(appCode, payVo.getUserId(), inventoryReductionVo);
                    resultObjectVO = feignProductSkuStockLockService.inventoryReduction(SignUtil.sign(appCode, requestJsonVO.getEntityJson()), requestJsonVO);

                    resultObjectVO.setMsg("支付完成!");
                }
            }
//
//                            } catch (Exception e) {
//                                logger.error("支付宝回调业务处理报错,params:" + paramsJson, e);
//                            }
//                        } else {
//                            logger.error("没有处理支付宝回调业务，支付宝交易状态：{},params:{}",trade_status,paramsJson);
//                        }
//                    }
//                });
//            } else {
//                logger.info("支付宝回调签名认证失败，signVerified=false, paramsJson:{}", paramsJson);
//                resultObjectVO.setCode(ResultVO.FAILD);
//                resultObjectVO.setMsg("支付宝回调签名认证失败,请重试!");
//            }
        } catch (Exception e) {
            logger.warn(e.getMessage(),e);

            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("支付失败,请重试!");
        }

        return resultObjectVO;
    }

}
