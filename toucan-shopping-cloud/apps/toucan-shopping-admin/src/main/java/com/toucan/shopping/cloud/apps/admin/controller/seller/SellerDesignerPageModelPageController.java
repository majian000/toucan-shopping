package com.toucan.shopping.cloud.apps.admin.controller.seller;


import com.toucan.shopping.cloud.admin.auth.api.FunctionServiceAPI;
import com.toucan.shopping.cloud.apps.admin.controller.base.UIController;
import com.toucan.shopping.modules.auth.admin.AdminAuth;
import com.toucan.shopping.modules.common.properties.Toucan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import jakarta.servlet.http.HttpServletRequest;

/**
 *  卖家设计器页面模型管理 - 页面控制器
 */
@Controller
public class SellerDesignerPageModelPageController extends UIController {

    @Value("${toucan.app-code}")
    private String appCode;

    @Autowired
    private Toucan toucan;

    @Autowired
    private FunctionServiceAPI functionServiceAPI;


    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH,requestType = AdminAuth.REQUEST_FORM,responseType=AdminAuth.RESPONSE_FORM)
    @RequestMapping(value = "/sellerDesignerPageModel/listPage",method = RequestMethod.GET)
    public String listPage(HttpServletRequest request)
    {
        //初始化工具条按钮、操作按钮
        super.initButtons(request,toucan,"/sellerDesignerPageModel/listPage", functionServiceAPI);
        return "pages/seller/designer/pageModel/list.html";
    }

}
