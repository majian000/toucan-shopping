package com.toucan.shopping.cloud.apps.seller.web.controller.shop;

import com.toucan.shopping.cloud.apps.seller.web.controller.BaseController;
import com.toucan.shopping.cloud.apps.seller.web.service.ShopPageService;
import com.toucan.shopping.cloud.seller.api.SellerShopServiceAPI;
import com.toucan.shopping.cloud.user.api.UserServiceAPI;
import com.toucan.shopping.modules.auth.user.UserAuth;
import com.toucan.shopping.modules.common.generator.RequestJsonVOGenerator;
import com.toucan.shopping.modules.common.properties.Toucan;
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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


/**
 * 个人店铺
 */
@Controller("userShopPageController")
@RequestMapping("/page/user/shop")
public class UserShopPageController extends BaseController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private UserServiceAPI userService;

    @Autowired
    private SellerShopServiceAPI sellerShopService;

    @Autowired
    private Toucan toucan;

    @Autowired
    private ImageUploadService imageUploadService;

    @Autowired
    private ShopPageService shopPageService;

    /**
     * 个人店铺申请
     * @return
     */
    @UserAuth(requestType = UserAuth.REQUEST_FORM,responseType = UserAuth.RESPONSE_FORM)
    @RequestMapping("/regist")
    public String regist(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse)
    {

        try {
            UserVO userVO = new UserVO();
            String userMainId = UserAuthHeaderUtil.getUserMainId(httpServletRequest.getHeader(toucan.getUserAuth().getHttpToucanAuthHeader()));
            userVO.setUserMainId(Long.parseLong(userMainId));
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(this.getAppCode(), userVO);
            ResultObjectVO resultObjectVO = userService.verifyRealName( requestJsonVO);
            if(resultObjectVO.isSuccess())
            {
                boolean result = Boolean.valueOf(String.valueOf(resultObjectVO.getData()));
                if(result)
                {
                    SellerShop querySellerShop = new SellerShop();
                    querySellerShop.setUserMainId(userVO.getUserMainId());
                    requestJsonVO = RequestJsonVOGenerator.generator(this.getAppCode(), querySellerShop);
                    //判断是个人店铺还是企业店铺
                    resultObjectVO = sellerShopService.findByUser(requestJsonVO);
                    if(resultObjectVO.isSuccess())
                    {
                        //该账号存在店铺
                        SellerShopVO sellerShopVO = resultObjectVO.formatData(SellerShopVO.class);
                        if(sellerShopVO!=null)
                        {
                            //重定向到店铺首页
                            httpServletResponse.sendRedirect("/index");
                        }

                    }
                    return "shop/userShop/regist";
                }else{
                    //重定向到实名审核页面
                    httpServletResponse.sendRedirect("/page/user/true/name/approve/page");
                }
            }
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);
        }
        return shopPageService.shopInfo(httpServletRequest);
    }

}
