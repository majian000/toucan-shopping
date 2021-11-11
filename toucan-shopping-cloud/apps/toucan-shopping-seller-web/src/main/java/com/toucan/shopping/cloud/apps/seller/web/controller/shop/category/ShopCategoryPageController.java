package com.toucan.shopping.cloud.apps.seller.web.controller.shop.category;

import com.toucan.shopping.cloud.apps.seller.web.controller.BaseController;
import com.toucan.shopping.cloud.seller.api.feign.service.FeignSellerShopService;
import com.toucan.shopping.cloud.seller.api.feign.service.FeignShopCategoryService;
import com.toucan.shopping.cloud.user.api.feign.service.FeignUserService;
import com.toucan.shopping.modules.auth.shop.ShopAuth;
import com.toucan.shopping.modules.auth.user.UserAuth;
import com.toucan.shopping.modules.common.generator.RequestJsonVOGenerator;
import com.toucan.shopping.modules.common.properties.Toucan;
import com.toucan.shopping.modules.common.util.UserAuthHeaderUtil;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.seller.entity.SellerShop;
import com.toucan.shopping.modules.seller.vo.ShopCategoryVO;
import com.toucan.shopping.modules.user.vo.UserVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * 店铺分类
 */
@Controller("categoryPageController")
@RequestMapping("/page/shop/category")
public class ShopCategoryPageController extends BaseController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private FeignShopCategoryService feignShopCategoryService;

    @Autowired
    private Toucan toucan;

    @Autowired
    private FeignUserService feignUserService;

    @Autowired
    private FeignSellerShopService feignSellerShopService;


    @ShopAuth
    @UserAuth(requestType = UserAuth.REQUEST_FORM)
    @RequestMapping("/list")
    public String list(HttpServletRequest request)
    {
        return "shop/category/index";
    }


    @UserAuth(requestType = UserAuth.REQUEST_FORM)
    @RequestMapping("/{parentId}/add")
    public String add(HttpServletRequest request, @PathVariable String parentId)
    {
        request.setAttribute("parentId",parentId);
        return "shop/category/add";
    }



    @UserAuth(requestType = UserAuth.REQUEST_FORM)
    @RequestMapping("/edit/{id}")
    public String edit(HttpServletRequest request, @PathVariable Long id)
    {
        try {
            String userMainId = UserAuthHeaderUtil.getUserMainId(request.getHeader(toucan.getUserAuth().getHttpToucanAuthHeader()));
            ShopCategoryVO shopCategoryVO = new ShopCategoryVO();
            shopCategoryVO.setId(id);
            shopCategoryVO.setUserMainId(Long.parseLong(userMainId));
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(), shopCategoryVO);
            ResultObjectVO resultObjectVO = feignShopCategoryService.queryById(requestJsonVO);
            if(resultObjectVO.isSuccess())
            {
                request.setAttribute("model",resultObjectVO.formatData(ShopCategoryVO.class));
            }else{
                request.setAttribute("model",new ShopCategoryVO());
            }
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);
            request.setAttribute("model",new ShopCategoryVO());
        }
        return "shop/category/edit";
    }

}
