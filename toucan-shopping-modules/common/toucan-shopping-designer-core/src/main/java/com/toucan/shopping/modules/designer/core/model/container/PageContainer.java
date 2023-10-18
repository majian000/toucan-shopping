package com.toucan.shopping.modules.designer.core.model.container;


import com.toucan.shopping.modules.designer.core.enums.DesignerComponentEnum;
import lombok.Data;

import java.util.List;

/**
 * 页面容器
 * @author majian
 */
@Data
public class PageContainer extends Container {


    public PageContainer(){
        this.setType(DesignerComponentEnum.PAGE_CONTAINER.value());
    }




}
