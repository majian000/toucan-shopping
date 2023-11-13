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
import com.toucan.shopping.modules.designer.seller.plugin.ShopBannerViewPlugin;
import com.toucan.shopping.modules.designer.seller.view.ShopBannerView;
import com.toucan.shopping.modules.designer.seller.view.ShopIndexPageView;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.Map;


@Component("freemarkerPageParser")
public class FreemarkerShopIndexPageParser implements IPageParser {

    private final int blockUnit=5; //设计器1个单元块占5%

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
        pageView.setComponentViewPlugins(new LinkedList<>());
        if(CollectionUtils.isNotEmpty(pageContainer.getComponents()))
        {
            for(AbstractComponent component:pageContainer.getComponents())
            {
                //店铺轮播图组件
                if(SellerDesignerComponentEnum.SHOP_BANNER.value().equals(component.getType()))
                {
                    //组件视图
                    ShopBannerView shopBannerView = new ShopBannerView();
                    shopBannerView.setTitle(component.getTitle());
                    shopBannerView.setType(SellerComponentViewEnum.SHOP_BANNER_VIEW.value());
                    Integer width = Integer.parseInt(component.getWidth())*blockUnit;
                    Integer height = Integer.parseInt(component.getHeight())*blockUnit*10; //单位px
                    Integer x = Integer.parseInt(component.getX())*blockUnit;
                    Integer y = Integer.parseInt(component.getY())*blockUnit;
                    shopBannerView.setWidth(String.valueOf(width));
                    shopBannerView.setWidthUnit("%");
                    shopBannerView.setHeight(String.valueOf(height));
                    shopBannerView.setHeightUnit("px");
                    shopBannerView.setX(String.valueOf(x));
                    shopBannerView.setxUnit("%");
                    shopBannerView.setY(String.valueOf(y));
                    shopBannerView.setyUnit("%");
                    pageView.getComponentViews().add(shopBannerView);

                    //视图插件
                    ShopBannerViewPlugin shopBannerViewPlugin=new ShopBannerViewPlugin();
                    shopBannerViewPlugin.setComponentType(component.getType());
                    shopBannerViewPlugin.setPluginName("sliderMe");
                    shopBannerViewPlugin.setPluginVersion("1.0");
                    pageView.getComponentViewPlugins().add(shopBannerViewPlugin);
                }
            }
        }
        return pageView;
    }
}
