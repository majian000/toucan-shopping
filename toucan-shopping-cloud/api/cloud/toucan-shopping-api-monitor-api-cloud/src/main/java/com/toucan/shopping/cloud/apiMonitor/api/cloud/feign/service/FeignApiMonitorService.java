package com.toucan.shopping.cloud.apiMonitor.api.cloud.feign.service;

import com.toucan.shopping.cloud.apiMonitor.api.ApiMonitorServiceAPI;
import com.toucan.shopping.cloud.apiMonitor.api.cloud.feign.fallback.FeignApiMonitorServiceFallbackFactory;
import com.toucan.shopping.modules.apiMonitor.vo.ApiMonitorRecordVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * 接口监控 Feign 客户端
 */
@FeignClient(value = "toucan-shopping-api-monitor", path = "/api-monitor", fallbackFactory = FeignApiMonitorServiceFallbackFactory.class)
public interface FeignApiMonitorService extends ApiMonitorServiceAPI {

    @Override
    @RequestMapping(value = "/reports/batch", method = RequestMethod.POST)
    ResultObjectVO sendBatch(@RequestBody List<ApiMonitorRecordVO> records);

    @Override
    @RequestMapping(value = "/dashboard/summary", method = RequestMethod.GET)
    ResultObjectVO getSummary(@RequestParam(defaultValue = "5") int minutes);

    @Override
    @RequestMapping(value = "/dashboard/trend", method = RequestMethod.GET)
    ResultObjectVO getTrend(@RequestParam String apiUrl,
                            @RequestParam(required = false) String appName,
                            @RequestParam(defaultValue = "60") int range);

    @Override
    @RequestMapping(value = "/dashboard/slow-list", method = RequestMethod.GET)
    ResultObjectVO getSlowList(@RequestParam(defaultValue = "3000") int minElapsed,
                               @RequestParam(defaultValue = "1") int page,
                               @RequestParam(defaultValue = "20") int size);
}
