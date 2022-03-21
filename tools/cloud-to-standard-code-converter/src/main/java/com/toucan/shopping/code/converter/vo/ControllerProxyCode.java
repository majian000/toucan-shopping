package com.toucan.shopping.code.converter.vo;

import lombok.Data;

/**
 * 控制器代理配置
 */
@Data
public class ControllerProxyCode {

    /**
     * 标准版输出路径
     */
    private String standardDest;

    /**
     * 微服务版源代码路径
     */
    private String cloudSrc;

    /**
     * 匹配方法名正则表达式
     */
    private String methodNameRegex;




}
