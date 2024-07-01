package com.toucan.shopping.cloud.apps.seller.web.controller.shop.product;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.toucan.shopping.cloud.apps.seller.web.controller.BaseController;
import com.toucan.shopping.cloud.apps.seller.web.redis.ShopProductRedisKey;
import com.toucan.shopping.cloud.apps.seller.web.util.VCodeUtil;
import com.toucan.shopping.cloud.common.data.api.feign.service.FeignCategoryService;
import com.toucan.shopping.cloud.product.api.feign.service.FeignAttributeKeyValueService;
import com.toucan.shopping.cloud.product.api.feign.service.FeignBrandService;
import com.toucan.shopping.cloud.product.api.feign.service.FeignProductSkuService;
import com.toucan.shopping.cloud.product.api.feign.service.FeignShopProductService;
import com.toucan.shopping.cloud.seller.api.feign.service.FeignFreightTemplateService;
import com.toucan.shopping.cloud.seller.api.feign.service.FeignSellerShopService;
import com.toucan.shopping.cloud.seller.api.feign.service.FeignShopCategoryService;
import com.toucan.shopping.modules.auth.user.UserAuth;
import com.toucan.shopping.modules.category.vo.CategoryVO;
import com.toucan.shopping.modules.common.generator.RequestJsonVOGenerator;
import com.toucan.shopping.modules.common.properties.Toucan;
import com.toucan.shopping.modules.common.util.GlobalUUID;
import com.toucan.shopping.modules.common.util.ImageUtils;
import com.toucan.shopping.modules.common.util.UserAuthHeaderUtil;
import com.toucan.shopping.modules.common.util.VerifyCodeUtil;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.common.vo.ResultVO;
import com.toucan.shopping.modules.image.upload.service.ImageUploadService;
import com.toucan.shopping.modules.product.page.ProductSkuPageInfo;
import com.toucan.shopping.modules.product.page.ShopProductPageInfo;
import com.toucan.shopping.modules.product.vo.*;
import com.toucan.shopping.modules.redis.service.ToucanStringRedisService;
import com.toucan.shopping.modules.seller.entity.SellerShop;
import com.toucan.shopping.modules.seller.vo.FreightTemplateVO;
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

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.*;
import java.util.concurrent.TimeUnit;


/**
 * 店铺商品信息
 */
