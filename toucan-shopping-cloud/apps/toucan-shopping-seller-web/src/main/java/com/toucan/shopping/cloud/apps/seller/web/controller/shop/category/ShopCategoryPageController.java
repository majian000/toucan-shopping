package com.toucan.shopping.cloud.apps.seller.web.controller.shop.category;

import com.toucan.shopping.cloud.apps.seller.web.controller.BaseController;
import com.toucan.shopping.modules.auth.user.UserAuth;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * 店铺分类
 */
@Controller("categoryPageController")
@RequestMapping("/page/shop/category")
public class ShopCategoryPageController extends BaseController {

    private final Logger logger = LoggerFactory.getLogger(getClass());


    @UserAuth(requestType = UserAuth.REQUEST_FORM)
    @RequestMapping("/index")
    public String index(HttpServletRequest request)
    {
        return "shop/category/index";
    }

    @UserAuth(requestType = UserAuth.REQUEST_FORM)
    @RequestMapping("/list")
    public String list(HttpServletRequest request)
    {
        return "shop/category/list";
    }


    @UserAuth(requestType = UserAuth.REQUEST_FORM)
    @RequestMapping("/{parentId}/add")
    public String add(HttpServletRequest request, @PathVariable String parentId)
    {
        request.setAttribute("parentId",parentId);
        return "shop/category/add";
    }
}
