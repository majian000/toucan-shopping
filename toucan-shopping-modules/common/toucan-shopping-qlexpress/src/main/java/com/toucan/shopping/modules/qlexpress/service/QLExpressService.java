package com.toucan.shopping.modules.qlexpress.service;

import com.ql.util.express.IExpressContext;

/**
 * 表达式处理类
 * @author majian
 *
 */
public interface QLExpressService {

    /**
     * 解析表达式
     * @param expressString
     * @param context
     * @return
     */
    Object execute(String expressString, IExpressContext<String, Object> context) throws Exception;
}
