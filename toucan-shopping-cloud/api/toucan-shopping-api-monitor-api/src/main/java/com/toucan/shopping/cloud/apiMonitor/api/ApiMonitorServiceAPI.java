package com.toucan.shopping.cloud.apiMonitor.api;

import com.toucan.shopping.modules.apiMonitor.vo.ApiMonitorRecordVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;

import java.util.List;

/**
 * 接口监控服务 API
 */
public interface ApiMonitorServiceAPI {

    /**
     * 批量上报监控记录
     */
    ResultObjectVO sendBatch(List<ApiMonitorRecordVO> records);

    /**
     * 获取概要统计
     */
    ResultObjectVO getSummary(int minutes);

    /**
     * 获取趋势数据
     */
    ResultObjectVO getTrend(String apiUrl, String appName, int range);

    /**
     * 获取慢请求列表
     */
    ResultObjectVO getSlowList(int minElapsed, int page, int size);
}
