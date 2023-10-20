package com.toucan.shopping.modules.designer.seller.parser;

import com.alibaba.fastjson.JSONObject;
import com.toucan.shopping.modules.designer.core.model.container.PageContainer;
import com.toucan.shopping.modules.designer.core.parser.IPageParser;
import com.toucan.shopping.modules.designer.seller.model.container.ShopPageContainer;
import lombok.Data;
import org.springframework.stereotype.Component;


@Component("freemarkerPageParser")
public class FreemarkerPageParser implements IPageParser {

    @Override
    public PageContainer convertToPageModel(String json) {
        ShopPageContainer shopPageContainer= JSONObject.parseObject(json, ShopPageContainer.class);
        return shopPageContainer;
    }

    @Override
    public Object parse(PageContainer pageContainer) {
        return null;
    }
}
