package com.toucan.shopping.cloud.apps.admin.controller.product.productSpu;


import com.alibaba.fastjson2.JSON;
import com.toucan.shopping.cloud.admin.auth.api.FunctionServiceAPI;
import com.toucan.shopping.cloud.apps.admin.controller.base.UIController;
import com.toucan.shopping.cloud.common.data.api.CategoryServiceAPI;
import com.toucan.shopping.cloud.product.api.BrandServiceAPI;
import com.toucan.shopping.cloud.product.api.ProductSpuServiceAPI;
import com.toucan.shopping.modules.auth.admin.AdminAuth;
import com.toucan.shopping.modules.category.vo.CategoryTreeVO;
import com.toucan.shopping.modules.category.vo.CategoryVO;
import com.toucan.shopping.modules.common.generator.RequestJsonVOGenerator;
import com.toucan.shopping.modules.common.properties.Toucan;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.image.upload.service.ImageUploadService;
import com.toucan.shopping.modules.product.vo.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * SPU管理 - 页面控制器
 * @author majian
 */
@Controller
public class ProductSpuPageController extends UIController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Value("${toucan.app-code}")
    private String appCode;

    @Autowired
    private Toucan toucan;

    @Autowired
    private FunctionServiceAPI functionServiceAPI;


    @Autowired
    private CategoryServiceAPI categoryServiceAPI;

    @Autowired
    private ImageUploadService imageUploadService;

    @Autowired
    private ProductSpuServiceAPI productSpuServiceAPI;

    @Autowired
    private BrandServiceAPI brandServiceAPI;



    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH,requestType = AdminAuth.REQUEST_FORM,responseType=AdminAuth.RESPONSE_FORM)
    @RequestMapping(value = "/productSpu/listPage",method = RequestMethod.GET)
    public String listPage(HttpServletRequest request)
    {
        //初始化工具条按钮、操作按钮
        super.initButtons(request,toucan,"/productSpu/listPage", functionServiceAPI);
        return "pages/product/productSpu/list.html";
    }





    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH,requestType = AdminAuth.REQUEST_FORM,responseType=AdminAuth.RESPONSE_FORM)
    @RequestMapping(value = "/productSpu/addPage/{categoryId}",method = RequestMethod.GET)
    public String addPage(HttpServletRequest request,@PathVariable Long categoryId)
    {

        if(categoryId!=null&&categoryId!=-1)
        {
            try {
                CategoryVO queryCategoryVO = new CategoryVO();
                queryCategoryVO.setId(categoryId);
                RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(), queryCategoryVO);
                ResultObjectVO resultObjectVO = categoryServiceAPI.queryById(requestJsonVO);
                if(resultObjectVO.isSuccess())
                {
                    CategoryTreeVO categoryTreeVO = resultObjectVO.formatData(CategoryTreeVO.class);
                    request.setAttribute("categoryId",categoryTreeVO.getId());
                    request.setAttribute("categoryName",categoryTreeVO.getName());
                }else{
                    request.setAttribute("categoryId","");
                    request.setAttribute("categoryName","");
                }
                return "pages/product/productSpu/add.html";
            }catch(Exception e)
            {
                logger.warn(e.getMessage(),e);
            }
        }
        request.setAttribute("categoryId","");
        request.setAttribute("categoryName","");
        return "pages/product/productSpu/add.html";
    }



    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH,requestType = AdminAuth.REQUEST_FORM,responseType=AdminAuth.RESPONSE_FORM)
    @RequestMapping(value = "/productSpu/selectBrandPage/{categoryId}",method = RequestMethod.GET)
    public String selectBrandPage(HttpServletRequest request, @PathVariable Long categoryId)
    {
        request.setAttribute("categoryId",categoryId);
        return "pages/product/productSpu/brand_list.html";
    }




    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH,requestType = AdminAuth.REQUEST_FORM,responseType=AdminAuth.RESPONSE_FORM)
    @RequestMapping(value = "/productSpu/editPage/{id}",method = RequestMethod.GET)
    public String editPage(HttpServletRequest request,@PathVariable Long id)
    {
        try {
            ProductSpuVO queryProductSpu = new ProductSpuVO();
            queryProductSpu.setId(id);
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(appCode, queryProductSpu);
            ResultObjectVO resultObjectVO = productSpuServiceAPI.findById(requestJsonVO);
            if(resultObjectVO.getCode().intValue()==ResultObjectVO.SUCCESS.intValue())
            {
                if(resultObjectVO.getData()!=null) {
                    ProductSpuVO productSpuVO = resultObjectVO.formatData(ProductSpuVO.class);

                    //查询分类
                    CategoryVO queryCategory = new CategoryVO();
                    queryCategory.setId(productSpuVO.getCategoryId());
                    requestJsonVO = RequestJsonVOGenerator.generator(appCode, queryCategory);
                    resultObjectVO = categoryServiceAPI.queryById(requestJsonVO);
                    if(resultObjectVO.isSuccess()&&resultObjectVO.getData()!=null)
                    {
                        CategoryTreeVO categoryTreeVO = resultObjectVO.formatData(CategoryTreeVO.class);
                        if(categoryTreeVO!=null) {
                            productSpuVO.setCategoryName(categoryTreeVO.getName());
                        }
                    }

                    //查询品牌
                    BrandVO queryBrand = new BrandVO();
                    queryBrand.setId(productSpuVO.getBrandId());
                    requestJsonVO = RequestJsonVOGenerator.generator(appCode, queryBrand);
                    resultObjectVO = brandServiceAPI.findById(requestJsonVO);
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
        return "pages/product/productSpu/edit.html";
    }


    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH,requestType = AdminAuth.REQUEST_FORM,responseType=AdminAuth.RESPONSE_FORM)
    @RequestMapping(value = "/productSpu/detailPage/{id}",method = RequestMethod.GET)
    public String detailPage(HttpServletRequest request,@PathVariable Long id)
    {
        try {
            ProductSpuVO queryProductSpu = new ProductSpuVO();
            queryProductSpu.setId(id);
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(appCode, queryProductSpu);
            ResultObjectVO resultObjectVO = productSpuServiceAPI.findById(requestJsonVO);
            if(resultObjectVO.getCode().intValue()==ResultObjectVO.SUCCESS.intValue())
            {
                if(resultObjectVO.getData()!=null) {
                    ProductSpuVO productSpuVO = resultObjectVO.formatData(ProductSpuVO.class);

                    //查询分类
                    CategoryVO queryCategory = new CategoryVO();
                    queryCategory.setId(productSpuVO.getCategoryId());
                    requestJsonVO = RequestJsonVOGenerator.generator(appCode, queryCategory);
                    resultObjectVO = categoryServiceAPI.queryById(requestJsonVO);
                    if(resultObjectVO.isSuccess()&&resultObjectVO.getData()!=null)
                    {
                        CategoryTreeVO categoryTreeVO = resultObjectVO.formatData(CategoryTreeVO.class);
                        if(categoryTreeVO!=null) {
                            productSpuVO.setCategoryName(categoryTreeVO.getName());
                            productSpuVO.setCategoryPath(categoryTreeVO.getPath());
                        }
                    }

                    //查询品牌
                    BrandVO queryBrand = new BrandVO();
                    queryBrand.setId(productSpuVO.getBrandId());
                    requestJsonVO = RequestJsonVOGenerator.generator(appCode, queryBrand);
                    resultObjectVO = brandServiceAPI.findById(requestJsonVO);
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
                        productSpuVO.setBrandChineseName(brandVO.getChineseName());
                        productSpuVO.setBrandEnglishName(brandVO.getEnglishName());
                        productSpuVO.setBrandLogo(brandVO.getLogoPath());
                        if(brandVO.getLogoPath()!=null) {
                            productSpuVO.setBrandHttpLogo(imageUploadService.getImageHttpPrefix() +brandVO.getLogoPath());
                        }

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
        return "pages/product/productSpu/detail.html";
    }


}
