package com.toucan.shopping.cloud.apiMonitor.api;

import com.toucan.shopping.modules.apiMonitor.vo.ApiMonitorRecordVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;

import java.util.List;

/**
 * 接口监控上报服务 API
 */
public interface ApiMonitorReportServiceAPI {

    /**
     * 批量上报监控记录
     */
    ResultObjectVO sendBatch(List<ApiMonitorRecordVO> records);
}
