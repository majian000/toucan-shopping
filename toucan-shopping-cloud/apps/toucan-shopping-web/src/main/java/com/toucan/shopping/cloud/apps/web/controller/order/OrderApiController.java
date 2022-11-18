package com.toucan.shopping.cloud.apps.web.controller.order;

import com.alibaba.fastjson.JSONObject;
import com.toucan.shopping.cloud.apps.web.vo.BuyFailProductVo;
import com.toucan.shopping.cloud.apps.web.vo.BuyVo;
import com.toucan.shopping.cloud.order.api.feign.service.FeignOrderService;
import com.toucan.shopping.cloud.product.api.feign.service.FeignProductSkuService;
import com.toucan.shopping.cloud.stock.api.feign.service.FeignProductSkuStockService;
import com.toucan.shopping.cloud.user.api.feign.service.FeignUserBuyCarService;
import com.toucan.shopping.modules.auth.user.UserAuth;
import com.toucan.shopping.modules.common.generator.RequestJsonVOGenerator;
import com.toucan.shopping.modules.common.persistence.event.entity.EventProcess;
import com.toucan.shopping.modules.common.persistence.event.service.EventProcessService;
import com.toucan.shopping.modules.common.properties.Toucan;
import com.toucan.shopping.modules.common.util.SignUtil;
import com.toucan.shopping.modules.common.util.UserAuthHeaderUtil;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.common.vo.ResultVO;
import com.toucan.shopping.modules.order.no.OrderNoService;
import com.toucan.shopping.modules.product.constant.ProductConstant;
import com.toucan.shopping.modules.product.entity.ProductSku;
import com.toucan.shopping.modules.product.util.ProductRedisKeyUtil;
import com.toucan.shopping.modules.product.vo.ProductSkuVO;
import com.toucan.shopping.modules.skylark.lock.service.SkylarkLock;
import com.toucan.shopping.modules.stock.kafka.constant.StockMessageTopicConstant;
import com.toucan.shopping.modules.product.vo.RestoreStockVo;
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
    private FeignProductSkuStockService feignProductSkuStockService;

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

    /**
     * 创建订单
     * TODO:订单30分钟未支付 恢复预扣库存数量,支付宝回调刷新订单状态 如果失败创建本地事件以便事务补偿回滚
     * @param buyVo
     * @return
     */
    @UserAuth(requestType = UserAuth.REQUEST_AJAX)
    @RequestMapping(value = "/create",method = RequestMethod.POST)
    @ResponseBody
    public ResultObjectVO buy(HttpServletRequest request, @RequestBody BuyVo buyVo){
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

            //可购买商品列表
            List<ProductSku> releaseProductSkuList = new ArrayList<ProductSku>();
            //库存不够商品列表
            List<BuyFailProductVo> buyFailProductVos = new ArrayList<BuyFailProductVo>();
            //购买商品信息
            List<Map> buyProductItems = new LinkedList<>();


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
                    BuyFailProductVo buyFailProductVo = new BuyFailProductVo();
                    buyFailProductVo.setProductSkuVO(productSku);
                    buyFailProductVo.setFailCode(ProductConstant.SOLD_OUT);
                    buyFailProductVo.setFailMsg(productSku.getName() + " 已下架");
                    buyFailProductVos.add(buyFailProductVo);
                    break;
                }
            }
            if(!CollectionUtils.isEmpty(buyFailProductVos))
            {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("请稍后重试!");
                resultObjectVO.setData(buyFailProductVos);
                return resultObjectVO;
            }

            //判断商品数量
            boolean buyStatus=true; //是否可以购买
            for (ProductSkuVO productSku : queryProductSkuList) {
                if (productSku.getStockNum().intValue() <= 0) {
                    BuyFailProductVo buyFailProductVo = new BuyFailProductVo();
                    buyFailProductVo.setProductSkuVO(productSku);
                    buyFailProductVo.setFailCode(ProductConstant.NO_STOCK);
                    buyFailProductVo.setFailMsg(productSku.getName()+" 库存不足");
                    buyFailProductVos.add(buyFailProductVo);
                    continue;
                }
                for(UserBuyCarItemVO userBuyCarItemVO:buyVo.getBuyCarItems())
                {
                    if(productSku.getId().longValue()==userBuyCarItemVO.getShopProductSkuId().longValue())
                    {
                        //购买数量大于库存数量
                        if(userBuyCarItemVO.getBuyCount().intValue()>productSku.getStockNum().intValue()) {
                            buyStatus = false;
                            BuyFailProductVo buyFailProductVo = new BuyFailProductVo();
                            buyFailProductVo.setProductSkuVO(productSku);
                            buyFailProductVo.setFailCode(ProductConstant.NO_STOCK);
                            buyFailProductVo.setFailMsg(productSku.getName()+" 库存不足");
                            buyFailProductVos.add(buyFailProductVo);
                        }

                        //拍下减库存
                        if(productSku.getBuckleInventoryMethod().intValue()==1) {
                            Map<String, Object> buyItem = new HashMap<>();
                            buyItem.put("productName", productSku.getName());
                            buyItem.put("buyCount", userBuyCarItemVO.getBuyCount());
                            buyItem.put("price", productSku.getPrice());
                            buyItem.put("freightTemplate", userBuyCarItemVO.getFreightTemplateVO());
                            buyProductItems.add(buyItem);
                        }
                        break;
                    }
                }
                if(buyStatus) {
                    releaseProductSkuList.add(productSku);
                }
            }

            if(!CollectionUtils.isEmpty(buyFailProductVos))
            {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("请稍后重试!");
                resultObjectVO.setData(buyFailProductVos);
                return resultObjectVO;
            }


            this.recalculateProductPrice(buyVo);

            List<EventProcess> restoreStock = new ArrayList<EventProcess>();
            //创建本地补偿事务消息
            for (UserBuyCarItemVO userBuyCarItemVO : buyVo.getBuyCarItems()) {

                //还原库存对象
                RestoreStockVo restoreStockVo = new RestoreStockVo();
                restoreStockVo.setAppCode(appCode);
                restoreStockVo.setUserId(userId);
                restoreStockVo.setBuyProductItems(buyProductItems);  //当前购买的商品列表包含:购买数量、选择的运费、商品名称、单价等
                requestJsonVO = RequestJsonVOGenerator.generatorByUser(appCode, userId, restoreStockVo);

                EventProcess eventProcess = new EventProcess();
                eventProcess.setCreateDate(new Date());
                eventProcess.setBusinessId(String.valueOf(userBuyCarItemVO.getId()));
                eventProcess.setRemark("还原预扣库存");
                eventProcess.setTableName(null);
                eventProcess.setTransactionId(globalTransactionId);
                eventProcess.setPayload(JSONObject.toJSONString(requestJsonVO));
                eventProcess.setStatus((short) 0); //待处理
                eventProcess.setType(StockMessageTopicConstant.restore_redis_stock.name());
                eventProcessService.insert(eventProcess);

                restoreStock.add(eventProcess);
            }

//            //预扣库存对象
//            InventoryReductionVo inventoryReductionVo = new InventoryReductionVo();
//            inventoryReductionVo.setAppCode(appCode);
//            inventoryReductionVo.setUserId(userId);
//            inventoryReductionVo.setProductSkuList(buyVo.getProductSkuList());
//            //预扣库存
//            logger.info("开始预扣库存 {}",JSONObject.toJSONString(requestJsonVO));
//            requestJsonVO = RequestJsonVOGenerator.generatorByUser(appCode,userId,buyVo.getProductSkuList());
//            resultObjectVO = feignProductSkuStockService.deductCacheStock(SignUtil.sign(appCode,requestJsonVO.getEntityJson()),requestJsonVO);
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
//                    ResultObjectVO restoreStockResultObjectVO = feignProductSkuStockService.restoreStock(JSONObject.parseObject(eventProcess.getPayload(), RequestJsonVO.class));
//                    if (restoreStockResultObjectVO.getCode().intValue() == ResultVO.SUCCESS.intValue()) {
//                        eventProcess.setStatus((short) 1);
//                        eventProcessService.updateStatus(eventProcess);
//                    }
//                }
//            }

        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);

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

}
