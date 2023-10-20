package com.toucan.shopping.modules.designer.core.view;

import com.toucan.shopping.modules.designer.core.model.container.PageContainer;
import lombok.Data;

/**
 * 页面视图对象
 * @author majian
 */
@Data
public abstract class PageView {

    /**
     * 源类型 1:正式访问 2:预览访问
     */
    private Integer srcType;

    /**
     * 页面组件
     */
    private PageContainer pageContainer;

}
