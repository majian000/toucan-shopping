package com.toucan.shopping.modules.designer.seller.model.container;


import com.toucan.shopping.modules.designer.core.model.component.AbstractComponent;
import com.toucan.shopping.modules.designer.core.model.container.AbstractContainer;
import com.toucan.shopping.modules.designer.seller.enums.SellerDesignerComponentEnum;
import lombok.Data;

import java.util.List;

/**
 * 页面容器
 * @author majian
 */
@Data
public class PageContainer extends AbstractContainer {


    public PageContainer(){
        this.setType(SellerDesignerComponentEnum.PAGE_CONTAINER.value());
    }




}
