package com.toucan.shopping.modules.designer.core.model.container;

import com.toucan.shopping.modules.designer.core.model.component.AbstractComponent;
import lombok.Data;

import java.util.List;

/**
 * 抽象容器组件
 * @author majian
 */
@Data
public abstract class AbstractContainer extends AbstractComponent implements IContainer{

    private List<AbstractComponent> components; //组件列表

}
