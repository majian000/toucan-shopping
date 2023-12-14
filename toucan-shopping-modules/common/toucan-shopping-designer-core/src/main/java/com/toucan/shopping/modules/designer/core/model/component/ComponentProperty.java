package com.toucan.shopping.modules.designer.core.model.component;

import lombok.Data;

/**
 * 组件属性
 * @author majian
 */
@Data
public class ComponentProperty {

    private String name; //属性名

    private String value; //属性值

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
