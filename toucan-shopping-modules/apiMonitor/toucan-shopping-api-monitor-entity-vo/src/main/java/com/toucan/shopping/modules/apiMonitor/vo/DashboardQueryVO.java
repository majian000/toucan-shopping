package com.toucan.shopping.modules.apiMonitor.vo;

import lombok.Data;

@Data
public class DashboardQueryVO {

    /** 接口URL（trend 必填） */
    private String apiUrl;

    /** 应用名称（可选，模糊匹配） */
    private String appName;

    /** 开始时间 yyyy-MM-dd HH:mm:ss */
    private String startTime;

    /** 结束时间 yyyy-MM-dd HH:mm:ss */
    private String endTime;

    /** 页码 */
    private int page = 1;

    /** 每页条数 */
    private int limit = 30;

    /** 最低耗时ms（慢请求用） */
    private int minElapsed = 3000;
}
