package com.toucan.shopping.cloud.apps.seller.web.controller.order;

import com.alibaba.fastjson.serializer.SimplePropertyPreFilter;
import com.toucan.shopping.cloud.apps.seller.web.controller.BaseController;
import com.toucan.shopping.cloud.apps.seller.web.service.CategoryService;
import com.toucan.shopping.cloud.seller.api.feign.service.FeignShopCategoryService;
import com.toucan.shopping.modules.auth.user.UserAuth;
import com.toucan.shopping.modules.category.vo.CategoryVO;
import com.toucan.shopping.modules.common.properties.Toucan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;


/**
 * 订单
 */
@Controller("orderPageController")
@RequestMapping("/page/order")
public class OrderPageController extends BaseController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private Toucan toucan;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private FeignShopCategoryService feignShopCategoryService;


    @UserAuth(requestType = UserAuth.REQUEST_FORM,responseType = UserAuth.RESPONSE_FORM)
    @RequestMapping("/index")
    public String index(HttpServletRequest request){

        return "order/index";
    }

    @UserAuth(requestType = UserAuth.REQUEST_FORM,responseType = UserAuth.RESPONSE_FORM)
    @RequestMapping("/show/{id}")
    public String show(HttpServletRequest request, @PathVariable String id ){
        request.setAttribute("id",id);
        return "order/show";
    }



}
