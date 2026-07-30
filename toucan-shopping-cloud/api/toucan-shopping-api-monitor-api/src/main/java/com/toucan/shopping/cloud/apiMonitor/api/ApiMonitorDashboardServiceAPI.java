package com.toucan.shopping.cloud.apiMonitor.api;

import com.toucan.shopping.modules.common.vo.ResultObjectVO;

/**
 * 接口监控看板查询服务 API
 */
public interface ApiMonitorDashboardServiceAPI {

    /**
     * 获取概要统计
     */
    ResultObjectVO getSummary(int minutes, String appName, int page, int limit);

    /**
     * 获取趋势数据
     */
    ResultObjectVO getTrend(String apiUrl, String appName, int range, int page, int limit);

    /**
     * 获取慢请求列表
     */
    ResultObjectVO getSlowList(int minElapsed, int page, int size);
}
