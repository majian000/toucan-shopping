package com.toucan.shopping.cloud.apps.h5.controller.order;

import com.alibaba.fastjson2.JSONObject;
import com.toucan.shopping.cloud.apps.h5.controller.BaseController;
import com.toucan.shopping.cloud.order.api.MainOrderServiceAPI;
import com.toucan.shopping.cloud.order.api.OrderServiceAPI;
import com.toucan.shopping.cloud.product.api.ProductSkuServiceAPI;
import com.toucan.shopping.cloud.seller.api.FreightTemplateServiceAPI;
import com.toucan.shopping.cloud.stock.api.ProductSkuStockLockServiceAPI;
import com.toucan.shopping.cloud.user.api.ConsigneeAddressServiceAPI;
import com.toucan.shopping.cloud.user.api.UserBuyCarServiceAPI;
import com.toucan.shopping.modules.auth.user.UserAuth;
import com.toucan.shopping.modules.common.generator.IdGenerator;
import com.toucan.shopping.modules.common.generator.RequestJsonVOGenerator;
import com.toucan.shopping.modules.common.properties.Toucan;
import com.toucan.shopping.modules.common.util.DateUtils;
import com.toucan.shopping.modules.common.util.UserAuthHeaderUtil;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.common.vo.ResultVO;
import com.toucan.shopping.modules.image.upload.service.ImageUploadService;
import com.toucan.shopping.modules.order.constant.OrderConstant;
import com.toucan.shopping.modules.order.entity.MainOrder;
import com.toucan.shopping.modules.order.exception.CreateOrderException;
import com.toucan.shopping.modules.order.no.OrderNoService;
import com.toucan.shopping.modules.order.page.OrderPageInfo;
import com.toucan.shopping.modules.order.vo.CreateOrderVO;
import com.toucan.shopping.modules.order.vo.MainOrderVO;
import com.toucan.shopping.modules.order.vo.OrderFreightVO;
import com.toucan.shopping.modules.order.vo.OrderItemVO;
import com.toucan.shopping.modules.order.vo.OrderVO;
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
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * H5 订单
 */
