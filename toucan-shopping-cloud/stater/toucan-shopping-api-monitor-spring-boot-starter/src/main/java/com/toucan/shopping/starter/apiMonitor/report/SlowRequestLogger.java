package com.toucan.shopping.starter.apiMonitor.report;

import com.toucan.shopping.modules.apiMonitor.vo.ApiMonitorRecordVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 慢请求日志告警
 */
public class SlowRequestLogger {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    /** 慢请求阈值(毫秒) */
    private static final long SLOW_THRESHOLD_MS = 3000;

    public void check(ApiMonitorRecordVO record) {
        if (record.getElapsedMs() > SLOW_THRESHOLD_MS) {
            logger.warn("[SLOW] {} {} {}ms traceId={}",
                    record.getMethod(),
                    record.getApiUrl(),
                    record.getElapsedMs(),
                    record.getTraceId() != null ? record.getTraceId() : "-");
        }
    }
}
