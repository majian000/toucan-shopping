package com.toucan.shopping.cloud.apps.admin.controller.product.shopProductApprove;


import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.toucan.shopping.cloud.admin.auth.api.FunctionServiceAPI;
import com.toucan.shopping.cloud.apps.admin.controller.base.UIController;
import com.toucan.shopping.cloud.common.data.api.CategoryServiceAPI;
import com.toucan.shopping.cloud.product.api.BrandServiceAPI;
import com.toucan.shopping.cloud.product.api.ProductSpuServiceAPI;
import com.toucan.shopping.cloud.product.api.ShopProductApproveServiceAPI;
import com.toucan.shopping.cloud.seller.api.SellerShopServiceAPI;
import com.toucan.shopping.cloud.seller.api.ShopCategoryServiceAPI;
import com.toucan.shopping.modules.auth.admin.AdminAuth;
import com.toucan.shopping.modules.category.vo.CategoryTreeVO;
import com.toucan.shopping.modules.category.vo.CategoryVO;
import com.toucan.shopping.modules.common.generator.RequestJsonVOGenerator;
import com.toucan.shopping.modules.common.properties.Toucan;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.image.upload.service.ImageUploadService;
import com.toucan.shopping.modules.product.vo.*;
import com.toucan.shopping.modules.seller.vo.SellerShopVO;
import com.toucan.shopping.modules.seller.vo.ShopCategoryVO;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * 店铺商品审核管理 - 页面控制器
 * @author majian
 */
