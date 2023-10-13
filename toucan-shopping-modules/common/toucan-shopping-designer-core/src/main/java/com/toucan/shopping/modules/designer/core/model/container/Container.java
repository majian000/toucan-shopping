package com.toucan.shopping.modules.designer.core.model.container;

import com.toucan.shopping.modules.designer.core.model.component.Component;
import lombok.Data;

import java.util.List;

/**
 * 抽象容器组件
 * @author majian
 */
@Data
public abstract class Container extends Component implements IContainer{

    private List<Component> components; //组件列表

}
