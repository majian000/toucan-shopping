package com.toucan.shopping.cloud.apps.admin.controller.product.attribute;


import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.toucan.shopping.cloud.admin.auth.api.FunctionServiceAPI;
import com.toucan.shopping.cloud.apps.admin.controller.base.UIController;
import com.toucan.shopping.cloud.common.data.api.CategoryServiceAPI;
import com.toucan.shopping.cloud.product.api.AttributeKeyServiceAPI;
import com.toucan.shopping.modules.auth.admin.AdminAuth;
import com.toucan.shopping.modules.category.vo.CategoryTreeVO;
import com.toucan.shopping.modules.category.vo.CategoryVO;
import com.toucan.shopping.modules.common.generator.RequestJsonVOGenerator;
import com.toucan.shopping.modules.common.properties.Toucan;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.product.vo.AttributeKeyVO;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * 商品属性管理页面控制器
 */
@Controller
public class AttributeKeyPageController extends UIController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Value("${toucan.app-code}")
    private String appCode;

    @Autowired
    private Toucan toucan;

    @Autowired
    private FunctionServiceAPI functionServiceAPI;

    @Autowired
    private AttributeKeyServiceAPI attributeKeyService;

    @Autowired
    private CategoryServiceAPI categoryService;




    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH,requestType = AdminAuth.REQUEST_FORM,responseType=AdminAuth.RESPONSE_FORM)
    @RequestMapping(value = "/attributeKey/listPage",method = RequestMethod.GET)
    public String listPage(HttpServletRequest request)
    {
        //初始化工具条按钮、操作按钮
        super.initButtons(request,toucan,"/product/attribute/attributeKey/listPage", functionServiceAPI);
        return "pages/product/attribute/attributeKey/list.html";
    }




    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH,requestType = AdminAuth.REQUEST_FORM,responseType=AdminAuth.RESPONSE_FORM)
    @RequestMapping(value = "/attributeKey/addPage/{categoryId}",method = RequestMethod.GET)
    public String addPage(HttpServletRequest request,@PathVariable Long categoryId)
    {
        if(categoryId!=null&&categoryId!=-1)
        {
            try {
                CategoryVO queryCategoryVO = new CategoryVO();
                queryCategoryVO.setId(categoryId);
                RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(), queryCategoryVO);
                ResultObjectVO resultObjectVO = categoryService.queryById(requestJsonVO);
                if(resultObjectVO.isSuccess())
                {
                    CategoryTreeVO categoryTreeVO = resultObjectVO.formatData(CategoryTreeVO.class);
                    request.setAttribute("categoryId",categoryTreeVO.getId());
                    request.setAttribute("categoryName",categoryTreeVO.getPath());
                }else{
                    request.setAttribute("categoryId","");
                    request.setAttribute("categoryName","");
                }
                return "pages/product/attribute/attributeKey/add.html";
            }catch(Exception e)
            {
                logger.warn(e.getMessage(),e);
            }
        }
        request.setAttribute("categoryId","");
        request.setAttribute("categoryName","");
        return "pages/product/attribute/attributeKey/add.html";
    }




    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH,requestType = AdminAuth.REQUEST_FORM,responseType=AdminAuth.RESPONSE_FORM)
    @RequestMapping(value = "/attributeKey/editPage/{id}",method = RequestMethod.GET)
    public String editPage(HttpServletRequest request,@PathVariable Long id)
    {
        try {
            AttributeKeyVO attributeKeyVO = new AttributeKeyVO();
            attributeKeyVO.setId(id);
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(appCode, attributeKeyVO);
            ResultObjectVO resultObjectVO = attributeKeyService.findById(requestJsonVO);
            if(resultObjectVO.getCode().intValue()==ResultObjectVO.SUCCESS.intValue())
            {
                if(resultObjectVO.getData()!=null) {
                    List<AttributeKeyVO> attributeKeyVOS = JSONArray.parseArray(JSONObject.toJSONString(resultObjectVO.getData()),AttributeKeyVO.class);
                    if(!CollectionUtils.isEmpty(attributeKeyVOS))
                    {
                        attributeKeyVO = attributeKeyVOS.get(0);

                        //查询类别名称
                        CategoryVO queryCategoryVO = new CategoryVO();
                        queryCategoryVO.setId(attributeKeyVO.getCategoryId());
                        requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(),queryCategoryVO);
                        resultObjectVO = categoryService.findById(requestJsonVO);
                        if(resultObjectVO.isSuccess())
                        {
                            List<CategoryVO> categoryVOS = (List<CategoryVO>)resultObjectVO.formatDataList(CategoryVO.class);
                            if(CollectionUtils.isNotEmpty(categoryVOS))
                            {
                                for(CategoryVO categoryVO:categoryVOS)
                                {
                                    if(attributeKeyVO.getCategoryId().longValue()==categoryVO.getId().longValue())
                                    {
                                        attributeKeyVO.setCategoryName(categoryVO.getName());
                                        break;
                                    }
                                }
                            }
                        }
                        request.setAttribute("model",attributeKeyVO);
                    }
                }

            }
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);
        }
        return "pages/product/attribute/attributeKey/edit.html";
    }




}