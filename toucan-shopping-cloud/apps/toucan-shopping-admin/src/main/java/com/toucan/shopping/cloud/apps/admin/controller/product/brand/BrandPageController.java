package com.toucan.shopping.cloud.apps.admin.controller.product.brand;


import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.toucan.shopping.cloud.admin.auth.api.FunctionServiceAPI;
import com.toucan.shopping.cloud.apps.admin.controller.base.UIController;
import com.toucan.shopping.cloud.common.data.api.CategoryServiceAPI;
import com.toucan.shopping.cloud.product.api.BrandServiceAPI;
import com.toucan.shopping.modules.auth.admin.AdminAuth;
import com.toucan.shopping.modules.category.entity.Category;
import com.toucan.shopping.modules.category.vo.CategoryVO;
import com.toucan.shopping.modules.common.generator.RequestJsonVOGenerator;
import com.toucan.shopping.modules.common.properties.Toucan;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.image.upload.service.ImageUploadService;
import com.toucan.shopping.modules.product.vo.BrandVO;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import java.util.LinkedList;
import java.util.List;

@Controller
public class BrandPageController extends UIController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Value("${toucan.app-code}")
    private String appCode;

    @Autowired
    private Toucan toucan;

    @Autowired
    private FunctionServiceAPI functionServiceAPI;

    @Autowired
    private BrandServiceAPI brandService;

    @Autowired
    private CategoryServiceAPI categoryService;

    @Autowired
    private ImageUploadService imageUploadService;


    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH, requestType = AdminAuth.REQUEST_FORM, responseType = AdminAuth.RESPONSE_FORM)
    @RequestMapping(value = "/brand/listPage", method = RequestMethod.GET)
    public String listPage(HttpServletRequest request) {
        super.initButtons(request, toucan, "/brand/listPage", functionServiceAPI);
        return "pages/product/brand/list.html";
    }


    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH, requestType = AdminAuth.REQUEST_FORM, responseType = AdminAuth.RESPONSE_FORM)
    @RequestMapping(value = "/brand/addPage", method = RequestMethod.GET)
    public String addPage(HttpServletRequest request) {
        return "pages/product/brand/add.html";
    }


    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH, requestType = AdminAuth.REQUEST_FORM, responseType = AdminAuth.RESPONSE_FORM)
    @RequestMapping(value = "/brand/editPage/{id}", method = RequestMethod.GET)
    public String editPage(HttpServletRequest request, @PathVariable Long id) {
        try {
            BrandVO brandVO = new BrandVO();
            brandVO.setId(id);
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(appCode, brandVO);
            ResultObjectVO resultObjectVO = brandService.findById(requestJsonVO);
            if (resultObjectVO.getCode().intValue() == ResultObjectVO.SUCCESS.intValue()) {
                if (resultObjectVO.getData() != null) {
                    List<BrandVO> brandVOS = JSONArray.parseArray(JSONObject.toJSONString(resultObjectVO.getData()), BrandVO.class);
                    if (!CollectionUtils.isEmpty(brandVOS)) {
                        brandVO = brandVOS.get(0);

                        List<Category> categories = new LinkedList<Category>();
                        brandVO.setCategoryNamePathList(new LinkedList<>());
                        String[] categoryIdArray = brandVO.getCategoryIdCacheArray();
                        if (categoryIdArray != null && categoryIdArray.length > 0) {
                            for (String categoryId : categoryIdArray) {
                                if (categoryId != null) {
                                    Category category = new Category();
                                    category.setId(Long.parseLong(categoryId));
                                    categories.add(category);
                                }
                            }
                        }

                        requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(), categories);
                        resultObjectVO = categoryService.queryByIdList(requestJsonVO);
                        if (resultObjectVO.isSuccess()) {
                            List<CategoryVO> categoryList = resultObjectVO.formatDataList(CategoryVO.class);
                            if (!CollectionUtils.isEmpty(categoryList)) {
                                StringBuilder categoryNamePath = new StringBuilder();
                                int categoryListSize = categoryList.size();
                                for (int i = 0; i < categoryListSize; i++) {
                                    CategoryVO categoryVO = categoryList.get(i);
                                    if (categoryIdArray != null && categoryIdArray.length > 0) {
                                        for (String categoryId : categoryIdArray) {
                                            if (String.valueOf(categoryVO.getId()).equals(categoryId)) {
                                                brandVO.getCategoryNamePathList().add(categoryVO.getNamePath());
                                                categoryNamePath.append(categoryVO.getNamePath());
                                            }
                                        }
                                    }
                                    if (i + 1 < categoryListSize) {
                                        categoryNamePath.append("、");
                                    }
                                }
                                brandVO.setCategoryNamePath(categoryNamePath.toString());
                            }
                        }

                        if (StringUtils.isNotEmpty(brandVO.getLogoPath())) {
                            brandVO.setHttpLogoPath(imageUploadService.getImageHttpPrefix() + brandVO.getLogoPath());
                        }

                        request.setAttribute("model", brandVO);
                    }
                }

            }
        } catch (Exception e) {
            logger.warn(e.getMessage(), e);
        }
        return "pages/product/brand/edit.html";
    }

}
