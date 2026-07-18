package com.toucan.shopping.starter.apiMonitor.core;

import com.toucan.shopping.modules.common.properties.Toucan;
import com.toucan.shopping.modules.common.properties.plugins.ApiMonitor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.event.EventListener;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 监控端点注册表
 * 运行时从 ApplicationContext 获取 RequestMappingHandlerMapping，避免循环依赖
 */
public class MonitorRegistry {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final Toucan toucan;
    private final ApplicationContext applicationContext;
    private final AntPathMatcher matcher = new AntPathMatcher();
    private final List<String> patterns = new ArrayList<>();

    public MonitorRegistry(Toucan toucan, ApplicationContext applicationContext) {
        this.toucan = toucan;
        this.applicationContext = applicationContext;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void init() {
        ApiMonitor config = toucan.getPlugins().getApiMonitor();
        if (config == null || !config.isEnabled()) {
            logger.info("apiMonitor 未启用，跳过端点扫描");
            return;
        }
        List<String> scanPackages = config.getScanPackages();
        if (scanPackages == null || scanPackages.isEmpty()) {
            logger.warn("apiMonitor.scanPackages 未配置，跳过端点扫描");
            return;
        }

        RequestMappingHandlerMapping handlerMapping =
                applicationContext.getBean("requestMappingHandlerMapping", RequestMappingHandlerMapping.class);

        Map<RequestMappingInfo, HandlerMethod> handlerMethods = handlerMapping.getHandlerMethods();
        for (HandlerMethod handlerMethod : handlerMethods.values()) {
            String className = handlerMethod.getBeanType().getName();
            for (String scanPackage : scanPackages) {
                if (className.startsWith(scanPackage)) {
                    if (handlerMethod.getMethod().isAnnotationPresent(
                            org.springframework.web.bind.annotation.RequestMapping.class) ||
                        handlerMethod.getMethod().isAnnotationPresent(
                            org.springframework.web.bind.annotation.GetMapping.class) ||
                        handlerMethod.getMethod().isAnnotationPresent(
                            org.springframework.web.bind.annotation.PostMapping.class) ||
                        handlerMethod.getMethod().isAnnotationPresent(
                            org.springframework.web.bind.annotation.PutMapping.class) ||
                        handlerMethod.getMethod().isAnnotationPresent(
                            org.springframework.web.bind.annotation.DeleteMapping.class)) {

                        RequestMappingInfo mappingInfo = handlerMethods.entrySet()
                                .stream()
                                .filter(e -> e.getValue().equals(handlerMethod))
                                .map(Map.Entry::getKey)
                                .findFirst()
                                .orElse(null);
                        if (mappingInfo != null) {
                            Set<String> urlPatterns = mappingInfo.getPatternValues();
                            if (urlPatterns != null) {
                                patterns.addAll(urlPatterns);
                            }
                        }
                    }
                    break;
                }
            }
        }
        logger.info("apiMonitor 扫描完成，共匹配 {} 个端点: {}", patterns.size(), patterns);
    }

    /**
     * 匹配请求 URI，返回命中的 pattern
     */
    public String match(String requestUri) {
        for (String pattern : patterns) {
            if (matcher.match(pattern, requestUri)) {
                return pattern;
            }
        }
        return null;
    }
}
