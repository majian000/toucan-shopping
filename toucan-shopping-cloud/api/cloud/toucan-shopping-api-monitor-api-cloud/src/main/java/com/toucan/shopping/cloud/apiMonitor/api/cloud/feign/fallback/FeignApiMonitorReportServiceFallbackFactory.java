package com.toucan.shopping.cloud.apiMonitor.api.cloud.feign.fallback;

import com.toucan.shopping.cloud.apiMonitor.api.cloud.feign.service.FeignApiMonitorReportService;
import com.toucan.shopping.modules.apiMonitor.vo.ApiMonitorRecordVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 接口监控上报 Feign 降级工厂
 */
@Component
public class FeignApiMonitorReportServiceFallbackFactory implements FallbackFactory<FeignApiMonitorReportService> {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public FeignApiMonitorReportService create(Throwable throwable) {
        logger.warn(throwable.getMessage(), throwable);
        return new FeignApiMonitorReportService() {
            @Override
            public ResultObjectVO sendBatch(List<ApiMonitorRecordVO> records) {
                ResultObjectVO vo = new ResultObjectVO();
                vo.setCode(ResultObjectVO.FAILD);
                vo.setMsg("监控上报失败");
                return vo;
            }
        };
    }
}
