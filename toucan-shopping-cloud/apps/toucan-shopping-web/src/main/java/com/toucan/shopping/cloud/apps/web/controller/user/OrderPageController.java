package com.toucan.shopping.cloud.apps.web.controller.user;

import com.toucan.shopping.cloud.apps.web.controller.BaseController;
import com.toucan.shopping.cloud.apps.web.redis.UserLoginRedisKey;
import com.toucan.shopping.cloud.user.api.feign.service.FeignUserService;
import com.toucan.shopping.modules.auth.user.UserAuth;
import com.toucan.shopping.modules.common.generator.RequestJsonVOGenerator;
import com.toucan.shopping.modules.common.properties.Toucan;
import com.toucan.shopping.modules.common.util.IPUtil;
import com.toucan.shopping.modules.common.util.UserAuthHeaderUtil;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.redis.service.ToucanStringRedisService;
import com.toucan.shopping.modules.user.vo.UserLoginVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * 订单控制器
 */
@Controller("pageOrderController")
@RequestMapping("/page/order")
public class OrderPageController extends BaseController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private Toucan toucan;

    @Autowired
    private ToucanStringRedisService toucanStringRedisService;

    @Autowired
    private FeignUserService feignUserService;



    @RequestMapping("/list")
    public String regist()
    {
        return "user/order_list";
    }

}
