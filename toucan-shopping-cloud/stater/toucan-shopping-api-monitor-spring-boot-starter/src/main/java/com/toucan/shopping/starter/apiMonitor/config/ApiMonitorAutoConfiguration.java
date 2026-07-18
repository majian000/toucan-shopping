package com.toucan.shopping.starter.apiMonitor.config;

import com.toucan.shopping.starter.apiMonitor.core.MonitorRegistry;
import com.toucan.shopping.starter.apiMonitor.core.RecordCollector;
import com.toucan.shopping.starter.apiMonitor.interceptor.ApiMonitorInterceptor;
import com.toucan.shopping.starter.apiMonitor.report.SlowRequestLogger;
import com.toucan.shopping.starter.apiMonitor.schedule.ReportScheduler;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

/**
 * 接口监控自动配置
 */
@AutoConfiguration
@EnableScheduling
@ConditionalOnProperty(prefix = "toucan.plugins.apiMonitor", name = "enabled", havingValue = "true")
public class ApiMonitorAutoConfiguration implements WebMvcConfigurer {

    @Value("${spring.application.name:unknown}")
    private String appName;

    @Value("${toucan.ip:unknown}")
    private String serverIp;

    @Bean
    public MonitorRegistry monitorRegistry(@Qualifier("requestMappingHandlerMapping") RequestMappingHandlerMapping handlerMapping) {
        return new MonitorRegistry(handlerMapping);
    }

    @Bean
    public RecordCollector recordCollector() {
        return new RecordCollector();
    }

    @Bean
    public SlowRequestLogger slowRequestLogger() {
        return new SlowRequestLogger();
    }

    @Bean
    public ApiMonitorInterceptor apiMonitorInterceptor(MonitorRegistry monitorRegistry,
                                                        RecordCollector collector,
                                                        SlowRequestLogger slowRequestLogger) {
        ApiMonitorInterceptor interceptor = new ApiMonitorInterceptor(monitorRegistry, collector, slowRequestLogger);
        interceptor.setAppName(appName);
        interceptor.setServerIp(serverIp);
        return interceptor;
    }

    @Bean
    public ReportScheduler reportScheduler() {
        return new ReportScheduler();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(apiMonitorInterceptor(
                monitorRegistry(null),
                recordCollector(),
                slowRequestLogger())).order(0);
    }
}
