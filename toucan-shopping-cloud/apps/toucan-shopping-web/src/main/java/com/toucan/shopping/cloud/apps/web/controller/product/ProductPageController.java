package com.toucan.shopping.cloud.apps.web.controller.product;

import com.toucan.shopping.cloud.apps.web.controller.BaseController;
import com.toucan.shopping.cloud.apps.web.redis.UserLoginRedisKey;
import com.toucan.shopping.cloud.apps.web.service.LoginUserService;
import com.toucan.shopping.cloud.user.api.feign.service.FeignUserService;
import com.toucan.shopping.modules.auth.user.UserAuth;
import com.toucan.shopping.modules.common.generator.RequestJsonVOGenerator;
import com.toucan.shopping.modules.common.properties.Toucan;
import com.toucan.shopping.modules.common.util.DateUtils;
import com.toucan.shopping.modules.common.util.IPUtil;
import com.toucan.shopping.modules.common.util.UserAuthHeaderUtil;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.image.upload.service.ImageUploadService;
import com.toucan.shopping.modules.redis.service.ToucanStringRedisService;
import com.toucan.shopping.modules.user.vo.UserLoginVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;


/**
 * 商品页控制器
 * @auth majian
 * @date 2022-5-5 20:31:03
 */
@Controller("pageProductController")
@RequestMapping("/page/product")
public class ProductPageController extends BaseController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private Toucan toucan;



    @RequestMapping("/detail/{id}")
    public String detailPage(@PathVariable String id)
    {
        return "product/detail";
    }


    @RequestMapping("/preview/{id}")
    public String previewPage(@PathVariable String id)
    {
        return "product/preview";
    }

    @RequestMapping("/detail/pid/{id}")
    public String detailPageByProductApproveId(@PathVariable String id)
    {
        return "product/detail";
    }


    @RequestMapping("/preview/pid/{id}")
    public String previewPageByProductApproveId(@PathVariable String id)
    {
        return "product/preview";
    }
}
