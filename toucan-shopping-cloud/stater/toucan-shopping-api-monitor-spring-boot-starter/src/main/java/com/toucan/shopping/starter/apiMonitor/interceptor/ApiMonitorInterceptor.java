package com.toucan.shopping.starter.apiMonitor.interceptor;

import com.toucan.shopping.modules.apiMonitor.vo.ApiMonitorRecordVO;
import com.toucan.shopping.modules.common.constant.TraceConstants;
import com.toucan.shopping.starter.apiMonitor.core.MonitorRegistry;
import com.toucan.shopping.starter.apiMonitor.core.RecordCollector;
import com.toucan.shopping.starter.apiMonitor.report.SlowRequestLogger;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 接口监控拦截器
 */
public class ApiMonitorInterceptor implements HandlerInterceptor {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final MonitorRegistry monitorRegistry;
    private final RecordCollector collector;
    private final SlowRequestLogger slowRequestLogger;
    private String appName;
    private String serverIp;

    private static final String START_TIME_ATTR = "_api_monitor_start";
    private static final String PATTERN_ATTR = "_api_monitor_pattern";
    private static final DateTimeFormatter DTF = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS");

    public ApiMonitorInterceptor(MonitorRegistry monitorRegistry,
                                  RecordCollector collector,
                                  SlowRequestLogger slowRequestLogger) {
        this.monitorRegistry = monitorRegistry;
        this.collector = collector;
        this.slowRequestLogger = slowRequestLogger;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public void setServerIp(String serverIp) {
        this.serverIp = serverIp;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
                             Object handler) {
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }
        String pattern = monitorRegistry.match(request.getRequestURI());
        if (pattern == null) {
            return true;
        }
        request.setAttribute(PATTERN_ATTR, pattern);
        request.setAttribute(START_TIME_ATTR, System.nanoTime());
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
                                Object handler, Exception ex) {
        String pattern = (String) request.getAttribute(PATTERN_ATTR);
        if (pattern == null) {
            return;
        }
        try {
            Long startNanos = (Long) request.getAttribute(START_TIME_ATTR);
            long elapsedMs = (System.nanoTime() - startNanos) / 1_000_000;

            String traceId = (String) request.getAttribute(TraceConstants.TRACE_ID_ATTR);
            if (traceId == null) {
                traceId = MDC.get(TraceConstants.TRACE_ID_KEY);
            }

            ApiMonitorRecordVO record = new ApiMonitorRecordVO();
            record.setApiUrl(pattern);
            record.setMethod(request.getMethod());
            record.setElapsedMs(elapsedMs);
            record.setStatusCode(response.getStatus());
            record.setTraceId(traceId);
            record.setAppName(appName);
            record.setServerIp(serverIp);
            record.setRequestTime(LocalDateTime.now().format(DTF));

            collector.collect(record);
            slowRequestLogger.check(record);
        } catch (Exception e) {
            // 静默，不影响业务
        }
    }
}
