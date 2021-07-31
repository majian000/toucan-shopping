package com.toucan.shopping.modules.common.properties;

import lombok.Data;

/**
 * freemarker自定义配置
 */
@Data
public class Freemarker {

    /**
     * 最终文件路径
     */
    private String releaseLocation;

    /**
     * 预览文件路径
     */
    private String previewLocation;

    /**
     * 模板路径
     */
    private String ftlLocation;

    /**
     * 映射地址
     */
    private String releaseMappingUrl;

    /**
     * 映射地址
     */
    private String previewMappingUrl;

}
