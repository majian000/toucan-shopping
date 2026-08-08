package com.toucan.shopping.cloud.apps.admin.controller.product.productSku.esSearch;

import com.toucan.shopping.cloud.apps.admin.controller.base.UIController;
import com.toucan.shopping.cloud.admin.auth.api.FunctionServiceAPI;
import com.toucan.shopping.cloud.common.data.api.CategoryServiceAPI;
import com.toucan.shopping.cloud.search.api.ProductSearchServiceAPI;
import com.toucan.shopping.modules.auth.admin.AdminAuth;
import com.toucan.shopping.modules.category.vo.CategoryVO;
import com.toucan.shopping.modules.common.generator.RequestJsonVOGenerator;
import com.toucan.shopping.modules.common.properties.Toucan;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.image.upload.service.ImageUploadService;
import com.toucan.shopping.modules.search.vo.ProductSearchResultVO;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
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
 * 商品SKU ES搜索管理 - 页面控制器
 */
@Controller
public class ProductSkuSearchPageController extends UIController {

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
    private ProductSearchServiceAPI productSearchService;

    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH, requestType = AdminAuth.REQUEST_FORM, responseType = AdminAuth.RESPONSE_FORM)
    @RequestMapping(value = "/productSkuSearch/listPage", method = RequestMethod.GET)
    public String listPage(HttpServletRequest request) {
        super.initButtons(request, toucan, "/productSkuSearch/listPage", functionServiceAPI);
        request.setAttribute("pcProductSkuPreviewPage", toucan.getShoppingPC().getBasePath() + toucan.getShoppingPC().getProductSkuDetailPage());
        return "pages/product/productSku/search/list.html";
    }

    void queryCategory(List<ProductSearchResultVO> list, Long[] categoryIds) {
        try {
            CategoryVO queryCategoryVO = new CategoryVO();
            queryCategoryVO.setIdArray(categoryIds);
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(), queryCategoryVO);
            ResultObjectVO resultObjectVO = categoryService.findByIdArray(requestJsonVO);
            if (resultObjectVO.isSuccess()) {
                List<CategoryVO> categoryVOS = resultObjectVO.formatDataList(CategoryVO.class);
                if (CollectionUtils.isNotEmpty(categoryVOS)) {
                    for (ProductSearchResultVO productSearchResultVO : list) {
                        for (CategoryVO categoryVO : categoryVOS) {
                            if (productSearchResultVO.getCategoryId() != null && productSearchResultVO.getCategoryId().longValue() == categoryVO.getId().longValue()) {
                                productSearchResultVO.setCategoryName(categoryVO.getName());
                                productSearchResultVO.setCategoryPath(categoryVO.getNamePath());
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
    @RequestMapping(value = "/productSkuSearch/detailPage/{id}", method = RequestMethod.GET)
    public String detailPage(HttpServletRequest request, @PathVariable Long id) {
        try {
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(), id);
            ResultObjectVO resultObjectVO = productSearchService.queryBySkuId(requestJsonVO);
            if (resultObjectVO.isSuccess()) {
                List<ProductSearchResultVO> productSearchResultVOS = resultObjectVO.formatDataList(ProductSearchResultVO.class);
                if (CollectionUtils.isNotEmpty(productSearchResultVOS)) {
                    ProductSearchResultVO productSearchResultVO = productSearchResultVOS.get(0);
                    if (ObjectUtils.isNotEmpty(productSearchResultVO)) {
                        Long[] categoryIds = new Long[1];
                        List<Long> brandIdList = new LinkedList<>();
                        List<Long> shopIdList = new LinkedList<>();

                        categoryIds[0] = productSearchResultVO.getCategoryId();

                        if (productSearchResultVO.getBrandId() != null) {
                            brandIdList.add(productSearchResultVO.getBrandId());
                        }

                        if (productSearchResultVO.getShopId() != null) {
                            shopIdList.add(productSearchResultVO.getShopId());
                        }
                        productSearchResultVO.setHttpProductPreviewPath(imageUploadService.getImageHttpPrefix() + productSearchResultVO.getProductPreviewPath());

                        List<ProductSearchResultVO> list = new LinkedList<>();
                        list.add(productSearchResultVO);

                        this.queryCategory(list, categoryIds);

                        request.setAttribute("model", list.get(0));
                    } else {
                        request.setAttribute("model", new ProductSearchResultVO());
                    }
                }
            }
        } catch (Exception e) {
            request.setAttribute("model", new ProductSearchResultVO());
            logger.warn(e.getMessage(), e);
        }
        return "pages/product/productSku/search/detail.html";
    }

}
