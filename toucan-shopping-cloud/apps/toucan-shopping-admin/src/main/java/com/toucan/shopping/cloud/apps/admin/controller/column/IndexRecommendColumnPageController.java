package com.toucan.shopping.cloud.apps.admin.controller.column;


import com.toucan.shopping.cloud.apps.admin.controller.base.UIController;
import com.toucan.shopping.cloud.admin.auth.api.FunctionServiceAPI;
import com.toucan.shopping.modules.auth.admin.AdminAuth;
import com.toucan.shopping.modules.common.properties.Toucan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;

/**
 * 首页推荐栏目
 */
@Controller
public class IndexRecommendColumnPageController extends UIController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private Toucan toucan;

    @Autowired
    private FunctionServiceAPI functionServiceAPI;


    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH,requestType = AdminAuth.REQUEST_FORM,responseType=AdminAuth.RESPONSE_FORM)
    @RequestMapping(value = "/indexRecommendColumn/listPage",method = RequestMethod.GET)
    public String listPage(HttpServletRequest request)
    {
        //初始化工具条按钮、操作按钮
        super.initButtons(request,toucan,"/indexRecommendColumn/listPage", functionServiceAPI);

        initColumnTypeCode(request);

        return "pages/column/indexRecommendColumn/list.html";
    }

    private void initColumnTypeCode(HttpServletRequest request)
    {
        try {
            request.setAttribute("columnTypeCode",toucan.getShoppingPC().getPcIndexColumnTypeCode());
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);

            request.setAttribute("columnTypeCode","");
        }
    }



    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH,requestType = AdminAuth.REQUEST_FORM,responseType=AdminAuth.RESPONSE_FORM)
    @RequestMapping(value = "/indexRecommendColumn/addPage",method = RequestMethod.GET)
    public String addPage(HttpServletRequest request)
    {
        initColumnTypeCode(request);
        return "pages/column/indexRecommendColumn/add.html";
    }

    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH,requestType = AdminAuth.REQUEST_FORM,responseType=AdminAuth.RESPONSE_FORM)
    @RequestMapping(value = "/indexRecommendColumn/editPage/{id}",method = RequestMethod.GET)
    public String editPage(HttpServletRequest request,@PathVariable Long id)
    {
        request.setAttribute("id",id);
        return "pages/column/indexRecommendColumn/edit.html";
    }


    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH,requestType = AdminAuth.REQUEST_FORM,responseType=AdminAuth.RESPONSE_FORM)
    @RequestMapping(value = "/indexRecommendColumn/showPage/{id}",method = RequestMethod.GET)
    public String showPage(HttpServletRequest request,@PathVariable Long id)
    {
        request.setAttribute("id",id);
        return "pages/column/indexRecommendColumn/show.html";
    }


}
