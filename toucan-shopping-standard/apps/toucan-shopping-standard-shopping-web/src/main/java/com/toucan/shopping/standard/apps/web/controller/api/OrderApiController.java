package com.toucan.shopping.standard.apps.web.controller.api;

import com.toucan.shopping.modules.common.lock.redis.RedisLock;
import com.toucan.shopping.modules.common.persistence.event.service.EventProcessService;
import com.toucan.shopping.modules.common.properties.Toucan;
import com.toucan.shopping.modules.order.no.OrderNoService;
import com.toucan.shopping.modules.order.service.OrderService;
import com.toucan.shopping.modules.product.service.ProductSkuService;
import com.toucan.shopping.modules.stock.service.ProductSkuStockLockService;
import com.toucan.shopping.standard.apps.web.service.PayService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/order")
public class OrderApiController {

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

    @Autowired
    private PayService payService;


//    /**
//     * 调用支付宝
//     * @param payVo
//     * @return
//     */
//    @RequestMapping(value="/alipay",produces = "application/json;charset=UTF-8")
//    @ResponseBody
//    public ResultObjectVO pay(@RequestBody PayVo payVo){
//        ResultObjectVO resultObjectVO = new ResultObjectVO();
//        if(payVo==null)
//        {
//            resultObjectVO.setCode(ResultVO.FAILD);
//            resultObjectVO.setMsg("支付失败,请重试!");
//            return resultObjectVO;
//        }
//        logger.info("支付订单: param:"+ JSONObject.toJSONString(payVo));
//
//        try {
//            String appCode = toucan.getAppCode();
//            //调用第三方,传进去callback接口
//            payService.orderPay(null);
//        }catch(Exception e)
//        {
//            logger.warn(e.getMessage(),e);
//
//            resultObjectVO.setCode(ResultVO.FAILD);
//            resultObjectVO.setMsg("支付失败,请重试!");
//        }
//
//        return resultObjectVO;
//    }
//
//
//    /**
//     * 支付宝回调刷新本地订单状态 实扣库存等
//     * @param request
//     * @return
//     */
//    @RequestMapping(value="/alipay/callback")
//    @ResponseBody
//    public ResultObjectVO buy(HttpServletRequest request){
//        ResultObjectVO resultObjectVO = new ResultObjectVO();
//        Map<String, String> params = HttpParamUtil.convertRequestParamsToMap(request); // 将异步通知中收到的待验证所有参数都存放到map中
//        String paramsJson = JSON.toJSONString(params);
//        logger.info("支付宝支付回调: param {}",paramsJson);
//
//        try {
////            AlipayConfig alipayConfig = new AlipayConfig();// 支付宝配置
////            // 调用SDK验证签名
////            boolean signVerified = AlipaySignature.rsaCheckV1(params, alipayConfig.getAlipay_public_key(),
////                    alipayConfig.getCharset(), alipayConfig.getSigntype());
////            if (signVerified) {
////                logger.info("支付宝回调签名认证成功");
////                // 按照支付结果异步通知中的描述，对支付结果中的业务内容进行1\2\3\4二次校验，校验成功后在response中返回success，校验失败返回failure
////                this.check(params);
////                // 另起线程处理业务
////                executorService.execute(new Runnable() {
////                    @Override
////                    public void run() {
////                        AlipayNotifyParam param = buildAlipayNotifyParam(params);
////                        String trade_status = param.getTradeStatus();
////                        // 支付成功
////                        if (trade_status.equals(AlipayTradeStatus.TRADE_SUCCESS.getStatus())
////                                || trade_status.equals(AlipayTradeStatus.TRADE_FINISHED.getStatus())) {
////                            // 处理支付成功逻辑
////                            try {
//                                PayVo payVo = JSONObject.parseObject(paramsJson,PayVo.class);
//                                Order order = new Order();
//                                order.setOrderNo(payVo.getOrderNo());
//                                order.setUserId(payVo.getUserId());
//                                order.setPayDate(new Date());
//                                order.setPayStatus(1);
//                                order.setTradeStatus(3);
//                                order.setOuterTradeNo("支付宝交易流水号");
//                                RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generatorByUser(toucan.getAppCode(),payVo.getUserId(),order);
//                                //完成订单
//                                resultObjectVO = orderService.finish(SignUtil.sign(toucan.getAppCode(),requestJsonVO.getEntityJson()),requestJsonVO);
//
//                                if(resultObjectVO.getCode().intValue()== ResultVO.SUCCESS.intValue()) {
//                                    String appCode = toucan.getAppCode();
//                                    QueryOrderVo queryOrderVo = new QueryOrderVo();
//                                    queryOrderVo.setUserId(payVo.getUserId());
//                                    queryOrderVo.setOrderNo(payVo.getOrderNo());
//
//                                    requestJsonVO = RequestJsonVOGenerator.generatorByUser(toucan.getAppCode(), payVo.getUserId(), queryOrderVo);
//                                    resultObjectVO = orderService.querySkuUuidsByOrderNo(SignUtil.sign(toucan.getAppCode(),requestJsonVO.getEntityJson()),requestJsonVO);
//
//                                    if (resultObjectVO.getCode().intValue() == ResultVO.SUCCESS.intValue()) {
//                                        //实扣库存对象
//                                        InventoryReductionVo inventoryReductionVo = new InventoryReductionVo();
//                                        inventoryReductionVo.setAppCode(appCode);
//                                        inventoryReductionVo.setUserId(payVo.getUserId());
//                                        List<ProductSku> productSkus = JSONArray.parseArray(JSONArray.toJSONString(resultObjectVO.getData()), ProductSku.class);
//                                        inventoryReductionVo.setProductSkuList(productSkus);
//
//                                        requestJsonVO = RequestJsonVOGenerator.generatorByUser(appCode, payVo.getUserId(), inventoryReductionVo);
//                                        resultObjectVO = productSkuStockLockService.inventoryReduction(SignUtil.sign(appCode, requestJsonVO.getEntityJson()), requestJsonVO);
//
//                                        resultObjectVO.setMsg("支付完成!");
//                                    }
//                                }
////
////                            } catch (Exception e) {
////                                logger.error("支付宝回调业务处理报错,params:" + paramsJson, e);
////                            }
////                        } else {
////                            logger.error("没有处理支付宝回调业务，支付宝交易状态：{},params:{}",trade_status,paramsJson);
////                        }
////                    }
////                });
////            } else {
////                logger.info("支付宝回调签名认证失败，signVerified=false, paramsJson:{}", paramsJson);
////                resultObjectVO.setCode(ResultVO.FAILD);
////                resultObjectVO.setMsg("支付宝回调签名认证失败,请重试!");
////            }
//        } catch (Exception e) {
//            logger.warn(e.getMessage(),e);
//
//            resultObjectVO.setCode(ResultVO.FAILD);
//            resultObjectVO.setMsg("支付失败,请重试!");
//        }
//
//        return resultObjectVO;
//    }


}
