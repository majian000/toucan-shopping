package com.toucan.shopping.modules.designer.core.model.component;

import java.util.Map;

/**
 * 基本组件(非容器组件)
 * @author majian
 */
public class BaseComponent extends AbstractComponent {

    /**
     * 属性键值对
     */
    private Map<String,String> propertys;

    public Map<String, String> getPropertys() {
        return propertys;
    }

    public void setPropertys(Map<String, String> propertys) {
        this.propertys = propertys;
    }
}