@Controller("shopProductApiController")
@RequestMapping("/api/shop/product")
public class ShopProductApiController extends BaseController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private Toucan toucan;

    @Autowired
    private FeignShopProductService feignShopProductService;

    @Autowired
    private FeignSellerShopService feignSellerShopService;

    @Autowired
    private FeignCategoryService feignCategoryService;

    @Autowired
    private ImageUploadService imageUploadService;

    @Autowired
    private FeignShopCategoryService feignShopCategoryService;


    @Autowired
    private FeignAttributeKeyValueService feignAttributeKeyValueService;


    @Autowired
    private ToucanStringRedisService toucanStringRedisService;

    @Autowired
    private FeignFreightTemplateService feignFreightTemplateService;

    @Autowired
    private FeignBrandService feignBrandService;

    private String[] imageExtScope = new String[]{".JPG", ".JPEG", ".PNG"};


    /**
     * 查询类别信息
     *
     * @param list
     * @param categoryIds
     */
    void queryCategory(List<ShopProductVO> list, Long[] categoryIds) {
        try {
            //查询类别名称
            CategoryVO queryCategoryVO = new CategoryVO();
            queryCategoryVO.setIdArray(categoryIds);
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(), queryCategoryVO);
            ResultObjectVO resultObjectVO = feignCategoryService.findByIdArray(requestJsonVO.sign(), requestJsonVO);
            if (resultObjectVO.isSuccess()) {
                List<CategoryVO> categoryVOS = resultObjectVO.formatDataList(CategoryVO.class);
                if (CollectionUtils.isNotEmpty(categoryVOS)) {
                    for (ShopProductVO shopProductVO : list) {
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
        } catch (Exception e) {
            logger.warn(e.getMessage(), e);
        }
    }


    /**
     * 查询店铺类别
     * @param list
     * @param shopCategoryIds
     */
    void queryShopCategory(List<ShopProductVO> list,Long[] shopCategoryIds)
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
                    for(ShopProductVO shopProductVO:list)
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
    void queryBrand(List<ShopProductVO> list,List<Long> brandIdList)
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
                    for(ShopProductVO shopProductVO:list)
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
     * 查询列表
     *
     * @param pageInfo
     * @return
     */
    @UserAuth
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ResponseBody
    public ResultObjectVO list(HttpServletRequest httpServletRequest, @RequestBody ShopProductPageInfo pageInfo) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            if (pageInfo == null) {
                pageInfo = new ShopProductPageInfo();
            }
            String userMainId = UserAuthHeaderUtil.getUserMainId(httpServletRequest.getHeader(toucan.getUserAuth().getHttpToucanAuthHeader()));
            if (StringUtils.isEmpty(userMainId)) {
                logger.warn("查询商品审核 没有找到用户ID {} ", userMainId);
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("查询失败,请稍后重试");
                return resultObjectVO;
            }

            SellerShop querySellerShop = new SellerShop();
            querySellerShop.setUserMainId(Long.parseLong(userMainId));
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(this.getAppCode(), querySellerShop);
            resultObjectVO = feignSellerShopService.findByUser(requestJsonVO.sign(), requestJsonVO);
            if (resultObjectVO.isSuccess() && resultObjectVO.getData() != null) {
                SellerShopVO sellerShopVO = resultObjectVO.formatData(SellerShopVO.class);
                if (sellerShopVO != null) {
                    pageInfo.setShopId(sellerShopVO.getId());
                    pageInfo.setOrderColumn("update_date");
                    pageInfo.setOrderSort("desc");
                    requestJsonVO = RequestJsonVOGenerator.generator(this.getAppCode(), pageInfo);
                    resultObjectVO = feignShopProductService.queryListPage(requestJsonVO);
                    if (resultObjectVO.isSuccess() && resultObjectVO.getData() != null) {
                        Map<String, Object> resultObjectDataMap = (Map<String, Object>) resultObjectVO.getData();
                        List<ShopProductVO> list = JSONArray.parseArray(JSONObject.toJSONString(resultObjectDataMap.get("list")), ShopProductVO.class);
                        if (CollectionUtils.isNotEmpty(list)) {

                            Long[] categoryIds = new Long[list.size()];
                            boolean categoryExists = false;
                            for (int i = 0; i < list.size(); i++) {
                                ShopProductVO shopProductVO = list.get(i);

                                if(StringUtils.isNotEmpty(shopProductVO.getMainPhotoFilePath()))
                                {
                                    shopProductVO.setHttpMainPhotoFilePath(imageUploadService.getImageHttpPrefix()+shopProductVO.getMainPhotoFilePath());
                                }

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

                            resultObjectDataMap.put("list", list);

                            resultObjectVO.setData(resultObjectDataMap);
                        }
                    }
                }
            }
        } catch (Exception e) {
            logger.warn(e.getMessage(), e);
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            resultObjectVO.setMsg("查询失败,请稍后重试");
        }

        return resultObjectVO;
    }


    /**
     * 店铺商品 上架/下架
     *
     * @param shopProductVO
     * @return
     */
    @UserAuth
    @RequestMapping(value = "/shelves", method = RequestMethod.POST)
    @ResponseBody
    public ResultObjectVO shelves(HttpServletRequest request,@RequestBody ShopProductVO shopProductVO)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try{
            if(shopProductVO==null||shopProductVO.getId()==null)
            {
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("商品ID不能为空");
                return resultObjectVO;
            }
            String userMainId="-1";
            userMainId = UserAuthHeaderUtil.getUserMainId(request.getHeader(toucan.getUserAuth().getHttpToucanAuthHeader()));
            SellerShop querySellerShop = new SellerShop();
            querySellerShop.setUserMainId(Long.parseLong(userMainId));
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(this.getAppCode(), querySellerShop);
            //查询店铺
            resultObjectVO = feignSellerShopService.findByUser(requestJsonVO.sign(),requestJsonVO);
            if(resultObjectVO.isSuccess()) {
                if (resultObjectVO.getData() != null) {
                    SellerShopVO sellerShopVORet = resultObjectVO.formatData(SellerShopVO.class);
                    //设置店铺ID
                    shopProductVO.setShopId(sellerShopVORet.getId());

                    requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(),shopProductVO);
                    resultObjectVO = feignShopProductService.shelves(requestJsonVO);
                }
            }
        }catch(Exception e) {
            logger.warn(e.getMessage(), e);
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            resultObjectVO.setMsg("查询失败,请稍后重试");
        }

        return resultObjectVO;
    }



    /**
     * 删除
     * @param queryShopProductVO
     * @return
     */
    @UserAuth
    @RequestMapping(value = "/delete",method = RequestMethod.POST)
    @ResponseBody
    public ResultObjectVO deleteByApproveId(HttpServletRequest request,@RequestBody ShopProductVO queryShopProductVO)
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
            if(resultObjectVO.isSuccess()) {
                if (resultObjectVO.getData() != null) {
                    SellerShopVO sellerShopVORet = resultObjectVO.formatData(SellerShopVO.class);
                    ShopProductVO shopProductVO = new ShopProductVO();
                    shopProductVO.setId(queryShopProductVO.getId());
                    shopProductVO.setShopId(sellerShopVORet.getId());
                    requestJsonVO = RequestJsonVOGenerator.generator(this.getAppCode(), shopProductVO);
                    resultObjectVO = feignShopProductService.deleteById(requestJsonVO);

                }
            }
        }catch (Exception e)
        {
            logger.warn(e.getMessage(),e);
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            resultObjectVO.setMsg("删除失败,请稍后重试");
        }
        return resultObjectVO;
    }



    /**
     * 修改运费模板
     * @param requestShopProductVO
     * @return
     */
    @UserAuth
    @RequestMapping(value = "/modifyFreightTemplate",method = RequestMethod.POST)
    @ResponseBody
    public ResultObjectVO modifyFreightTemplate(HttpServletRequest request,@RequestBody ShopProductVO requestShopProductVO)
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
            if(resultObjectVO.isSuccess()) {
                if (resultObjectVO.getData() != null) {
                    SellerShopVO sellerShopVORet = resultObjectVO.formatData(SellerShopVO.class);
                    ShopProductVO shopProductVO = new ShopProductVO();
                    shopProductVO.setId(requestShopProductVO.getId());
                    shopProductVO.setShopId(sellerShopVORet.getId());
                    shopProductVO.setFreightTemplateId(requestShopProductVO.getFreightTemplateId());
                    requestJsonVO = RequestJsonVOGenerator.generator(this.getAppCode(), shopProductVO);
                    resultObjectVO = feignShopProductService.updateFreightTemplate(requestJsonVO);

                }
            }
        }catch (Exception e)
        {
            logger.warn(e.getMessage(),e);
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            resultObjectVO.setMsg("删除失败,请稍后重试");
        }
        return resultObjectVO;
    }



    /**
     * 查询列表
     *
     * @param shopProductVO
     * @return
     */
    @UserAuth
    @RequestMapping(value = "/findById", method = RequestMethod.POST)
    @ResponseBody
    public ResultObjectVO findById(@RequestBody ShopProductVO shopProductVO)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try{
            ShopProductVO requestShopProductVO = new ShopProductVO();
            requestShopProductVO.setId(shopProductVO.getId());;
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(),requestShopProductVO);
            resultObjectVO = feignShopProductService.queryByShopProductId(requestJsonVO);
            if(resultObjectVO.isSuccess())
            {
                List<ShopProductVO> list = resultObjectVO.formatDataList(ShopProductVO.class);
                if(CollectionUtils.isNotEmpty(list)) {
                    Long[] categoryIds = new Long[list.size()];
                    Long[] shopCategoryIds = new Long[list.size()];
                    List<Long> brandIdList = new LinkedList<>();
                    List<Long> shopIdList = new LinkedList<>();

                    boolean brandExists = false;
                    boolean shopCategoryExists = false;
                    boolean shopExists = false;
                    for (int i = 0; i < list.size(); i++) {
                        ShopProductVO shopProductVOTmp = list.get(i);
                        categoryIds[i] = shopProductVOTmp.getCategoryId();

                        //设置品牌ID
                        brandExists = false;
                        for (Long brandId : brandIdList) {
                            if (shopProductVOTmp.getBrandId() != null && brandId != null
                                    && brandId.longValue() == shopProductVOTmp.getBrandId().longValue()) {
                                brandExists = true;
                                break;
                            }

                        }
                        if (!brandExists) {
                            if (shopProductVOTmp.getBrandId() != null) {
                                brandIdList.add(shopProductVOTmp.getBrandId());
                            }
                        }


                        //设置店铺分类ID
                        shopCategoryExists = false;
                        for (int sci = 0; sci < shopCategoryIds.length; sci++) {
                            Long shopCategoryId = shopCategoryIds[sci];
                            if (shopProductVOTmp.getShopCategoryId() != null && shopCategoryId != null
                                    && shopCategoryId.longValue() == shopProductVOTmp.getShopCategoryId().longValue()) {
                                shopCategoryExists = true;
                                break;
                            }

                        }
                        if (!shopCategoryExists) {
                            if (shopProductVOTmp.getShopCategoryId() != null) {
                                shopCategoryIds[i] = shopProductVOTmp.getShopCategoryId();
                            }
                        }


                        //设置店铺ID
                        shopExists = false;
                        for (Long shopId : shopIdList) {
                            if (shopProductVOTmp.getShopId() != null && shopId != null
                                    && shopId.longValue() == shopProductVOTmp.getShopId().longValue()) {
                                shopExists = true;
                                break;
                            }

                        }
                        if (!shopExists) {
                            if (shopProductVOTmp.getShopId() != null) {
                                shopIdList.add(shopProductVOTmp.getShopId());
                            }
                        }

                    }


                    //查询类别名称
                    this.queryCategory(list, categoryIds);


                    //查询店铺类别名称
                    this.queryShopCategory(list,shopCategoryIds);

                    //查询品牌名称
                    this.queryBrand(list,brandIdList);

                    for (ShopProductVO shopProductVOTmp : list) {
                        if (shopProductVOTmp.getMainPhotoFilePath() != null) {
                            shopProductVOTmp.setHttpMainPhotoFilePath(imageUploadService.getImageHttpPrefix() + shopProductVOTmp.getMainPhotoFilePath());
                        }

                        if (CollectionUtils.isNotEmpty(shopProductVOTmp.getPreviewPhotoPaths())) {
                            shopProductVOTmp.setHttpPreviewPhotoPaths(new LinkedList<>());
                            for (String previewPhotoPath : shopProductVOTmp.getPreviewPhotoPaths()) {
                                shopProductVOTmp.getHttpPreviewPhotoPaths().add(imageUploadService.getImageHttpPrefix() + previewPhotoPath);
                            }
                        }

                        if (CollectionUtils.isNotEmpty(shopProductVOTmp.getProductSkuVOList())) {
                            shopProductVOTmp.setHttpSkuPreviewPhotoPaths(new LinkedList<>());
                            for (ProductSkuVO productSkuVO : shopProductVOTmp.getProductSkuVOList()) {
                                if (StringUtils.isNotEmpty(productSkuVO.getProductPreviewPath())) {
                                    productSkuVO.setHttpProductPreviewPath(imageUploadService.getImageHttpPrefix() + productSkuVO.getProductPreviewPath());
                                    shopProductVOTmp.getHttpSkuPreviewPhotoPaths().add(imageUploadService.getImageHttpPrefix() + productSkuVO.getProductPreviewPath());
                                }
                                if(StringUtils.isNotEmpty(productSkuVO.getDescriptionImgFilePath())){
                                    productSkuVO.setHttpDescriptionImgPath(imageUploadService.getImageHttpPrefix() + productSkuVO.getDescriptionImgFilePath());
                                }
                            }
                        }
                    }


                    if (list.get(0).getShopProductDescriptionVO() != null) {
                        if (CollectionUtils.isNotEmpty(list.get(0).getShopProductDescriptionVO().getProductDescriptionImgs())) {
                            for (ShopProductDescriptionImgVO shopProductDescriptionImgVO : list.get(0).getShopProductDescriptionVO().getProductDescriptionImgs()) {
                                shopProductDescriptionImgVO.setHttpFilePath(imageUploadService.getImageHttpPrefix() + shopProductDescriptionImgVO.getFilePath());
                            }
                            list.get(0).setShopProductDescriptionJson(JSONObject.toJSONString(list.get(0).getShopProductDescriptionVO()));
                        }
                    }


                    //查询运费模板
                    FreightTemplateVO freightTemplateVO = new FreightTemplateVO();
                    freightTemplateVO.setId(list.get(0).getFreightTemplateId());
                    requestJsonVO = RequestJsonVOGenerator.generator(this.getAppCode(), freightTemplateVO);
                    resultObjectVO = feignFreightTemplateService.findById(requestJsonVO);
                    if (resultObjectVO.isSuccess()) {
                        freightTemplateVO = resultObjectVO.formatData(FreightTemplateVO.class);
                        if (freightTemplateVO != null) {
                            list.get(0).setFreightTemplateName(freightTemplateVO.getName());
                        }
                    }

                    resultObjectVO.setData(list.get(0));
                }
            }
        }catch (Exception e){
            logger.warn(e.getMessage(),e);
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            resultObjectVO.setMsg("查询失败,请稍后重试");
        }
        return resultObjectVO;
    }





}
