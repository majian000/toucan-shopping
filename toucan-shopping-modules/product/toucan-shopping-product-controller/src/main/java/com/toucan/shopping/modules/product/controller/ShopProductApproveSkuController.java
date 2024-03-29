package com.toucan.shopping.modules.product.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.toucan.shopping.modules.product.service.ShopProductApproveSkuRedisService;
import com.toucan.shopping.modules.common.page.PageInfo;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.common.vo.ResultVO;
import com.toucan.shopping.modules.product.entity.Brand;
import com.toucan.shopping.modules.product.entity.ShopProductApproveDescription;
import com.toucan.shopping.modules.product.entity.ShopProductApproveImg;
import com.toucan.shopping.modules.product.entity.ShopProductApproveSku;
import com.toucan.shopping.modules.product.page.ShopProductApproveSkuPageInfo;
import com.toucan.shopping.modules.product.service.*;
import com.toucan.shopping.modules.product.vo.*;
import com.toucan.shopping.modules.redis.service.ToucanStringRedisService;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@RestController
@RequestMapping("/shopProductApproveSku")
public class ShopProductApproveSkuController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private ShopProductApproveSkuService shopProductApproveSkuService;

    @Autowired
    private ShopProductApproveService shopProductApproveService;

    @Autowired
    private ShopProductApproveDescriptionService shopProductApproveDescriptionService;

    @Autowired
    private ShopProductApproveDescriptionImgService shopProductApproveDescriptionImgService;

    @Autowired
    private ShopProductApproveImgService shopProductApproveImgService;

    @Autowired
    private ToucanStringRedisService toucanStringRedisService;

    @Autowired
    private ShopProductApproveSkuRedisService shopProductApproveSkuRedisService;

    @Autowired
    private BrandService brandService;

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
            ShopProductApproveSkuPageInfo queryPageInfo = JSONObject.parseObject(requestJsonVO.getEntityJson(), ShopProductApproveSkuPageInfo.class);
            PageInfo<ShopProductApproveSkuVO> pageInfo =  shopProductApproveSkuService.queryListPage(queryPageInfo);
            if(!CollectionUtils.isEmpty(pageInfo.getList()))
            {
                ShopProductApproveDescription shopProductApproveDescription = shopProductApproveDescriptionService.queryByApproveId(queryPageInfo.getProductApproveId());
                if(shopProductApproveDescription!=null) {
                    List<ShopProductApproveDescriptionImgVO> shopProductApproveDescriptionImgVOS = shopProductApproveDescriptionImgService.queryVOListByProductApproveIdAndDescriptionIdOrderBySortDesc(queryPageInfo.getProductApproveId(),shopProductApproveDescription.getId());
                    if(!CollectionUtils.isEmpty(shopProductApproveDescriptionImgVOS))
                    {
                        for(ShopProductApproveSkuVO shopProductApproveSkuVO:pageInfo.getList()) {
                            for (ShopProductApproveDescriptionImgVO shopProductApproveDescriptionImgVO : shopProductApproveDescriptionImgVOS) {
                                if(shopProductApproveDescriptionImgVO.getType()==2
                                        &&shopProductApproveDescriptionImgVO.getProductSkuId()!=null
                                        &&shopProductApproveSkuVO.getId().longValue()==shopProductApproveDescriptionImgVO.getProductSkuId().longValue())
                                {
                                    shopProductApproveSkuVO.setDescriptionImgFilePath(shopProductApproveDescriptionImgVO.getFilePath());
                                    break;
                                }
                            }
                        }
                    }
                }
            }
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
            ShopProductApproveSku productSku = JSONObject.parseObject(requestJsonVO.getEntityJson(), ShopProductApproveSku.class);
            resultObjectVO.setData(shopProductApproveSkuService.queryById(productSku.getId()));
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("查询失败!");
        }

        return resultObjectVO;
    }


    /**
     * 从缓存或者数据库中查询
     * @param skuId
     * @return
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    private ShopProductApproveSkuVO queryProductApproveSkuByCacheOrDB(Long skuId) throws InvocationTargetException, IllegalAccessException {
        ShopProductApproveSkuVO shopProductApproveSkuVO = shopProductApproveSkuRedisService.queryProductApproveSku(String.valueOf(skuId));
        if(shopProductApproveSkuVO==null) { //查询数据库然后同步缓存
            shopProductApproveSkuVO = shopProductApproveSkuService.queryVOById(skuId);

            if(shopProductApproveSkuVO!=null) { //如果数据库中这条记录没被删除,就刷新到缓存
                ShopProductApproveVO shopProductApproveVO = shopProductApproveService.queryById(shopProductApproveSkuVO.getProductApproveId());
                if(shopProductApproveVO!=null) {
                    shopProductApproveSkuVO.setProductAttributes(shopProductApproveVO.getAttributes());
                    shopProductApproveSkuVO.setPreviewPhotoPaths(new LinkedList<>());

                    //查询商品图片
                    ShopProductApproveImgVO shopProductImgVO = new ShopProductApproveImgVO();
                    shopProductImgVO.setProductApproveId(shopProductApproveVO.getId());
                    List<ShopProductApproveImg> shopProductImgs = shopProductApproveImgService.queryListOrderByImgSortAsc(shopProductImgVO);
                    if (!CollectionUtils.isEmpty(shopProductImgs)) {
                        for (ShopProductApproveImg shopProductImg : shopProductImgs) {
                            //如果是商品主图
                            if (shopProductImg.getImgType().intValue() == 1) {
                                shopProductApproveSkuVO.setMainPhotoFilePath(shopProductImg.getFilePath());
                            } else if (shopProductImg.getImgType().intValue() == 2) {
                                shopProductApproveSkuVO.getPreviewPhotoPaths().add(shopProductImg.getFilePath());
                            }
                        }
                    }


                    //查询商品介绍
                    ShopProductApproveDescription shopProductApproveDescription = shopProductApproveDescriptionService.queryByApproveId(shopProductApproveVO.getId());
                    if(shopProductApproveDescription!=null) {
                        ShopProductApproveDescriptionVO shopProductApproveDescriptionVO = new ShopProductApproveDescriptionVO();
                        BeanUtils.copyProperties(shopProductApproveDescriptionVO,shopProductApproveDescription);

                        List<ShopProductApproveDescriptionImgVO> shopProductApproveDescriptionImgVOS = shopProductApproveDescriptionImgService.queryVOListByProductApproveIdAndDescriptionIdOrderBySortDesc(shopProductApproveVO.getId(),shopProductApproveDescription.getId());
                        if(!CollectionUtils.isEmpty(shopProductApproveDescriptionImgVOS))
                        {
                            shopProductApproveDescriptionVO.setProductDescriptionImgs(new LinkedList<>());
                            for(ShopProductApproveDescriptionImgVO shopProductApproveDescriptionImgVO:shopProductApproveDescriptionImgVOS)
                            {
                                if(shopProductApproveDescriptionImgVO.getType().intValue()==1)
                                {
                                    shopProductApproveDescriptionVO.getProductDescriptionImgs().add(shopProductApproveDescriptionImgVO);
                                }
                                if(shopProductApproveDescriptionImgVO.getType().intValue()==2
                                        &&shopProductApproveDescriptionImgVO.getProductSkuId().longValue()==skuId.longValue())
                                {
                                    shopProductApproveDescriptionVO.getProductDescriptionImgs().add(shopProductApproveDescriptionImgVO);
                                }
                            }
                        }
                        shopProductApproveSkuVO.setShopProductApproveDescriptionVO(shopProductApproveDescriptionVO);


                        //商品介绍中的属性列表
                        List<ShopProductApproveSkuAttribute> attributes = new ArrayList<>();


                        ShopProductApproveSkuAttribute productNameAttribute = new ShopProductApproveSkuAttribute("",new ArrayList<>());
                        productNameAttribute.setKey("商品名称");
                        productNameAttribute.getValues().add(shopProductApproveVO.getName());
                        attributes.add(productNameAttribute);

                        Brand brand = brandService.findByIdIngoreDeleteStatus(shopProductApproveVO.getBrandId());
                        if(brand!=null) {
                            ShopProductApproveSkuAttribute brandNameAttribute = new ShopProductApproveSkuAttribute("",new ArrayList<>());
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

                            shopProductApproveDescriptionVO.setBrandNameAttribute(brandNameAttribute);


                            ShopProductApproveSkuAttribute brandSeminaryAttribute = new ShopProductApproveSkuAttribute("",new ArrayList<>());
                            brandSeminaryAttribute.setKey("商品产地");
                            if(StringUtils.isNotEmpty(brand.getSeminary())) {
                                brandSeminaryAttribute.getValues().add(brand.getSeminary());
                            }else{
                                brandSeminaryAttribute.getValues().add("");
                            }

                            attributes.add(brandSeminaryAttribute);
                        }

                        shopProductApproveDescriptionVO.setAttributes(attributes);
                    }

                    //查询商品SKU列表
                    shopProductApproveSkuVO.setProductSkuVOList(shopProductApproveSkuService.queryVOListByApproveId(shopProductApproveVO.getId()));

                    //刷新到缓存
                    shopProductApproveSkuRedisService.addToCache(shopProductApproveSkuVO);
                }
            }
        }
        return shopProductApproveSkuVO;
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
            ShopProductApproveSku productSku = JSONObject.parseObject(requestJsonVO.getEntityJson(), ShopProductApproveSku.class);

            if(productSku.getId()==null)
            {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("商品ID不能为空!");
                return resultObjectVO;
            }
            ShopProductApproveSkuVO shopProductApproveSkuVO =queryProductApproveSkuByCacheOrDB(productSku.getId());
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
    @RequestMapping(value="/query/one/by/productApproveId/for/front",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO queryOneByProductApproveIdForFront(@RequestBody RequestJsonVO requestJsonVO)
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

            ShopProductApproveSku productSku = shopProductApproveSkuService.queryFirstOneByProductApproveId(shopProductApproveVO.getId());
            ShopProductApproveSkuVO shopProductApproveSkuVO =queryProductApproveSkuByCacheOrDB(productSku.getId());
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
     * 根据ID查询
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/query/ids",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO queryByIdList(@RequestHeader("toucan-sign-header") String signHeader,@RequestBody RequestJsonVO requestJsonVO)
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
            List<ShopProductApproveSku> productSkus = JSONArray.parseArray(requestJsonVO.getEntityJson(),ShopProductApproveSku.class);
            if(!CollectionUtils.isEmpty(productSkus)) {
                List<ShopProductApproveSku> productSkuList = new ArrayList<ShopProductApproveSku>();
                for(ShopProductApproveSku productSku:productSkus) {
                    if(StringUtils.isNotEmpty(productSku.getUuid())) {
                        ShopProductApproveSku productSkuEntity= shopProductApproveSkuService.queryByUuid(productSku.getUuid());
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







}
