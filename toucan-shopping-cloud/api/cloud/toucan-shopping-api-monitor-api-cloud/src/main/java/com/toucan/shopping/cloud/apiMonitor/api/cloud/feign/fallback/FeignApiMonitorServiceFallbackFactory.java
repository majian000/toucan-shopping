package com.toucan.shopping.cloud.apiMonitor.api.cloud.feign.fallback;

import com.toucan.shopping.cloud.apiMonitor.api.ApiMonitorServiceAPI;
import com.toucan.shopping.cloud.apiMonitor.api.cloud.feign.service.FeignApiMonitorService;
import com.toucan.shopping.modules.apiMonitor.vo.ApiMonitorRecordVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 接口监控 Feign 降级工厂
 */
@Component
public class FeignApiMonitorServiceFallbackFactory implements FallbackFactory<FeignApiMonitorService> {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public FeignApiMonitorService create(Throwable throwable) {
        logger.warn(throwable.getMessage(), throwable);
        return new FeignApiMonitorService() {
            @Override
            public ResultObjectVO sendBatch(List<ApiMonitorRecordVO> records) {
                ResultObjectVO vo = new ResultObjectVO();
                vo.setCode(ResultObjectVO.FAILD);
                vo.setMsg("监控上报失败");
                return vo;
            }

            @Override
            public ResultObjectVO getSummary(int minutes) {
                return fail();
            }

            @Override
            public ResultObjectVO getTrend(String apiUrl, String appName, int range) {
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
