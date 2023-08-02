package com.toucan.shopping.modules.qlexpress.service.impl;

import com.ql.util.express.ExpressRunner;
import com.ql.util.express.IExpressContext;
import com.toucan.shopping.modules.qlexpress.service.QLExpressService;

/**
 * 表达式处理类
 * @author majian
 */
public class QLExpressServiceImpl implements QLExpressService {

    @Override
    public Object execute(String expressString, IExpressContext<String, Object> context) throws Exception {
        ExpressRunner runner = new ExpressRunner();
        return runner.execute(expressString, context, null, true, false);
    }
}
