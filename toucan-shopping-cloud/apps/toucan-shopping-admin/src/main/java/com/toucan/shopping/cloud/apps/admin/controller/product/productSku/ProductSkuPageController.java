package com.toucan.shopping.cloud.apps.admin.controller.product.productSku;


import com.toucan.shopping.cloud.admin.auth.api.FunctionServiceAPI;
import com.toucan.shopping.cloud.apps.admin.controller.base.UIController;
import com.toucan.shopping.cloud.common.data.api.CategoryServiceAPI;
import com.toucan.shopping.cloud.product.api.BrandServiceAPI;
import com.toucan.shopping.cloud.product.api.ProductSkuServiceAPI;
import com.toucan.shopping.cloud.seller.api.SellerShopServiceAPI;
import com.toucan.shopping.cloud.seller.api.ShopCategoryServiceAPI;
import com.toucan.shopping.modules.auth.admin.AdminAuth;
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
import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * 商品SKU管理 - 页面控制器
 * @author majian
 */
@Controller
public class ProductSkuPageController extends UIController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Value("${toucan.app-code}")
    private String appCode;

    @Autowired
    private Toucan toucan;

    @Autowired
    private FunctionServiceAPI functionServiceAPI;

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
    private ProductSkuServiceAPI productSkuService;


    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH,requestType = AdminAuth.REQUEST_FORM,responseType=AdminAuth.RESPONSE_FORM)
    @RequestMapping(value = "/productSku/listPage",method = RequestMethod.GET)
    public String listPage(HttpServletRequest request)
    {
        //初始化工具条按钮、操作按钮
        super.initButtons(request,toucan,"/product/productSku/listPage", functionServiceAPI);
        request.setAttribute("pcProductSkuPreviewPage",toucan.getShoppingPC().getBasePath()+toucan.getShoppingPC().getProductSkuPreviewPage());
        return "pages/product/productSku/list.html";
    }





    /**
     * 查询类别信息
     * @param list
     * @param categoryIds
     */
    void queryCategory(List<ProductSkuVO> list,Long[] categoryIds)
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
                    for (ProductSkuVO productSkuVO : list) {
                        for (CategoryVO categoryVO : categoryVOS) {
                            if (productSkuVO.getCategoryId() != null && productSkuVO.getCategoryId().longValue() == categoryVO.getId().longValue()) {
                                productSkuVO.setCategoryName(categoryVO.getName());
                                productSkuVO.setCategoryPath(categoryVO.getNamePath());
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
    void queryShopCategory(List<ProductSkuVO> list,Long[] shopCategoryIds)
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
                    for(ProductSkuVO productSkuVO:list)
                    {
                        for(ShopCategoryVO shopCategoryVO:shopCategoryVOS)
                        {
                            if(productSkuVO.getShopCategoryId()!=null&&productSkuVO.getShopCategoryId().longValue()==shopCategoryVO.getId().longValue())
                            {
                                productSkuVO.setShopCategoryName(shopCategoryVO.getName());
                                productSkuVO.setShopCategoryPath(shopCategoryVO.getNamePath());
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
    void queryBrand(List<ProductSkuVO> list,List<Long> brandIdList)
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
                    for(ProductSkuVO productSkuVO:list)
                    {
                        for(BrandVO brandVO:brandVOS)
                        {
                            if(productSkuVO.getBrandId()!=null&&productSkuVO.getBrandId().longValue()==brandVO.getId().longValue())
                            {
                                productSkuVO.setBrandChineseName(brandVO.getChineseName());
                                productSkuVO.setBrandEnglishName(brandVO.getEnglishName());
                                productSkuVO.setBrandLogo(brandVO.getLogoPath());
                                if(brandVO.getLogoPath()!=null) {
                                    productSkuVO.setBrandHttpLogo(imageUploadService.getImageHttpPrefix() +brandVO.getLogoPath());
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
    void queryShop(List<ProductSkuVO> list,List<Long> shopIdList)
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
                    for(ProductSkuVO productSkuVO:list)
                    {
                        for(SellerShopVO sellerShopVO:sellerShopVOS)
                        {
                            if(productSkuVO.getShopId()!=null&&productSkuVO.getShopId().longValue()==sellerShopVO.getId().longValue())
                            {
                                productSkuVO.setShopName(sellerShopVO.getName());
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





    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH,requestType = AdminAuth.REQUEST_FORM,responseType=AdminAuth.RESPONSE_FORM)
    @RequestMapping(value = "/productSku/detailPage/{id}",method = RequestMethod.GET)
    public String detailPage(HttpServletRequest request,@PathVariable Long id)
    {
        try {
            ProductSkuVO queryProductSkuVO = new ProductSkuVO();
            queryProductSkuVO.setId(id);
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(),queryProductSkuVO);
            ResultObjectVO resultObjectVO = productSkuService.queryById(requestJsonVO);
            if(resultObjectVO.isSuccess())
            {
                ProductSkuVO productSkuVO = resultObjectVO.formatData(ProductSkuVO.class);
                if(ObjectUtils.isNotEmpty(productSkuVO)) {
                    Long[] categoryIds = new Long[1];
                    Long[] shopCategoryIds = new Long[1];
                    List<Long> brandIdList = new LinkedList<>();
                    List<Long> shopIdList =new LinkedList<>();

                    categoryIds[0] = productSkuVO.getCategoryId();

                    //设置品牌ID
                    if(productSkuVO.getBrandId()!=null) {
                        brandIdList.add(productSkuVO.getBrandId());
                    }


                    //设置店铺分类ID
                    if(productSkuVO.getShopCategoryId()!=null) {
                        shopCategoryIds[0] = productSkuVO.getShopCategoryId();
                    }



                    //设置店铺ID
                    if(productSkuVO.getShopId()!=null) {
                        shopIdList.add(productSkuVO.getShopId());
                    }


                    productSkuVO.setHttpMainPhotoFilePath(imageUploadService.getImageHttpPrefix()+productSkuVO.getProductPreviewPath());

                    List<ProductSkuVO> list = new LinkedList<>();
                    list.add(productSkuVO);

                    //查询类别名称
                    this.queryCategory(list,categoryIds);


                    //查询店铺类别名称
                    this.queryShopCategory(list,shopCategoryIds);

                    //查询品牌名称
                    this.queryBrand(list,brandIdList);

                    //查询店铺名称
                    this.queryShop(list,shopIdList);


                    request.setAttribute("model", list.get(0));
                }else{
                    request.setAttribute("model", new ProductSkuVO());
                }
            }
        }catch(Exception e)
        {
            request.setAttribute("model", new ProductSkuVO());
            logger.warn(e.getMessage(),e);
        }
        return "pages/product/productSku/detail.html";
    }


}
