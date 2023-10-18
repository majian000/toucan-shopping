package com.toucan.shopping.modules.designer.core.validator;

import com.toucan.shopping.modules.designer.core.exception.validator.ValidatorException;
import com.toucan.shopping.modules.designer.core.model.container.PageContainer;

/**
 * 页面验证器
 * @author majian
 */
public interface IPageValidator {

    /**
     * 校验
     * @param pageContainer
     * @return
     * @throws ValidatorException
     */
    void valid(PageContainer pageContainer) throws ValidatorException;


}
