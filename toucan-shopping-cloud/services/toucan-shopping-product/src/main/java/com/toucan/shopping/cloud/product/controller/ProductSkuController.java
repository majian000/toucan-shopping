package com.toucan.shopping.cloud.product.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.toucan.shopping.cloud.product.service.ProductSkuRedisService;
import com.toucan.shopping.modules.common.page.PageInfo;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultListVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.common.vo.ResultVO;
import com.toucan.shopping.modules.product.entity.Brand;
import com.toucan.shopping.modules.product.entity.ProductSku;
import com.toucan.shopping.modules.product.entity.ShopProductDescription;
import com.toucan.shopping.modules.product.entity.ShopProductImg;
import com.toucan.shopping.modules.product.page.ProductSkuPageInfo;
import com.toucan.shopping.modules.product.service.*;
import com.toucan.shopping.modules.product.util.ProductRedisKeyUtil;
import com.toucan.shopping.modules.product.vo.*;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/productSku")
public class ProductSkuController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private ProductSkuService productSkuService;



    @Autowired
    private ProductSkuRedisService productSkuRedisService;

    @Autowired
    private ShopProductService shopProductService;

    @Autowired
    private ShopProductImgService shopProductImgService;

    @Autowired
    private ShopProductDescriptionService shopProductDescriptionService;

    @Autowired
    private ShopProductDescriptionImgService shopProductDescriptionImgService;

    @Autowired
    private BrandService brandService;

    /**
     * 查询所有上架商品
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/shelves/list",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultListVO queryShelvesList(@RequestHeader("toucan-sign-header") String signHeader,@RequestBody RequestJsonVO requestJsonVO)
    {
        Map<String,Object> params=new HashMap<String,Object>();
        params.put("status",1);
        params.put("deleteStatus",0);
        params.put("appCode",requestJsonVO.getAppCode());

        ResultListVO<ProductSku> resultListVO = new ResultListVO<>();
//        resultListVO.setData(productSkuService.queryList(params));
        return resultListVO;
    }


    /**
     * 从缓存或者数据库中查询
     * @param skuId
     * @return
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    private ProductSkuVO queryProductSkuByCacheOrDB(Long skuId) throws InvocationTargetException, IllegalAccessException {
        ProductSkuVO shopProductSkuVO = productSkuRedisService.queryProductSkuById(String.valueOf(skuId));
        if(shopProductSkuVO==null) { //查询数据库然后同步缓存
            shopProductSkuVO = productSkuService.queryVOById(skuId); //查询数据库

            if(shopProductSkuVO!=null) { //如果数据库中这条记录没被删除,就刷新到缓存
                ShopProductVO shopProductVO = shopProductService.findById(shopProductSkuVO.getShopProductId());
                if(shopProductVO!=null) {
                    shopProductSkuVO.setProductAttributes(shopProductVO.getAttributes());
                    shopProductSkuVO.setPreviewPhotoPaths(new LinkedList<>());
                    shopProductSkuVO.setFreightTemplateId(shopProductVO.getFreightTemplateId());
                    shopProductSkuVO.setBuckleInventoryMethod(shopProductVO.getBuckleInventoryMethod()); //库存扣减方式

                    //查询商品图片
                    ShopProductImgVO shopProductImgVO = new ShopProductImgVO();
                    shopProductImgVO.setShopProductId(shopProductVO.getId());
                    List<ShopProductImg> shopProductImgs = shopProductImgService.queryListOrderByImgSortAsc(shopProductImgVO);
                    if (org.apache.commons.collections4.CollectionUtils.isNotEmpty(shopProductImgs)) {
                        for (ShopProductImg shopProductImg : shopProductImgs) {
                            //如果是商品主图
                            if (shopProductImg.getImgType().intValue() == 1) {
                                shopProductSkuVO.setMainPhotoFilePath(shopProductImg.getFilePath());
                            } else if (shopProductImg.getImgType().intValue() == 2) {
                                shopProductSkuVO.getPreviewPhotoPaths().add(shopProductImg.getFilePath());
                            }
                        }
                    }


                    //查询商品介绍
                    ShopProductDescription shopProductDescription = shopProductDescriptionService.queryByShopProductId(shopProductVO.getId());
                    if(shopProductDescription!=null) {
                        ShopProductDescriptionVO shopProductDescriptionVO = new ShopProductDescriptionVO();
                        BeanUtils.copyProperties(shopProductDescriptionVO,shopProductDescription);

                        List<ShopProductDescriptionImgVO> shopProductDescriptionImgVOS = shopProductDescriptionImgService.queryVOListByProductIdAndDescriptionIdOrderBySortDesc(shopProductVO.getId(),shopProductDescription.getId());
                        shopProductDescriptionVO.setProductDescriptionImgs(shopProductDescriptionImgVOS);
                        shopProductSkuVO.setShopProductDescriptionVO(shopProductDescriptionVO);


                        //商品介绍中的属性列表
                        List<ProductSkuAttribute> attributes = new ArrayList<>();


                        ProductSkuAttribute productNameAttribute = new ProductSkuAttribute("",new ArrayList<>());
                        productNameAttribute.setKey("商品名称");
                        productNameAttribute.getValues().add(shopProductVO.getName());
                        attributes.add(productNameAttribute);


                        if(shopProductSkuVO.getSuttle()!=null) {
                            ProductSkuAttribute suttleAttribute = new ProductSkuAttribute("", new ArrayList<>());
                            suttleAttribute.setKey("商品净重");
                            suttleAttribute.getValues().add(String.valueOf(shopProductSkuVO.getSuttle())+"kg");
                            attributes.add(suttleAttribute);
                        }

                        Brand brand = brandService.findByIdIngoreDeleteStatus(shopProductVO.getBrandId());
                        if(brand!=null) {
                            ProductSkuAttribute brandNameAttribute = new ProductSkuAttribute("",new ArrayList<>());
                            brandNameAttribute.setKey("品牌");

                            if(StringUtils.isNotEmpty(brand.getChineseName())&&StringUtils.isNotEmpty(brand.getEnglishName()))
                            {
                                brandNameAttribute.getValues().add(brand.getChineseName()+"/"+brand.getEnglishName());
                            }else {
                                if (StringUtils.isNotEmpty(brand.getChineseName())) {
                                    brandNameAttribute.getValues().add(brand.getChineseName());
                                }
                                if (StringUtils.isNotEmpty(brand.getEnglishName())) {
                                    brandNameAttribute.getValues().add(brand.getEnglishName());
                                }
                            }

                            shopProductDescriptionVO.setBrandNameAttribute(brandNameAttribute);


                            ProductSkuAttribute brandSeminaryAttribute = new ProductSkuAttribute("",new ArrayList<>());
                            brandSeminaryAttribute.setKey("商品产地");
                            if(StringUtils.isNotEmpty(brand.getSeminary())) {
                                brandSeminaryAttribute.getValues().add(brand.getSeminary());
                            }else{
                                brandSeminaryAttribute.getValues().add("");
                            }

                            attributes.add(brandSeminaryAttribute);
                        }

                        shopProductDescriptionVO.setAttributes(attributes);
                    }

                    //查询商品SKU列表
                    shopProductSkuVO.setProductSkuVOList(productSkuService.queryShelvesVOListByShopProductId(shopProductVO.getId()));

                    //刷新到缓存
                    productSkuRedisService.addToCache(shopProductSkuVO);
                }
            }
        }
        return shopProductSkuVO;
    }





    /**
     * 根据ID查询(商城PC端使用)
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/query/id/for/front",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO queryByIdForFront(@RequestBody RequestJsonVO requestJsonVO)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        if(requestJsonVO==null)
        {
            logger.info("请求参数为空");
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请重试!");
            return resultObjectVO;
        }
        if(requestJsonVO.getAppCode()==null)
        {
            logger.info("没有找到应用: param:"+ JSONObject.toJSONString(requestJsonVO));
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("没有找到应用!");
            return resultObjectVO;
        }
        try {
            ProductSku productSku = JSONObject.parseObject(requestJsonVO.getEntityJson(), ProductSku.class);

            if(productSku.getId()==null)
            {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("商品ID不能为空!");
                return resultObjectVO;
            }
            ProductSkuVO shopProductApproveSkuVO =queryProductSkuByCacheOrDB(productSku.getId());
            resultObjectVO.setData(shopProductApproveSkuVO);
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("查询失败!");
        }

        return resultObjectVO;
    }



    /**
     * 根据ID查询,只查询1个sku(商城PC端使用)
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/query/one/by/shop/product/id/for/front",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO queryOneByShopProductIdForFront(@RequestBody RequestJsonVO requestJsonVO)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        if(requestJsonVO==null)
        {
            logger.info("请求参数为空");
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请重试!");
            return resultObjectVO;
        }
        if(requestJsonVO.getAppCode()==null)
        {
            logger.info("没有找到应用: param:"+ JSONObject.toJSONString(requestJsonVO));
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("没有找到应用!");
            return resultObjectVO;
        }
        try {
            ShopProductApproveVO shopProductApproveVO = JSONObject.parseObject(requestJsonVO.getEntityJson(), ShopProductApproveVO.class);
            if(shopProductApproveVO.getId()==null)
            {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("商品ID不能为空!");
                return resultObjectVO;
            }

            ProductSku productSku = productSkuService.queryFirstOneByShopProductId(shopProductApproveVO.getId());
            ProductSkuVO shopProductApproveSkuVO =queryProductSkuByCacheOrDB(productSku.getId());
            resultObjectVO.setData(shopProductApproveSkuVO);
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("查询失败!");
        }

        return resultObjectVO;
    }


    /**
     * 查询列表
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/query/list/page",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO queryListPage(@RequestBody RequestJsonVO requestJsonVO)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        if(requestJsonVO==null)
        {
            logger.warn("请求参数为空");
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请重试!");
            return resultObjectVO;
        }
        if(requestJsonVO.getAppCode()==null)
        {
            logger.warn("没有找到应用编码: param:"+ JSONObject.toJSONString(requestJsonVO));
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("没有找到应用编码!");
            return resultObjectVO;
        }
        try {
            ProductSkuPageInfo queryPageInfo = JSONObject.parseObject(requestJsonVO.getEntityJson(), ProductSkuPageInfo.class);
            PageInfo<ProductSkuVO> pageInfo =  productSkuService.queryListPage(queryPageInfo);
            resultObjectVO.setData(pageInfo);
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("查询失败!");
        }

        return resultObjectVO;
    }





    /**
     * 根据ID查询
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/query/id",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO queryById(@RequestBody RequestJsonVO requestJsonVO)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        if(requestJsonVO==null)
        {
            logger.info("请求参数为空");
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请重试!");
            return resultObjectVO;
        }
        if(requestJsonVO.getAppCode()==null)
        {
            logger.info("没有找到应用: param:"+ JSONObject.toJSONString(requestJsonVO));
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("没有找到应用!");
            return resultObjectVO;
        }
        try {
            ProductSku productSku = JSONObject.parseObject(requestJsonVO.getEntityJson(), ProductSku.class);
            resultObjectVO.setData(productSkuService.queryById(productSku.getId()));
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("查询失败!");
        }

        return resultObjectVO;
    }





    /**
     * 根据ID查询
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/query/ids",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO queryByIdList(@RequestHeader( value = "toucan-sign-header",defaultValue = "-1") String signHeader,@RequestBody RequestJsonVO requestJsonVO)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        if(requestJsonVO==null)
        {
            logger.info("请求参数为空");
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请重试!");
            return resultObjectVO;
        }
        if(requestJsonVO.getAppCode()==null)
        {
            logger.info("没有找到应用: param:"+ JSONObject.toJSONString(requestJsonVO));
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("没有找到应用!");
            return resultObjectVO;
        }
        try {
            List<ProductSkuVO> productSkus = JSONArray.parseArray(requestJsonVO.getEntityJson(),ProductSkuVO.class);
            if(!CollectionUtils.isEmpty(productSkus)) {
                List<ProductSku> productSkuList = new ArrayList<ProductSku>();
                for(ProductSkuVO productSku:productSkus) {
                    if(productSku.getId()!=null) {
                        ProductSkuVO productSkuEntity = queryProductSkuByCacheOrDB(productSku.getId());
                        if (productSkuEntity != null) {
                            productSkuList.add(productSkuEntity);
                        }
                    }else{
                        logger.warn("exists product sku uuid is null {}",requestJsonVO.getEntityJson());
                    }
                }
                resultObjectVO.setData(productSkuList);
            }
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("查询失败!");
        }

        return resultObjectVO;
    }




    /**
     * 扣库存
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(method= RequestMethod.POST,value="/inventoryReduction",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO inventoryReduction(@RequestBody RequestJsonVO requestJsonVO)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        if(requestJsonVO==null|| StringUtils.isEmpty(requestJsonVO.getEntityJson()))
        {
            logger.info("请求参数为空");
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请重试!");
            return resultObjectVO;
        }

        List<InventoryReductionVO> inventoryReductions = requestJsonVO.formatEntityList(InventoryReductionVO.class);

        logger.info("扣库存: param: {} ",requestJsonVO.getEntityJson());
        if(CollectionUtils.isEmpty(inventoryReductions))
        {
            logger.info("没有找到扣除库存的商品: param:{} " , requestJsonVO.getEntityJson());
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("没有找到商品!");
            return resultObjectVO;
        }
        if(requestJsonVO.getAppCode()==null)
        {
            logger.info("没有找到应用: param: {} ",requestJsonVO.getAppCode());
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("没有找到应用!");
            return resultObjectVO;
        }

        for(InventoryReductionVO inventoryReductionVO:inventoryReductions) {
            if (StringUtils.isEmpty(inventoryReductionVO.getUserId())) {
                logger.info("没有找到用户: param: {} " , JSONObject.toJSONString(inventoryReductionVO));
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("没有找到用户");
                return resultObjectVO;
            }
            if (inventoryReductionVO.getProductSkuId()==null) {
                logger.info("没有找到商品ID: param: {} " , JSONObject.toJSONString(inventoryReductionVO));
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("没有找到商品ID");
                return resultObjectVO;
            }
            if (inventoryReductionVO.getStockNum()==null||inventoryReductionVO.getStockNum().intValue()<=0) {
                logger.info("扣库存数量不能为0: param: {} " , JSONObject.toJSONString(inventoryReductionVO));
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("扣库存数量不能为0");
                return resultObjectVO;
            }
        }

        List<InventoryReductionVO> restoreInventoryReductions = new LinkedList<>();
        try {
            //校验库存数量是否足够
            List<Long> productSkuIds = inventoryReductions.stream().map(InventoryReductionVO::getProductSkuId).distinct().collect(Collectors.toList());
            List<ProductSku> productSkus = productSkuService.queryShelvesListByIdList(productSkuIds);
            boolean isFind = false;
            for(InventoryReductionVO inventoryReductionVO:inventoryReductions) {
                isFind = false;
                for(ProductSku productSku:productSkus)
                {
                    if(productSku.getId().longValue()==inventoryReductionVO.getProductSkuId().longValue())
                    {
                        if(productSku.getStockNum().intValue()<inventoryReductionVO.getStockNum().intValue())
                        {
                            resultObjectVO.setCode(ResultVO.FAILD);
                            resultObjectVO.setMsg("没有库存了");
                            resultObjectVO.setData(inventoryReductionVO);
                            return resultObjectVO;
                        }
                        isFind = true;
                        break;
                    }
                }
                if(!isFind)
                {
                    resultObjectVO.setCode(ResultVO.FAILD);
                    resultObjectVO.setMsg("商品已下架");
                    resultObjectVO.setData(inventoryReductionVO);
                    return resultObjectVO;
                }
            }

            for(InventoryReductionVO inventoryReductionVO:inventoryReductions) {
                logger.info("扣库存: skuId:{} stockNum:{} userId:{} " , inventoryReductionVO.getProductSkuId(),inventoryReductionVO.getStockNum(),inventoryReductionVO.getUserId());
                int row = productSkuService.inventoryReduction(inventoryReductionVO.getProductSkuId(),inventoryReductionVO.getStockNum());
                if (row <= 0) {
                    logger.warn("没有库存了 skuId:{} stockNum:{} userId:{} " , inventoryReductionVO.getProductSkuId(),inventoryReductionVO.getStockNum(),inventoryReductionVO.getUserId());
                    throw new IllegalArgumentException("没有库存了");
                }else{ //清空商品缓存
                    restoreInventoryReductions.add(inventoryReductionVO);
                    productSkuRedisService.deleteCache(String.valueOf(inventoryReductionVO.getProductSkuId()));
                }
            }
        }catch(Exception e)
        {
            //还原库存
            if(!CollectionUtils.isEmpty(restoreInventoryReductions))
            {
                for(InventoryReductionVO inventoryReductionVO:restoreInventoryReductions) {
                    int row = productSkuService.restoreStock(inventoryReductionVO.getProductSkuId(),inventoryReductionVO.getStockNum());
                    if (row <= 0) {
                        logger.warn("还原扣库存失败 skuId:{} stockNum:{} userId:{} " , inventoryReductionVO.getProductSkuId(),inventoryReductionVO.getStockNum(),inventoryReductionVO.getUserId());
                    }
                }
            }
            logger.warn(e.getMessage(),e);
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("扣库存失败!");
        }
        return resultObjectVO;
    }




}
