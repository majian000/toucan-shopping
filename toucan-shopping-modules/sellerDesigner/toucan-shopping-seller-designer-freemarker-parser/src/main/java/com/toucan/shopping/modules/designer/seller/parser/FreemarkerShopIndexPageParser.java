package com.toucan.shopping.modules.designer.seller.parser;

import com.alibaba.fastjson.JSONObject;
import com.toucan.shopping.modules.designer.core.model.component.AbstractComponent;
import com.toucan.shopping.modules.designer.core.model.component.IComponent;
import com.toucan.shopping.modules.designer.core.model.container.PageContainer;
import com.toucan.shopping.modules.designer.core.parser.IPageParser;
import com.toucan.shopping.modules.designer.core.view.ComponentView;
import com.toucan.shopping.modules.designer.core.view.PageView;
import com.toucan.shopping.modules.designer.seller.enums.SellerDesignerComponentEnum;
import com.toucan.shopping.modules.designer.seller.enums.SellerComponentViewEnum;
import com.toucan.shopping.modules.designer.seller.model.component.ImageComponent;
import com.toucan.shopping.modules.designer.seller.model.component.ShopBannerComponent;
import com.toucan.shopping.modules.designer.seller.model.container.ShopPageContainer;
import com.toucan.shopping.modules.designer.seller.plugin.ShopBannerViewPlugin;
import com.toucan.shopping.modules.designer.seller.view.ImageView;
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
                AbstractComponent component = null ;
                if(SellerDesignerComponentEnum.SHOP_BANNER.value().equals(mapComponent.get("type")))
                {
                    component=new ShopBannerComponent();
                }else if(SellerDesignerComponentEnum.IMAGE.value().equals(mapComponent.get("type")))
                {
                    component=new ImageComponent();
                }
                BeanUtils.populate(component, mapComponent);
                shopPageContainer.getComponents().add(component);
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
                ComponentView componentView = null;
                Integer width = Integer.parseInt(component.getWidth())*blockUnit;
                Integer height = Integer.parseInt(component.getHeight())*blockUnit*10; //单位px
                Integer x = Integer.parseInt(component.getX())*blockUnit;
                Integer y = Integer.parseInt(component.getY())*blockUnit;
                //店铺轮播图组件
                if(SellerDesignerComponentEnum.SHOP_BANNER.value().equals(component.getType()))
                {
                    componentView = new ShopBannerView();
                    componentView.setTitle(component.getTitle());
                    componentView.setType(SellerComponentViewEnum.SHOP_BANNER_VIEW.value());

                    //插件
                    ShopBannerViewPlugin shopBannerViewPlugin=new ShopBannerViewPlugin();
                    shopBannerViewPlugin.setComponentType(component.getType());
                    shopBannerViewPlugin.setPluginName("sliderMe");
                    shopBannerViewPlugin.setPluginVersion("1.0");
                    pageView.getComponentViewPlugins().add(shopBannerViewPlugin);
                }else if(SellerDesignerComponentEnum.IMAGE.value().equals(component.getType())){
                    componentView =  new ImageView();
                }

                if(componentView!=null) {
                    componentView.setWidth(String.valueOf(width));
                    componentView.setWidthUnit("%");
                    componentView.setHeight(String.valueOf(height));
                    componentView.setHeightUnit("px");
                    componentView.setX(String.valueOf(x));
                    componentView.setxUnit("%");
                    componentView.setY(String.valueOf(y));
                    componentView.setyUnit("%");
                    pageView.getComponentViews().add(componentView);
                }
            }
        }
        return pageView;
    }
}
