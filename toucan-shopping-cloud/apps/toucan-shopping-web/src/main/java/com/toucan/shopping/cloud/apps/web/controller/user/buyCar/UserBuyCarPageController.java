package com.toucan.shopping.cloud.apps.web.controller.user.buyCar;


import com.alibaba.fastjson.JSONArray;
import com.toucan.shopping.cloud.common.data.api.feign.service.FeignAreaService;
import com.toucan.shopping.modules.area.vo.AreaVO;
import com.toucan.shopping.modules.auth.user.UserAuth;
import com.toucan.shopping.modules.common.generator.RequestJsonVOGenerator;
import com.toucan.shopping.modules.common.properties.Toucan;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * 用户购物车
 */
@Controller("buyCarPageController")
@RequestMapping("/page/user/buyCar")
public class UserBuyCarPageController {


    private final Logger logger = LoggerFactory.getLogger(getClass());

    @UserAuth
    @RequestMapping("/index")
    public String indexPage()
    {
        return "user/buyCar/user_buy_car";
    }


    @Autowired
    private FeignAreaService feignAreaService;

    @Autowired
    private Toucan toucan;

    @UserAuth(requestType =UserAuth.REQUEST_FORM)
    @RequestMapping("/confirm")
    public String confirmPage(HttpServletRequest request)
    {
        return "user/buyCar/user_buy_confirm";
    }

}
