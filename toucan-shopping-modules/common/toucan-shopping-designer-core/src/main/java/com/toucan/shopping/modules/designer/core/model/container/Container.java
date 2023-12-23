package com.toucan.shopping.modules.designer.core.model.container;

import com.toucan.shopping.modules.designer.core.model.component.AbstractComponent;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * 抽象容器组件
 * @author majian
 */
@Data
public abstract class Container extends AbstractComponent implements IContainer{

    private List<Map<String,String>> requestComponents; //接收组件

    private List<AbstractComponent> components; //组件列表

}