@RestController("h5OrderController")
@RequestMapping("/h5/order")
public class OrderApiController extends BaseController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private Toucan toucan;

    @Autowired
    private OrderServiceAPI orderService;

    @Autowired
    private MainOrderServiceAPI mainOrderService;

    @Autowired
    private ProductSkuServiceAPI productSkuService;

    @Autowired
    private ProductSkuStockLockServiceAPI productSkuStockLockService;

    @Autowired
    private FreightTemplateServiceAPI freightTemplateService;

    @Autowired
    private ConsigneeAddressServiceAPI consigneeAddressService;

    @Autowired
    private UserBuyCarServiceAPI userBuyCarService;

    @Autowired
    private SkylarkLock skylarkLock;

    @Autowired
    private OrderNoService orderNoService;

    @Autowired
    private IdGenerator idGenerator;

    @Autowired
    private ImageUploadService imageUploadService;

    /** 订单详情（按订单号 + 用户） */
    @UserAuth
    @RequestMapping(value = "/detail", produces = "application/json;charset=UTF-8")
    public ResultObjectVO detail(HttpServletRequest request, @RequestBody OrderVO orderVO) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            orderVO.setUserId(UserAuthHeaderUtil.getUserMainId(request.getHeader(this.getToucan().getUserAuth().getHttpToucanAuthHeader())));
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(this.getAppCode(), orderVO);
            resultObjectVO = orderService.queryByOrderNoAndUserId(requestJsonVO);
            if (resultObjectVO.isSuccess()) {
                orderVO = resultObjectVO.formatData(OrderVO.class);
                if (orderVO.getCreateDate() != null) {
                    orderVO.setCreateDateLong(orderVO.getCreateDate().getTime());
                    orderVO.setSystemDateLong(new Date().getTime());
                    long timeRemaining = orderVO.getSystemDateLong().longValue() - orderVO.getCreateDateLong().longValue();
                    if (timeRemaining > OrderConstant.MAX_PAY_TIME) {
                        timeRemaining = 0L;
                    }
                    orderVO.setTimeRemaining(timeRemaining);
                    orderVO.setMaxPayTime(OrderConstant.MAX_PAY_TIME);
                }
                resultObjectVO.setData(orderVO);
            }
        } catch (Exception e) {
            logger.warn(e.getMessage(), e);
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            resultObjectVO.setMsg("请稍后重试");
        }
        return resultObjectVO;
    }

    /** 订单列表 */
    @UserAuth
    @RequestMapping(value = "/query/order/list", produces = "application/json;charset=UTF-8")
    public ResultObjectVO list(HttpServletRequest request, @RequestBody OrderPageInfo orderPageInfo) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            orderPageInfo.setUserId(UserAuthHeaderUtil.getUserMainId(request.getHeader(this.getToucan().getUserAuth().getHttpToucanAuthHeader())));
            orderPageInfo.setAppCode(this.getAppCode());
            if (StringUtils.isNotEmpty(orderPageInfo.getKeyword())) {
                orderPageInfo.setKeyword(orderPageInfo.getKeyword().trim());
            }
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(this.getAppCode(), orderPageInfo);
            resultObjectVO = orderService.queryListPage(requestJsonVO);
            if (resultObjectVO.isSuccess()) {
                orderPageInfo = resultObjectVO.formatData(OrderPageInfo.class);
                if (!CollectionUtils.isEmpty(orderPageInfo.getList())) {
                    for (OrderVO orderVO : orderPageInfo.getList()) {
                        if (!CollectionUtils.isEmpty(orderVO.getOrderItems())) {
                            for (OrderItemVO orderItemVO : orderVO.getOrderItems()) {
                                if (StringUtils.isNotEmpty(orderItemVO.getProductPreviewPath())) {
                                    orderItemVO.setHttpProductPreviewPath(imageUploadService.getImageHttpPrefix() + orderItemVO.getProductPreviewPath());
                                }
                            }
                        }
                    }
                }
                resultObjectVO.setData(orderPageInfo);
            }
        } catch (Exception e) {
            logger.warn(e.getMessage(), e);
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            resultObjectVO.setMsg("请稍后重试");
        }
        return resultObjectVO;
    }

    /** 下单 */
    @UserAuth
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public ResultObjectVO create(HttpServletRequest request, @RequestBody CreateOrderVO createOrderVO) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        if (createOrderVO == null) {
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("没有找到要购买的商品!");
            return resultObjectVO;
        }
        if (CollectionUtils.isEmpty(createOrderVO.getBuyCarItems())) {
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("没有找到要购买的商品!");
            return resultObjectVO;
        }
        if (createOrderVO.getConsigneeAddress() == null || createOrderVO.getConsigneeAddress().getId() == null) {
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("收货人信息不能为空!");
            return resultObjectVO;
        }
        if (StringUtils.isNotEmpty(createOrderVO.getRemark()) && createOrderVO.getRemark().length() > 255) {
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("订单备注长度最多255个汉字!");
            return resultObjectVO;
        }

        List<String> lockKeys = new ArrayList<>();
        String userId = "-1";
        try {
            userId = UserAuthHeaderUtil.getUserMainId(request.getHeader(toucan.getUserAuth().getHttpToucanAuthHeader()));
            if ("-1".equals(userId)) {
                throw new IllegalArgumentException("登录认证失败");
            }
            createOrderVO.setUserId(userId);

            String orderNo = orderNoService.generateOrderNo();
            MainOrderVO mainOrderVO = new MainOrderVO();
            mainOrderVO.setId(idGenerator.id());
            mainOrderVO.setOrderNo(orderNo);
            mainOrderVO.setUserId(userId);
            mainOrderVO.setAppCode(toucan.getAppCode());
            mainOrderVO.setRemark(createOrderVO.getRemark());
            mainOrderVO.setCreateDate(new Date());
            createOrderVO.setMainOrder(mainOrderVO);
            String globalTransactionId = UUID.randomUUID().toString().replace("-", "");
            String appCode = toucan.getAppCode();

            // 商品加锁
            for (UserBuyCarItemVO buyCarItemVO : createOrderVO.getBuyCarItems()) {
                if (buyCarItemVO != null) {
                    if (buyCarItemVO.getShopProductSkuId() == null) {
                        throw new IllegalArgumentException("没有找到商品");
                    }
                    String productBuyKey = ProductRedisKeyUtil.getProductBuyKey(appCode, String.valueOf(buyCarItemVO.getShopProductSkuId()));
                    boolean lockStatus = skylarkLock.lock(productBuyKey, userId);
                    if (!lockStatus) {
                        if (!CollectionUtils.isEmpty(lockKeys)) {
                            for (String lockKey : lockKeys) {
                                skylarkLock.unLock(lockKey, userId);
                            }
                        }
                        resultObjectVO.setCode(ResultObjectVO.FAILD);
                        resultObjectVO.setMsg("请稍后重试");
                        return resultObjectVO;
                    }
                    lockKeys.add(productBuyKey);
                }
            }

            // 查询当前库中的购物车项
            resultObjectVO = this.queryBuyCarItems(createOrderVO, Long.parseLong(userId));
            if (!resultObjectVO.isSuccess()) {
                resultObjectVO.setMsg("购物车商品不存在");
                return resultObjectVO;
            }

            List<ProductSkuStockLockVO> productSkuStockLocks = new LinkedList<>();
            List<InventoryReductionVO> inventoryReductions = new LinkedList<>();

            List<ProductSkuVO> productSkuVOS = new LinkedList<>();
            for (UserBuyCarItemVO userBuyCarItemVO : createOrderVO.getBuyCarItems()) {
                ProductSkuVO productSkuVO = new ProductSkuVO();
                productSkuVO.setId(userBuyCarItemVO.getShopProductSkuId());
                productSkuVOS.add(productSkuVO);
            }
            if (CollectionUtils.isEmpty(productSkuVOS)) {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("请稍后重试!");
                return resultObjectVO;
            }
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generatorByUser(appCode, userId, productSkuVOS);
            resultObjectVO = productSkuService.queryByIdList(requestJsonVO);
            if (!resultObjectVO.isSuccess()) {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("请稍后重试!");
                return resultObjectVO;
            }

            List<ProductSkuVO> queryProductSkuList = resultObjectVO.formatDataList(ProductSkuVO.class);
            if (CollectionUtils.isEmpty(queryProductSkuList)) {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("请稍后重试!");
                return resultObjectVO;
            }

            for (ProductSkuVO productSku : queryProductSkuList) {
                if (productSku.getStatus().intValue() == 0) {
                    resultObjectVO.setCode(ResultVO.FAILD);
                    resultObjectVO.setMsg(productSku.getName() + " 已下架");
                    return resultObjectVO;
                }
            }

            boolean isProductIsDel;
            for (UserBuyCarItemVO userBuyCarItemVO : createOrderVO.getBuyCarItems()) {
                if (userBuyCarItemVO.getBuyCount() == null || userBuyCarItemVO.getBuyCount().intValue() <= 0) {
                    resultObjectVO.setCode(ResultVO.FAILD);
                    resultObjectVO.setMsg(userBuyCarItemVO.getProductSkuName() + " 购买数量不能为0");
                    return resultObjectVO;
                }
                isProductIsDel = true;
                for (ProductSkuVO productSku : queryProductSkuList) {
                    if (userBuyCarItemVO.getShopProductSkuId().longValue() == productSku.getId().longValue()) {
                        userBuyCarItemVO.setProductPrice(productSku.getPrice());
                        userBuyCarItemVO.setProductPreviewPath(productSku.getProductPreviewPath());
                        userBuyCarItemVO.setProductSkuName(productSku.getName());
                        Map<String, String> productSkuMap = BeanUtils.describe(productSku);
                        productSkuMap.remove("previewPhotoPaths");
                        productSkuMap.remove("shopProductDescriptionVO");
                        productSkuMap.remove("productSkuVOList");
                        productSkuMap.remove("createDate");
                        productSkuMap.remove("class");
                        userBuyCarItemVO.setProductSkuJson(JSONObject.toJSONString(productSkuMap));
                        isProductIsDel = false;
                        break;
                    }
                }
                if (isProductIsDel) {
                    resultObjectVO.setCode(ResultVO.FAILD);
                    resultObjectVO.setMsg(userBuyCarItemVO.getProductSkuName() + " 已下架");
                    return resultObjectVO;
                }
            }

            ProductSkuStockLockVO queryProductSkuStockLockVO = new ProductSkuStockLockVO();
            queryProductSkuStockLockVO.setProductSkuIdList(createOrderVO.getBuyCarItems().stream().map(UserBuyCarItemVO::getShopProductSkuId).collect(Collectors.toList()));
            requestJsonVO = RequestJsonVOGenerator.generatorByUser(appCode, userId, queryProductSkuStockLockVO);
            resultObjectVO = productSkuStockLockService.findLockStockNumByProductSkuIds(requestJsonVO);
            if (!resultObjectVO.isSuccess()) {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("查询库存失败");
                return resultObjectVO;
            }

            List<ProductSkuStockLockVO> productSkuStockLockVOS = resultObjectVO.formatDataList(ProductSkuStockLockVO.class);
            if (!CollectionUtils.isEmpty(productSkuStockLockVOS)) {
                for (ProductSkuStockLockVO productSkuStockLockVO : productSkuStockLockVOS) {
                    for (UserBuyCarItemVO userBuyCarItemVO : createOrderVO.getBuyCarItems()) {
                        if (productSkuStockLockVO.getProductSkuId().longValue() == userBuyCarItemVO.getShopProductSkuId().longValue()) {
                            userBuyCarItemVO.setLockStockNum(productSkuStockLockVO.getStockNum());
                            break;
                        }
                    }
                }
            }

            Date orderCreateDate = new Date();
            List<Long> freightTemplateIdList = new LinkedList<>();
            for (ProductSkuVO productSku : queryProductSkuList) {
                if (productSku.getStockNum().intValue() <= 0) {
                    resultObjectVO.setCode(ResultVO.FAILD);
                    resultObjectVO.setMsg(productSku.getName() + " 库存不足");
                    return resultObjectVO;
                }
                freightTemplateIdList.add(productSku.getFreightTemplateId());
                for (UserBuyCarItemVO userBuyCarItemVO : createOrderVO.getBuyCarItems()) {
                    if (productSku.getId().longValue() == userBuyCarItemVO.getShopProductSkuId().longValue()) {
                        if (productSku.getStockNum().intValue() - userBuyCarItemVO.getLockStockNum().intValue() < userBuyCarItemVO.getBuyCount().intValue()) {
                            resultObjectVO.setCode(ResultVO.FAILD);
                            resultObjectVO.setMsg(productSku.getName() + " 库存不足");
                            return resultObjectVO;
                        }
                        userBuyCarItemVO.setProductPrice(productSku.getPrice());
                        userBuyCarItemVO.setRoughWeight(productSku.getRoughWeight());
                        userBuyCarItemVO.setSuttle(productSku.getSuttle());
                        userBuyCarItemVO.setFreightTemplateId(productSku.getFreightTemplateId());
                        userBuyCarItemVO.setShopId(productSku.getShopId());

                        ProductSkuStockLockVO productSkuStockLockVO = new ProductSkuStockLockVO();
                        productSkuStockLockVO.setMainOrderNo(orderNo);
                        productSkuStockLockVO.setAppCode(toucan.getAppCode());
                        productSkuStockLockVO.setProductSkuId(userBuyCarItemVO.getShopProductSkuId());
                        productSkuStockLockVO.setUserMainId(Long.parseLong(userId));
                        productSkuStockLockVO.setOrderCreateDate(orderCreateDate);
                        productSkuStockLockVO.setPayStatus((short) 0);
                        productSkuStockLockVO.setStockNum(userBuyCarItemVO.getBuyCount());
                        productSkuStockLockVO.setRemark(productSku.getName() + " 锁库存数量:" + userBuyCarItemVO.getBuyCount());
                        productSkuStockLockVO.setCreateDate(new Date());
                        if (productSku.getBuckleInventoryMethod().intValue() == 1) {
                            productSkuStockLockVO.setRestoreStatus((short) 0);
                        } else {
                            productSkuStockLockVO.setRestoreStatus((short) 1);
                        }
                        if (productSku.getBuckleInventoryMethod().intValue() == 1) {
                            productSkuStockLockVO.setType((short) 1);
                            InventoryReductionVO inventoryReductionVO = new InventoryReductionVO();
                            inventoryReductionVO.setProductSkuId(userBuyCarItemVO.getShopProductSkuId());
                            inventoryReductionVO.setStockNum(userBuyCarItemVO.getBuyCount());
                            inventoryReductionVO.setUserId(userId);
                            inventoryReductions.add(inventoryReductionVO);
                        } else {
                            productSkuStockLockVO.setType((short) 2);
                        }
                        productSkuStockLocks.add(productSkuStockLockVO);
                        break;
                    }
                }
            }

            if (CollectionUtils.isEmpty(freightTemplateIdList)) {
                throw new CreateOrderException("没有找到运费模板");
            }
            FreightTemplateVO queryFreightTemplateVO = new FreightTemplateVO();
            queryFreightTemplateVO.setIdList(freightTemplateIdList);
            requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(), queryFreightTemplateVO);
            resultObjectVO = freightTemplateService.findByIdList(requestJsonVO);
            if (!resultObjectVO.isSuccess()) {
                throw new CreateOrderException("没有找到运费模板");
            }
            List<UBCIFreightTemplateVO> freightTemplateVOS = resultObjectVO.formatDataList(UBCIFreightTemplateVO.class);
            if (CollectionUtils.isEmpty(freightTemplateVOS)) {
                throw new CreateOrderException("没有找到运费模板");
            }

            for (UBCIFreightTemplateVO freightTemplateVO : freightTemplateVOS) {
                for (UserBuyCarItemVO userBuyCarItemVO : createOrderVO.getBuyCarItems()) {
                    if (userBuyCarItemVO.getFreightTemplateId().longValue() == freightTemplateVO.getId().longValue()) {
                        userBuyCarItemVO.setFreightTemplateVO(freightTemplateVO);
                        if (freightTemplateVO.getFreightStatus().intValue() == 1) {
                            String[] transportModels = freightTemplateVO.getTransportModel().split(",");
                            if (StringUtils.isEmpty(userBuyCarItemVO.getSelectTransportModel())) {
                                userBuyCarItemVO.setSelectTransportModel(transportModels[0]);
                            }
                        }
                        break;
                    }
                }
            }

            ConsigneeAddress queryConsigneeAddress = new ConsigneeAddress();
            queryConsigneeAddress.setAppCode(toucan.getAppCode());
            queryConsigneeAddress.setUserMainId(Long.parseLong(userId));
            queryConsigneeAddress.setId(createOrderVO.getConsigneeAddress().getId());
            resultObjectVO = consigneeAddressService.findByIdAndUserMainIdAndAppcode(RequestJsonVOGenerator.generator(toucan.getAppCode(), queryConsigneeAddress));
            if (!resultObjectVO.isSuccess()) {
                throw new CreateOrderException("没有找到收货人信息");
            }
            createOrderVO.setConsigneeAddress(resultObjectVO.formatData(ConsigneeAddressVO.class));
            if (StringUtils.isEmpty(createOrderVO.getConsigneeAddress().getCityCode())) {
                createOrderVO.getConsigneeAddress().setCityCode(createOrderVO.getConsigneeAddress().getProvinceCode());
            }

            this.recalculateProductPrice(createOrderVO);

            if (!CollectionUtils.isEmpty(createOrderVO.getMainOrder().getOrders())) {
                boolean isFind;
                for (ProductSkuStockLockVO productSkuStockLockVO : productSkuStockLocks) {
                    isFind = false;
                    for (OrderVO orderVO : createOrderVO.getMainOrder().getOrders()) {
                        for (OrderItemVO orderItemVO : orderVO.getOrderItems()) {
                            if (productSkuStockLockVO.getProductSkuId().equals(orderItemVO.getSkuId())) {
                                isFind = true;
                                productSkuStockLockVO.setOrderNo(orderVO.getOrderNo());
                            }
                        }
                        if (isFind) {
                            break;
                        }
                    }
                }
            }

            requestJsonVO = RequestJsonVOGenerator.generatorByUser(appCode, userId, productSkuStockLocks);
            logger.info("开始锁定库存 {}", requestJsonVO.getEntityJson());
            resultObjectVO = productSkuStockLockService.lockStock(requestJsonVO);
            if (!resultObjectVO.isSuccess()) {
                throw new CreateOrderException("锁定库存失败");
            }
            productSkuStockLocks = resultObjectVO.formatDataList(ProductSkuStockLockVO.class);
            logger.info("锁定库存结束.....");

            if (!CollectionUtils.isEmpty(inventoryReductions)) {
                requestJsonVO = RequestJsonVOGenerator.generatorByUser(appCode, userId, inventoryReductions);
                logger.info("开始扣库存 {} ", requestJsonVO.getEntityJson());
                resultObjectVO = productSkuService.inventoryReduction(requestJsonVO);
                if (!resultObjectVO.isSuccess()) {
                    logger.warn("扣库存失败 {} ", requestJsonVO.getEntityJson());
                    requestJsonVO = RequestJsonVOGenerator.generatorByUser(appCode, userId, productSkuStockLocks);
                    resultObjectVO = productSkuStockLockService.deleteLockStock(requestJsonVO);
                    if (!resultObjectVO.isSuccess()) {
                        resultObjectVO.setMsg("创建订单失败,请稍后重试");
                        return resultObjectVO;
                    }
                }
                logger.info("扣库存结束.....");
            }

            requestJsonVO = RequestJsonVOGenerator.generatorByUser(appCode, userId, createOrderVO);
            logger.info("生成订单.... {} ", requestJsonVO.getEntityJson());
            resultObjectVO = mainOrderService.create(requestJsonVO);
            if (!resultObjectVO.isSuccess()) {
                logger.info("创建订单失败.....");
                requestJsonVO = RequestJsonVOGenerator.generatorByUser(appCode, userId, productSkuStockLocks);
                resultObjectVO = productSkuStockLockService.deleteLockStock(requestJsonVO);
                requestJsonVO = RequestJsonVOGenerator.generatorByUser(appCode, userId, inventoryReductions);
                resultObjectVO = productSkuService.restoreStock(requestJsonVO);
                throw new CreateOrderException("订单创建失败,请稍后重试");
            }

            UserBuyCarItemVO userBuyCarVO = new UserBuyCarItemVO();
            userBuyCarVO.setUserMainId(Long.parseLong(userId));
            requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(), userBuyCarVO);
            resultObjectVO = userBuyCarService.clearByUserMainId(requestJsonVO);

            resultObjectVO.setData(createOrderVO);

        } catch (Exception e) {
            logger.warn(e.getMessage(), e);
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("购买失败,请重试!");
        } finally {
            for (String lockKey : lockKeys) {
                skylarkLock.unLock(lockKey, userId);
            }
        }
        return resultObjectVO;
    }

    private ResultObjectVO queryBuyCarItems(CreateOrderVO createOrderVo, Long userId) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            UserBuyCarItemVO userBuyCarVO = new UserBuyCarItemVO();
            userBuyCarVO.setUserMainId(userId);
            resultObjectVO = userBuyCarService.listByUserMainId(RequestJsonVOGenerator.generator(toucan.getAppCode(), userBuyCarVO));
            if (!resultObjectVO.isSuccess()) {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("请稍后重试!");
                return resultObjectVO;
            }
            List<UserBuyCarItemVO> userBuyCarVOList = resultObjectVO.formatDataList(UserBuyCarItemVO.class);
            if (CollectionUtils.isEmpty(userBuyCarVOList)) {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("请稍后重试!");
                return resultObjectVO;
            }
            for (UserBuyCarItemVO userBuyCarItemVO : userBuyCarVOList) {
                for (UserBuyCarItemVO frontBuyCarItem : createOrderVo.getBuyCarItems()) {
                    if (userBuyCarItemVO.getId().longValue() == frontBuyCarItem.getId().longValue()) {
                        userBuyCarItemVO.setBuyCount(frontBuyCarItem.getBuyCount());
                        userBuyCarItemVO.setSelectTransportModel(frontBuyCarItem.getSelectTransportModel());
                    }
                }
            }
            createOrderVo.setBuyCarItems(userBuyCarVOList);
        } catch (Exception e) {
            resultObjectVO.setMsg("请稍后重试!");
            resultObjectVO.setCode(ResultVO.FAILD);
        }
        return resultObjectVO;
    }

    private OrderFreightVO findOrderFreight(UserBuyCarItemVO userBuyCarItemVO, CreateOrderVO createOrderVO) {
        OrderFreightVO orderFreightVO = new OrderFreightVO();
        orderFreightVO.setTransportModel(userBuyCarItemVO.getSelectTransportModel());
        orderFreightVO.setValuationMethod(userBuyCarItemVO.getFreightTemplateVO().getValuationMethod());
        if ("1".equals(userBuyCarItemVO.getSelectTransportModel())) {
            List<UBCIFreightTemplateAreaRuleVO> areaRules = userBuyCarItemVO.getFreightTemplateVO().getExpressAreaRules();
            if (!CollectionUtils.isEmpty(areaRules)) {
                for (UBCIFreightTemplateAreaRuleVO rowAreaRule : areaRules) {
                    if (!CollectionUtils.isEmpty(rowAreaRule.getSelectItems())) {
                        for (UBCIFreightTemplateAreaRuleVO areaRule : rowAreaRule.getSelectItems()) {
                            if (areaRule.getCityCode().equals(createOrderVO.getConsigneeAddress().getCityCode())) {
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
            orderFreightVO.setFirstWeight(defaultRuleVO.getDefaultWeight());
            orderFreightVO.setFirstWeightMoney(defaultRuleVO.getDefaultWeightMoney());
            orderFreightVO.setAppendWeight(defaultRuleVO.getDefaultAppendWeight());
            orderFreightVO.setAppendWeightMoney(defaultRuleVO.getDefaultAppendWeightMoney());
            return orderFreightVO;
        } else if ("2".equals(userBuyCarItemVO.getSelectTransportModel())) {
            List<UBCIFreightTemplateAreaRuleVO> areaRules = userBuyCarItemVO.getFreightTemplateVO().getEmsAreaRules();
            if (!CollectionUtils.isEmpty(areaRules)) {
                for (UBCIFreightTemplateAreaRuleVO rowAreaRule : areaRules) {
                    if (!CollectionUtils.isEmpty(rowAreaRule.getSelectItems())) {
                        for (UBCIFreightTemplateAreaRuleVO areaRule : rowAreaRule.getSelectItems()) {
                            if (areaRule.getCityCode().equals(createOrderVO.getConsigneeAddress().getCityCode())) {
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
            orderFreightVO.setFirstWeight(defaultRuleVO.getDefaultWeight());
            orderFreightVO.setFirstWeightMoney(defaultRuleVO.getDefaultWeightMoney());
            orderFreightVO.setAppendWeight(defaultRuleVO.getDefaultAppendWeight());
            orderFreightVO.setAppendWeightMoney(defaultRuleVO.getDefaultAppendWeightMoney());
            return orderFreightVO;
        } else if ("3".equals(userBuyCarItemVO.getSelectTransportModel())) {
            List<UBCIFreightTemplateAreaRuleVO> areaRules = userBuyCarItemVO.getFreightTemplateVO().getOrdinaryMailAreaRules();
            if (!CollectionUtils.isEmpty(areaRules)) {
                for (UBCIFreightTemplateAreaRuleVO rowAreaRule : areaRules) {
                    if (!CollectionUtils.isEmpty(rowAreaRule.getSelectItems())) {
                        for (UBCIFreightTemplateAreaRuleVO areaRule : rowAreaRule.getSelectItems()) {
                            if (areaRule.getCityCode().equals(createOrderVO.getConsigneeAddress().getCityCode())) {
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
            orderFreightVO.setFirstWeight(defaultRuleVO.getDefaultWeight());
            orderFreightVO.setFirstWeightMoney(defaultRuleVO.getDefaultWeightMoney());
            orderFreightVO.setAppendWeight(defaultRuleVO.getDefaultAppendWeight());
            orderFreightVO.setAppendWeightMoney(defaultRuleVO.getDefaultAppendWeightMoney());
            return orderFreightVO;
        }
        return orderFreightVO;
    }

    private void recalculateProductPrice(CreateOrderVO createOrderVo) throws Exception {
        logger.info("计算订单价格: param {} ", JSONObject.toJSONString(createOrderVo));
        List<OrderVO> orders = new LinkedList<>();
        createOrderVo.getBuyCarItems().sort(Comparator.comparing(UserBuyCarItemVO::getShopId).reversed());
        createOrderVo.getBuyCarItems().sort(Comparator.comparing(UserBuyCarItemVO::getFreightTemplateId).reversed());

        Date paymentDeadlineTime = DateUtils.addMillisecond(DateUtils.currentDate(), OrderConstant.MAX_PAY_TIME);

        OrderVO orderVO = new OrderVO();
        orderVO.setBuyCarItems(new LinkedList<>());
        orderVO.setOrderItems(new LinkedList<>());
        orderVO.setId(idGenerator.id());
        orderVO.setOrderNo(orderNoService.generateOrderNo());
        orderVO.setMainOrderNo(createOrderVo.getMainOrder().getOrderNo());
        orderVO.setPaymentDeadlineTime(paymentDeadlineTime);
        orderVO.setSrcType(createOrderVo.getSrcType());
        orderVO.setRemark(createOrderVo.getRemark());
        orders.add(orderVO);

        for (int i = 0; i < createOrderVo.getBuyCarItems().size(); i++) {
            UserBuyCarItemVO currentUserBuyCarItem = createOrderVo.getBuyCarItems().get(i);
            orderVO.getBuyCarItems().add(currentUserBuyCarItem);
            orderVO.setShopId(currentUserBuyCarItem.getShopId());
            if (orderVO.getOrderFreight() == null) {
                if (StringUtils.isNotEmpty(currentUserBuyCarItem.getSelectTransportModel())) {
                    orderVO.setOrderFreight(this.findOrderFreight(currentUserBuyCarItem, createOrderVo));
                }
            }
            for (int j = i + 1; j < createOrderVo.getBuyCarItems().size(); j++) {
                UserBuyCarItemVO nextUserBuyCarItem = createOrderVo.getBuyCarItems().get(j);
                if (currentUserBuyCarItem.getFreightTemplateId().longValue() != nextUserBuyCarItem.getFreightTemplateId().longValue()) {
                    i = j - 1;
                    orderVO = new OrderVO();
                    orderVO.setBuyCarItems(new LinkedList<>());
                    orderVO.setOrderItems(new LinkedList<>());
                    orderVO.setId(idGenerator.id());
                    orderVO.setOrderNo(orderNoService.generateOrderNo());
                    orderVO.setMainOrderNo(createOrderVo.getMainOrder().getOrderNo());
                    orderVO.setPaymentDeadlineTime(paymentDeadlineTime);
                    orderVO.setSrcType(createOrderVo.getSrcType());
                    orderVO.setRemark(createOrderVo.getRemark());
                    orders.add(orderVO);
                    if (StringUtils.isNotEmpty(currentUserBuyCarItem.getSelectTransportModel())) {
                        orderVO.setOrderFreight(this.findOrderFreight(nextUserBuyCarItem, createOrderVo));
                    }
                    break;
                } else {
                    i = j;
                    orderVO.getBuyCarItems().add(nextUserBuyCarItem);
                }
            }
        }

        MainOrderVO mainOrder = createOrderVo.getMainOrder();
        mainOrder.setOrderAmount(new BigDecimal(0));
        mainOrder.setTotalAmount(new BigDecimal(0));
        mainOrder.setFreightAmount(new BigDecimal(0));
        mainOrder.setPaymentDeadlineTime(paymentDeadlineTime);

        for (OrderVO ovo : orders) {
            BigDecimal orderAmount = new BigDecimal(0.0D);
            BigDecimal buyCountTotal = new BigDecimal(0.0D);
            BigDecimal roughWeightTotal = new BigDecimal(0.0D);
            OrderFreightVO orderFreight = ovo.getOrderFreight();
            for (UserBuyCarItemVO ubc : ovo.getBuyCarItems()) {
                OrderItemVO orderItemVO = new OrderItemVO();
                orderItemVO.setId(idGenerator.id());
                orderItemVO.setOrderId(ovo.getId());
                orderItemVO.setOrderNo(ovo.getOrderNo());
                orderItemVO.setProductNum(ubc.getBuyCount());
                orderItemVO.setProductPrice(ubc.getProductPrice());
                orderItemVO.setProductRoughWeight(ubc.getRoughWeight());
                orderItemVO.setSkuId(ubc.getShopProductSkuId());
                orderItemVO.setProductSkuName(ubc.getProductSkuName());
                orderItemVO.setProductPreviewPath(ubc.getProductPreviewPath());
                orderItemVO.setProductSkuJson(ubc.getProductSkuJson());
                orderItemVO.setFreightTemplateId(ubc.getFreightTemplateId());
                orderItemVO.setDeliveryStatus(0);
                orderItemVO.setBuyerStatus(0);
                orderItemVO.setSellerStatus(1);
                orderItemVO.setUserId(createOrderVo.getUserId());
                orderItemVO.setCreateDate(new Date());
                orderItemVO.setDeleteStatus((short) 0);
                orderItemVO.setAppCode(toucan.getAppCode());

                BigDecimal orderItemAmount = ubc.getProductPrice().multiply(new BigDecimal(ubc.getBuyCount()));
                orderItemVO.setOrderItemAmount(orderItemAmount);

                buyCountTotal = buyCountTotal.add(new BigDecimal(ubc.getBuyCount()));
                roughWeightTotal = roughWeightTotal.add(new BigDecimal(ubc.getBuyCount()).multiply(ubc.getRoughWeight()));
                orderAmount = orderAmount.add(orderItemAmount);
                ovo.getOrderItems().add(orderItemVO);
            }

            if (orderFreight != null) {
                if (orderFreight.getValuationMethod().intValue() == 1) {
                    int ret = buyCountTotal.compareTo(orderFreight.getFirstWeight());
                    if (ret == -1 || ret == 0) {
                        ovo.setFreightAmount(orderFreight.getFirstWeightMoney());
                    } else {
                        BigDecimal freightAmount = buyCountTotal.subtract(orderFreight.getFirstWeight()).divide(orderFreight.getAppendWeight(), 2, BigDecimal.ROUND_HALF_UP).multiply(orderFreight.getAppendWeightMoney());
                        if (freightAmount.compareTo(new BigDecimal(0)) <= 0) {
                            freightAmount = new BigDecimal(0);
                        }
                        ovo.setFreightAmount(freightAmount.add(orderFreight.getFirstWeightMoney()));
                    }
                } else if (orderFreight.getValuationMethod().intValue() == 2) {
                    int ret = roughWeightTotal.compareTo(orderFreight.getFirstWeight());
                    if (ret == -1 || ret == 0) {
                        ovo.setFreightAmount(orderFreight.getFirstWeightMoney());
                    } else {
                        BigDecimal freightAmount = roughWeightTotal.subtract(orderFreight.getFirstWeight()).divide(orderFreight.getAppendWeight(), 2, BigDecimal.ROUND_HALF_UP).multiply(orderFreight.getAppendWeightMoney());
                        if (freightAmount.compareTo(new BigDecimal(0)) <= 0) {
                            freightAmount = new BigDecimal(0);
                        }
                        ovo.setFreightAmount(freightAmount.add(orderFreight.getFirstWeightMoney()));
                    }
                }
            }
            ovo.setPayAmount(new BigDecimal(0));
            ovo.setOrderAmount(orderAmount.add(ovo.getFreightAmount()));
            ovo.setTotalAmount(orderAmount.add(ovo.getFreightAmount()));
            ovo.setRedPackageAmount(new BigDecimal(0));
            ovo.setCouponAmount(new BigDecimal(0));
            ovo.setTradeStatus(0);
            ovo.setPayStatus(OrderConstant.PAY_STATUS_NON_PAYMENT);
            ovo.setPayMethod(1);
            ovo.setCreateDate(new Date());
            ovo.setAppCode(toucan.getAppCode());
            ovo.setUserId(createOrderVo.getUserId());
            ovo.setDeleteStatus((short) 0);
            if (createOrderVo.getPayType() != null && createOrderVo.getPayType().intValue() == 1) {
                ovo.setPayType(0);
            } else if (createOrderVo.getPayType() != null && createOrderVo.getPayType().intValue() == 2) {
                ovo.setPayType(1);
            } else {
                ovo.setPayType(-1);
            }
            mainOrder.setOrderAmount(mainOrder.getOrderAmount().add(ovo.getOrderAmount()));
            mainOrder.setFreightAmount(mainOrder.getFreightAmount().add(ovo.getFreightAmount()));
            mainOrder.setTotalAmount(mainOrder.getTotalAmount().add(ovo.getTotalAmount()));
        }

        if (createOrderVo.getPayType() != null && createOrderVo.getPayType().intValue() == 1) {
            mainOrder.setPayType(0);
        } else if (createOrderVo.getPayType() != null && createOrderVo.getPayType().intValue() == 2) {
            mainOrder.setPayType(1);
        } else {
            mainOrder.setPayType(-1);
        }
        mainOrder.setPayAmount(new BigDecimal(0));
        mainOrder.setRedPackageAmount(new BigDecimal(0));
        mainOrder.setCouponAmount(new BigDecimal(0));
        mainOrder.setTradeStatus(0);
        mainOrder.setPayStatus(OrderConstant.PAY_STATUS_NON_PAYMENT);
        mainOrder.setPayMethod(1);
        mainOrder.setCreateDate(new Date());
        mainOrder.setAppCode(toucan.getAppCode());
        mainOrder.setUserId(createOrderVo.getUserId());
        mainOrder.setDeleteStatus((short) 0);

        mainOrder.setOrders(orders);
    }
}
