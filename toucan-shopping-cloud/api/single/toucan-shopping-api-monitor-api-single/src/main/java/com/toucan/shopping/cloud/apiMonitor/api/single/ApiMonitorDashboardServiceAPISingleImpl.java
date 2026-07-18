package com.toucan.shopping.cloud.apiMonitor.api.single;

import com.toucan.shopping.cloud.apiMonitor.api.ApiMonitorDashboardServiceAPI;
import com.toucan.shopping.modules.apiMonitor.service.DashboardService;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 单服务版接口监控看板查询实现
 */
@Service
public class ApiMonitorDashboardServiceAPISingleImpl implements ApiMonitorDashboardServiceAPI {

    @Autowired
    private DashboardService dashboardService;

    @Override
    public ResultObjectVO getSummary(int minutes) {
        ResultObjectVO result = new ResultObjectVO();
        try {
            result.setData(dashboardService.getSummary(null, null, minutes));
            result.setCode(ResultObjectVO.SUCCESS);
        } catch (Exception e) {
            result.setCode(ResultObjectVO.FAILD);
            result.setMsg(e.getMessage());
        }
        return result;
    }

    @Override
    public ResultObjectVO getTrend(String apiUrl, String appName, int range) {
        ResultObjectVO result = new ResultObjectVO();
        try {
            result.setData(dashboardService.getTrend(apiUrl, appName, range));
            result.setCode(ResultObjectVO.SUCCESS);
        } catch (Exception e) {
            result.setCode(ResultObjectVO.FAILD);
            result.setMsg(e.getMessage());
        }
        return result;
    }

    @Override
    public ResultObjectVO getSlowList(int minElapsed, int page, int size) {
        ResultObjectVO result = new ResultObjectVO();
        try {
            result.setData(dashboardService.getSlowList(null, null, minElapsed, page, size));
            result.setCode(ResultObjectVO.SUCCESS);
        } catch (Exception e) {
            result.setCode(ResultObjectVO.FAILD);
            result.setMsg(e.getMessage());
        }
        return result;
    }
}
