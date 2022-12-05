package com.toucan.shopping.cloud.apps.web.controller.order;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.toucan.shopping.cloud.apps.web.service.PayService;
import com.toucan.shopping.cloud.seller.api.feign.service.FeignFreightTemplateService;
import com.toucan.shopping.cloud.user.api.feign.service.FeignConsigneeAddressService;
import com.toucan.shopping.modules.order.exception.CreateOrderException;
import com.toucan.shopping.modules.order.vo.*;
import com.toucan.shopping.cloud.apps.web.vo.PayVo;
import com.toucan.shopping.cloud.order.api.feign.service.FeignOrderService;
import com.toucan.shopping.cloud.product.api.feign.service.FeignProductSkuService;
import com.toucan.shopping.cloud.stock.api.feign.service.FeignProductSkuStockLockService;
import com.toucan.shopping.cloud.user.api.feign.service.FeignUserBuyCarService;
import com.toucan.shopping.modules.auth.user.UserAuth;
import com.toucan.shopping.modules.common.generator.IdGenerator;
import com.toucan.shopping.modules.common.generator.RequestJsonVOGenerator;
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
import com.toucan.shopping.modules.product.entity.ProductSku;
import com.toucan.shopping.modules.product.util.ProductRedisKeyUtil;
import com.toucan.shopping.modules.product.vo.InventoryReductionVO;
import com.toucan.shopping.modules.product.vo.ProductSkuVO;
import com.toucan.shopping.modules.seller.vo.FreightTemplateVO;
import com.toucan.shopping.modules.skylark.lock.service.SkylarkLock;
import com.toucan.shopping.modules.stock.vo.ProductSkuStockLockVO;
import com.toucan.shopping.modules.user.entity.ConsigneeAddress;
import com.toucan.shopping.modules.user.vo.ConsigneeAddressVO;
import com.toucan.shopping.modules.user.vo.UserBuyCarItemVO;
import com.toucan.shopping.modules.user.vo.freightTemplate.UBCIFreightTemplateAreaRuleVO;
import com.toucan.shopping.modules.user.vo.freightTemplate.UBCIFreightTemplateDefaultRuleVO;
import com.toucan.shopping.modules.user.vo.freightTemplate.UBCIFreightTemplateVO;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;


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

    @Autowired
    private FeignFreightTemplateService feignFreightTemplateService;

    @Autowired
    private FeignConsigneeAddressService feignConsigneeAddressService;

    /**
     * 创建订单
     * TODO:订单30分钟未支付 恢复预扣库存数量,支付宝回调刷新订单状态 如果失败创建本地事件以便事务补偿回滚
     * @param createOrderVO
     * @return
     */
    @UserAuth(requestType = UserAuth.REQUEST_AJAX)
    @RequestMapping(value = "/create",method = RequestMethod.POST)
    @ResponseBody
    public ResultObjectVO create(HttpServletRequest request, @RequestBody CreateOrderVO createOrderVO){
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        if(createOrderVO ==null)
        {
            logger.info("没有找到要购买的商品: param:"+ JSONObject.toJSONString(createOrderVO));
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("没有找到要购买的商品!");
            return resultObjectVO;
        }
        if(createOrderVO.getConsigneeAddress()==null||createOrderVO.getConsigneeAddress().getId()==null)
        {
            logger.info("收货人信息不能为空: param:"+ JSONObject.toJSONString(createOrderVO));
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("收货人信息不能为空!");
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
            createOrderVO.setUserId(userId);
            //主订单
            String orderNo= orderNoService.generateOrderNo();
            MainOrderVO mainOrderVO = new MainOrderVO();
            mainOrderVO.setId(idGenerator.id());
            mainOrderVO.setOrderNo(orderNo);
            mainOrderVO.setUserId(userId);
            mainOrderVO.setAppCode(toucan.getAppCode());
            mainOrderVO.setCreateDate(new Date());
            createOrderVO.setMainOrder(mainOrderVO);
            String globalTransactionId = UUID.randomUUID().toString().replace("-","");
            String appCode = toucan.getAppCode();
            //商品加锁
            for(UserBuyCarItemVO buyCarItemVO : createOrderVO.getBuyCarItems())
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
            resultObjectVO = this.queryBuyCarItems(createOrderVO,Long.parseLong(userId));
            if(!resultObjectVO.isSuccess())
            {
                return resultObjectVO;
            }

            //商品库存锁定对象
            List<ProductSkuStockLockVO> productSkuStockLocks = new LinkedList<>();
            //商品扣库存对象
            List<InventoryReductionVO> inventoryReductions= new LinkedList<>();

            //==============================查询商品
            List<ProductSkuVO> productSkuVOS = new LinkedList<>();
            for(UserBuyCarItemVO userBuyCarItemVO: createOrderVO.getBuyCarItems())
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
            //判断购物车中的商品是否被删除了
            boolean isProductIsDel = true;
            for(UserBuyCarItemVO userBuyCarItemVO: createOrderVO.getBuyCarItems())
            {
                isProductIsDel = true;
                for (ProductSkuVO productSku : queryProductSkuList) {
                    if(userBuyCarItemVO.getShopProductSkuId().longValue()==productSku.getId().longValue())
                    {
                        isProductIsDel = false;
                        break;
                    }
                }
                if(isProductIsDel)
                {
                    resultObjectVO.setCode(ResultVO.FAILD);
                    resultObjectVO.setMsg(userBuyCarItemVO.getProductSkuName() + " 已下架");
                    return resultObjectVO;
                }
            }


            //======================查询商品锁定库存
            ProductSkuStockLockVO queryProductSkuStockLockVO = new ProductSkuStockLockVO();
            queryProductSkuStockLockVO.setProductSkuIdList(createOrderVO.getBuyCarItems().stream().map(UserBuyCarItemVO::getShopProductSkuId).collect(Collectors.toList()));
            requestJsonVO = RequestJsonVOGenerator.generatorByUser(appCode,userId,queryProductSkuStockLockVO);
            resultObjectVO = feignProductSkuStockLockService.findLockStockNumByProductSkuIds(requestJsonVO);
            if(!resultObjectVO.isSuccess())
            {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("查询库存失败");
                return resultObjectVO;
            }

            List<ProductSkuStockLockVO> productSkuStockLockVOS = resultObjectVO.formatDataList(ProductSkuStockLockVO.class);
            if(!CollectionUtils.isEmpty(productSkuStockLockVOS))
            {
                for(ProductSkuStockLockVO productSkuStockLockVO:productSkuStockLockVOS)
                {
                    for(UserBuyCarItemVO userBuyCarItemVO: createOrderVO.getBuyCarItems())
                    {
                        if(productSkuStockLockVO.getProductSkuId().longValue()==userBuyCarItemVO.getShopProductSkuId().longValue())
                        {
                            userBuyCarItemVO.setLockStockNum(productSkuStockLockVO.getStockNum());
                            break;
                        }
                    }
                }
            }

            Date orderCreateDate = new Date();
            //查询运费模板
            List<Long> freightTemplateIdList = new LinkedList<>();
            //判断商品数量
            for (ProductSkuVO productSku : queryProductSkuList) {
                if (productSku.getStockNum().intValue() <= 0) {
                    resultObjectVO.setCode(ResultVO.FAILD);
                    resultObjectVO.setMsg(productSku.getName()+" 库存不足");
                    return resultObjectVO;
                }
                freightTemplateIdList.add(productSku.getFreightTemplateId());
                for(UserBuyCarItemVO userBuyCarItemVO: createOrderVO.getBuyCarItems())
                {
                    if(productSku.getId().longValue()==userBuyCarItemVO.getShopProductSkuId().longValue())
                    {
                        //可购买数量=当前库存数-锁定库存数
                        if(productSku.getStockNum().intValue()-userBuyCarItemVO.getLockStockNum().intValue()<userBuyCarItemVO.getBuyCount().intValue()) {
                            resultObjectVO.setCode(ResultVO.FAILD);
                            resultObjectVO.setMsg(productSku.getName()+" 库存不足");
                            return resultObjectVO;
                        }

                        //设置商品单价
                        userBuyCarItemVO.setProductPrice(productSku.getPrice());
                        //设置商品重量
                        userBuyCarItemVO.setRoughWeight(productSku.getRoughWeight());
                        userBuyCarItemVO.setSuttle(productSku.getSuttle());

                        //设置运费模板
                        userBuyCarItemVO.setFreightTemplateId(productSku.getFreightTemplateId());
                        userBuyCarItemVO.setShopId(productSku.getShopId());

                        //锁库存对象
                        ProductSkuStockLockVO productSkuStockLockVO = new ProductSkuStockLockVO();
                        productSkuStockLockVO.setOrderNo(orderNo);
                        productSkuStockLockVO.setAppCode(toucan.getAppCode());
                        productSkuStockLockVO.setProductSkuId(userBuyCarItemVO.getShopProductSkuId());
                        productSkuStockLockVO.setUserMainId(Long.parseLong(userId));
                        productSkuStockLockVO.setOrderCreateDate(orderCreateDate);
                        productSkuStockLockVO.setPayStatus((short)0);
                        productSkuStockLockVO.setStockNum(userBuyCarItemVO.getBuyCount()); //减库存数量
                        productSkuStockLockVO.setRemark(productSku.getName()+" 锁库存数量:"+userBuyCarItemVO.getBuyCount());
                        productSkuStockLockVO.setCreateDate(new Date());

                        if(productSku.getBuckleInventoryMethod().intValue()==1) { //拍下减库存
                            productSkuStockLockVO.setType((short)1); //实扣库存

                            //扣库存对象
                            InventoryReductionVO inventoryReductionVO = new InventoryReductionVO();
                            inventoryReductionVO.setProductSkuId(userBuyCarItemVO.getShopProductSkuId());
                            inventoryReductionVO.setStockNum(userBuyCarItemVO.getBuyCount());
                            inventoryReductionVO.setUserId(userId);
                            inventoryReductions.add(inventoryReductionVO);

                        }else{ //支付减库存
                            productSkuStockLockVO.setType((short)2); //预扣库存
                        }
                        productSkuStockLocks.add(productSkuStockLockVO);


                        break;
                    }

                }
            }

            //================查询运费模板
            if(CollectionUtils.isEmpty(freightTemplateIdList))
            {
                throw new CreateOrderException("没有找到运费模板");
            }
            FreightTemplateVO queryFreightTemplateVO = new FreightTemplateVO();
            queryFreightTemplateVO.setIdList(freightTemplateIdList);
            requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(), queryFreightTemplateVO);
            resultObjectVO = feignFreightTemplateService.findByIdList(requestJsonVO);

            if(!resultObjectVO.isSuccess())
            {
                throw new CreateOrderException("没有找到运费模板");
            }

            List<UBCIFreightTemplateVO> freightTemplateVOS = resultObjectVO.formatDataList(UBCIFreightTemplateVO.class);
            if(CollectionUtils.isEmpty(freightTemplateVOS))
            {
                throw new CreateOrderException("没有找到运费模板");
            }



            for (UBCIFreightTemplateVO freightTemplateVO : freightTemplateVOS) {
                for (UserBuyCarItemVO userBuyCarItemVO : createOrderVO.getBuyCarItems()) {
                    if(userBuyCarItemVO.getFreightTemplateId().longValue()==freightTemplateVO.getId().longValue())
                    {
                        userBuyCarItemVO.setFreightTemplateVO(freightTemplateVO);
                        //校验是否选择了运送方式,如果不为包邮的话,选择运送方式不能为空,那么就设置一个默认的运送方式
                        if(freightTemplateVO.getFreightStatus().intValue()==1)
                        {
                            String[] transportModels = freightTemplateVO.getTransportModel().split(",");
                            if(StringUtils.isEmpty(userBuyCarItemVO.getSelectTransportModel()))
                            {
                                userBuyCarItemVO.setSelectTransportModel(transportModels[0]);
                            }
                        }
                        break;
                    }
                }
            }

            //查询传过来的收货人信息
            ConsigneeAddress queryConsingeeAddress = new ConsigneeAddress();
            queryConsingeeAddress.setAppCode(toucan.getAppCode());
            queryConsingeeAddress.setUserMainId(Long.parseLong(userId));
            queryConsingeeAddress.setId(createOrderVO.getConsigneeAddress().getId());
            resultObjectVO = feignConsigneeAddressService.findByIdAndUserMainIdAndAppcode(RequestJsonVOGenerator.generator(toucan.getAppCode(),queryConsingeeAddress));
            if(!resultObjectVO.isSuccess())
            {
                throw new CreateOrderException("没有找到收货人信息");
            }
            createOrderVO.setConsigneeAddress(resultObjectVO.formatData(ConsigneeAddressVO.class));

            this.recalculateProductPrice(createOrderVO);


            //预扣库存
            requestJsonVO = RequestJsonVOGenerator.generatorByUser(appCode,userId,productSkuStockLocks);
            logger.info("开始锁定库存 {}",requestJsonVO.getEntityJson());
            resultObjectVO = feignProductSkuStockLockService.lockStock(requestJsonVO);
            if(!resultObjectVO.isSuccess())
            {
                throw new CreateOrderException("锁定库存失败");
            }
            //保存锁定库存的ID
            productSkuStockLocks = resultObjectVO.formatDataList(ProductSkuStockLockVO.class);
            logger.info("锁定库存结束.....");

            if(!CollectionUtils.isEmpty(inventoryReductions))
            {
                //将拍下扣库存的那些商品 进行扣库存
                requestJsonVO = RequestJsonVOGenerator.generatorByUser(appCode,userId,inventoryReductions);
                logger.info("开始扣库存 {} ",requestJsonVO.getEntityJson());
                resultObjectVO = feignProductSkuService.inventoryReduction(requestJsonVO);
                if(!resultObjectVO.isSuccess())
                {
                    logger.warn("扣库存失败 {} ",requestJsonVO.getEntityJson());
                    requestJsonVO = RequestJsonVOGenerator.generatorByUser(appCode,userId,productSkuStockLocks);
                    logger.warn("开始删除锁定库存数据... {} ",requestJsonVO.getEntityJson());
                    resultObjectVO = feignProductSkuStockLockService.deleteLockStock(requestJsonVO);
                    if(!resultObjectVO.isSuccess())
                    {
                        resultObjectVO.setMsg("创建订单失败,请稍后重试");
                    }
                    logger.info("删除锁定库存数据结束.... ");
                }
                logger.info("扣库存结束.....");
            }
            //扣库存成功后创建订单
//            CreateOrderVO createOrderVo = new CreateOrderVO();
//            requestJsonVO = RequestJsonVOGenerator.generatorByUser(appCode, userId, createOrderVo);
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
     * @param createOrderVo
     * @return
     */
    private ResultObjectVO queryBuyCarItems(CreateOrderVO createOrderVo, Long userId)
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
                for(UserBuyCarItemVO frontBuyCarItem : createOrderVo.getBuyCarItems())
                {
                    if(userBuyCarItemVO.getId().longValue()==frontBuyCarItem.getId().longValue())
                    {
                        userBuyCarItemVO.setBuyCount(frontBuyCarItem.getBuyCount());
                        userBuyCarItemVO.setSelectTransportModel(frontBuyCarItem.getSelectTransportModel());
                    }
                }
            }
            //将数据库中的购物车项设置进去
            createOrderVo.setBuyCarItems(userBuyCarVOList);
        }catch(Exception e)
        {
            resultObjectVO.setMsg("请稍后重试!");
            resultObjectVO.setCode(ResultVO.FAILD);
        }
        return resultObjectVO;
    }


    /**
     * 查询改收货信息对应的运费价格规则
     * @param userBuyCarItemVO
     * @param createOrderVO
     */
    private OrderFreightVO findOrderFreight(UserBuyCarItemVO userBuyCarItemVO,CreateOrderVO createOrderVO)
    {
        OrderFreightVO orderFreightVO = new OrderFreightVO();
        orderFreightVO.setTransportModel(userBuyCarItemVO.getSelectTransportModel());
        if("1".equals(userBuyCarItemVO.getSelectTransportModel())) //快递
        {
            List<UBCIFreightTemplateAreaRuleVO> areaRules = userBuyCarItemVO.getFreightTemplateVO().getExpressAreaRules();
            if(!CollectionUtils.isEmpty(areaRules))
            {
                //查询地区规则
                for(int i=0;i<areaRules.size();i++){
                    UBCIFreightTemplateAreaRuleVO rowAreaRule = areaRules.get(i);
                    if(!CollectionUtils.isEmpty(rowAreaRule.getSelectItems()))
                    {
                        for(int j=0;j<rowAreaRule.getSelectItems().size();j++)
                        {
                            UBCIFreightTemplateAreaRuleVO areaRule = rowAreaRule.getSelectItems().get(j);
                            //判断等于该城市编码
                            if(areaRule.getCityCode().equals(createOrderVO.getConsigneeAddress().getCityCode()))
                            {
                                //设置首重/续重/首重价格/续重价格
                                orderFreightVO.setFirstWeight(areaRule.getFirstWeight());
                                orderFreightVO.setFirstWeightMoney(areaRule.getFirstWeightMoney());
                                orderFreightVO.setAppendWeight(areaRule.getAppendWeight());
                                orderFreightVO.setAppendWeightMoney(areaRule.getAppendWeightMoney());
                                return orderFreightVO;
                            }
                        }
                    }
                }
            }
            UBCIFreightTemplateDefaultRuleVO defaultRuleVO = userBuyCarItemVO.getFreightTemplateVO().getExpressDefaultRule();
            //设置首重/续重/首重价格/续重价格
            orderFreightVO.setFirstWeight(defaultRuleVO.getDefaultWeight());
            orderFreightVO.setFirstWeightMoney(defaultRuleVO.getDefaultWeightMoney());
            orderFreightVO.setAppendWeight(defaultRuleVO.getDefaultAppendWeight());
            orderFreightVO.setAppendWeightMoney(defaultRuleVO.getDefaultAppendWeightMoney());
            return orderFreightVO;
        }else if("2".equals(userBuyCarItemVO.getSelectTransportModel())) //EMS
        {
            List<UBCIFreightTemplateAreaRuleVO> areaRules = userBuyCarItemVO.getFreightTemplateVO().getEmsAreaRules();
            if(!CollectionUtils.isEmpty(areaRules))
            {
                //查询地区规则
                for(int i=0;i<areaRules.size();i++){
                    UBCIFreightTemplateAreaRuleVO rowAreaRule = areaRules.get(i);
                    if(!CollectionUtils.isEmpty(rowAreaRule.getSelectItems()))
                    {
                        for(int j=0;j<rowAreaRule.getSelectItems().size();j++)
                        {
                            UBCIFreightTemplateAreaRuleVO areaRule = rowAreaRule.getSelectItems().get(j);
                            //判断等于该城市编码
                            if(areaRule.getCityCode().equals(createOrderVO.getConsigneeAddress().getCityCode()))
                            {
                                //设置首重/续重/首重价格/续重价格
                                orderFreightVO.setFirstWeight(areaRule.getFirstWeight());
                                orderFreightVO.setFirstWeightMoney(areaRule.getFirstWeightMoney());
                                orderFreightVO.setAppendWeight(areaRule.getAppendWeight());
                                orderFreightVO.setAppendWeightMoney(areaRule.getAppendWeightMoney());
                                return orderFreightVO;
                            }
                        }
                    }
                }
            }
            UBCIFreightTemplateDefaultRuleVO defaultRuleVO = userBuyCarItemVO.getFreightTemplateVO().getEmsDefaultRule();
            //设置首重/续重/首重价格/续重价格
            orderFreightVO.setFirstWeight(defaultRuleVO.getDefaultWeight());
            orderFreightVO.setFirstWeightMoney(defaultRuleVO.getDefaultWeightMoney());
            orderFreightVO.setAppendWeight(defaultRuleVO.getDefaultAppendWeight());
            orderFreightVO.setAppendWeightMoney(defaultRuleVO.getDefaultAppendWeightMoney());
            return orderFreightVO;

        }else if("3".equals(userBuyCarItemVO.getSelectTransportModel())) //平邮
        {
            List<UBCIFreightTemplateAreaRuleVO> areaRules = userBuyCarItemVO.getFreightTemplateVO().getOrdinaryMailAreaRules();
            if(!CollectionUtils.isEmpty(areaRules))
            {
                //查询地区规则
                for(int i=0;i<areaRules.size();i++){
                    UBCIFreightTemplateAreaRuleVO rowAreaRule = areaRules.get(i);
                    if(!CollectionUtils.isEmpty(rowAreaRule.getSelectItems()))
                    {
                        for(int j=0;j<rowAreaRule.getSelectItems().size();j++)
                        {
                            UBCIFreightTemplateAreaRuleVO areaRule = rowAreaRule.getSelectItems().get(j);
                            //判断等于该城市编码
                            if(areaRule.getCityCode().equals(createOrderVO.getConsigneeAddress().getCityCode()))
                            {
                                //设置首重/续重/首重价格/续重价格
                                orderFreightVO.setFirstWeight(areaRule.getFirstWeight());
                                orderFreightVO.setFirstWeightMoney(areaRule.getFirstWeightMoney());
                                orderFreightVO.setAppendWeight(areaRule.getAppendWeight());
                                orderFreightVO.setAppendWeightMoney(areaRule.getAppendWeightMoney());
                                return orderFreightVO;
                            }
                        }
                    }
                }
            }
            UBCIFreightTemplateDefaultRuleVO defaultRuleVO = userBuyCarItemVO.getFreightTemplateVO().getOrdinaryMailDefaultRule();
            //设置首重/续重/首重价格/续重价格
            orderFreightVO.setFirstWeight(defaultRuleVO.getDefaultWeight());
            orderFreightVO.setFirstWeightMoney(defaultRuleVO.getDefaultWeightMoney());
            orderFreightVO.setAppendWeight(defaultRuleVO.getDefaultAppendWeight());
            orderFreightVO.setAppendWeightMoney(defaultRuleVO.getDefaultAppendWeightMoney());
            return orderFreightVO;
        }
        return orderFreightVO;
    }

    /**
     * 重新计算商品价格(可能存在APP端加入了购物车,这时候在PC端进行了支付)
     */
    private void recalculateProductPrice(CreateOrderVO createOrderVo) throws Exception
    {
        logger.info("计算订单价格: param {} ",JSONObject.toJSONString(createOrderVo));
        List<OrderVO> orders = new LinkedList<>();
        //按照店铺排序,相邻商品在一起
        createOrderVo.getBuyCarItems().sort(Comparator.comparing(UserBuyCarItemVO::getShopId).reversed());
        //按照运费模板排序,用于合并运送方式
        createOrderVo.getBuyCarItems().sort(Comparator.comparing(UserBuyCarItemVO::getFreightTemplateId).reversed());

        //开始拆分订单
        OrderVO orderVO = new OrderVO(new LinkedList<>(),null,new LinkedList<>());
        orderVO.setId(idGenerator.id());
        orderVO.setOrderNo(orderNoService.generateOrderNo());
        orderVO.setMainOrderNo(createOrderVo.getMainOrder().getOrderNo());
        orderVO.setUserId(String.valueOf(createOrderVo.getUserId()));
        orderVO.setAppCode(toucan.getAppCode());
        orderVO.setCreateDate(new Date());
        orders.add(orderVO);
        for(int i = 0; i< createOrderVo.getBuyCarItems().size();i++)
        {
            UserBuyCarItemVO currentUserBuyCarItem = createOrderVo.getBuyCarItems().get(i);
            orderVO.getBuyCarItems().add(currentUserBuyCarItem);
            if(orderVO.getOrderFreight()==null)
            {
                //如果为空默认为包邮
                if(StringUtils.isNotEmpty(currentUserBuyCarItem.getSelectTransportModel())) {
                    //查询匹配收货人信息的运费规则
                    orderVO.setOrderFreight(this.findOrderFreight(currentUserBuyCarItem, createOrderVo));
                }
            }
            for(int j = i+1; j< createOrderVo.getBuyCarItems().size(); j++)
            {
                UserBuyCarItemVO nextUserBuyCarItem = createOrderVo.getBuyCarItems().get(j);
                //将同一个运费模板的商品,放到同一个订单里
                if(currentUserBuyCarItem.getFreightTemplateId().longValue()!=nextUserBuyCarItem.getFreightTemplateId().longValue())
                {
                    i = j-1; //将运费模板ID不相等的设为下一个分组起始位置
                    orderVO = new OrderVO(new LinkedList<>(),null,new LinkedList<>());
                    orderVO.setId(idGenerator.id());
                    orderVO.setOrderNo(orderNoService.generateOrderNo());
                    orderVO.setMainOrderNo(createOrderVo.getMainOrder().getOrderNo());
                    orderVO.setUserId(String.valueOf(createOrderVo.getUserId()));
                    orderVO.setAppCode(toucan.getAppCode());
                    orderVO.setCreateDate(new Date());
                    orders.add(orderVO);
                    //如果为空默认为包邮
                    if(StringUtils.isNotEmpty(currentUserBuyCarItem.getSelectTransportModel())) {
                        //查询匹配收货人信息的运费规则
                        orderVO.setOrderFreight(this.findOrderFreight(nextUserBuyCarItem, createOrderVo));
                    }
                    break;
                }else{
                    i = j; //将运费模板ID不相等的设为下一个分组起始位置
                    orderVO.getBuyCarItems().add(nextUserBuyCarItem);
                }
            }
        }

        //计算每个订单的金额
        for(OrderVO ovo:orders)
        {
            //订单总金额
            BigDecimal orderAmount=new BigDecimal(0.0D);
            //购买总数量
            BigDecimal buyCountTotal = new BigDecimal(0.0D);
            //子订单毛重总数
            BigDecimal roughWeightTotal = new BigDecimal(0.0D);
            //订单运费规则
            OrderFreightVO orderFreight = ovo.getOrderFreight();
            for(UserBuyCarItemVO ubc:ovo.getBuyCarItems())
            {
                //订单项 订单项里不保存运费,因为运费会把所有订单项中购买商品的数量加到一起计算
                OrderItemVO orderItemVO = new OrderItemVO();
                orderItemVO.setId(idGenerator.id());
                orderItemVO.setUserId(ovo.getUserId());
                orderItemVO.setOrderId(ovo.getId());
                orderItemVO.setOrderNo(ovo.getOrderNo());
                orderItemVO.setProductNum(ubc.getBuyCount()); //购买数量
                orderItemVO.setProductPrice(ubc.getProductPrice()); //购买时商品价格
                orderItemVO.setProductRoughWeight(ubc.getRoughWeight()); //购买时商品毛重
                orderItemVO.setSkuId(ubc.getShopProductSkuId()); //商品ID
                orderItemVO.setDeliveryStatus(0); //未收货
                orderItemVO.setSellerStatus(1); //备货完成

                orderItemVO.setAppCode(toucan.getAppCode());


                BigDecimal orderItemAmount = ubc.getProductPrice().multiply(new BigDecimal(ubc.getBuyCount()));
                orderItemVO.setOrderItemAmount(orderItemAmount);

                //子订单中购买项总数量
                buyCountTotal = buyCountTotal.add(new BigDecimal(ubc.getBuyCount()));
                //子订单毛重总数量 = 订单项购买数量*商品毛重
                roughWeightTotal = roughWeightTotal.add(new BigDecimal(ubc.getBuyCount()).multiply(ubc.getRoughWeight()));
                orderAmount = orderAmount.add(orderItemAmount);

                ovo.getOrderItems().add(orderItemVO);
            }
            //如果不为包邮
            if(orderFreight!=null)
            {
            }
            ovo.setOrderAmount(orderAmount);
        }

        createOrderVo.getMainOrder().setOrders(orders);
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
//                    resultObjectVO = feignProductSkuStockLockService.inventoryReduction(SignUtil.sign(appCode, requestJsonVO.getEntityJson()), requestJsonVO);

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
