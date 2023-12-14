package com.toucan.shopping.modules.designer.core.model.component;

import java.util.List;
import java.util.Map;

/**
 * 基本组件(非容器组件)
 * @author majian
 */
public class BaseComponent extends AbstractComponent {

    /**
     * 属性键值对
     */
    private List<ComponentProperty> propertys;

    public List<ComponentProperty> getPropertys() {
        return propertys;
    }

    public void setPropertys(List<ComponentProperty> propertys) {
        this.propertys = propertys;
    }
}
