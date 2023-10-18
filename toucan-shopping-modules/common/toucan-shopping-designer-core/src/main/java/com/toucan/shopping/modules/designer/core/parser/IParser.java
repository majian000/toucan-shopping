package com.toucan.shopping.modules.designer.core.parser;

import com.toucan.shopping.modules.designer.core.model.container.PageContainer;

/**
 * 页面解析器
 */
public interface IParser {

    /**
     * 将页面对象转换成页面
     * @param pageContainer
     * @return
     */
    Object parse(PageContainer pageContainer);

}
