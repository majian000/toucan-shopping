package com.toucan.shopping.modules.common.properties.plugins;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 接口监控配置
 */
@Data
public class ApiMonitor {

    /**
     * 是否启用
     */
    private boolean enabled = false;

    /**
     * 监控的 Controller 包路径
     */
    private List<String> scanPackages = new ArrayList<>();

    /**
     * 排除的 URL 模式 (AntPathMatcher)
     */
    private List<String> excludePatterns = new ArrayList<>();

    /**
     * 上报配置
     */
    private Report report = new Report();

    /**
     * 上报配置
     */
    @Data
    public static class Report {

        /**
         * 上报间隔(秒)，默认5秒
         */
        private int interval = 5;

        /**
         * 每批上报最大条数，默认500
         */
        private int batchSize = 500;

        /**
         * 监控服务 Nacos 名称，必填
         */
        private String serverUrl;
    }
}
