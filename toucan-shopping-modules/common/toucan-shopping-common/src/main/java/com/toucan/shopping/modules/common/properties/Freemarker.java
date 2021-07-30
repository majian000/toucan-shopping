package com.toucan.shopping.modules.common.properties;

import lombok.Data;

/**
 * freemarker自定义配置
 */
@Data
public class Freemarker {

    /**
     * 静态资源路径
     */
    private String staticLocation;

    /**
     * 模板路径
     */
    private String ftlLocation;

    /**
     * 映射地址
     */
    private String mappingUrl;
}
