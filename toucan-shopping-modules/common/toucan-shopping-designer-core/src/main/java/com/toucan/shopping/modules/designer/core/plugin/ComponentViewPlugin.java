package com.toucan.shopping.modules.designer.core.plugin;


import lombok.Data;

/**
 * 组件视图插件
 * @author majian
 */
@Data
public abstract class ComponentViewPlugin {

    private String componentType; //组件类型

    private String pluginName; //插件名称

    private String pluginVersion; //插件版本

}
