package com.toucan.shopping.cloud.apps.admin.controller.seller;


import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.toucan.shopping.cloud.admin.auth.api.FunctionServiceAPI;
import com.toucan.shopping.cloud.apps.admin.controller.base.UIController;
import com.toucan.shopping.cloud.seller.api.ShopCategoryServiceAPI;
import com.toucan.shopping.modules.auth.admin.AdminAuth;
import com.toucan.shopping.modules.category.entity.Category;
import com.toucan.shopping.modules.common.generator.RequestJsonVOGenerator;
import com.toucan.shopping.modules.common.properties.Toucan;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.seller.entity.ShopCategory;
import com.toucan.shopping.modules.seller.vo.ShopCategoryVO;
import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import java.util.List;

/**
 *店铺分类控制器 - 页面控制器
 */
@Controller
public class ShopCategoryPageController extends UIController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Value("${toucan.app-code}")
    private String appCode;

    @Autowired
    private Toucan toucan;

    @Autowired
    private ShopCategoryServiceAPI shopCategoryService;

    @Autowired
    private FunctionServiceAPI functionServiceAPI;


    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH,requestType = AdminAuth.REQUEST_FORM,responseType=AdminAuth.RESPONSE_FORM)
    @RequestMapping(value = "/shopCategory/listPage/{shopId}",method = RequestMethod.GET)
    public String listPage(HttpServletRequest request,@PathVariable Long shopId)
    {
        //初始化工具条按钮、操作按钮
        super.initButtons(request,toucan,"/shopCategory/listPage", functionServiceAPI);

        request.setAttribute("shopId",shopId);

        return "pages/seller/shopCategory/list.html";
    }


    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH,requestType = AdminAuth.REQUEST_FORM,responseType=AdminAuth.RESPONSE_FORM)
    @RequestMapping(value = "/shopCategory/addPage/{shopId}/{parentId}",method = RequestMethod.GET)
    public String addPage(HttpServletRequest request,@PathVariable Long shopId,@PathVariable Long parentId)
    {
        request.setAttribute("shopId",shopId);
        request.setAttribute("parentId",parentId);
        return "pages/seller/shopCategory/add.html";
    }


    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH,requestType = AdminAuth.REQUEST_FORM,responseType=AdminAuth.RESPONSE_FORM)
    @RequestMapping(value = "/shopCategory/editPage/{id}",method = RequestMethod.GET)
    public String editPage(HttpServletRequest request,@PathVariable Long id)
    {
        try {
            Category entity = new Category();
            entity.setId(id);
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(appCode, entity);
            ResultObjectVO resultObjectVO = shopCategoryService.findById(requestJsonVO);
            if(resultObjectVO.getCode().intValue()==ResultObjectVO.SUCCESS.intValue())
            {
                if(resultObjectVO.getData()!=null) {
                    List<ShopCategory> entitys = JSONArray.parseArray(JSONObject.toJSONString(resultObjectVO.getData()),ShopCategory.class);
                    if(!CollectionUtils.isEmpty(entitys))
                    {
                        ShopCategoryVO shopCategoryVO = new ShopCategoryVO();
                        BeanUtils.copyProperties(shopCategoryVO,entitys.get(0));
                        request.setAttribute("model",shopCategoryVO);
                    }
                }

            }
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);
        }
        return "pages/seller/shopCategory/edit.html";
    }

}
