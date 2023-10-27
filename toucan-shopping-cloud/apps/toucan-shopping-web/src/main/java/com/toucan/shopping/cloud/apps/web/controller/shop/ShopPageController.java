package com.toucan.shopping.cloud.apps.web.controller.shop;

import com.toucan.shopping.cloud.apps.web.controller.BaseController;
import com.toucan.shopping.cloud.seller.api.feign.service.FeignSellerDesignerPageModelService;
import com.toucan.shopping.modules.auth.user.UserAuth;
import com.toucan.shopping.modules.common.properties.Toucan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;


/**
 * 店铺控制器
 * @author majian
 * @date 2023-10-27 14:45:14
 */
@Controller("pageShopController")
@RequestMapping("/page/shop")
public class ShopPageController extends BaseController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private Toucan toucan;


    @Autowired
    private FeignSellerDesignerPageModelService feignSellerDesignerPageModelService;


    @UserAuth(requestType = UserAuth.REQUEST_FORM)
    @RequestMapping("/index/preview")
    public String indexPreviewPage()
    {
        return "shop/index_preview";
    }

}
