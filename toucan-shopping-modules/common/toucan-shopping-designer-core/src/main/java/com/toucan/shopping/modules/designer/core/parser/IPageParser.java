package com.toucan.shopping.modules.designer.core.parser;

import com.toucan.shopping.modules.designer.core.model.container.PageContainer;
import com.toucan.shopping.modules.designer.core.view.PageView;

/**
 * 页面解析器
 */
public interface IPageParser {

    /**
     * 将字符串转换成模型
     * @param json
     * @return
     */
    PageContainer convertToPageModel(String json);

    /**
     * 将组件转换成页面
     * @param pageContainer
     * @return
     */
    PageView parse(PageContainer pageContainer)  throws Exception;

}
