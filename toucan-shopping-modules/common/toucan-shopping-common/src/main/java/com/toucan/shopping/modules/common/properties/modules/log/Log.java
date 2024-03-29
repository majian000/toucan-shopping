package com.toucan.shopping.modules.common.properties.modules.log;

import lombok.Data;

/**
 * 自定义日志
 */
@Data
public class Log {

    /**
     * 日志邮件
     */
    private Email email;

    /**
     * 参数
     */
    private Param param;

}
