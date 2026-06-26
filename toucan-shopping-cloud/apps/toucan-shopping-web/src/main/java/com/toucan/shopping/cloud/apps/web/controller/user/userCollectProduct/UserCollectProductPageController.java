package com.toucan.shopping.cloud.apps.web.controller.user.userCollectProduct;

import com.toucan.shopping.cloud.apps.web.controller.BaseController;
import com.toucan.shopping.cloud.apps.web.service.LoginUserService;
import com.toucan.shopping.modules.auth.user.UserAuth;
import com.toucan.shopping.modules.common.properties.Toucan;
import com.toucan.shopping.modules.redis.service.ToucanStringRedisService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpServletRequest;


/**
 * 用户收藏商品
 */
@Controller("userCollectProductPageController")
@RequestMapping("/page/user/collect/product")
public class UserCollectProductPageController extends BaseController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private Toucan toucan;

    @Autowired
    private LoginUserService loginUserService;



    @UserAuth(requestType = UserAuth.REQUEST_FORM,responseType = UserAuth.RESPONSE_FORM)
    @RequestMapping("/list")
    public String userCollectProductList(HttpServletRequest httpServletRequest)
    {
        loginUserService.setAttributeUser(httpServletRequest);
        return "user/userCollectProduct/collect_product_list";
    }

}