@Controller
public class ShopProductApprovePageController extends UIController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Value("${toucan.app-code}")
    private String appCode;

    @Autowired
    private Toucan toucan;

    @Autowired
    private FunctionServiceAPI functionServiceAPI;

    @Autowired
    private ShopProductApproveServiceAPI shopProductApproveService;

    @Autowired
    private CategoryServiceAPI categoryService;

    @Autowired
    private ImageUploadService imageUploadService;

    @Autowired
    private BrandServiceAPI brandService;

    @Autowired
    private ShopCategoryServiceAPI shopCategoryService;

    @Autowired
    private SellerShopServiceAPI sellerShopService;

    @Autowired
    private ProductSpuServiceAPI productSpuService;

    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH,requestType = AdminAuth.REQUEST_FORM,responseType=AdminAuth.RESPONSE_FORM)
    @RequestMapping(value = "/shopProductApprove/listPage",method = RequestMethod.GET)
    public String listPage(HttpServletRequest request)
    {
        //初始化工具条按钮、操作按钮
        super.initButtons(request,toucan,"/product/shopProductApprove/listPage", functionServiceAPI);
        request.setAttribute("pcProductPreviewPage",toucan.getShoppingPC().getBasePath()+toucan.getShoppingPC().getProductApprovePreviewPage());
        return "pages/product/shopProductApprove/list.html";
    }



    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH,requestType = AdminAuth.REQUEST_FORM,responseType=AdminAuth.RESPONSE_FORM)
    @RequestMapping(value = "/shopProductApprove/spuListPage/{categoryId}",method = RequestMethod.GET)
    public String spuListPage(HttpServletRequest request,@PathVariable Long categoryId)
    {
        //初始化工具条按钮、操作按钮
        super.initButtons(request,toucan,"/product/shopProductApprove/spuListPage", functionServiceAPI);

        request.setAttribute("categoryId",categoryId);
        return "pages/product/shopProductApprove/spu_list.html";
    }


    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH,requestType = AdminAuth.REQUEST_FORM,responseType=AdminAuth.RESPONSE_FORM)
    @RequestMapping(value = "/shopProductApprove/spuDetailPage/{id}",method = RequestMethod.GET)
    public String spuDetailPage(HttpServletRequest request,@PathVariable Long id)
    {
        try {
            ProductSpuVO queryProductSpu = new ProductSpuVO();
            queryProductSpu.setId(id);
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(appCode, queryProductSpu);
            ResultObjectVO resultObjectVO = productSpuService.findById(requestJsonVO);
            if(resultObjectVO.getCode().intValue()==ResultObjectVO.SUCCESS.intValue())
            {
                if(resultObjectVO.getData()!=null) {
                    ProductSpuVO productSpuVO = resultObjectVO.formatData(ProductSpuVO.class);

                    //查询分类
                    CategoryVO queryCategory = new CategoryVO();
                    queryCategory.setId(productSpuVO.getCategoryId());
                    requestJsonVO = RequestJsonVOGenerator.generator(appCode, queryCategory);
                    resultObjectVO = categoryService.queryById(requestJsonVO);
                    if(resultObjectVO.isSuccess()&&resultObjectVO.getData()!=null)
                    {
                        CategoryTreeVO categoryTreeVO = resultObjectVO.formatData(CategoryTreeVO.class);
                        if(categoryTreeVO!=null) {
                            productSpuVO.setCategoryName(categoryTreeVO.getPath());
                        }
                    }

                    //查询品牌
                    BrandVO queryBrand = new BrandVO();
                    queryBrand.setId(productSpuVO.getBrandId());
                    requestJsonVO = RequestJsonVOGenerator.generator(appCode, queryBrand);
                    resultObjectVO = brandService.findById(requestJsonVO);
                    if(resultObjectVO.isSuccess()&&resultObjectVO.getData()!=null)
                    {
                        List<BrandVO> brandVOS = resultObjectVO.formatDataList(BrandVO.class);
                        BrandVO brandVO = brandVOS.get(0);
                        String brandName = "";
                        if(StringUtils.isNotEmpty(brandVO.getChineseName()))
                        {
                            brandName+=brandVO.getChineseName();
                        }
                        if(StringUtils.isNotEmpty(brandVO.getEnglishName()))
                        {
                            brandName+=" "+brandVO.getEnglishName();
                        }
                        productSpuVO.setBrandName(brandName);

                    }

                    //将属性名和属性值转换成字符串
                    productSpuVO.setAttributeKeyValuesJson(JSON.toJSONString(productSpuVO.getAttributeKeyValues()));

                    request.setAttribute("model",productSpuVO);
                }
            }
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);
        }
        return "pages/product/shopProductApprove/spu_detail.html";
    }



    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH,requestType = AdminAuth.REQUEST_FORM,responseType=AdminAuth.RESPONSE_FORM)
    @RequestMapping(value = "/shopProductApprove/approvePage/{id}",method = RequestMethod.GET)
    public String approvePage(HttpServletRequest request,@PathVariable Long id)
    {
        try {
            ShopProductApproveVO shopProductVO = new ShopProductApproveVO();
            shopProductVO.setId(id);;
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(),shopProductVO);
            ResultObjectVO resultObjectVO = shopProductApproveService.queryByProductApproveId(requestJsonVO);
            if(resultObjectVO.isSuccess())
            {
                List<ShopProductApproveVO> list = resultObjectVO.formatDataList(ShopProductApproveVO.class);
                if(CollectionUtils.isNotEmpty(list)) {
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

                        if(CollectionUtils.isNotEmpty(shopProductVOTmp.getPreviewPhotoPaths())) {
                            shopProductVOTmp.setHttpPreviewPhotoPaths(new LinkedList<>());
                            for(String previewPhotoPath:shopProductVOTmp.getPreviewPhotoPaths())
                            {
                                shopProductVOTmp.getHttpPreviewPhotoPaths().add(imageUploadService.getImageHttpPrefix()+previewPhotoPath);
                            }
                        }

                        if(CollectionUtils.isNotEmpty(shopProductVOTmp.getProductSkuVOList()))
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
                    request.setAttribute("model", list.get(0));
                }else{
                    request.setAttribute("model", new ShopProductApproveVO());
                }
            }
        }catch(Exception e)
        {
            request.setAttribute("model", new ShopProductApproveVO());
            logger.warn(e.getMessage(),e);
        }
        return "pages/product/shopProductApprove/approve.html";
    }





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
            ResultObjectVO resultObjectVO = categoryService.findByIdArray(requestJsonVO);
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
            ResultObjectVO resultObjectVO = shopCategoryService.findByIdArray(requestJsonVO);
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
            ResultObjectVO resultObjectVO = brandService.findByIdList(requestJsonVO);
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
            ResultObjectVO resultObjectVO = sellerShopService.findByIdList(requestJsonVO);
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


}
