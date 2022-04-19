package com.toucan.shopping.cloud.apps.seller.web.controller.shop.product;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.toucan.shopping.cloud.apps.seller.web.controller.BaseController;
import com.toucan.shopping.cloud.apps.seller.web.redis.ShopProductRedisKey;
import com.toucan.shopping.cloud.apps.seller.web.util.VCodeUtil;
import com.toucan.shopping.cloud.apps.seller.web.vo.selectPage.GridResult;
import com.toucan.shopping.cloud.common.data.api.feign.service.FeignCategoryService;
import com.toucan.shopping.cloud.product.api.feign.service.FeignAttributeKeyValueService;
import com.toucan.shopping.cloud.product.api.feign.service.FeignShopProductApproveService;
import com.toucan.shopping.cloud.seller.api.feign.service.FeignSellerShopService;
import com.toucan.shopping.cloud.seller.api.feign.service.FeignShopCategoryService;
import com.toucan.shopping.modules.auth.user.UserAuth;
import com.toucan.shopping.modules.category.vo.CategoryVO;
import com.toucan.shopping.modules.common.generator.RequestJsonVOGenerator;
import com.toucan.shopping.modules.common.page.PageInfo;
import com.toucan.shopping.modules.common.properties.Toucan;
import com.toucan.shopping.modules.common.util.GlobalUUID;
import com.toucan.shopping.modules.common.util.ImageUtils;
import com.toucan.shopping.modules.common.util.UserAuthHeaderUtil;
import com.toucan.shopping.modules.common.util.VerifyCodeUtil;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.common.vo.ResultVO;
import com.toucan.shopping.modules.image.upload.service.ImageUploadService;
import com.toucan.shopping.modules.product.page.ShopProductApprovePageInfo;
import com.toucan.shopping.modules.product.vo.*;
import com.toucan.shopping.modules.redis.service.ToucanStringRedisService;
import com.toucan.shopping.modules.seller.entity.SellerShop;
import com.toucan.shopping.modules.seller.vo.SellerShopVO;
import com.toucan.shopping.modules.seller.vo.ShopCategoryVO;
import com.toucan.shopping.modules.user.vo.UserVO;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import sun.awt.image.ImageWatched;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.*;
import java.util.concurrent.TimeUnit;


/**
 * 店铺商品审核信息
 */
