package com.toucan.shopping.starter.apiMonitor.interceptor;

import com.toucan.shopping.modules.apiMonitor.vo.ApiMonitorRecordVO;
import com.toucan.shopping.modules.common.constant.TraceConstants;
import com.toucan.shopping.starter.apiMonitor.core.MonitorRegistry;
import com.toucan.shopping.starter.apiMonitor.core.RecordCollector;
import com.toucan.shopping.modules.common.context.ApiMonitorContext;
import com.toucan.shopping.modules.common.context.TraceContext;
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
    private String appName;
    private String serverIp;

    private static final DateTimeFormatter DTF = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS");

    public ApiMonitorInterceptor(MonitorRegistry monitorRegistry,
                                  RecordCollector collector) {
        this.monitorRegistry = monitorRegistry;
        this.collector = collector;
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
        ApiMonitorContext.setPattern(pattern);
        ApiMonitorContext.setStartNanos(System.nanoTime());
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
                                Object handler, Exception ex) {
        String pattern = ApiMonitorContext.getPattern();
        if (pattern == null) {
            return;
        }
        try {
            Long startNanos = ApiMonitorContext.getStartNanos();
            long elapsedMs = startNanos != null ? (System.nanoTime() - startNanos) / 1_000_000 : 0;

            String traceId = TraceContext.get();
            if (traceId == null) {
                traceId = MDC.get(TraceConstants.TRACE_ID_KEY);
            }
            logger.info("[ApiMonitor] 记录 traceId={}, url={}, elapsed={}ms, thread={}",
                    traceId, pattern, elapsedMs, Thread.currentThread().getName());

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
        } catch (Exception e) {
            // 静默，不影响业务
        } finally {
            ApiMonitorContext.remove();
        }
    }
}
