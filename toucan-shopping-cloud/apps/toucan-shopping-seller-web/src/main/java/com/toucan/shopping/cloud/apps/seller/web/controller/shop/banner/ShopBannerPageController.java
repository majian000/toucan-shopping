package com.toucan.shopping.cloud.apps.seller.web.controller.shop.banner;

import com.toucan.shopping.cloud.apps.seller.web.controller.BaseController;
import com.toucan.shopping.cloud.seller.api.SellerShopServiceAPI;
import com.toucan.shopping.cloud.seller.api.ShopBannerServiceAPI;
import com.toucan.shopping.cloud.seller.api.ShopCategoryServiceAPI;
import com.toucan.shopping.cloud.user.api.UserServiceAPI;
import com.toucan.shopping.modules.auth.shop.ShopAuth;
import com.toucan.shopping.modules.auth.user.UserAuth;
import com.toucan.shopping.modules.common.generator.RequestJsonVOGenerator;
import com.toucan.shopping.modules.common.properties.Toucan;
import com.toucan.shopping.modules.common.util.DateUtils;
import com.toucan.shopping.modules.common.util.UserAuthHeaderUtil;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.image.upload.service.ImageUploadService;
import com.toucan.shopping.modules.seller.entity.SellerShop;
import com.toucan.shopping.modules.seller.vo.SellerShopVO;
import com.toucan.shopping.modules.seller.vo.ShopBannerVO;
import com.toucan.shopping.modules.seller.vo.ShopCategoryVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.http.HttpServletRequest;

/**
 * 店铺轮播图
 */
@Controller("bannerPageController")
@RequestMapping("/page/shop/banner")
public class ShopBannerPageController extends BaseController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private ShopBannerServiceAPI shopBannerService;

    @Autowired
    private Toucan toucan;

    @Autowired
    private UserServiceAPI userService;

    @Autowired
    private SellerShopServiceAPI sellerShopService;

    @Autowired
    private ImageUploadService imageUploadService;


    @ShopAuth
    @UserAuth(requestType = UserAuth.REQUEST_FORM,responseType = UserAuth.RESPONSE_FORM)
    @RequestMapping("/list")
    public String list(HttpServletRequest request)
    {
        return "shop/banner/index";
    }



    @UserAuth(requestType = UserAuth.REQUEST_FORM,responseType = UserAuth.RESPONSE_FORM)
    @RequestMapping("/add")
    public String add(HttpServletRequest request){

        return "shop/banner/add";
    }



    private SellerShopVO queryByShop(String userMainId) throws Exception
    {
        SellerShop querySellerShop = new SellerShop();
        querySellerShop.setUserMainId(Long.parseLong(userMainId));
        RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(this.getAppCode(), querySellerShop);
        ResultObjectVO resultObjectVO = sellerShopService.findByUser(requestJsonVO);
        if(resultObjectVO.isSuccess()&&resultObjectVO.getData()!=null) {
            SellerShopVO sellerShopVO = resultObjectVO.formatData(SellerShopVO.class);
            return sellerShopVO;
        }
        return null;
    }


    @UserAuth(requestType = UserAuth.REQUEST_FORM,responseType = UserAuth.RESPONSE_FORM)
    @RequestMapping("/edit/{id}")
    public String edit(HttpServletRequest request,@PathVariable Long id){

        try {
            String userMainId = UserAuthHeaderUtil.getUserMainId(request.getHeader(toucan.getUserAuth().getHttpToucanAuthHeader()));
            SellerShopVO sellerShopVO = queryByShop(userMainId);
            if(sellerShopVO!=null)
            {
                ShopBannerVO shopBannerVO = new ShopBannerVO();
                shopBannerVO.setId(id);
                ResultObjectVO resultObjectVO = shopBannerService.findById(RequestJsonVOGenerator.generator(toucan.getAppCode(),shopBannerVO));
                if(resultObjectVO.isSuccess())
                {
                    ShopBannerVO resultShopBannerVO = resultObjectVO.formatData(ShopBannerVO.class);
                    if(resultShopBannerVO!=null&&resultShopBannerVO.getShopId().equals(sellerShopVO.getId()))
                    {
                        if(resultShopBannerVO.getImgPath()!=null) {
                            resultShopBannerVO.setHttpImgPath(imageUploadService.getImageHttpPrefix()+resultShopBannerVO.getImgPath());
                        }
                        if(resultShopBannerVO.getStartShowDate()!=null)
                        {
                            resultShopBannerVO.setStartShowDateString(DateUtils.FORMATTER_SS.get().format(resultShopBannerVO.getStartShowDate()));
                        }

                        if(resultShopBannerVO.getEndShowDate()!=null)
                        {
                            resultShopBannerVO.setEndShowDateString(DateUtils.FORMATTER_SS.get().format(resultShopBannerVO.getEndShowDate()));
                        }

                        request.setAttribute("shopBanner",resultShopBannerVO);
                    }
                }
            }
        }catch(Exception e)
        {
            logger.error(e.getMessage(),e);
        }

        return "shop/banner/edit";
    }

}
