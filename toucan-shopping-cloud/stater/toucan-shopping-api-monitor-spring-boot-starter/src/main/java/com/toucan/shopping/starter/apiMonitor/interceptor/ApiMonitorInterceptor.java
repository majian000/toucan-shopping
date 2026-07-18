package com.toucan.shopping.starter.apiMonitor.interceptor;

import com.toucan.shopping.cloud.apiMonitor.api.cloud.feign.service.FeignApiMonitorService;
import com.toucan.shopping.modules.apiMonitor.vo.ApiMonitorRecordVO;
import com.toucan.shopping.modules.common.constant.TraceConstants;
import com.toucan.shopping.modules.common.properties.Toucan;
import com.toucan.shopping.starter.apiMonitor.core.MonitorRegistry;
import com.toucan.shopping.starter.apiMonitor.core.RecordCollector;
import com.toucan.shopping.starter.apiMonitor.report.ReportScheduler;
import com.toucan.shopping.starter.apiMonitor.report.SlowRequestLogger;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import java.net.InetAddress;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 接口监控拦截器
 * preHandle 记录开始时间，afterCompletion 计算耗时并采集
 */
public class ApiMonitorInterceptor implements HandlerInterceptor {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private MonitorRegistry monitorRegistry;

    @Autowired
    private RecordCollector collector;

    @Autowired
    private SlowRequestLogger slowRequestLogger;

    @Value("${spring.application.name:unknown}")
    private String appName;

    @Value("${toucan.ip:unknown}")
    private String serverIp;

    private static final String START_TIME_ATTR = "_api_monitor_start";
    private static final String PATTERN_ATTR = "_api_monitor_pattern";
    private static final DateTimeFormatter DTF = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS");

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
