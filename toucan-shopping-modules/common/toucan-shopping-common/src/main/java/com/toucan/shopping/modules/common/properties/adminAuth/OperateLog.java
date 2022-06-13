package com.toucan.shopping.modules.common.properties.adminAuth;


import lombok.Data;

/**
 * 操作日志
 */
@Data
public class OperateLog {

    /**
     * 是否开启操作记录
     */
    private boolean enabled = false;


    /**
     * 拦截指定路径
     */
    private String pathPatterns;

    /**
     * 忽略拦截的后缀
     */
    private String excludePathPatterns;
}
