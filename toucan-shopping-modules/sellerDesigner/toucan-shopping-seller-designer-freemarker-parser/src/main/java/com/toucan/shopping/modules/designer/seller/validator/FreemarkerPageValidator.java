package com.toucan.shopping.modules.designer.seller.validator;

import com.toucan.shopping.modules.designer.core.exception.validator.ValidatorException;
import com.toucan.shopping.modules.designer.core.model.container.PageContainer;
import com.toucan.shopping.modules.designer.core.validator.IPageValidator;
import org.springframework.stereotype.Component;


/**
 * freemarker校验器
 * @author majian
 */
@Component("freemarkerPageValidator")
public class FreemarkerPageValidator implements IPageValidator {


    @Override
    public void valid(PageContainer pageContainer) throws ValidatorException {


    }

}
