package com.toucan.shopping.modules.designer.seller.model.container;


import com.toucan.shopping.modules.designer.core.model.container.PageContainer;
import com.toucan.shopping.modules.designer.seller.enums.SellerDesignerComponentEnum;
import com.toucan.shopping.modules.designer.seller.model.component.ShopBannerComponent;
import lombok.Data;

import java.util.List;

/**
 * 卖家店铺页面容器
 * @author majian
 */
@Data
public class ShopPageContainer extends PageContainer {


    public ShopPageContainer(){
        this.setType(SellerDesignerComponentEnum.SHOP_PAGE_CONTAINER.value());
    }


    private List<ShopBannerComponent> shopBannerComponents;  //店铺轮播图组件



}
