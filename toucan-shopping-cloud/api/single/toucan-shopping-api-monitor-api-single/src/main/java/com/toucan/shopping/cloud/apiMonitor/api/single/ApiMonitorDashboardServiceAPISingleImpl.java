package com.toucan.shopping.cloud.apiMonitor.api.single;

import com.toucan.shopping.cloud.apiMonitor.api.ApiMonitorDashboardServiceAPI;
import com.toucan.shopping.modules.apiMonitor.service.DashboardService;
import com.toucan.shopping.modules.apiMonitor.vo.DashboardQueryVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ApiMonitorDashboardServiceAPISingleImpl implements ApiMonitorDashboardServiceAPI {

    @Autowired
    private DashboardService dashboardService;

    @Override
    public ResultObjectVO getSummary(DashboardQueryVO query) {
        return dashboardService.getSummary(query);
    }

    @Override
    public ResultObjectVO getRequestLog(DashboardQueryVO query) {
        return dashboardService.getRequestLog(query);
    }
}
