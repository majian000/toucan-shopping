package com.toucan.shopping.cloud.apiMonitor.api.cloud.feign.service;

import com.toucan.shopping.cloud.apiMonitor.api.ApiMonitorReportServiceAPI;
import com.toucan.shopping.cloud.apiMonitor.api.cloud.feign.fallback.FeignApiMonitorReportServiceFallbackFactory;
import com.toucan.shopping.modules.apiMonitor.vo.ApiMonitorRecordVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * 接口监控上报 Feign 客户端
 */
@FeignClient(value = "toucan-shopping-gateway", path = "/toucan-shopping-api-monitor-proxy/api-monitor/report", fallbackFactory = FeignApiMonitorReportServiceFallbackFactory.class)
public interface FeignApiMonitorReportService extends ApiMonitorReportServiceAPI {

    @Override
    @RequestMapping(value = "/batch", method = RequestMethod.POST)
    ResultObjectVO sendBatch(@RequestBody List<ApiMonitorRecordVO> records);
}
