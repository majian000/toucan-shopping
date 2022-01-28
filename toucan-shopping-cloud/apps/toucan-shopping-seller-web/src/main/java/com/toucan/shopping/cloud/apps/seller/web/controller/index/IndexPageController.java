package com.toucan.shopping.cloud.apps.seller.web.controller.index;

import com.toucan.shopping.cloud.apps.seller.web.service.ShopPageService;
import com.toucan.shopping.modules.auth.user.UserAuth;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;


/**
 * 首页控制器
 */
@Controller("pageIndexController")
public class IndexPageController {

    private final Logger logger = LoggerFactory.getLogger(getClass());


    @Autowired
    private ShopPageService shopPageService;



    @RequestMapping("/freeShop")
    public String freeShop(HttpServletRequest request)
    {
        return "/htmls/release/freeShop";
    }




    @UserAuth(requestType = UserAuth.REQUEST_FORM)
    @RequestMapping("/")
    public String defaultIndex(HttpServletRequest request)
    {
        return shopPageService.shopInfo(request);
    }


    @UserAuth(requestType = UserAuth.REQUEST_FORM)
    @RequestMapping("/index")
    public String index(HttpServletRequest request)
    {
        return shopPageService.shopInfo(request);
    }

}
