package com.toucan.shopping.modules.designer.seller.model.container;


import com.toucan.shopping.modules.designer.core.model.container.Container;
import com.toucan.shopping.modules.designer.seller.enums.SellerDesignerComponentEnum;
import com.toucan.shopping.modules.designer.seller.model.component.ShopBannerComponent;
import lombok.Data;

import java.util.List;

/**
 * 页面容器
 * @author majian
 */
@Data
public class PageContainer extends Container {


    public PageContainer(){
        this.setType(SellerDesignerComponentEnum.PAGE_CONTAINER.value());
    }


    private List<ShopBannerComponent> shopBannerComponents;  //店铺轮播图组件



}
