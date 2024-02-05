package com.toucan.shopping.modules.common.properties.modules.log;

import lombok.Data;

import java.util.List;

/**
 * 参数日志
 */
@Data
public class Param {

    /**
     * 忽略URL正则
     */
    private List<String> ignoreUrlPatterns;

    /**
     * 不忽略的路径
     */
    private List<String> notIgnoreUrls;

    /**
     * 请求配置
     */
    private Request request;

    /**
     * 响应配置
     */
    private Response response;
}
