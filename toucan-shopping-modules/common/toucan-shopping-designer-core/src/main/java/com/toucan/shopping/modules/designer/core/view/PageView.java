package com.toucan.shopping.modules.designer.core.view;

import com.toucan.shopping.modules.designer.core.plugin.ComponentViewPlugin;
import lombok.Data;

import java.util.List;

/**
 * 页面视图对象
 * @author majian
 */
@Data
public abstract class PageView extends ComponentView{

    /**
     * 源类型 1:正式访问 2:预览访问
     */
    private Integer srcType;

    /**
     * 组件视图
     */
    private List<ComponentView> componentViews;

    /**
     * 组件视图插件
     */
    private List<ComponentViewPlugin> componentViewPlugins;
}
