package com.toucan.shopping.modules.designer.seller.parser;

import com.alibaba.fastjson.JSONObject;
import com.toucan.shopping.modules.designer.core.model.component.AbstractComponent;
import com.toucan.shopping.modules.designer.core.model.container.PageContainer;
import com.toucan.shopping.modules.designer.core.parser.IPageParser;
import com.toucan.shopping.modules.designer.core.view.PageView;
import com.toucan.shopping.modules.designer.seller.enums.SellerDesignerComponentEnum;
import com.toucan.shopping.modules.designer.seller.enums.SellerComponentViewEnum;
import com.toucan.shopping.modules.designer.seller.model.component.ShopBannerComponent;
import com.toucan.shopping.modules.designer.seller.model.container.ShopPageContainer;
import com.toucan.shopping.modules.designer.seller.view.ShopBannerView;
import com.toucan.shopping.modules.designer.seller.view.ShopIndexPageView;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.Map;


@Component("freemarkerPageParser")
public class FreemarkerShopIndexPageParser implements IPageParser {

    @Override
    public PageContainer convertToPageModel(String json) throws Exception {
        ShopPageContainer shopPageContainer= JSONObject.parseObject(json, ShopPageContainer.class);
        shopPageContainer.setComponents(new LinkedList<>());
        if(CollectionUtils.isNotEmpty(shopPageContainer.getMapComponents()))
        {
            for(Map mapComponent:shopPageContainer.getMapComponents())
            {
                if(SellerDesignerComponentEnum.SHOP_BANNER.value().equals(mapComponent.get("type")))
                {
                    ShopBannerComponent shopBannerComponent=new ShopBannerComponent();
                    BeanUtils.populate(shopBannerComponent, mapComponent);
                    shopPageContainer.getComponents().add(shopBannerComponent);
                }
            }
        }
        return shopPageContainer;
    }

    @Override
    public PageView parse(PageContainer pageContainer) throws Exception{
        ShopIndexPageView pageView=new ShopIndexPageView();
        BeanUtils.copyProperties(pageView,pageContainer);
        pageView.setType(SellerComponentViewEnum.SHOP_PAGE_VIEW.value());
        pageView.setComponentViews(new LinkedList<>());
        if(CollectionUtils.isNotEmpty(pageContainer.getComponents()))
        {
            for(AbstractComponent component:pageContainer.getComponents())
            {
                //店铺轮播图组件
                if(SellerDesignerComponentEnum.SHOP_BANNER.value().equals(component.getType()))
                {
                    ShopBannerView shopBannerView = new ShopBannerView();
                    shopBannerView.setTitle(component.getTitle());
                    shopBannerView.setType(SellerComponentViewEnum.SHOP_BANNER_VIEW.value());
                    shopBannerView.setWidth(component.getWidth()+"%");
                    shopBannerView.setHeight(component.getHeight()+"%");
                    shopBannerView.setX(component.getX()+"%");
                    shopBannerView.setY(component.getY()+"%");
                    pageView.getComponentViews().add(shopBannerView);
                }
            }
        }
        return pageView;
    }
}
