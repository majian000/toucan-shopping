package com.toucan.shopping.modules.designer.seller.parser;

import com.alibaba.fastjson.JSONObject;
import com.toucan.shopping.modules.designer.core.model.container.PageContainer;
import com.toucan.shopping.modules.designer.core.parser.IPageParser;
import com.toucan.shopping.modules.designer.core.view.PageView;
import com.toucan.shopping.modules.designer.seller.model.container.ShopPageContainer;
import com.toucan.shopping.modules.designer.seller.view.ShopIndexPageView;
import lombok.Data;
import org.springframework.stereotype.Component;


@Component("freemarkerPageParser")
public class FreemarkerShopIndexPageParser implements IPageParser {

    @Override
    public PageContainer convertToPageModel(String json) {
        ShopPageContainer shopPageContainer= JSONObject.parseObject(json, ShopPageContainer.class);
        return shopPageContainer;
    }

    @Override
    public PageView parse(PageContainer pageContainer) {
        ShopIndexPageView pageView=new ShopIndexPageView();
        pageView.setPageContainer(pageContainer);
        return pageView;
    }
}
