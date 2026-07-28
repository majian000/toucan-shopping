package com.toucan.shopping.cloud.user.order;

import com.alibaba.fastjson2.JSONObject;
import com.toucan.shopping.cloud.common.data.api.CategoryServiceAPI;
import com.toucan.shopping.cloud.product.api.ShopProductServiceAPI;
import com.toucan.shopping.modules.category.vo.CategoryVO;
import com.toucan.shopping.modules.common.generator.RequestJsonVOGenerator;
import com.toucan.shopping.modules.common.properties.Toucan;
import com.toucan.shopping.modules.common.util.DateUtils;
import com.toucan.shopping.modules.common.util.SignUtil;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.common.vo.ResultVO;
import com.toucan.shopping.modules.order.exception.CreateOrderException;
import com.toucan.shopping.modules.order.vo.CreateOrderVO;
import com.toucan.shopping.modules.order.vo.MainOrderVO;
import com.toucan.shopping.modules.order.vo.OrderItemVO;
import com.toucan.shopping.modules.order.vo.OrderVO;
import com.toucan.shopping.modules.product.page.ProductSkuPageInfo;
import com.toucan.shopping.modules.product.page.ShopProductPageInfo;
import com.toucan.shopping.modules.product.util.ProductRedisKeyUtil;
import com.toucan.shopping.modules.product.vo.*;
import com.toucan.shopping.modules.seller.vo.FreightTemplateVO;
import com.toucan.shopping.modules.user.entity.ConsigneeAddress;
import com.toucan.shopping.modules.user.page.ConsigneeAddressPageInfo;
import com.toucan.shopping.modules.user.vo.ConsigneeAddressVO;
import com.toucan.shopping.modules.user.vo.UserBuyCarItemVO;
import com.toucan.shopping.modules.user.vo.freightTemplate.UBCIFreightTemplateVO;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.stream.Collectors;

import com.toucan.shopping.cloud.order.api.MainOrderServiceAPI;
import com.toucan.shopping.cloud.seller.api.FreightTemplateServiceAPI;
import com.toucan.shopping.cloud.user.api.ConsigneeAddressServiceAPI;
import com.toucan.shopping.modules.order.constant.OrderConstant;
import com.toucan.shopping.modules.order.entity.MainOrder;
import com.toucan.shopping.modules.order.vo.*;
import com.toucan.shopping.cloud.product.api.ProductSkuServiceAPI;
import com.toucan.shopping.cloud.stock.api.ProductSkuStockLockServiceAPI;
import com.toucan.shopping.cloud.user.api.UserBuyCarServiceAPI;
import com.toucan.shopping.modules.common.generator.IdGenerator;
import com.toucan.shopping.modules.order.no.OrderNoService;
import com.toucan.shopping.modules.product.vo.InventoryReductionVO;
import com.toucan.shopping.modules.product.vo.ProductSkuVO;
import com.toucan.shopping.modules.skylark.lock.service.SkylarkLock;
import com.toucan.shopping.modules.stock.vo.ProductSkuStockLockVO;
import com.toucan.shopping.modules.user.vo.freightTemplate.UBCIFreightTemplateAreaRuleVO;
import com.toucan.shopping.modules.user.vo.freightTemplate.UBCIFreightTemplateDefaultRuleVO;


//@RunWith(SpringRunner.class)
//@SpringBootTest(classes = CloudWebApplication.class)
public class UserOrderTest {

    private final Logger logger = LoggerFactory.getLogger(getClass());


    @Autowired
    private Toucan toucan;


    @Autowired
    private ProductSkuStockLockServiceAPI productSkuStockLockService;

    @Autowired
    private SkylarkLock skylarkLock;


    @Autowired
    private OrderNoService orderNoService;


    @Autowired
    private ProductSkuServiceAPI productSkuService;

    @Autowired
    private UserBuyCarServiceAPI userBuyCarService;


    @Autowired
    private IdGenerator idGenerator;

    @Autowired
    private FreightTemplateServiceAPI freightTemplateService;

    @Autowired
    private ConsigneeAddressServiceAPI consigneeAddressService;

    @Autowired
    private MainOrderServiceAPI mainOrderService;

    @Autowired
    private CategoryServiceAPI categoryService;

    @Autowired
    private ShopProductServiceAPI shopProductService;

