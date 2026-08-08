package com.toucan.shopping.cloud.apps.admin.controller.freightTemplate;


import com.toucan.shopping.cloud.admin.auth.api.FunctionServiceAPI;
import com.toucan.shopping.cloud.apps.admin.controller.base.UIController;
import com.toucan.shopping.modules.auth.admin.AdminAuth;
import com.toucan.shopping.modules.common.properties.Toucan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;

/**
 * 运费模板
 * @author majian
 */
@Controller
public class FreightTemplatePageController extends UIController {


    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private Toucan toucan;

    @Autowired
    private FunctionServiceAPI functionServiceAPI;


    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH,requestType = AdminAuth.REQUEST_FORM,responseType=AdminAuth.RESPONSE_FORM)
    @RequestMapping(value = "/freightTemplate/listPage",method = RequestMethod.GET)
    public String listPage(HttpServletRequest request)
    {
        //初始化工具条按钮、操作按钮
        super.initButtons(request,toucan,"/freightTemplate/listPage", functionServiceAPI);
        return "pages/seller/freightTemplate/list.html";
    }


    /**
     * 查看运费模板
     * @param request
     * @param id
     * @return
     */
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH,requestType = AdminAuth.REQUEST_FORM,responseType=AdminAuth.RESPONSE_FORM)
    @RequestMapping(value = "/freightTemplate/detailPage/{id}",method = RequestMethod.GET)
    public String detailPage(HttpServletRequest request,@PathVariable Long id)
    {
        request.setAttribute("freightTemplateId",id);
        return "pages/seller/freightTemplate/detail.html";
    }

}