@Controller("shopProductApproveApiController")
@RequestMapping("/api/shop/product/approve")
public class ShopProductApproveApiController extends BaseController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private Toucan toucan;

    @Autowired
    private FeignShopProductApproveService feignShopProductApproveService;

    @Autowired
    private FeignSellerShopService feignSellerShopService;

    @Autowired
    private FeignCategoryService feignCategoryService;

    @Autowired
    private ImageUploadService imageUploadService;

    @Autowired
    private FeignShopCategoryService feignShopCategoryService;


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
     * 查询详情
     * @param queryShopProductApproveVO
     * @return
     */
    @UserAuth
    @RequestMapping(value = "/detail",method = RequestMethod.POST)
    @ResponseBody
    public ResultObjectVO detail(HttpServletRequest request,@RequestBody ShopProductApproveVO queryShopProductApproveVO)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try{
            String userMainId="-1";
            userMainId = UserAuthHeaderUtil.getUserMainId(request.getHeader(toucan.getUserAuth().getHttpToucanAuthHeader()));
            SellerShop querySellerShop = new SellerShop();
            querySellerShop.setUserMainId(Long.parseLong(userMainId));
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(this.getAppCode(), querySellerShop);
            //查询店铺
            resultObjectVO = feignSellerShopService.findByUser(requestJsonVO.sign(),requestJsonVO);
            if(resultObjectVO.isSuccess())
            {
                if(resultObjectVO.getData()!=null)
                {
                    SellerShopVO sellerShopVORet = resultObjectVO.formatData(SellerShopVO.class);
                    ShopProductApproveVO shopProductApproveVO = new ShopProductApproveVO();
                    shopProductApproveVO.setId(queryShopProductApproveVO.getId());
                    shopProductApproveVO.setShopId(sellerShopVORet.getId());
                    requestJsonVO = RequestJsonVOGenerator.generator(this.getAppCode(), shopProductApproveVO);
                    resultObjectVO = feignShopProductApproveService.queryByProductApproveIdAndShopId(requestJsonVO);
                    if(resultObjectVO.isSuccess())
                    {
                        shopProductApproveVO = resultObjectVO.formatData(ShopProductApproveVO.class);
                        CategoryVO queryCateogry = new CategoryVO();
                        queryCateogry.setId(shopProductApproveVO.getCategoryId());
                        requestJsonVO = RequestJsonVOGenerator.generator(this.getAppCode(), queryCateogry);

                        //查询分类
                        ResultObjectVO resultCategoryObjectVO = feignCategoryService.findIdPathById(requestJsonVO);
                        if(resultCategoryObjectVO.isSuccess()&&resultCategoryObjectVO.getData()!=null)
                        {
                            CategoryVO categoryVO = resultCategoryObjectVO.formatData(CategoryVO.class);
                            List<String> categoryIdPath = new LinkedList<>();
                            List<String> categoryNamePath = new LinkedList<>();
                            if(CollectionUtils.isNotEmpty(categoryVO.getIdPath()))
                            {
                                for(Long categoryId:categoryVO.getIdPath())
                                {
                                    categoryIdPath.add(String.valueOf(categoryId));
                                }
                            }
                            if(CollectionUtils.isNotEmpty(categoryVO.getNamePaths()))
                            {
                                for(String categoryName:categoryVO.getNamePaths())
                                {
                                    categoryNamePath.add(String.valueOf(categoryName));
                                }
                            }
                            shopProductApproveVO.setCategoryIdPath(categoryIdPath);
                            shopProductApproveVO.setCategoryNamePath(categoryNamePath);

                        }


                        //查询店铺分类
                        if(shopProductApproveVO.getShopCategoryId()!=null) {
                            ShopCategoryVO queryShopCateogry = new ShopCategoryVO();
                            queryShopCateogry.setId(shopProductApproveVO.getShopCategoryId());
                            requestJsonVO = RequestJsonVOGenerator.generator(this.getAppCode(), queryShopCateogry);
                            ResultObjectVO resultShopCategoryObjectVO = feignShopCategoryService.findIdPathById(requestJsonVO);
                            if (resultShopCategoryObjectVO.isSuccess() && resultShopCategoryObjectVO.getData() != null) {
                                ShopCategoryVO shopCategoryVO = resultShopCategoryObjectVO.formatData(ShopCategoryVO.class);
                                List<String> shopCategoryIdPath = new LinkedList<>();
                                List<String> shopCategoryNamePath = new LinkedList<>();
                                if (CollectionUtils.isNotEmpty(shopCategoryVO.getIdPath())) {
                                    for (Long shopCategoryId : shopCategoryVO.getIdPath()) {
                                        shopCategoryIdPath.add(String.valueOf(shopCategoryId));
                                    }
                                }
                                if (CollectionUtils.isNotEmpty(shopCategoryVO.getNamePaths())) {
                                    for (String shopCategoryName : shopCategoryVO.getNamePaths()) {
                                        shopCategoryNamePath.add(String.valueOf(shopCategoryName));
                                    }
                                }
                                shopProductApproveVO.setShopCategoryIdPath(shopCategoryIdPath);
                                shopProductApproveVO.setShopCategoryNamePath(shopCategoryNamePath);

                            }
                        }

                    }
                    if(StringUtils.isNotEmpty(shopProductApproveVO.getMainPhotoFilePath()))
                    {
                        shopProductApproveVO.setHttpMainPhotoFilePath(imageUploadService.getImageHttpPrefix()+shopProductApproveVO.getMainPhotoFilePath());
                    }
                    if(CollectionUtils.isNotEmpty(shopProductApproveVO.getPreviewPhotoPaths()))
                    {
                        List<String> httpPreviewPhotos = new LinkedList<>();
                        for(String previewPhoto:shopProductApproveVO.getPreviewPhotoPaths())
                        {
                            httpPreviewPhotos.add(imageUploadService.getImageHttpPrefix()+previewPhoto);
                        }
                        shopProductApproveVO.setHttpPreviewPhotoPaths(httpPreviewPhotos);
                    }

                    if(CollectionUtils.isNotEmpty(shopProductApproveVO.getProductSkuVOList())) {
                        List<ShopProductApproveSkuAttribute> skuAttributes = new LinkedList<>();
                        for(ShopProductApproveSkuVO shopProductApproveSkuVO:shopProductApproveVO.getProductSkuVOList())
                        {
                            //设置商品预览图
                            if(StringUtils.isNotEmpty(shopProductApproveSkuVO.getProductPreviewPath()))
                            {
                                shopProductApproveSkuVO.setHttpProductPreviewPath(imageUploadService.getImageHttpPrefix()+shopProductApproveSkuVO.getProductPreviewPath());
                            }

                            Map<String,String> attributeKeyValue = JSONObject.parseObject(shopProductApproveSkuVO.getAttributes(),Map.class);
                            Set<String> keysSet = attributeKeyValue.keySet();
                            for(String key:keysSet)
                            {
                                ShopProductApproveSkuAttribute shopProductApproveSkuAttribute = null;
                                Optional<ShopProductApproveSkuAttribute> shopProductApproveSkuAttributeOptional = skuAttributes.stream().filter(item -> item.getKey().equals(key)).findFirst();
                                //如果不存在属性名对象就创建
                                if(!shopProductApproveSkuAttributeOptional.isPresent())
                                {
                                    shopProductApproveSkuAttribute = new ShopProductApproveSkuAttribute();
                                    shopProductApproveSkuAttribute.setKey(key);
                                    shopProductApproveSkuAttribute.setValues(new LinkedList<>());
                                    skuAttributes.add(shopProductApproveSkuAttribute);
                                }else{
                                    shopProductApproveSkuAttribute = shopProductApproveSkuAttributeOptional.get();
                                }

                                //如果这个属性名没有任何属性值就直接添加
                                if(CollectionUtils.isEmpty(shopProductApproveSkuAttribute.getValues()))
                                {
                                    shopProductApproveSkuAttribute.getValues().add(attributeKeyValue.get(key));
                                }else{
                                    //判断是否有重复
                                    Optional<String> shopProductApproveSkuAttributeValueOptional = shopProductApproveSkuAttribute.getValues().stream().filter(item -> item.equals(attributeKeyValue.get(key))).findFirst();
                                    if(!shopProductApproveSkuAttributeValueOptional.isPresent())
                                    {
                                        shopProductApproveSkuAttribute.getValues().add(attributeKeyValue.get(key));
                                    }
                                }

                            }
                        }

                        shopProductApproveVO.setSkuAttributes(skuAttributes);
                    }
                    resultObjectVO.setData(shopProductApproveVO);
                }
            }
        }catch (Exception e)
        {
            logger.warn(e.getMessage(),e);
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            resultObjectVO.setMsg("查询失败,请稍后重试");
        }
        return resultObjectVO;
    }

    /**
     * 查询列表
     * @param pageInfo
     * @return
     */
    @UserAuth
    @RequestMapping(value = "/list",method = RequestMethod.POST)
    @ResponseBody
    public ResultObjectVO list(HttpServletRequest httpServletRequest,@RequestBody ShopProductApprovePageInfo pageInfo)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try{
            if(pageInfo==null)
            {
                pageInfo = new ShopProductApprovePageInfo();
            }
            String userMainId = UserAuthHeaderUtil.getUserMainId(httpServletRequest.getHeader(toucan.getUserAuth().getHttpToucanAuthHeader()));
            if(StringUtils.isEmpty(userMainId))
            {
                logger.warn("查询商品审核 没有找到用户ID {} ",userMainId);
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("查询失败,请稍后重试");
                return resultObjectVO;
            }

            SellerShop querySellerShop = new SellerShop();
            querySellerShop.setUserMainId(Long.parseLong(userMainId));
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(this.getAppCode(), querySellerShop);
            resultObjectVO = feignSellerShopService.findByUser(requestJsonVO.sign(),requestJsonVO);
            if(resultObjectVO.isSuccess()&&resultObjectVO.getData()!=null) {
                SellerShopVO sellerShopVO = resultObjectVO.formatData(SellerShopVO.class);
                if(sellerShopVO!=null) {
                    pageInfo.setShopId(sellerShopVO.getId());
                    requestJsonVO = RequestJsonVOGenerator.generator(this.getAppCode(), pageInfo);
                    resultObjectVO = feignShopProductApproveService.queryListPage(requestJsonVO);
                    if (resultObjectVO.isSuccess()&&resultObjectVO.getData() != null) {
                        Map<String, Object> resultObjectDataMap = (Map<String, Object>) resultObjectVO.getData();
                        List<ShopProductApproveVO> list = JSONArray.parseArray(JSONObject.toJSONString(resultObjectDataMap.get("list")), ShopProductApproveVO.class);
                        if (CollectionUtils.isNotEmpty(list)) {

                            Long[] categoryIds = new Long[list.size()];
                            boolean categoryExists = false;
                            for (int i = 0; i < list.size(); i++) {
                                ShopProductApproveVO shopProductVO = list.get(i);


                                //设置店铺分类ID
                                categoryExists = false;
                                for (int sci = 0; sci < categoryIds.length; sci++) {
                                    Long categoryId = categoryIds[sci];
                                    if (shopProductVO.getCategoryId() != null && categoryId != null
                                            && categoryId.longValue() == shopProductVO.getCategoryId().longValue()) {
                                        categoryExists = true;
                                        break;
                                    }

                                }
                                if (!categoryExists) {
                                    if (shopProductVO.getCategoryId() != null) {
                                        categoryIds[i] = shopProductVO.getCategoryId();
                                    }
                                }

                            }

                            //查询类别名称
                            this.queryCategory(list, categoryIds);

                            resultObjectDataMap.put("list",list);

                            resultObjectVO.setData(resultObjectDataMap);
                        }
                    }
                }
            }
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            resultObjectVO.setMsg("查询失败,请稍后重试");
        }

        return resultObjectVO;
    }

}
