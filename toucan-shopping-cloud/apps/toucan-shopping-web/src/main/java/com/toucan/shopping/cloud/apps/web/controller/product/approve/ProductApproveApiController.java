package com.toucan.shopping.cloud.apps.web.controller.product.approve;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.toucan.shopping.cloud.apps.web.vo.BuyResultVo;
import com.toucan.shopping.cloud.apps.web.vo.BuyVo;
import com.toucan.shopping.cloud.common.data.api.feign.service.FeignCategoryService;
import com.toucan.shopping.cloud.order.api.feign.service.FeignOrderService;
import com.toucan.shopping.cloud.product.api.feign.service.*;
import com.toucan.shopping.cloud.seller.api.feign.service.FeignSellerShopService;
import com.toucan.shopping.cloud.seller.api.feign.service.FeignShopCategoryService;
import com.toucan.shopping.cloud.stock.api.feign.service.FeignProductSkuStockService;
import com.toucan.shopping.modules.category.vo.CategoryVO;
import com.toucan.shopping.modules.common.generator.RequestJsonVOGenerator;
import com.toucan.shopping.modules.common.persistence.event.entity.EventProcess;
import com.toucan.shopping.modules.common.persistence.event.service.EventProcessService;
import com.toucan.shopping.modules.common.persistence.event.service.EventPublishService;
import com.toucan.shopping.modules.common.properties.Toucan;
import com.toucan.shopping.modules.common.util.DateUtils;
import com.toucan.shopping.modules.common.util.SignUtil;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.common.vo.ResultVO;
import com.toucan.shopping.modules.image.upload.service.ImageUploadService;
import com.toucan.shopping.modules.order.no.OrderNoService;
import com.toucan.shopping.modules.order.vo.CreateOrderVo;
import com.toucan.shopping.modules.product.entity.ProductSku;
import com.toucan.shopping.modules.product.util.ProductRedisKeyUtil;
import com.toucan.shopping.modules.product.vo.*;
import com.toucan.shopping.modules.seller.vo.SellerShopVO;
import com.toucan.shopping.modules.seller.vo.ShopCategoryVO;
import com.toucan.shopping.modules.skylark.lock.service.SkylarkLock;
import com.toucan.shopping.modules.stock.entity.ProductSkuStock;
import com.toucan.shopping.modules.stock.kafka.constant.StockMessageTopicConstant;
import com.toucan.shopping.modules.stock.vo.InventoryReductionVo;
import com.toucan.shopping.modules.stock.vo.RestoreStockVo;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@RestController
@RequestMapping("/api/product/approve")
public class ProductApproveApiController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private Toucan toucan;


    @Autowired
    private FeignShopProductApproveService feignShopProductApproveService;

    @Autowired
    private FeignCategoryService feignCategoryService;

    @Autowired
    private ImageUploadService imageUploadService;

    @Autowired
    private FeignBrandService feignBrandService;

    @Autowired
    private FeignShopCategoryService feignShopCategoryService;

    @Autowired
    private FeignSellerShopService feignSellerShopService;

    @Autowired
    private FeignShopProductApproveSkuService feignShopProductApproveSkuService;

    @Autowired
    private FeignProductSpuService feignProductSpuService;


    @Autowired
    private EventPublishService eventPublishService;




    /**
     * 查询类别信息
     * @param list
     * @param categoryIds
     */
    void queryCategory(List<ShopProductApproveVO> list,Long[] categoryIds)
    {
        try {
            //查询类别名称
            CategoryVO queryCategoryVO = new CategoryVO();
            queryCategoryVO.setIdArray(categoryIds);
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(), queryCategoryVO);
            ResultObjectVO resultObjectVO = feignCategoryService.findByIdArray(requestJsonVO.sign(), requestJsonVO);
            if (resultObjectVO.isSuccess()) {
                List<CategoryVO> categoryVOS = resultObjectVO.formatDataList(CategoryVO.class);
                if (CollectionUtils.isNotEmpty(categoryVOS)) {
                    for (ShopProductApproveVO shopProductVO : list) {
                        for (CategoryVO categoryVO : categoryVOS) {
                            if (shopProductVO.getCategoryId() != null && shopProductVO.getCategoryId().longValue() == categoryVO.getId().longValue()) {
                                shopProductVO.setCategoryName(categoryVO.getName());
                                shopProductVO.setCategoryPath(categoryVO.getNamePath());
                                break;
                            }
                        }
                    }
                }
            }
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);
        }
    }


    /**
     * 查询店铺类别
     * @param list
     * @param shopCategoryIds
     */
    void queryShopCategory(List<ShopProductApproveVO> list,Long[] shopCategoryIds)
    {
        try {
            ShopCategoryVO queryShopCategoryVO = new ShopCategoryVO();
            queryShopCategoryVO.setIdArray(shopCategoryIds);
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(),queryShopCategoryVO);
            ResultObjectVO resultObjectVO = feignShopCategoryService.findByIdArray(requestJsonVO);
            if(resultObjectVO.isSuccess())
            {
                List<ShopCategoryVO> shopCategoryVOS = resultObjectVO.formatDataList(ShopCategoryVO.class);
                if(CollectionUtils.isNotEmpty(shopCategoryVOS))
                {
                    for(ShopProductApproveVO shopProductVO:list)
                    {
                        for(ShopCategoryVO shopCategoryVO:shopCategoryVOS)
                        {
                            if(shopProductVO.getShopCategoryId()!=null&&shopProductVO.getShopCategoryId().longValue()==shopCategoryVO.getId().longValue())
                            {
                                shopProductVO.setShopCategoryName(shopCategoryVO.getName());
                                shopProductVO.setShopCategoryPath(shopCategoryVO.getNamePath());
                                break;
                            }
                        }
                    }
                }
            }
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);
        }
    }

    /**
     * 查询品牌
     * @param list
     * @param brandIdList
     */
    void queryBrand(List<ShopProductApproveVO> list,List<Long> brandIdList)
    {
        try {
            BrandVO queryBrandVO = new BrandVO();
            queryBrandVO.setIdList(brandIdList);
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(),queryBrandVO);
            ResultObjectVO resultObjectVO = feignBrandService.findByIdList(requestJsonVO);
            if(resultObjectVO.isSuccess())
            {
                List<BrandVO> brandVOS = resultObjectVO.formatDataList(BrandVO.class);
                if(CollectionUtils.isNotEmpty(brandVOS))
                {
                    for(ShopProductApproveVO shopProductVO:list)
                    {
                        for(BrandVO brandVO:brandVOS)
                        {
                            if(shopProductVO.getBrandId()!=null&&shopProductVO.getBrandId().longValue()==brandVO.getId().longValue())
                            {
                                shopProductVO.setBrandChineseName(brandVO.getChineseName());
                                shopProductVO.setBrandEnglishName(brandVO.getEnglishName());
                                shopProductVO.setBrandLogo(brandVO.getLogoPath());
                                if(brandVO.getLogoPath()!=null) {
                                    shopProductVO.setBrandHttpLogo(imageUploadService.getImageHttpPrefix() +brandVO.getLogoPath());
                                }
                                break;
                            }
                        }
                    }
                }
            }
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);
        }
    }


    /**
     * 查询店铺
     * @param list
     * @param shopIdList
     */
    void queryShop(List<ShopProductApproveVO> list,List<Long> shopIdList)
    {
        try {
            SellerShopVO queryShopVO = new SellerShopVO();
            queryShopVO.setIdList(shopIdList);
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(),queryShopVO);
            ResultObjectVO resultObjectVO = feignSellerShopService.findByIdList(requestJsonVO);
            if(resultObjectVO.isSuccess())
            {
                List<SellerShopVO> sellerShopVOS = resultObjectVO.formatDataList(SellerShopVO.class);
                if(CollectionUtils.isNotEmpty(sellerShopVOS))
                {
                    for(ShopProductApproveVO shopProductVO:list)
                    {
                        for(SellerShopVO sellerShopVO:sellerShopVOS)
                        {
                            if(shopProductVO.getShopId()!=null&&shopProductVO.getShopId().longValue()==sellerShopVO.getId().longValue())
                            {
                                shopProductVO.setShopName(sellerShopVO.getName());
                                break;
                            }
                        }
                    }
                }
            }
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);
        }
    }





    /**
     * 查询品牌
     * @param list
     * @param brandIdList
     */
    void queryBrandForProductSpu(List<ProductSpuVO> list, List<Long> brandIdList)
    {
        try {
            BrandVO queryBrandVO = new BrandVO();
            queryBrandVO.setIdList(brandIdList);
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(),queryBrandVO);
            ResultObjectVO resultObjectVO = feignBrandService.findByIdList(requestJsonVO);
            if(resultObjectVO.isSuccess())
            {
                List<BrandVO> brandVOS = resultObjectVO.formatDataList(BrandVO.class);
                if(CollectionUtils.isNotEmpty(brandVOS))
                {
                    for(ProductSpuVO productSpuVO:list)
                    {
                        for(BrandVO brandVO:brandVOS)
                        {
                            if(productSpuVO.getBrandId()!=null&&productSpuVO.getBrandId().longValue()==brandVO.getId().longValue())
                            {
                                productSpuVO.setBrandChineseName(brandVO.getChineseName());
                                productSpuVO.setBrandEnglishName(brandVO.getEnglishName());
                                productSpuVO.setBrandLogo(brandVO.getLogoPath());
                                if(brandVO.getLogoPath()!=null) {
                                    productSpuVO.setBrandHttpLogo(imageUploadService.getImageHttpPrefix() +brandVO.getLogoPath());
                                }
                                break;
                            }
                        }
                    }
                }
            }
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);
        }
    }



    @RequestMapping(value = "/detail",method = RequestMethod.POST)
    public ResultObjectVO detail(@RequestBody ShopProductApproveVO shopProductApproveVO)
    {
        ResultObjectVO retObject = new ResultObjectVO();
        try {
            ShopProductApproveVO shopProductVO = new ShopProductApproveVO();
            shopProductVO.setId(shopProductApproveVO.getId());;
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(),shopProductVO);
            ResultObjectVO resultObjectVO = feignShopProductApproveService.queryByProductApproveId(requestJsonVO);
            if(resultObjectVO.isSuccess())
            {
                List<ShopProductApproveVO> list = resultObjectVO.formatDataList(ShopProductApproveVO.class);
                if(org.apache.commons.collections.CollectionUtils.isNotEmpty(list)) {
                    Long[] categoryIds = new Long[list.size()];
                    Long[] shopCategoryIds = new Long[list.size()];
                    List<Long> brandIdList = new LinkedList<>();
                    List<Long> shopIdList =new LinkedList<>();

                    boolean brandExists=false;
                    boolean shopCategoryExists=false;
                    boolean shopExists=false;
                    for(int i=0;i<list.size();i++)
                    {
                        ShopProductApproveVO shopProductVOTmp = list.get(i);
                        categoryIds[i] = shopProductVOTmp.getCategoryId();

                        //设置品牌ID
                        brandExists=false;
                        for(Long brandId:brandIdList)
                        {
                            if(shopProductVOTmp.getBrandId()!=null&&brandId!=null
                                    &&brandId.longValue()==shopProductVOTmp.getBrandId().longValue())
                            {
                                brandExists=true;
                                break;
                            }

                        }
                        if(!brandExists) {
                            if(shopProductVOTmp.getBrandId()!=null) {
                                brandIdList.add(shopProductVOTmp.getBrandId());
                            }
                        }


                        //设置店铺分类ID
                        shopCategoryExists=false;
                        for(int sci=0;sci<shopCategoryIds.length;sci++)
                        {
                            Long shopCategoryId = shopCategoryIds[sci];
                            if(shopProductVOTmp.getShopCategoryId()!=null&&shopCategoryId!=null
                                    &&shopCategoryId.longValue()==shopProductVOTmp.getShopCategoryId().longValue())
                            {
                                shopCategoryExists=true;
                                break;
                            }

                        }
                        if(!shopCategoryExists) {
                            if(shopProductVOTmp.getShopCategoryId()!=null) {
                                shopCategoryIds[i] = shopProductVOTmp.getShopCategoryId();
                            }
                        }



                        //设置店铺ID
                        shopExists=false;
                        for(Long shopId:shopIdList)
                        {
                            if(shopProductVOTmp.getShopId()!=null&&shopId!=null
                                    &&shopId.longValue()==shopProductVOTmp.getShopId().longValue())
                            {
                                shopExists=true;
                                break;
                            }

                        }
                        if(!shopExists) {
                            if(shopProductVOTmp.getShopId()!=null) {
                                shopIdList.add(shopProductVOTmp.getShopId());
                            }
                        }

                    }


                    //查询类别名称
                    this.queryCategory(list,categoryIds);


                    //查询店铺类别名称
                    this.queryShopCategory(list,shopCategoryIds);

                    //查询品牌名称
                    this.queryBrand(list,brandIdList);

                    //查询店铺名称
                    this.queryShop(list,shopIdList);



                    for(ShopProductApproveVO shopProductVOTmp:list)
                    {
                        if(shopProductVOTmp.getMainPhotoFilePath()!=null) {
                            shopProductVOTmp.setHttpMainPhotoFilePath(imageUploadService.getImageHttpPrefix()+shopProductVOTmp.getMainPhotoFilePath());
                        }

                        if(org.apache.commons.collections.CollectionUtils.isNotEmpty(shopProductVOTmp.getPreviewPhotoPaths())) {
                            shopProductVOTmp.setHttpPreviewPhotoPaths(new LinkedList<>());
                            for(String previewPhotoPath:shopProductVOTmp.getPreviewPhotoPaths())
                            {
                                shopProductVOTmp.getHttpPreviewPhotoPaths().add(imageUploadService.getImageHttpPrefix()+previewPhotoPath);
                            }
                        }

                        if(org.apache.commons.collections.CollectionUtils.isNotEmpty(shopProductVOTmp.getProductSkuVOList()))
                        {
                            shopProductVOTmp.setHttpSkuPreviewPhotoPaths(new LinkedList<>());
                            for(ShopProductApproveSkuVO productSkuVO:shopProductVOTmp.getProductSkuVOList())
                            {
                                if(StringUtils.isNotEmpty(productSkuVO.getProductPreviewPath())) {
                                    shopProductVOTmp.getHttpSkuPreviewPhotoPaths().add(imageUploadService.getImageHttpPrefix() + productSkuVO.getProductPreviewPath());
                                }
                            }
                        }
                    }
                    if(list.get(0).getShopProductApproveDescriptionVO()!=null) {
                        if(CollectionUtils.isNotEmpty(list.get(0).getShopProductApproveDescriptionVO().getProductDescriptionImgs()))
                        {
                            for(ShopProductApproveDescriptionImgVO shopProductApproveDescriptionImgVO:list.get(0).getShopProductApproveDescriptionVO().getProductDescriptionImgs()) {
                                shopProductApproveDescriptionImgVO.setHttpFilePath(imageUploadService.getImageHttpPrefix()+shopProductApproveDescriptionImgVO.getFilePath());
                            }
                            list.get(0).setShopProductApproveDescriptionJson(JSONObject.toJSONString(list.get(0).getShopProductApproveDescriptionVO()));
                        }
                    }
                    retObject.setData(list.get(0));
                }else{
                    retObject.setMsg("查询失败,请稍后重试");
                    retObject.setCode(ResultObjectVO.FAILD);
                }
            }
        }catch(Exception e)
        {
            retObject.setMsg("查询失败,请稍后重试");
            retObject.setCode(ResultObjectVO.FAILD);
            logger.warn(e.getMessage(),e);
        }
        return retObject;
    }



}
