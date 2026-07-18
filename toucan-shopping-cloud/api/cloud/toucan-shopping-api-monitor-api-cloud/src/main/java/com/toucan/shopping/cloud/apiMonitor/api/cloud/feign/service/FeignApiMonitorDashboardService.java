package com.toucan.shopping.cloud.apiMonitor.api.cloud.feign.service;

import com.toucan.shopping.cloud.apiMonitor.api.ApiMonitorDashboardServiceAPI;
import com.toucan.shopping.cloud.apiMonitor.api.cloud.feign.fallback.FeignApiMonitorDashboardServiceFallbackFactory;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 接口监控看板查询 Feign 客户端
 */
@FeignClient(value = "toucan-shopping-gateway", path = "/toucan-shopping-api-monitor-proxy/api-monitor/dashboard", fallbackFactory = FeignApiMonitorDashboardServiceFallbackFactory.class)
public interface FeignApiMonitorDashboardService extends ApiMonitorDashboardServiceAPI {

    @Override
    @RequestMapping(value = "/summary", method = RequestMethod.GET)
    ResultObjectVO getSummary(@RequestParam(defaultValue = "5") int minutes);

    @Override
    @RequestMapping(value = "/trend", method = RequestMethod.GET)
    ResultObjectVO getTrend(@RequestParam String apiUrl,
                            @RequestParam(required = false) String appName,
                            @RequestParam(defaultValue = "60") int range);

    @Override
    @RequestMapping(value = "/slow-list", method = RequestMethod.GET)
    ResultObjectVO getSlowList(@RequestParam(defaultValue = "3000") int minElapsed,
                               @RequestParam(defaultValue = "1") int page,
                               @RequestParam(defaultValue = "20") int size);
}
