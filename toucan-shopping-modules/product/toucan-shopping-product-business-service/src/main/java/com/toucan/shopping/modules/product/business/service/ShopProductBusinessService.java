package com.toucan.shopping.modules.product.business.service;

import com.alibaba.fastjson2.JSONObject;
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
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import com.toucan.shopping.modules.common.annotation.RequestCheck;
import com.toucan.shopping.modules.common.exception.BusinessValidationException;
import com.toucan.shopping.modules.common.util.Check;

@Service
public class ShopProductBusinessService {

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
    @RequestCheck(requireEntity = true)
    public ResultObjectVO queryListPage(RequestJsonVO requestJsonVO)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
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
        }catch(BusinessValidationException e){
            return ResultObjectVO.fail(e.getCode(), e.getMessage());
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);
            return ResultObjectVO.fail(ResultVO.FAILD, "查询失败!");
        }

        return resultObjectVO;
    }

    /**
     * 查询列表
     * @param requestJsonVO
     * @return
     */
    @RequestCheck(requireEntity = true)
    public ResultObjectVO queryList(RequestJsonVO requestJsonVO)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
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
        }catch(BusinessValidationException e){
            return ResultObjectVO.fail(e.getCode(), e.getMessage());
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);
            return ResultObjectVO.fail(ResultVO.FAILD, "查询失败!");
        }

        return resultObjectVO;
    }

    /**
     * 根据ID查询
     * @param requestJsonVO
     * @return
     */
    @RequestCheck(requireEntity = true)
    public ResultObjectVO queryByShopProductId(RequestJsonVO requestJsonVO)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            ShopProductVO shopProductVO = JSONObject.parseObject(requestJsonVO.getEntityJson(), ShopProductVO.class);
            Check.notNull(shopProductVO, ResultVO.FAILD, "没有找到ID!");
            Check.notNull(shopProductVO.getId(), ResultVO.FAILD, "没有找到ID!");
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
        }catch(BusinessValidationException e){
            return ResultObjectVO.fail(e.getCode(), e.getMessage());
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);
            return ResultObjectVO.fail(ResultVO.FAILD, "查询失败!");
        }

        return resultObjectVO;
    }

    /**
     * 商品上架/下架
     * @param requestJsonVO
     * @return
     */
    @RequestCheck(requireEntity = true)
    public ResultObjectVO shelves(RequestJsonVO requestJsonVO)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        String shopProductIdId ="";
        try {
            logger.info("商品上架/下架 {} ",requestJsonVO.getEntityJson());
            ShopProductVO queryShopProductVO = JSONObject.parseObject(requestJsonVO.getEntityJson(), ShopProductVO.class);
            Check.notNull(queryShopProductVO.getId(), ResultVO.FAILD, "商品ID不能为空!");
            Check.notNull(queryShopProductVO.getShopId(), ResultVO.FAILD, "店铺ID不能为空!");
            shopProductIdId = String.valueOf(queryShopProductVO.getId());
            skylarkLock.lock(ShopProductRedisLockKey.getResaveProductLockKey(shopProductIdId), shopProductIdId);

            ShopProductVO shopProductVO = shopProductService.findById(queryShopProductVO.getId());
            Check.notNull(shopProductVO, ResultVO.FAILD, "该商品不存在!");
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
        }catch(BusinessValidationException e){
            return ResultObjectVO.fail(e.getCode(), e.getMessage());
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);
            return ResultObjectVO.fail(ResultVO.FAILD, "修改失败");
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
    @RequestCheck(requireEntity = true)
    public ResultObjectVO queryOneByFreightTemplateId(RequestJsonVO requestJsonVO) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            ShopProductVO queryShopProductVO = JSONObject.parseObject(requestJsonVO.getEntityJson(), ShopProductVO.class);
            Check.notNull(queryShopProductVO.getFreightTemplateId(), ResultVO.FAILD, "运费模板ID不能为空!");
            Check.notNull(queryShopProductVO.getShopId(), ResultVO.FAILD, "店铺ID不能为空!");
            resultObjectVO.setData(shopProductService.queryOne(queryShopProductVO));
        }catch(BusinessValidationException e){
            return ResultObjectVO.fail(e.getCode(), e.getMessage());
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);
            return ResultObjectVO.fail(ResultVO.FAILD, "查询失败");
        }
        return  resultObjectVO;
    }

    /**
     * 根据shop_product_uuid查询
     * @param requestJsonVO
     * @return
     */
    @RequestCheck(requireEntity = true)
    public ResultObjectVO queryListByShopProductUuid(RequestJsonVO requestJsonVO)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            List<ProductSkuVO>  lists = productSkuService.queryProductSkuListByShopProductUuid(requestJsonVO.formatEntity(String.class));
            resultObjectVO.setData(lists);
        }catch(BusinessValidationException e){
            return ResultObjectVO.fail(e.getCode(), e.getMessage());
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);
            return ResultObjectVO.fail(ResultVO.FAILD, "查询失败!");
        }

        return resultObjectVO;
    }

    /**
     * 根据ID删除
     * @param requestJsonVO
     * @return
     */
    @RequestCheck(requireEntity = true)
    public ResultObjectVO deleteById(RequestJsonVO requestJsonVO)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();

        try {
            ShopProductVO shopProductVO = JSONObject.parseObject(requestJsonVO.getEntityJson(), ShopProductVO.class);

            Check.notNull(shopProductVO, ResultVO.FAILD, "没有找到ID!");
            Check.notNull(shopProductVO.getId(), ResultVO.FAILD, "没有找到ID!");
            Check.notNull(shopProductVO.getShopId(), ResultVO.FAILD, "没有找到店铺ID!");

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

        }catch(BusinessValidationException e){
            return ResultObjectVO.fail(e.getCode(), e.getMessage());
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);
            return ResultObjectVO.fail(ResultVO.FAILD, "请重试!");
        }
        return resultObjectVO;
    }

    /**
     * 修改运费模板
     * @param requestJsonVO
     * @return
     */
    @RequestCheck(requireEntity = true)
    public ResultObjectVO updateFreightTemplate(RequestJsonVO requestJsonVO)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();

        try {
            ShopProductVO shopProductVO = JSONObject.parseObject(requestJsonVO.getEntityJson(), ShopProductVO.class);

            Check.notNull(shopProductVO, ResultVO.FAILD, "没有找到ID!");
            Check.notNull(shopProductVO.getId(), ResultVO.FAILD, "没有找到ID!");
            Check.notNull(shopProductVO.getShopId(), ResultVO.FAILD, "没有找到店铺ID!");
            Check.notNull(shopProductVO.getFreightTemplateId(), ResultVO.FAILD, "没有找到运费模板ID!");

            ShopProductVO updateShopProductVO = new ShopProductVO();
            updateShopProductVO.setId(shopProductVO.getId());
            updateShopProductVO.setShopId(shopProductVO.getShopId());
            updateShopProductVO.setFreightTemplateId(shopProductVO.getFreightTemplateId());

            int ret = shopProductService.updateFreightTemplate(updateShopProductVO);
            if(ret<=0){
                return ResultObjectVO.fail(ResultVO.FAILD, "修改失败!");
            }

        }catch(BusinessValidationException e){
            return ResultObjectVO.fail(e.getCode(), e.getMessage());
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);
            return ResultObjectVO.fail(ResultVO.FAILD, "请重试!");
        }
        return resultObjectVO;
    }

}
