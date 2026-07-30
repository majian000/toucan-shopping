package com.toucan.shopping.cloud.apiMonitor.api.cloud.feign.service;

import com.toucan.shopping.cloud.apiMonitor.api.ApiMonitorDashboardServiceAPI;
import com.toucan.shopping.cloud.apiMonitor.api.cloud.feign.fallback.FeignApiMonitorDashboardServiceFallbackFactory;
import com.toucan.shopping.modules.apiMonitor.vo.DashboardQueryVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(value = "toucan-shopping-gateway", path = "/toucan-shopping-api-monitor-proxy/dashboard", fallbackFactory = FeignApiMonitorDashboardServiceFallbackFactory.class)
public interface FeignApiMonitorDashboardService extends ApiMonitorDashboardServiceAPI {

    @Override
    @RequestMapping(value = "/summary", method = RequestMethod.GET)
    ResultObjectVO getSummary(@SpringQueryMap DashboardQueryVO query);

    @Override
    @RequestMapping(value = "/trend", method = RequestMethod.GET)
    ResultObjectVO getTrend(@SpringQueryMap DashboardQueryVO query);

    @Override
    @RequestMapping(value = "/slow-list", method = RequestMethod.GET)
    ResultObjectVO getSlowList(@SpringQueryMap DashboardQueryVO query);
}
