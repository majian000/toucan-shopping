package com.toucan.shopping.cloud.apps.seller.web.controller.user;

import com.toucan.shopping.cloud.apps.seller.web.controller.BaseController;
import com.toucan.shopping.cloud.user.api.feign.service.FeignUserService;
import com.toucan.shopping.modules.auth.user.UserAuth;
import com.toucan.shopping.modules.common.generator.RequestJsonVOGenerator;
import com.toucan.shopping.modules.common.properties.Toucan;
import com.toucan.shopping.modules.common.util.UserAuthHeaderUtil;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.user.vo.UserVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * 店铺控制器
 */
@Controller("pageShopController")
@RequestMapping("/page/shop")
public class ShopPageController extends BaseController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private FeignUserService feignUserService;


    @Autowired
    private Toucan toucan;


    /**
     * 个人店铺申请
     * @return
     */
    @UserAuth(requestType = UserAuth.REQUEST_FORM)
    @RequestMapping("/shop_regist/user")
    public String center(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse)
    {

        try {
            UserVO userVO = new UserVO();
            String userMainId = UserAuthHeaderUtil.getUserMainId(toucan.getAppCode(), httpServletRequest.getHeader(toucan.getUserAuth().getHttpToucanAuthHeader()));
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(this.getAppCode(), userVO);
            ResultObjectVO resultObjectVO = feignUserService.verifyRealName(requestJsonVO.sign(), requestJsonVO);
            if(resultObjectVO.isSuccess())
            {
                boolean result = Boolean.valueOf(String.valueOf(resultObjectVO.getData()));
                if(result)
                {
                    return "shop_regist";
                }else{
//                    Cookie[] cookies = httpServletRequest.getCookies();
//                    String tss_uid="";
//                    String tss_lt="";
//                    if(cookies!=null)
//                    {
//                        for(Cookie cookie:cookies)
//                        {
//                            if(cookie.getName().equals("tss_uid"))
//                            {
//                                tss_uid = cookie.getValue();
//                            }
//                            if(cookie.getName().equals("tss_lt"))
//                            {
//                                tss_lt = cookie.getValue();
//                            }
//                        }
//
//                    }
                    httpServletResponse.sendRedirect(toucan.getShoppingPC().getRealNameAuthenticationPage());
                }
            }

        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);
        }
        return "index";
    }

}
