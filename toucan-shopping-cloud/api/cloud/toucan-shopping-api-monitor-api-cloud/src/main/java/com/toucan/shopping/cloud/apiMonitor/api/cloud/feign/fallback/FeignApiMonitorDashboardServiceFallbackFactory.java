package com.toucan.shopping.cloud.apiMonitor.api.cloud.feign.fallback;

import com.toucan.shopping.cloud.apiMonitor.api.cloud.feign.service.FeignApiMonitorDashboardService;
import com.toucan.shopping.modules.apiMonitor.vo.DashboardQueryVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

@Component
public class FeignApiMonitorDashboardServiceFallbackFactory implements FallbackFactory<FeignApiMonitorDashboardService> {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public FeignApiMonitorDashboardService create(Throwable throwable) {
        logger.warn(throwable.getMessage(), throwable);
        return new FeignApiMonitorDashboardService() {
            @Override
            public ResultObjectVO getSummary(DashboardQueryVO query) { return fail(); }
            @Override
            public ResultObjectVO getRequestLog(DashboardQueryVO query) { return fail(); }

            private ResultObjectVO fail() {
                ResultObjectVO vo = new ResultObjectVO();
                vo.setCode(ResultObjectVO.FAILD);
                vo.setMsg("监控服务暂不可用");
                return vo;
            }
        };
    }
}
