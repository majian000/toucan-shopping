package com.toucan.shopping.cloud.apiMonitor.api;

import com.toucan.shopping.modules.apiMonitor.vo.DashboardQueryVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;

public interface ApiMonitorDashboardServiceAPI {

    ResultObjectVO getSummary(DashboardQueryVO query);

    ResultObjectVO getRequestLog(DashboardQueryVO query);
}
