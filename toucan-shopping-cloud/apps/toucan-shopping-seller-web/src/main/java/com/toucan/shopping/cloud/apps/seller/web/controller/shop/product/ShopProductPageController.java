package com.toucan.shopping.cloud.apps.seller.web.controller.shop.product;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.serializer.SimplePropertyPreFilter;
import com.toucan.shopping.cloud.apps.seller.web.controller.BaseController;
import com.toucan.shopping.cloud.apps.seller.web.service.CategoryService;
import com.toucan.shopping.cloud.seller.api.feign.service.FeignShopCategoryService;
import com.toucan.shopping.modules.auth.user.UserAuth;
import com.toucan.shopping.modules.category.vo.CategoryVO;
import com.toucan.shopping.modules.common.generator.RequestJsonVOGenerator;
import com.toucan.shopping.modules.common.properties.Toucan;
import com.toucan.shopping.modules.common.util.UserAuthHeaderUtil;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.seller.vo.ShopCategoryVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.List;


/**
 * 店铺商品信息
 */
@Controller("shopProductPageController")
@RequestMapping("/page/shop/product")
public class ShopProductPageController extends BaseController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private Toucan toucan;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private FeignShopCategoryService feignShopCategoryService;

    private SimplePropertyPreFilter simplePropertyPreFilter =  new SimplePropertyPreFilter(CategoryVO.class, "id","name","children");

    @UserAuth(requestType = UserAuth.REQUEST_FORM,responseType = UserAuth.RESPONSE_FORM)
    @RequestMapping("/publishProductDescription")
    public String publishProductDescription(HttpServletRequest request){

        return "product/publish_product_description";
    }


    @UserAuth(requestType = UserAuth.REQUEST_FORM,responseType = UserAuth.RESPONSE_FORM)
    @RequestMapping("/publish")
    public String publish(HttpServletRequest request){

        try {
            request.setAttribute("categoryList", JSONArray.toJSONString(categoryService.queryMiniCategorys(),simplePropertyPreFilter));
        }catch(Exception e)
        {
            request.setAttribute("categoryList", "[]");
            logger.warn(e.getMessage(),e);
        }

        try{
            ShopCategoryVO queryShopCategory = new ShopCategoryVO();
            queryShopCategory.setUserMainId(Long.parseLong(UserAuthHeaderUtil.getUserMainId(request.getHeader(toucan.getUserAuth().getHttpToucanAuthHeader()))));
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(),queryShopCategory);
            ResultObjectVO resultObjectVO = feignShopCategoryService.queryAllList(requestJsonVO);
            if(resultObjectVO.isSuccess())
            {
                List<ShopCategoryVO> shopCategoryVOList = resultObjectVO.formatDataList(ShopCategoryVO.class);
                request.setAttribute("shopCategoryList", JSONArray.toJSONString(shopCategoryVOList));
            }else{
                request.setAttribute("shopCategoryList", "[]");
            }

        }catch(Exception e)
        {
            request.setAttribute("shopCategoryList", "[]");
            logger.warn(e.getMessage(),e);
        }

        return "product/publish_product";
    }

    @UserAuth(requestType = UserAuth.REQUEST_FORM,responseType = UserAuth.RESPONSE_FORM)
    @RequestMapping("/index")
    public String index(HttpServletRequest request){

        return "product/index";
    }



}
