package com.toucan.shopping.cloud.apps.seller.web.controller.designer;

import com.toucan.shopping.cloud.apps.seller.web.controller.BaseController;
import com.toucan.shopping.modules.auth.user.UserAuth;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;


/**
 * PC端设计器
 */
@Controller("designerPageController")
@RequestMapping("/page/designer")
public class DesignerPageController extends BaseController {

    private final Logger logger = LoggerFactory.getLogger(getClass());


    @UserAuth(requestType = UserAuth.REQUEST_FORM)
    @RequestMapping("/index")
    public String index(HttpServletRequest request){
        return "designer/index";
    }

    @UserAuth(requestType = UserAuth.REQUEST_FORM)
    @RequestMapping("/pc/index")
    public String pcIndex(HttpServletRequest request){
        return "designer/pc/index";
    }

}
