package com.toucan.shopping.cloud.apps.web.controller.shop;

import com.alibaba.fastjson.JSONObject;
import com.toucan.shopping.cloud.apps.web.controller.BaseController;
import com.toucan.shopping.cloud.search.api.feign.service.FeignProductSearchService;
import com.toucan.shopping.cloud.seller.api.feign.service.FeignSellerDesignerPageModelService;
import com.toucan.shopping.cloud.seller.api.feign.service.FeignSellerShopService;
import com.toucan.shopping.cloud.seller.api.feign.service.FeignShopCategoryService;
import com.toucan.shopping.modules.auth.user.UserAuth;
import com.toucan.shopping.modules.category.entity.Category;
import com.toucan.shopping.modules.common.generator.RequestJsonVOGenerator;
import com.toucan.shopping.modules.common.page.PageInfo;
import com.toucan.shopping.modules.common.properties.Toucan;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.designer.core.parser.IPageParser;
import com.toucan.shopping.modules.designer.seller.model.container.ShopPageContainer;
import com.toucan.shopping.modules.designer.seller.view.ShopIndexPageView;
import com.toucan.shopping.modules.image.upload.service.ImageUploadService;
import com.toucan.shopping.modules.search.vo.ProductSearchResultVO;
import com.toucan.shopping.modules.search.vo.ProductSearchVO;
import com.toucan.shopping.modules.seller.entity.SellerDesignerPageModel;
import com.toucan.shopping.modules.seller.entity.ShopCategory;
import com.toucan.shopping.modules.seller.util.ShopUtils;
import com.toucan.shopping.modules.seller.vo.SellerDesignerPageModelVO;
import com.toucan.shopping.modules.seller.vo.SellerShopVO;
import com.toucan.shopping.modules.seller.vo.ShopCategoryVO;
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
    private FeignShopCategoryService feignShopCategoryService;

    @Autowired
    private IPageParser pageParser;

    @Autowired
    private FeignProductSearchService feignProductSearchService;

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





    @RequestMapping("/category/product/list")
    public String queryProductListByShopCategoryId(HttpServletRequest request, @RequestParam String sid, @RequestParam String scid)
    {
        try {
            if(StringUtils.isNotEmpty(sid)) {
                this.setShopAttribute(request, sid);
                ShopCategoryVO queryShopCategoryVO=new ShopCategoryVO();
                queryShopCategoryVO.setShopId(Long.parseLong(sid));
                ResultObjectVO resultObjectVO = feignShopCategoryService.queryTree(RequestJsonVOGenerator.generator(toucan.getAppCode(),queryShopCategoryVO));
                if(resultObjectVO.isSuccess())
                {
                    List<ShopCategoryVO> rootShopCategoryTree = resultObjectVO.formatDataList(ShopCategoryVO.class);
                    if(CollectionUtils.isNotEmpty(rootShopCategoryTree)) {
                        request.setAttribute("shopCategorys",rootShopCategoryTree.get(0).getChildren());
                    }
                }
                ProductSearchVO productSearchVO = new ProductSearchVO();
                productSearchVO.setSize(20);
                productSearchVO.setPage(1);
                productSearchVO.setSid(sid);
                productSearchVO.setScid(scid);
                PageInfo pageInfo = null;

                resultObjectVO = feignProductSearchService.search(RequestJsonVOGenerator.generator(toucan.getAppCode(),productSearchVO));
                if(resultObjectVO.isSuccess()) {
                    pageInfo = resultObjectVO.formatData(PageInfo.class);
                    List<ProductSearchResultVO> productResult = pageInfo.formatDataList(ProductSearchResultVO.class);
                    if (CollectionUtils.isNotEmpty(productResult)) {

                        for(ProductSearchResultVO productSearchResultVO:productResult)
                        {
                            productSearchResultVO.setHttpProductPreviewPath(imageUploadService.getImageHttpPrefix()+productSearchResultVO.getProductPreviewPath());
                        }


                        request.setAttribute("productResult",productResult);
                        request.setAttribute("page",pageInfo.getPage());
                        request.setAttribute("total",pageInfo.getTotal());
                        request.setAttribute("pageTotal",pageInfo.getPageTotal());
                    }
                }
                request.setAttribute("curspid",sid);;
                request.setAttribute("sid",sid);;
                request.setAttribute("scid",scid);;
            }
        }catch(Exception e)
        {
            logger.error(e.getMessage(),e);
        }
        return "shop/category_product_list";
    }
}
