package com.toucan.shopping.cloud.apps.admin.controller.component;

import com.toucan.shopping.cloud.apps.admin.controller.base.UIController;
import com.alibaba.fastjson2.JSONObject;
import com.toucan.shopping.cloud.admin.auth.api.FunctionServiceAPI;
import com.toucan.shopping.cloud.common.data.api.CategoryServiceAPI;
import com.toucan.shopping.cloud.product.api.BrandServiceAPI;
import com.toucan.shopping.cloud.product.api.ShopProductServiceAPI;
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
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import java.util.LinkedList;
import java.util.List;

/**
 * 选择店铺商品 - 页面控制器
 */
@Controller
public class SelectShopProductPageController extends UIController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Value("${toucan.app-code}")
    private String appCode;

    @Autowired
    private Toucan toucan;

    @Autowired
    private BrandServiceAPI brandService;

    @Autowired
    private ShopCategoryServiceAPI shopCategoryService;

    @Autowired
    private SellerShopServiceAPI sellerShopService;

    @Autowired
    private ShopProductServiceAPI shopProductService;

    @Autowired
    private ImageUploadService imageUploadService;

    @Autowired
    private CategoryServiceAPI categoryService;

    @Autowired
    private FunctionServiceAPI functionServiceAPI;

    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH, requestType = AdminAuth.REQUEST_FORM, responseType = AdminAuth.RESPONSE_FORM)
    @RequestMapping(value = "/selectShopProduct/shopProductListPage", method = RequestMethod.GET)
    public String spuListPage(HttpServletRequest request, @RequestParam(required = false) Long categoryId, @RequestParam String selectProductIds) {
        super.initButtons(request, toucan, "/selectShopProduct/shopProductListPage", functionServiceAPI);
        request.setAttribute("categoryId", categoryId);
        request.setAttribute("selectProductIds", selectProductIds);
        return "pages/component/selectShopProduct/shop_product_list.html";
    }

    void queryCategory(List<ShopProductVO> list, Long[] categoryIds) {
        try {
            CategoryVO queryCategoryVO = new CategoryVO();
            queryCategoryVO.setIdArray(categoryIds);
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(), queryCategoryVO);
            ResultObjectVO resultObjectVO = categoryService.findByIdArray(requestJsonVO);
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

    void queryShopCategory(List<ShopProductVO> list, Long[] shopCategoryIds) {
        try {
            ShopCategoryVO queryShopCategoryVO = new ShopCategoryVO();
            queryShopCategoryVO.setIdArray(shopCategoryIds);
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(), queryShopCategoryVO);
            ResultObjectVO resultObjectVO = shopCategoryService.findByIdArray(requestJsonVO);
            if (resultObjectVO.isSuccess()) {
                List<ShopCategoryVO> shopCategoryVOS = resultObjectVO.formatDataList(ShopCategoryVO.class);
                if (CollectionUtils.isNotEmpty(shopCategoryVOS)) {
                    for (ShopProductVO shopProductVO : list) {
                        for (ShopCategoryVO shopCategoryVO : shopCategoryVOS) {
                            if (shopProductVO.getShopCategoryId() != null && shopProductVO.getShopCategoryId().longValue() == shopCategoryVO.getId().longValue()) {
                                shopProductVO.setShopCategoryName(shopCategoryVO.getName());
                                shopProductVO.setShopCategoryPath(shopCategoryVO.getNamePath());
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

    void queryBrand(List<ShopProductVO> list, List<Long> brandIdList) {
        try {
            BrandVO queryBrandVO = new BrandVO();
            queryBrandVO.setIdList(brandIdList);
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(), queryBrandVO);
            ResultObjectVO resultObjectVO = brandService.findByIdList(requestJsonVO);
            if (resultObjectVO.isSuccess()) {
                List<BrandVO> brandVOS = resultObjectVO.formatDataList(BrandVO.class);
                if (CollectionUtils.isNotEmpty(brandVOS)) {
                    for (ShopProductVO shopProductVO : list) {
                        for (BrandVO brandVO : brandVOS) {
                            if (shopProductVO.getBrandId() != null && shopProductVO.getBrandId().longValue() == brandVO.getId().longValue()) {
                                shopProductVO.setBrandChineseName(brandVO.getChineseName());
                                shopProductVO.setBrandEnglishName(brandVO.getEnglishName());
                                shopProductVO.setBrandLogo(brandVO.getLogoPath());
                                if (brandVO.getLogoPath() != null) {
                                    shopProductVO.setBrandHttpLogo(imageUploadService.getImageHttpPrefix() + brandVO.getLogoPath());
                                }
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

    void queryShop(List<ShopProductVO> list, List<Long> shopIdList) {
        try {
            SellerShopVO queryShopVO = new SellerShopVO();
            queryShopVO.setIdList(shopIdList);
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(), queryShopVO);
            ResultObjectVO resultObjectVO = sellerShopService.findByIdList(requestJsonVO);
            if (resultObjectVO.isSuccess()) {
                List<SellerShopVO> sellerShopVOS = resultObjectVO.formatDataList(SellerShopVO.class);
                if (CollectionUtils.isNotEmpty(sellerShopVOS)) {
                    for (ShopProductVO shopProductVO : list) {
                        for (SellerShopVO sellerShopVO : sellerShopVOS) {
                            if (shopProductVO.getShopId() != null && shopProductVO.getShopId().longValue() == sellerShopVO.getId().longValue()) {
                                shopProductVO.setShopName(sellerShopVO.getName());
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

    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH, requestType = AdminAuth.REQUEST_FORM, responseType = AdminAuth.RESPONSE_FORM)
    @RequestMapping(value = "/selectShopProduct/detailPage/{id}", method = RequestMethod.GET)
    public String detailPage(HttpServletRequest request, @PathVariable Long id) {
        try {
            ShopProductVO shopProductVO = new ShopProductVO();
            shopProductVO.setId(id);
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(), shopProductVO);
            ResultObjectVO resultObjectVO = shopProductService.queryByShopProductId(requestJsonVO);
            if (resultObjectVO.isSuccess()) {
                List<ShopProductVO> list = resultObjectVO.formatDataList(ShopProductVO.class);
                if (CollectionUtils.isNotEmpty(list)) {
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

                    this.queryCategory(list, categoryIds);
                    this.queryShopCategory(list, shopCategoryIds);
                    this.queryBrand(list, brandIdList);
                    this.queryShop(list, shopIdList);

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
                                    shopProductVOTmp.getHttpSkuPreviewPhotoPaths().add(imageUploadService.getImageHttpPrefix() + productSkuVO.getProductPreviewPath());
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

                    request.setAttribute("model", list.get(0));
                } else {
                    request.setAttribute("model", new ShopProductVO());
                }
            }
        } catch (Exception e) {
            request.setAttribute("model", new ShopProductVO());
            logger.warn(e.getMessage(), e);
        }
        return "pages/product/shopProduct/detail.html";
    }

}
