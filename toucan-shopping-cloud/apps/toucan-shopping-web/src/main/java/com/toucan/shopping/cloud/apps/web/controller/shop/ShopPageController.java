package com.toucan.shopping.cloud.apps.web.controller.shop;

import com.alibaba.fastjson.JSONObject;
import com.toucan.shopping.cloud.apps.web.controller.BaseController;
import com.toucan.shopping.cloud.seller.api.feign.service.FeignSellerDesignerPageModelService;
import com.toucan.shopping.modules.auth.user.UserAuth;
import com.toucan.shopping.modules.common.generator.RequestJsonVOGenerator;
import com.toucan.shopping.modules.common.properties.Toucan;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.designer.core.parser.IPageParser;
import com.toucan.shopping.modules.designer.seller.model.container.ShopPageContainer;
import com.toucan.shopping.modules.designer.seller.view.ShopIndexPageView;
import com.toucan.shopping.modules.seller.entity.SellerDesignerPageModel;
import com.toucan.shopping.modules.seller.util.ShopUtils;
import com.toucan.shopping.modules.seller.vo.SellerDesignerPageModelVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;


/**
 * 店铺控制器
 * @author majian
 * @date 2023-10-27 14:45:14
 */
@Controller("pageShopController")
@RequestMapping("/page/shop")
public class ShopPageController extends BaseController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private Toucan toucan;

    @Autowired
    private FeignSellerDesignerPageModelService feignSellerDesignerPageModelService;

    @Autowired
    private IPageParser pageParser;


    @UserAuth(requestType = UserAuth.REQUEST_FORM,responseType = UserAuth.RESPONSE_FORM)
    @RequestMapping("/pc/index/preview/{encShopId}/{shopId}")
    public String indexPreviewPage(HttpServletRequest request, @PathVariable String encShopId, @PathVariable String shopId)
    {
        try {
            if (ShopUtils.encShopId(shopId).equals(encShopId)) {
                SellerDesignerPageModelVO query =new SellerDesignerPageModelVO();
                query.setShopId(Long.parseLong(shopId));
                query.setType(1);
                query.setPosition(1);
                ResultObjectVO resultObjectVO = feignSellerDesignerPageModelService.queryLastOne(RequestJsonVOGenerator.generator(toucan.getAppCode(),query));
                if(resultObjectVO.isSuccess())
                {
                    SellerDesignerPageModel shopPageContainer = resultObjectVO.formatData(SellerDesignerPageModel.class);
                    ShopPageContainer pageContainer = (ShopPageContainer)pageParser.convertToPageModel(shopPageContainer.getPageJson());
                    ShopIndexPageView shopIndexPageView = (ShopIndexPageView) pageParser.parse(pageContainer);
                    shopIndexPageView.setShopId(shopId);
                    request.setAttribute("pageView",shopIndexPageView);
                    String pageJson = JSONObject.toJSONString(shopIndexPageView);
                    pageJson = pageJson.replaceAll("\"","'");
                    request.setAttribute("pageViewJson", pageJson);
                }
            }
        }catch(Exception e)
        {
            logger.error(e.getMessage(),e);
        }
        return "shop/index";
    }

}
