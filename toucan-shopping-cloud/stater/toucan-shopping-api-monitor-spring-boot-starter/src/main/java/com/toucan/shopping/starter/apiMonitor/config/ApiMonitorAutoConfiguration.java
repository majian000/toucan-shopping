package com.toucan.shopping.starter.apiMonitor.config;

import com.toucan.shopping.starter.apiMonitor.core.MonitorRegistry;
import com.toucan.shopping.starter.apiMonitor.core.RecordCollector;
import com.toucan.shopping.starter.apiMonitor.interceptor.ApiMonitorInterceptor;
import com.toucan.shopping.starter.apiMonitor.report.ReportScheduler;
import com.toucan.shopping.starter.apiMonitor.report.SlowRequestLogger;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private ApiMonitorInterceptor apiMonitorInterceptor;

    @Bean
    public MonitorRegistry monitorRegistry(RequestMappingHandlerMapping handlerMapping) {
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
    public ApiMonitorInterceptor apiMonitorInterceptor() {
        return new ApiMonitorInterceptor();
    }

    @Bean
    public ReportScheduler reportScheduler() {
        return new ReportScheduler();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 注册为第一个拦截器，确保最先记录开始时间
        registry.addInterceptor(apiMonitorInterceptor).order(0);
    }
}
