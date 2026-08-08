package com.toucan.shopping.cloud.apps.admin.controller.articleImage;


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
 * 文章图片管理
 */
@Controller
public class ArticleImagePageController extends UIController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private Toucan toucan;

    @Autowired
    private FunctionServiceAPI functionServiceAPI;

    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH,requestType = AdminAuth.REQUEST_FORM,responseType=AdminAuth.RESPONSE_FORM)
    @RequestMapping(value = "/articleImage/listPage",method = RequestMethod.GET)
    public String listPage(HttpServletRequest request)
    {
        //初始化工具条按钮、操作按钮
        super.initButtons(request,toucan,"/articleImage/listPage", functionServiceAPI);
        return "pages/articleImage/list.html";
    }


}
