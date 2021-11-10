package com.toucan.shopping.cloud.apps.seller.web.controller.index;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.toucan.shopping.cloud.apps.seller.web.service.ShopPageService;
import com.toucan.shopping.cloud.area.api.feign.service.FeignAreaService;
import com.toucan.shopping.cloud.seller.api.feign.service.FeignSellerShopService;
import com.toucan.shopping.cloud.user.api.feign.service.FeignUserService;
import com.toucan.shopping.modules.auth.user.UserAuth;
import com.toucan.shopping.modules.common.generator.RequestJsonVOGenerator;
import com.toucan.shopping.modules.common.properties.Toucan;
import com.toucan.shopping.modules.common.util.SignUtil;
import com.toucan.shopping.modules.common.util.UserAuthHeaderUtil;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.image.upload.service.ImageUploadService;
import com.toucan.shopping.modules.seller.entity.SellerShop;
import com.toucan.shopping.modules.seller.vo.SellerShopVO;
import com.toucan.shopping.modules.user.vo.UserVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


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
