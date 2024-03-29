package com.toucan.shopping.modules.product.controller;

import com.alibaba.fastjson.JSONObject;
import com.toucan.shopping.modules.product.service.ProductSkuRedisService;
import com.toucan.shopping.modules.common.generator.IdGenerator;
import com.toucan.shopping.modules.common.page.PageInfo;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.common.vo.ResultVO;
import com.toucan.shopping.modules.product.constant.ProductConstant;
import com.toucan.shopping.modules.product.entity.*;
import com.toucan.shopping.modules.product.page.ShopProductPageInfo;
import com.toucan.shopping.modules.product.redis.ShopProductRedisLockKey;
import com.toucan.shopping.modules.product.service.*;
import com.toucan.shopping.modules.product.vo.*;
import com.toucan.shopping.modules.skylark.lock.service.SkylarkLock;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedList;
import java.util.List;

/**
 * 店铺商品
 * @auth majian
 */
@RestController
@RequestMapping("/shopProduct")
public class ShopProductController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private IdGenerator idGenerator;

    @Autowired
    private SkylarkLock skylarkLock;

    @Autowired
    private ProductSkuService productSkuService;

    @Autowired
    private ShopProductService shopProductService;

    @Autowired
    private ShopProductImgService shopProductImgService;


    @Autowired
    private ProductSpuService productSpuService;

    @Autowired
    private ShopProductDescriptionService shopProductDescriptionService;

    @Autowired
    private ShopProductDescriptionImgService shopProductDescriptionImgService;

    @Autowired
    private ProductSkuRedisService productSkuRedisService;



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
            ShopProductPageInfo queryPageInfo = JSONObject.parseObject(requestJsonVO.getEntityJson(), ShopProductPageInfo.class);
            PageInfo<ShopProductVO> pageInfo =  shopProductService.queryListPage(queryPageInfo);

            if(pageInfo.getTotal()!=null&&pageInfo.getTotal().longValue()>0)
            {
                List<Long> shopProductIdList = new LinkedList<>();
                for(ShopProductVO shopProductVO : pageInfo.getList())
                {
                    shopProductVO.setPreviewPhotoPaths(new LinkedList<>());

                    shopProductIdList.add(shopProductVO.getId());
                }
                ShopProductImgVO shopProductImgVO = new ShopProductImgVO();
                shopProductImgVO.setShopProductIdList(shopProductIdList);
                List<ShopProductImg> shopProductImgs = shopProductImgService.queryList(shopProductImgVO);
                if(CollectionUtils.isNotEmpty(shopProductImgs))
                {
                    for(ShopProductImg shopProductImg:shopProductImgs)
                    {
                        for(ShopProductVO shopProductVO:pageInfo.getList()) {
                            if (shopProductImg.getShopProductId() != null &&
                                    shopProductImg.getShopProductId().longValue()==shopProductVO.getId().longValue())
                            {
                                //如果是商品主图
                                if(shopProductImg.getImgType().intValue()==1)
                                {
                                    shopProductVO.setMainPhotoFilePath(shopProductImg.getFilePath());
                                }else if(shopProductImg.getImgType().intValue()==2){
                                    shopProductVO.getPreviewPhotoPaths().add(shopProductImg.getFilePath());
                                }
                                break;
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
     * 查询列表
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/query/list",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO queryList(@RequestBody RequestJsonVO requestJsonVO)
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

            ShopProductVO queryShopProduct = JSONObject.parseObject(requestJsonVO.getEntityJson(), ShopProductVO.class);
            List<ShopProductVO> list =  shopProductService.queryList(queryShopProduct);

            List<Long> shopProductIdList = new LinkedList<>();
            for(ShopProductVO shopProductVO : list)
            {
                shopProductVO.setPreviewPhotoPaths(new LinkedList<>());

                shopProductIdList.add(shopProductVO.getId());
            }
            ShopProductImgVO shopProductImgVO = new ShopProductImgVO();
            shopProductImgVO.setShopProductIdList(shopProductIdList);
            List<ShopProductImg> shopProductImgs = shopProductImgService.queryList(shopProductImgVO);
            if(CollectionUtils.isNotEmpty(shopProductImgs))
            {
                for(ShopProductImg shopProductImg:shopProductImgs)
                {
                    for(ShopProductVO shopProductVO:list) {
                        if (shopProductImg.getShopProductId() != null &&
                                shopProductImg.getShopProductId().longValue()==shopProductVO.getId().longValue())
                        {
                            //如果是商品主图
                            if(shopProductImg.getImgType().intValue()==1)
                            {
                                shopProductVO.setMainPhotoFilePath(shopProductImg.getFilePath());
                            }else if(shopProductImg.getImgType().intValue()==2){
                                shopProductVO.getPreviewPhotoPaths().add(shopProductImg.getFilePath());
                            }
                            break;
                        }

                    }
                }
            }

            resultObjectVO.setData(list);
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
    public ResultObjectVO queryByShopProductId(@RequestBody RequestJsonVO requestJsonVO)
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
            ShopProductVO shopProductVO = JSONObject.parseObject(requestJsonVO.getEntityJson(), ShopProductVO.class);
            if(shopProductVO==null||shopProductVO.getId()==null||shopProductVO.getId().longValue()==-1)
            {
                logger.warn("没有找到ID: param:"+ JSONObject.toJSONString(requestJsonVO));
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("没有找到ID!");
                return resultObjectVO;
            }
            ShopProductVO queryShopProductVO = new ShopProductVO();
            queryShopProductVO.setId(shopProductVO.getId());
            List<ShopProductVO> shopProductVOS = shopProductService.queryList(queryShopProductVO);

            if(CollectionUtils.isNotEmpty(shopProductVOS)) {

                shopProductVO = shopProductVOS.get(0);
                shopProductVO.setPreviewPhotoPaths(new LinkedList<>());

                ProductSkuVO queryProductSku = new ProductSkuVO();
                queryProductSku.setShopProductId(shopProductVO.getId());
                //查询SKU
                List<ProductSkuVO> productSkuVOS = productSkuService.queryList(queryProductSku);
                shopProductVO.setProductSkuVOList(productSkuVOS);

                //查询商品图片
                ShopProductImgVO shopProductImgVO = new ShopProductImgVO();
                shopProductImgVO.setShopProductId(shopProductVO.getId());
                List<ShopProductImg> shopProductImgs = shopProductImgService.queryList(shopProductImgVO);
                if (CollectionUtils.isNotEmpty(shopProductImgs)) {
                    for (ShopProductImg shopProductImg : shopProductImgs) {
                        //如果是商品主图
                        if (shopProductImg.getImgType().intValue() == 1) {
                            shopProductVO.setMainPhotoFilePath(shopProductImg.getFilePath());
                        } else  if (shopProductImg.getImgType().intValue() == 2) {
                            shopProductVO.getPreviewPhotoPaths().add(shopProductImg.getFilePath());
                        }
                    }
                }

                //查询商品名称
                if(shopProductVO.getProductId()!=null) {
                    ProductSpu productSpu = productSpuService.queryByIdIgnoreDelete(shopProductVO.getProductId());
                    if(productSpu!=null)
                    {
                        shopProductVO.setProductSpuName(productSpu.getName());
                    }
                }

                //查询商品介绍
                ShopProductDescription shopProductDescription = shopProductDescriptionService.queryByShopProductId(shopProductVO.getId());
                if(shopProductDescription!=null) {
                    ShopProductDescriptionVO shopProductDescriptionVO = new ShopProductDescriptionVO();
                    BeanUtils.copyProperties(shopProductDescriptionVO,shopProductDescription);

                    List<ShopProductDescriptionImgVO> shopProductDescriptionImgVOS = shopProductDescriptionImgService.queryVOListByProductIdAndDescriptionIdOrderBySortDesc(shopProductVO.getId(),shopProductDescription.getId());
                    shopProductDescriptionVO.setProductDescriptionImgs(shopProductDescriptionImgVOS);
                    shopProductVO.setShopProductDescriptionVO(shopProductDescriptionVO);
                }
            }
            resultObjectVO.setData(shopProductVOS);
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("查询失败!");
        }

        return resultObjectVO;
    }




    /**
     * 商品上架/下架
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/shelves",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO shelves(@RequestBody RequestJsonVO requestJsonVO)
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
        String shopProductIdId ="";
        try {
            logger.info("商品上架/下架 {} ",requestJsonVO.getEntityJson());
            ShopProductVO queryShopProductVO = JSONObject.parseObject(requestJsonVO.getEntityJson(), ShopProductVO.class);
            if(queryShopProductVO.getId()==null) {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("商品ID不能为空!");
                return resultObjectVO;
            }
            if(queryShopProductVO.getShopId()==null) {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("店铺ID不能为空!");
                return resultObjectVO;
            }
            shopProductIdId = String.valueOf(queryShopProductVO.getId());
            skylarkLock.lock(ShopProductRedisLockKey.getResaveProductLockKey(shopProductIdId), shopProductIdId);

            ShopProductVO shopProductVO = shopProductService.findById(queryShopProductVO.getId());
            if(shopProductVO==null)
            {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("该商品不存在!");
                return resultObjectVO;
            }
            //如果当前是上架状态
            if(shopProductVO.getStatus()!=null
                    &&shopProductVO.getStatus().intValue()== ProductConstant.SHELVES_UP.intValue())
            {
                shopProductService.updateStatus(shopProductVO.getId(),shopProductVO.getShopId(),ProductConstant.SHELVES_DOWN); //下架
                productSkuService.updateStatusByShopProductId(shopProductVO.getId(),shopProductVO.getShopId(),ProductConstant.SHELVES_DOWN); //下架
            }else if(shopProductVO.getStatus()!=null
                    &&shopProductVO.getStatus().intValue()== ProductConstant.SHELVES_DOWN.intValue()) //如果当前是下架状态
            {
                shopProductService.updateStatus(shopProductVO.getId(),shopProductVO.getShopId(),ProductConstant.SHELVES_UP); //上架
                productSkuService.updateStatusByShopProductId(shopProductVO.getId(),shopProductVO.getShopId(),ProductConstant.SHELVES_UP); //上架
            }
            ProductSkuVO productSkuVO = new ProductSkuVO();
            productSkuVO.setShopProductId(shopProductVO.getId());
            List<ProductSkuVO> productSkuVOS = productSkuService.queryList(productSkuVO);
            if(CollectionUtils.isNotEmpty(productSkuVOS))
            {
                for(ProductSkuVO ps:productSkuVOS)
                {
                    productSkuRedisService.deleteCache(String.valueOf(ps.getId()));
                }
            }
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("修改失败");
        }finally{
            skylarkLock.unLock(ShopProductRedisLockKey.getResaveProductLockKey(shopProductIdId), shopProductIdId);
        }
        return resultObjectVO;
    }





    /**
     * 根据运费模板ID查询关联的商品
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/query/one/by/freightTemplateId",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO queryOneByFreightTemplateId(@RequestBody RequestJsonVO requestJsonVO) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        if (requestJsonVO == null) {
            logger.warn("请求参数为空");
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请重试!");
            return resultObjectVO;
        }
        if (requestJsonVO.getAppCode() == null) {
            logger.warn("没有找到应用编码: param:" + JSONObject.toJSONString(requestJsonVO));
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("没有找到应用编码!");
            return resultObjectVO;
        }
        try {
            ShopProductVO queryShopProductVO = JSONObject.parseObject(requestJsonVO.getEntityJson(), ShopProductVO.class);
            if(queryShopProductVO.getFreightTemplateId()==null) {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("运费模板ID不能为空!");
                return resultObjectVO;
            }
            if(queryShopProductVO.getShopId()==null) {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("店铺ID不能为空!");
                return resultObjectVO;
            }
            resultObjectVO.setData(shopProductService.queryOne(queryShopProductVO));
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("修改失败");
        }
        return  resultObjectVO;
    }

    /**
     * 根据shop_product_uuid查询
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/query/list/by/shop/product/uuid",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO queryListByShopProductUuid(@RequestBody RequestJsonVO requestJsonVO)
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
            List<ProductSkuVO>  lists = productSkuService.queryProductSkuListByShopProductUuid(requestJsonVO.formatEntity(String.class));
            resultObjectVO.setData(lists);
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("查询失败!");
        }

        return resultObjectVO;
    }


    /**
     * 根据ID删除
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/delete/id",produces = "application/json;charset=UTF-8",method = RequestMethod.DELETE)
    @ResponseBody
    public ResultObjectVO deleteById(@RequestBody RequestJsonVO requestJsonVO)
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
            logger.info("没有找到应用编码: param:"+ JSONObject.toJSONString(requestJsonVO));
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("没有找到应用编码!");
            return resultObjectVO;
        }

        try {
            ShopProductVO shopProductVO = JSONObject.parseObject(requestJsonVO.getEntityJson(), ShopProductVO.class);

            if(shopProductVO==null||shopProductVO.getId()==null||shopProductVO.getId().longValue()==-1)
            {
                logger.warn("没有找到ID: param:"+ JSONObject.toJSONString(requestJsonVO));
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("没有找到ID!");
                return resultObjectVO;
            }
            if(shopProductVO==null||shopProductVO.getShopId()==null||shopProductVO.getShopId().longValue()==-1)
            {
                logger.warn("没有找到店铺ID: param:"+ JSONObject.toJSONString(requestJsonVO));
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("没有找到店铺ID!");
                return resultObjectVO;
            }


            ShopProductVO queryShopProductVO = new ShopProductVO();
            queryShopProductVO.setId(shopProductVO.getId());
            queryShopProductVO.setShopId(shopProductVO.getShopId());

            shopProductVO = shopProductService.queryOne(queryShopProductVO);

            if(shopProductVO!=null) {

                List<ProductSkuVO> productSkuVOS = productSkuService.queryProductSkuListByShopProductId(shopProductVO.getId());
                if (CollectionUtils.isNotEmpty(productSkuVOS)) {
                    for (ProductSkuVO productSkuVO : productSkuVOS) {
                        if (productSkuVO.getId() != null) {
                            productSkuRedisService.deleteCache(String.valueOf(productSkuVO.getId()));
                        }
                    }
                }

                shopProductService.deleteById(shopProductVO.getId());
                productSkuService.deleteByShopProductId(shopProductVO.getId());
                shopProductImgService.deleteByShopProductId(shopProductVO.getId());
                shopProductDescriptionService.deleteByShopProductId(shopProductVO.getId());
                shopProductDescriptionImgService.deleteByShopProductId(shopProductVO.getId());

                //延时双删
                Thread.sleep(ProductConstant.DELETE_REDIS_SLEEP);

                if (CollectionUtils.isNotEmpty(productSkuVOS)) {
                    for (ProductSkuVO productSkuVO : productSkuVOS) {
                        if (productSkuVO.getId() != null) {
                            productSkuRedisService.deleteCache(String.valueOf(productSkuVO.getId()));
                        }
                    }
                }
            }

        }catch(Exception e)
        {
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请重试!");
            logger.warn(e.getMessage(),e);
        }
        return resultObjectVO;
    }




}
