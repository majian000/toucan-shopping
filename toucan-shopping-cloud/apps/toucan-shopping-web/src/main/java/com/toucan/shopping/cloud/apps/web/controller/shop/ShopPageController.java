package com.toucan.shopping.cloud.apps.web.controller.shop;

import com.alibaba.fastjson.JSONObject;
import com.toucan.shopping.cloud.apps.web.controller.BaseController;
import com.toucan.shopping.cloud.seller.api.feign.service.FeignSellerDesignerPageModelService;
import com.toucan.shopping.cloud.seller.api.feign.service.FeignSellerShopService;
import com.toucan.shopping.modules.auth.user.UserAuth;
import com.toucan.shopping.modules.common.generator.RequestJsonVOGenerator;
import com.toucan.shopping.modules.common.properties.Toucan;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.designer.core.parser.IPageParser;
import com.toucan.shopping.modules.designer.seller.model.container.ShopPageContainer;
import com.toucan.shopping.modules.designer.seller.view.ShopIndexPageView;
import com.toucan.shopping.modules.image.upload.service.ImageUploadService;
import com.toucan.shopping.modules.seller.entity.SellerDesignerPageModel;
import com.toucan.shopping.modules.seller.util.ShopUtils;
import com.toucan.shopping.modules.seller.vo.SellerDesignerPageModelVO;
import com.toucan.shopping.modules.seller.vo.SellerShopVO;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import java.util.List;


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
    private FeignSellerShopService feignSellerShopService;

    @Autowired
    private ImageUploadService imageUploadService;


    @Autowired
    private IPageParser pageParser;

    private void setShopAttribute(HttpServletRequest request, String shopId) throws Exception{
        SellerShopVO querySellerShopVO = new SellerShopVO();
        querySellerShopVO.setId(Long.parseLong(shopId));
        RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(), querySellerShopVO);
        ResultObjectVO resultObjectVO = feignSellerShopService.findById(requestJsonVO.sign(), requestJsonVO);
        if (resultObjectVO.isSuccess()) {
            List<SellerShopVO> sellerShopVOS = resultObjectVO.formatDataList(SellerShopVO.class);
            if (CollectionUtils.isNotEmpty(sellerShopVOS)) {
                SellerShopVO sellerShopVO = sellerShopVOS.get(0);
                if(StringUtils.isNotEmpty(sellerShopVO.getLogo()))
                {
                    sellerShopVO.setHttpLogo(imageUploadService.getImageHttpPrefix()+sellerShopVO.getLogo());
                }
                request.setAttribute("shop",sellerShopVO);
            }
        }
    }

    @RequestMapping("/pc/index/preview/{encShopId}/{shopId}")
    public String pcIndexPreviewPage(HttpServletRequest request, @PathVariable String encShopId, @PathVariable String shopId)
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
                    if(StringUtils.isNotEmpty(shopPageContainer.getPageJson())) {
                        ShopPageContainer pageContainer = (ShopPageContainer) pageParser.convertToPageModel(shopPageContainer.getPageJson());
                        pageContainer.setImageHttpPrefix(imageUploadService.getImageHttpPrefix());
                        ShopIndexPageView shopIndexPageView = (ShopIndexPageView) pageParser.parse(pageContainer);
                        shopIndexPageView.setShopId(shopId);
                        request.setAttribute("pageView", shopIndexPageView);
                        String pageJson = JSONObject.toJSONString(shopIndexPageView);
                        request.setAttribute("pageViewJson", pageJson);
                    }
                }

                this.setShopAttribute(request,shopId);
            }
        }catch(Exception e)
        {
            logger.error(e.getMessage(),e);
        }
        return "shop/index";
    }


    @RequestMapping("/pc/index/{shopId}")
    public String pcIndexReleasePage(HttpServletRequest request, @PathVariable String shopId)
    {
        try {
            SellerDesignerPageModelVO query =new SellerDesignerPageModelVO();
            query.setShopId(Long.parseLong(shopId));
            query.setType(2);
            query.setPosition(1);
            ResultObjectVO resultObjectVO = feignSellerDesignerPageModelService.queryLastOne(RequestJsonVOGenerator.generator(toucan.getAppCode(),query));
            if(resultObjectVO.isSuccess())
            {
                SellerDesignerPageModel shopPageContainer = resultObjectVO.formatData(SellerDesignerPageModel.class);
                ShopPageContainer pageContainer = (ShopPageContainer)pageParser.convertToPageModel(shopPageContainer.getPageJson());
                pageContainer.setImageHttpPrefix(imageUploadService.getImageHttpPrefix());
                ShopIndexPageView shopIndexPageView = (ShopIndexPageView) pageParser.parse(pageContainer);
                shopIndexPageView.setShopId(shopId);
                request.setAttribute("pageView",shopIndexPageView);
                String pageJson = JSONObject.toJSONString(shopIndexPageView);
                pageJson = pageJson.replaceAll("\"","'");
                request.setAttribute("pageViewJson", pageJson);
            }

            this.setShopAttribute(request,shopId);
        }catch(Exception e)
        {
            logger.error(e.getMessage(),e);
        }
        return "shop/index";
    }

}
