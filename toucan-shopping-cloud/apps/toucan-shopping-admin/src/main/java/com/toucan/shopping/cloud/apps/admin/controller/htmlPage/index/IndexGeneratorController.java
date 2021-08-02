package com.toucan.shopping.cloud.apps.admin.controller.htmlPage.index;


import com.toucan.shopping.cloud.apps.admin.auth.web.controller.base.UIController;
import com.toucan.shopping.modules.auth.admin.AdminAuth;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/index/html")
public class IndexGeneratorController extends UIController {


    private final Logger logger = LoggerFactory.getLogger(getClass());



    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH,requestType = AdminAuth.REQUEST_FORM)
    @RequestMapping(value = "/indexGeneratorPage",method = RequestMethod.GET)
    public String indexGeneratorPage(HttpServletRequest request)
    {
        return "pages/htmlPage/index/index.html";
    }

}