    /**
     * 批量插入订单
     */
//    @Test
    public void batchInsertOrder() throws NoSuchAlgorithmException {
        Random rand = new Random();
        String orderTemplate="{\n" +
                "\t\"buyCarItems\": [{\n" +
                "\t\t\"id\": \"1344249655143170091\",\n" +
                "\t\t\"userMainId\": \"891099441195384848\",\n" +
                "\t\t\"shopProductSkuId\": \"1197217720429445183\",\n" +
                "\t\t\"buyCount\": 1,\n" +
                "\t\t\"createDate\": \"2025-02-26 10:08:12\",\n" +
                "\t\t\"updateDate\": null,\n" +
                "\t\t\"deleteStatus\": 0,\n" +
                "\t\t\"productSkuName\": \"华为mate60pro 红色 256G\",\n" +
                "\t\t\"productPreviewPath\": null,\n" +
                "\t\t\"httpProductImgPath\": \"http://8.140.187.184:8049/group1/M00/00/10/rB5PVWWnf4uAZn0bAACgYFKM-yU78..JPG\",\n" +
                "\t\t\"productPrice\": 1074.91,\n" +
                "\t\t\"attributePreview\": \"颜色:红色 版本:256G \",\n" +
                "\t\t\"roughWeight\": 10,\n" +
                "\t\t\"suttle\": 10,\n" +
                "\t\t\"noAllowedBuyStatus\": null,\n" +
                "\t\t\"noAllowedBuyDesc\": null,\n" +
                "\t\t\"shopId\": 983769356921995300,\n" +
                "\t\t\"freightTemplateId\": 1032316349239525400,\n" +
                "\t\t\"freightTemplateVO\": {\n" +
                "\t\t\t\"id\": \"1032316349239525394\",\n" +
                "\t\t\t\"name\": \"包邮模板\",\n" +
                "\t\t\t\"freightStatus\": 1,\n" +
                "\t\t\t\"transportModel\": \"1\",\n" +
                "\t\t\t\"valuationMethod\": 2,\n" +
                "\t\t\t\"idList\": null,\n" +
                "\t\t\t\"oneTransportModel\": null,\n" +
                "\t\t\t\"transportModelExpress\": null,\n" +
                "\t\t\t\"transportModelEms\": null,\n" +
                "\t\t\t\"transportModelOrdinaryMail\": null,\n" +
                "\t\t\t\"expressDefaultRule\": {\n" +
                "\t\t\t\t\"id\": \"1038088301359136864\",\n" +
                "\t\t\t\t\"transportModel\": \"1\",\n" +
                "\t\t\t\t\"defaultWeight\": 1,\n" +
                "\t\t\t\t\"defaultWeightMoney\": 1,\n" +
                "\t\t\t\t\"defaultAppendWeight\": 1,\n" +
                "\t\t\t\t\"defaultAppendWeightMoney\": 1,\n" +
                "\t\t\t\t\"type\": 1\n" +
                "\t\t\t},\n" +
                "\t\t\t\"expressAreaRules\": [],\n" +
                "\t\t\t\"emsDefaultRule\": null,\n" +
                "\t\t\t\"emsAreaRules\": [],\n" +
                "\t\t\t\"ordinaryMailDefaultRule\": null,\n" +
                "\t\t\t\"ordinaryMailAreaRules\": [],\n" +
                "\t\t\t\"cityCodeToProvinceCode\": null,\n" +
                "\t\t\t\"cityNameToCityCode\": null,\n" +
                "\t\t\t\"cityNameToProvinceName\": null\n" +
                "\t\t},\n" +
                "\t\t\"selectTransportModel\": \"1\",\n" +
                "\t\t\"lockStockNum\": 0,\n" +
                "\t\t\"productSkuJson\": null,\n" +
                "\t\t\"isAllowedBuy\": true,\n" +
                "\t\t\"isMergeRow\": true,\n" +
                "\t\t\"mergeRowCount\": 0,\n" +
                "\t\t\"isFindFirstRow\": false,\n" +
                "\t\t\"transportModel\": \"1\"\n" +
                "\t}],\n" +
                "\t\"consigneeAddress\": {\n" +
                "\t\t\"id\": \"1202634848498352197\",\n" +
                "\t\t\"userMainId\": \"891099441195384848\",\n" +
                "\t\t\"name\": \"马先生\",\n" +
                "\t\t\"address\": \"11\",\n" +
                "\t\t\"phone\": \"18701601877\",\n" +
                "\t\t\"provinceCode\": \"220000\",\n" +
                "\t\t\"cityCode\": \"220100\",\n" +
                "\t\t\"areaCode\": \"220105\",\n" +
                "\t\t\"provinceName\": \"吉林省\",\n" +
                "\t\t\"cityName\": \"长春市\",\n" +
                "\t\t\"areaName\": \"二道区\",\n" +
                "\t\t\"defaultStatus\": 1,\n" +
                "\t\t\"createDate\": \"2024-02-01 15:21:31\",\n" +
                "\t\t\"updateDate\": null,\n" +
                "\t\t\"deleteStatus\": 0,\n" +
                "\t\t\"appCode\": \"10001001\",\n" +
                "\t\t\"vcode\": null,\n" +
                "\t\t\"ids\": null\n" +
                "\t},\n" +
                "\t\"payType\": -1,\n" +
                "\t\"remark\": \"\"\n" +
                "}";


        CreateOrderVO createOrderVO = JSONObject.parseObject(orderTemplate,CreateOrderVO.class);

        //查询所有收货地址
        ConsigneeAddressPageInfo consigneeAddressPageInfo = new ConsigneeAddressPageInfo();
        consigneeAddressPageInfo.setUserMainId(Long.parseLong("891099441195384848"));
        consigneeAddressPageInfo.setAppCode(toucan.getAppCode());
        consigneeAddressPageInfo.setPage(1);
        consigneeAddressPageInfo.setLimit(100);

        ResultObjectVO resultObjectVO = consigneeAddressService.queryListPage(RequestJsonVOGenerator.generator(toucan.getAppCode(),consigneeAddressPageInfo));
        List<ConsigneeAddress> consigneeAddressVOS =  resultObjectVO.formatData(ConsigneeAddressPageInfo.class).getList();
        int spage=0;
        while(true) {
            ShopProductPageInfo shopPageInfo = new ShopProductPageInfo();
            shopPageInfo.setCategoryId(889589266080858188L);
            spage++;
            shopPageInfo.setPage(spage);
            shopPageInfo.setLimit(100);
            if(shopPageInfo.getCategoryId()!=null&&shopPageInfo.getCategoryId().longValue()!=-1) {
                //查询分类以及子分类
                CategoryVO categoryVO = new CategoryVO();
                categoryVO.setId(shopPageInfo.getCategoryId());
                RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(), categoryVO);
                resultObjectVO = categoryService.queryChildListByPid(requestJsonVO);
                if (resultObjectVO.isSuccess()) {
                    if (resultObjectVO.getData() != null) {
                        List<CategoryVO> categoryVOS = resultObjectVO.formatDataList(CategoryVO.class);
                        if (org.apache.commons.collections.CollectionUtils.isNotEmpty(categoryVOS)) {
                            List<Long> categoryIdList = new LinkedList<>();
                            for (CategoryVO cv : categoryVOS) {
                                categoryIdList.add(cv.getId());
                            }
                            categoryIdList.add(shopPageInfo.getCategoryId());
                            shopPageInfo.setCategoryIdList(categoryIdList);
                            shopPageInfo.setCategoryId(null);
                        }
                    }
                }
            }
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(), shopPageInfo);
            resultObjectVO = shopProductService.queryListPage(requestJsonVO);
            if(resultObjectVO.isSuccess()) {
                ShopProductPageInfo shopProductPageInfo = resultObjectVO.formatData(ShopProductPageInfo.class);
                if (shopProductPageInfo == null || CollectionUtils.isEmpty(shopProductPageInfo.getList())) {
                    break;
                }
                for (ShopProductVO shopProductVO : shopProductPageInfo.getList()) {
                    //查询店铺商品
                    ProductSkuPageInfo pageInfo = new ProductSkuPageInfo();
                    pageInfo.setPage(1);
                    pageInfo.setShopProductId(shopProductVO.getId());
                    pageInfo.setLimit(10);
                    ResultObjectVO skuResult = productSkuService.queryListPage(RequestJsonVOGenerator.generator(toucan.getAppCode(), pageInfo));
                    pageInfo = skuResult.formatData(ProductSkuPageInfo.class);
                    if (pageInfo == null || CollectionUtils.isEmpty(pageInfo.getList())) {
                        break;
                    }
                    for (ProductSkuVO productSkuVO : pageInfo.getList()) {
                        UserBuyCarItemVO userBuyCarItemVO = createOrderVO.getBuyCarItems().get(0);
                        userBuyCarItemVO.setProductSkuName(productSkuVO.getName());
                        userBuyCarItemVO.setShopProductSkuId(productSkuVO.getId());
                        userBuyCarItemVO.setBuyCount(rand.nextInt(10));
                        userBuyCarItemVO.setRoughWeight(productSkuVO.getRoughWeight());


                        try {
                            ConsigneeAddress ca = consigneeAddressVOS.get(rand.nextInt(consigneeAddressVOS.size() - 1));
                            ConsigneeAddressVO cav = new ConsigneeAddressVO();
                            BeanUtils.copyProperties(cav,ca);
                            createOrderVO.setConsigneeAddress(cav);

                            insertOrder(createOrderVO);

                            /**
                             *
                             修改子订单表
                             UPDATE t_order_2025_2 SET pay_status=1 ,trade_status=3,pay_method=1 WHERE create_date>='2025-2-26 11:00:16' AND  trade_status=0
                             修改主订单表
                             UPDATE t_main_order_2025_2 SET pay_status=1 ,trade_status=3,pay_method=1,pay_type=1 WHERE create_date>='2025-2-26 11:00:16' AND  trade_status=0
                             */

                        } catch (Exception e) {
                            logger.error(e.getMessage(), e);
                        }


                    }
                }
            }


        }

    }


    public ResultObjectVO insertOrder(CreateOrderVO createOrderVO){
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        if(createOrderVO ==null)
        {
            logger.info("没有找到要购买的商品: param:"+ JSONObject.toJSONString(createOrderVO));
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("没有找到要购买的商品!");
            return resultObjectVO;
        }
        if(CollectionUtils.isEmpty(createOrderVO.getBuyCarItems()))
        {
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
        if(StringUtils.isNotEmpty(createOrderVO.getRemark()))
        {
            if(createOrderVO.getRemark().length()>255)
            {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("订单备注长度最多255个汉字!");
                return resultObjectVO;
            }
        }
        //锁住当前购买所有商品
        List<String> lockKeys= new ArrayList<String>();
        String userId = "891099441195384848";
        try {
            createOrderVO.setUserId(userId);
            //主订单
            String orderNo= orderNoService.generateOrderNo();
            MainOrderVO mainOrderVO = new MainOrderVO();
            mainOrderVO.setId(idGenerator.id());
            mainOrderVO.setOrderNo(orderNo);
            mainOrderVO.setUserId(userId);
            mainOrderVO.setAppCode(toucan.getAppCode());
            mainOrderVO.setRemark(createOrderVO.getRemark());
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
//            if(!resultObjectVO.isSuccess())
//            {
//                resultObjectVO.setMsg("购物车商品不存在");
//                return resultObjectVO;
//            }

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
            resultObjectVO = productSkuService.queryByIdList(requestJsonVO);
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
                if(userBuyCarItemVO.getBuyCount()==null||userBuyCarItemVO.getBuyCount().intValue()<=0)
                {
                    resultObjectVO.setCode(ResultVO.FAILD);
                    resultObjectVO.setMsg(userBuyCarItemVO.getProductSkuName() + " 购买数量不能为0");
                    return resultObjectVO;
                }
                isProductIsDel = true;
                for (ProductSkuVO productSku : queryProductSkuList) {
                    if(userBuyCarItemVO.getShopProductSkuId().longValue()==productSku.getId().longValue())
                    {
                        userBuyCarItemVO.setProductPrice(productSku.getPrice());
                        userBuyCarItemVO.setProductPreviewPath(productSku.getProductPreviewPath());
                        userBuyCarItemVO.setProductSkuName(productSku.getName());
                        Map productSkuMap = BeanUtils.describe(productSku);
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
            resultObjectVO = productSkuStockLockService.findLockStockNumByProductSkuIds(requestJsonVO);
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
                        productSkuStockLockVO.setMainOrderNo(orderNo);
                        productSkuStockLockVO.setAppCode(toucan.getAppCode());
                        productSkuStockLockVO.setProductSkuId(userBuyCarItemVO.getShopProductSkuId());
                        productSkuStockLockVO.setUserMainId(Long.parseLong(userId));
                        productSkuStockLockVO.setOrderCreateDate(orderCreateDate);
                        productSkuStockLockVO.setPayStatus((short)0);
                        productSkuStockLockVO.setStockNum(userBuyCarItemVO.getBuyCount()); //减库存数量
                        productSkuStockLockVO.setRemark(productSku.getName()+" 锁库存数量:"+userBuyCarItemVO.getBuyCount());
                        productSkuStockLockVO.setCreateDate(new Date());
                        if(productSku.getBuckleInventoryMethod().intValue()==1) {
                            productSkuStockLockVO.setRestoreStatus((short) 0); //库存未还原
                        }else{
                            productSkuStockLockVO.setRestoreStatus((short) 1); //库存已还原
                        }

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
            resultObjectVO = freightTemplateService.findByIdList(requestJsonVO);

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
            resultObjectVO = consigneeAddressService.findByIdAndUserMainIdAndAppcode(RequestJsonVOGenerator.generator(toucan.getAppCode(),queryConsingeeAddress));
            if(!resultObjectVO.isSuccess())
            {
                throw new CreateOrderException("没有找到收货人信息");
            }
            createOrderVO.setConsigneeAddress(resultObjectVO.formatData(ConsigneeAddressVO.class));
            //如果是直辖市的话,地市编码就为省份编码
            if(StringUtils.isEmpty(createOrderVO.getConsigneeAddress().getCityCode()))
            {
                createOrderVO.getConsigneeAddress().setCityCode(createOrderVO.getConsigneeAddress().getProvinceCode());
            }

            this.recalculateProductPrice(createOrderVO);

            //在库存锁定中存储子订单编号
            if(!CollectionUtils.isEmpty(createOrderVO.getMainOrder().getOrders())) {
                boolean isFind=false;
                for (ProductSkuStockLockVO productSkuStockLockVO : productSkuStockLocks) {
                    isFind=false;
                    for(OrderVO orderVO:createOrderVO.getMainOrder().getOrders())
                    {
                        for(OrderItemVO orderItemVO:orderVO.getOrderItems()) {
                            if (productSkuStockLockVO.getProductSkuId().equals(orderItemVO.getSkuId()))
                            {
                                isFind=true;
                                //设置子订单编号
                                productSkuStockLockVO.setOrderNo(orderVO.getOrderNo());
                            }
                        }
                        if(isFind)
                        {
                            break;
                        }
                    }
                }
            }

            //预扣库存
            requestJsonVO = RequestJsonVOGenerator.generatorByUser(appCode,userId,productSkuStockLocks);
            logger.info("开始锁定库存 {}",requestJsonVO.getEntityJson());
            resultObjectVO = productSkuStockLockService.lockStock(requestJsonVO);
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
                resultObjectVO = productSkuService.inventoryReduction(requestJsonVO);
                if(!resultObjectVO.isSuccess())
                {
                    logger.warn("扣库存失败 {} ",requestJsonVO.getEntityJson());
                    requestJsonVO = RequestJsonVOGenerator.generatorByUser(appCode,userId,productSkuStockLocks);
                    logger.warn("开始删除锁定库存数据... {} ",requestJsonVO.getEntityJson());
                    resultObjectVO = productSkuStockLockService.deleteLockStock(requestJsonVO);
                    if(!resultObjectVO.isSuccess())
                    {
                        resultObjectVO.setMsg("创建订单失败,请稍后重试");
                        return resultObjectVO;
                    }
                    logger.info("删除锁定库存数据结束.... ");
                }
                logger.info("扣库存结束.....");
            }
            //扣库存成功后创建订单
            requestJsonVO = RequestJsonVOGenerator.generatorByUser(appCode, userId, createOrderVO);
            logger.info("生成订单.... {} ",requestJsonVO.getEntityJson());
            resultObjectVO = mainOrderService.create(requestJsonVO);
            if(!resultObjectVO.isSuccess())
            {
                //删除锁定的库存
                logger.info("创建订单失败.....");
                requestJsonVO = RequestJsonVOGenerator.generatorByUser(appCode,userId,productSkuStockLocks);
                logger.warn("开始删除锁定库存数据... {} ",requestJsonVO.getEntityJson());
                resultObjectVO = productSkuStockLockService.deleteLockStock(requestJsonVO);
                //恢复商品库存数量
                logger.info("开始恢复库存 {} ",requestJsonVO.getEntityJson());
                requestJsonVO = RequestJsonVOGenerator.generatorByUser(appCode,userId,inventoryReductions);
                resultObjectVO = productSkuService.restoreStock(requestJsonVO);

                throw new CreateOrderException("订单创建失败,请稍后重试");
            }

            //清空购物车
            UserBuyCarItemVO userBuyCarVO = new UserBuyCarItemVO();
            userBuyCarVO.setUserMainId(Long.parseLong(userId));
            requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(), userBuyCarVO);
            resultObjectVO = userBuyCarService.clearByUserMainId(requestJsonVO);

            resultObjectVO.setData(createOrderVO);

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

        //支付截止时间
        Date paymentDeadlineTime = DateUtils.addMillisecond(DateUtils.currentDate(),OrderConstant.MAX_PAY_TIME);

        //开始拆分订单
        OrderVO orderVO = new OrderVO();
        orderVO.setBuyCarItems(new LinkedList<>());
        orderVO.setOrderItems(new LinkedList<>());

        orderVO.setId(idGenerator.id());
        orderVO.setOrderNo(orderNoService.generateOrderNo());
        orderVO.setMainOrderNo(createOrderVo.getMainOrder().getOrderNo());
        orderVO.setPaymentDeadlineTime(paymentDeadlineTime); //支付截止时间
        orderVO.setSrcType(createOrderVo.getSrcType()); //下单渠道
        orderVO.setRemark(createOrderVo.getRemark()); //订单备注
        orders.add(orderVO);
        for(int i = 0; i< createOrderVo.getBuyCarItems().size();i++)
        {
            UserBuyCarItemVO currentUserBuyCarItem = createOrderVo.getBuyCarItems().get(i);
            orderVO.getBuyCarItems().add(currentUserBuyCarItem);
            orderVO.setShopId(currentUserBuyCarItem.getShopId());
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
                    orderVO = new OrderVO();
                    orderVO.setBuyCarItems(new LinkedList<>());
                    orderVO.setOrderItems(new LinkedList<>());

                    orderVO.setId(idGenerator.id());
                    orderVO.setOrderNo(orderNoService.generateOrderNo());
                    orderVO.setMainOrderNo(createOrderVo.getMainOrder().getOrderNo());
                    orderVO.setPaymentDeadlineTime(paymentDeadlineTime); //支付截止时间
                    orderVO.setSrcType(createOrderVo.getSrcType()); //下单渠道
                    orderVO.setRemark(createOrderVo.getRemark()); //订单备注
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

        MainOrder mainOrder = createOrderVo.getMainOrder();
        mainOrder.setOrderAmount(new BigDecimal(0)); //订单总金额
        mainOrder.setTotalAmount(new BigDecimal(0)); //商品最终金额(折扣算完)
        mainOrder.setFreightAmount(new BigDecimal(0)); //运费总金额
        mainOrder.setPaymentDeadlineTime(paymentDeadlineTime); //设置支付截止时间

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
                orderItemVO.setOrderId(ovo.getId());
                orderItemVO.setOrderNo(ovo.getOrderNo());
                orderItemVO.setProductNum(ubc.getBuyCount()); //购买数量
                orderItemVO.setProductPrice(ubc.getProductPrice()); //购买时商品价格
                orderItemVO.setProductRoughWeight(ubc.getRoughWeight()); //购买时商品毛重
                orderItemVO.setSkuId(ubc.getShopProductSkuId()); //商品ID
                orderItemVO.setProductSkuName(ubc.getProductSkuName()); //商品SKU名称
                orderItemVO.setProductPreviewPath(ubc.getProductPreviewPath()); //商品预览图
                orderItemVO.setProductSkuJson(ubc.getProductSkuJson()); //商品快照
                orderItemVO.setFreightTemplateId(ubc.getFreightTemplateId()); //运费模板ID
                orderItemVO.setDeliveryStatus(0); //未收货
                orderItemVO.setBuyerStatus(0); //待收货
                orderItemVO.setSellerStatus(1); //备货完成
                orderItemVO.setUserId(createOrderVo.getUserId());
                orderItemVO.setCreateDate(new Date());
                orderItemVO.setDeleteStatus((short)0);
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
                if(orderFreight.getValuationMethod().intValue()==1) //按件数
                {
                    int ret = buyCountTotal.compareTo(orderFreight.getFirstWeight());
                    if(ret==-1||ret==0) //如果购买数量<=首件数
                    {
                        ovo.setFreightAmount(orderFreight.getFirstWeightMoney()); //首件价格
                    }else{ //购买数量>首件数
                        //运费金额=((购买数-首件数)/续件)*续件金额
                        BigDecimal freightAmount = ((buyCountTotal.subtract(orderFreight.getFirstWeight())).divide(orderFreight.getAppendWeight(),2, BigDecimal.ROUND_HALF_UP)).multiply(orderFreight.getAppendWeightMoney());
                        ret = freightAmount.compareTo(new BigDecimal(0));
                        if(ret==-1||ret==0) //如果购买数量<=首件数
                        {
                            freightAmount = new BigDecimal(0);
                        }
                        ovo.setFreightAmount(freightAmount.add(orderFreight.getFirstWeightMoney())); //加上首件金额
                    }
                }else if(orderFreight.getValuationMethod().intValue()==2) //按体积
                {
                    int ret = roughWeightTotal.compareTo(orderFreight.getFirstWeight());
                    if(ret==-1||ret==0) //如果购买毛重<=首重
                    {
                        ovo.setFreightAmount(orderFreight.getFirstWeightMoney()); //首重
                    }else{ //购买数量>首件数
                        //运费金额=((购买毛重-首重)/续重)*续重金额
                        BigDecimal freightAmount = ((roughWeightTotal.subtract(orderFreight.getFirstWeight())).divide(orderFreight.getAppendWeight(),2, BigDecimal.ROUND_HALF_UP)).multiply(orderFreight.getAppendWeightMoney());
                        ret = freightAmount.compareTo(new BigDecimal(0));
                        if(ret==-1||ret==0) //如果购买毛重<=首件数
                        {
                            freightAmount = new BigDecimal(0);
                        }
                        ovo.setFreightAmount(freightAmount.add(orderFreight.getFirstWeightMoney())); //加上首重金额
                    }
                }

            }
            ovo.setPayAmount(new BigDecimal(0)); //已支付金额
            ovo.setOrderAmount(orderAmount.add(ovo.getFreightAmount())); //订单最终金额(算完折扣)
            ovo.setTotalAmount(orderAmount.add(ovo.getFreightAmount())); //订单最终金额(不算折扣)
            ovo.setRedPackageAmount(new BigDecimal(0)); //红包金额
            ovo.setCouponAmount(new BigDecimal(0)); //优惠券金额
            ovo.setTradeStatus(0); //交易进行中
            ovo.setPayStatus(0); //待支付
            ovo.setPayMethod(1); //线上支付
            ovo.setCreateDate(new Date());
            ovo.setAppCode(toucan.getAppCode());
            ovo.setUserId(createOrderVo.getUserId());
            ovo.setDeleteStatus((short)0);
            if(createOrderVo.getPayType().intValue()==1) { //微信支付
                ovo.setPayType(0);
            }else if(createOrderVo.getPayType().intValue()==2){ //支付宝
                ovo.setPayType(1);
            }else{
                ovo.setPayType(-1); //支付方式未确定
            }
            //累加子订单金额到主订单中
            mainOrder.setOrderAmount(mainOrder.getOrderAmount().add(ovo.getOrderAmount())); //订单金额
            mainOrder.setFreightAmount(mainOrder.getFreightAmount().add(ovo.getFreightAmount())); //运费总金额
            mainOrder.setTotalAmount(mainOrder.getTotalAmount().add(ovo.getTotalAmount()));  //订单最终金额
        }

        if(createOrderVo.getPayType().intValue()==1) { //微信支付
            mainOrder.setPayType(0);
        }else if(createOrderVo.getPayType().intValue()==2){ //支付宝
            mainOrder.setPayType(1);
        }else{
            mainOrder.setPayType(-1); //支付方式未确定
        }
        mainOrder.setPayAmount(new BigDecimal(0)); //已支付金额
        mainOrder.setRedPackageAmount(new BigDecimal(0)); //红包金额
        mainOrder.setCouponAmount(new BigDecimal(0)); //优惠券金额
        mainOrder.setTradeStatus(0); //交易进行中
        mainOrder.setPayStatus(0); //待支付
        mainOrder.setPayMethod(1); //线上支付
        mainOrder.setCreateDate(new Date());
        mainOrder.setAppCode(toucan.getAppCode());
        mainOrder.setUserId(createOrderVo.getUserId());
        mainOrder.setDeleteStatus((short)0);

        createOrderVo.getMainOrder().setOrders(orders);
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
        orderFreightVO.setValuationMethod(userBuyCarItemVO.getFreightTemplateVO().getValuationMethod());
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
            resultObjectVO = userBuyCarService.listByUserMainId(RequestJsonVOGenerator.generator(toucan.getAppCode(),userBuyCarVO));
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


}
