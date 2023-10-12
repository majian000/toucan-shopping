package com.toucan.shopping.modules.designer.core.vo.container;


import com.toucan.shopping.modules.designer.core.vo.component.AbstractComponent;
import lombok.Data;

import java.util.List;

@Data
public class PageContainer {


    private String title; //标题

    private List<AbstractComponent> components; //组件列表



}
