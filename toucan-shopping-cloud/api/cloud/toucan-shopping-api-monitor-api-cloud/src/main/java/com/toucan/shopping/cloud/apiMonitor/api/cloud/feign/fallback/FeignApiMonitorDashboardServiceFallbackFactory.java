package com.toucan.shopping.cloud.apiMonitor.api.cloud.feign.fallback;

import com.toucan.shopping.cloud.apiMonitor.api.cloud.feign.service.FeignApiMonitorDashboardService;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

/**
 * 接口监控看板查询 Feign 降级工厂
 */
@Component
public class FeignApiMonitorDashboardServiceFallbackFactory implements FallbackFactory<FeignApiMonitorDashboardService> {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public FeignApiMonitorDashboardService create(Throwable throwable) {
        logger.warn(throwable.getMessage(), throwable);
        return new FeignApiMonitorDashboardService() {
            @Override
            public ResultObjectVO getSummary(int minutes, int page, int limit) {
                return fail();
            }

            @Override
            public ResultObjectVO getTrend(String apiUrl, String appName, int range, int page, int limit) {
                return fail();
            }

            @Override
            public ResultObjectVO getSlowList(int minElapsed, int page, int size) {
                return fail();
            }

            private ResultObjectVO fail() {
                ResultObjectVO vo = new ResultObjectVO();
                vo.setCode(ResultObjectVO.FAILD);
                vo.setMsg("监控服务暂不可用");
                return vo;
            }
        };
    }
}
