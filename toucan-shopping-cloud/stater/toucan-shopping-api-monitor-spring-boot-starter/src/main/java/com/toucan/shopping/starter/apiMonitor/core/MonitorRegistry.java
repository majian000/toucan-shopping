package com.toucan.shopping.starter.apiMonitor.core;

import com.toucan.shopping.modules.common.properties.Toucan;
import com.toucan.shopping.modules.common.properties.plugins.ApiMonitor;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
 * 启动时扫描 RequestMappingHandlerMapping，匹配 scanPackages，返回 AntPathMatcher 匹配的 pattern
 */
public class MonitorRegistry {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private Toucan toucan;

    private final RequestMappingHandlerMapping handlerMapping;
    private final AntPathMatcher matcher = new AntPathMatcher();
    private final List<String> patterns = new ArrayList<>();

    public MonitorRegistry(RequestMappingHandlerMapping handlerMapping) {
        this.handlerMapping = handlerMapping;
    }

    @PostConstruct
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

                        // 从类和方法级别的 @RequestMapping 获取 pattern
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
     * 匹配请求 URI，返回命中的 pattern（用于 SlidingWindow key）<br/>
     * 返回 pattern 而非原始 URI，避免 /user/detail/123 类动态路径导致内存泄漏
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
