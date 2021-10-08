package com.toucan.shopping.cloud.apps.seller.web.controller.shop.product;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.serializer.SimplePropertyPreFilter;
import com.toucan.shopping.cloud.apps.seller.web.controller.BaseController;
import com.toucan.shopping.cloud.apps.seller.web.service.CategoryService;
import com.toucan.shopping.cloud.seller.api.feign.service.FeignSellerShopService;
import com.toucan.shopping.cloud.seller.api.feign.service.FeignShopCategoryService;
import com.toucan.shopping.cloud.user.api.feign.service.FeignUserService;
import com.toucan.shopping.modules.auth.user.UserAuth;
import com.toucan.shopping.modules.category.vo.CategoryVO;
import com.toucan.shopping.modules.common.generator.RequestJsonVOGenerator;
import com.toucan.shopping.modules.common.properties.Toucan;
import com.toucan.shopping.modules.common.util.UserAuthHeaderUtil;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.image.upload.service.ImageUploadService;
import com.toucan.shopping.modules.seller.entity.SellerShop;
import com.toucan.shopping.modules.seller.vo.SellerShopVO;
import com.toucan.shopping.modules.seller.vo.ShopCategoryVO;
import com.toucan.shopping.modules.user.vo.UserVO;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


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

    @UserAuth(requestType = UserAuth.REQUEST_FORM)
    @RequestMapping("/release")
    public String submit_success(HttpServletRequest request){

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
                List<ShopCategoryVO> shopCategoryVOList = resultObjectVO.formatDataArray(ShopCategoryVO.class);
                if(CollectionUtils.isNotEmpty(shopCategoryVOList)) {
                    List<Map<String, Object>> shopCategoryTree = new ArrayList<Map<String, Object>>();
                    for(ShopCategoryVO shopCategoryVO:shopCategoryVOList) {
                        Map<String,Object> shopCategory = new HashMap<String,Object>();
                        shopCategory.put(String.valueOf(shopCategoryVO.getId()),shopCategoryVO.getName());
                        //遍历子节点
                        if(CollectionUtils.isNotEmpty(shopCategoryVO.getChildren()))
                        {
                            List<Map<String, String>> shopCategoryChildTree = new ArrayList<Map<String, String>>();
                            for(ShopCategoryVO shopCategoryChildVO:shopCategoryVO.getChildren()) {
                                Map<String, String> shopCategoryChild = new HashMap<String, String>();
                                shopCategoryChild.put(String.valueOf(shopCategoryChildVO.getId()), shopCategoryChildVO.getName());
                                shopCategoryChildTree.add(shopCategoryChild);
                            }
                            shopCategory.put("childList",shopCategoryChildTree);
                        }

                        shopCategoryTree.add(shopCategory);
                    }
                    request.setAttribute("shopCategoryList", JSONArray.toJSONString(shopCategoryTree));
                }else{
                    request.setAttribute("shopCategoryList", "[]");
                }
            }else{
                request.setAttribute("shopCategoryList", "[]");
            }

        }catch(Exception e)
        {
            request.setAttribute("shopCategoryList", "[]");
            logger.warn(e.getMessage(),e);
        }

        return "product/release_product";
    }

}
