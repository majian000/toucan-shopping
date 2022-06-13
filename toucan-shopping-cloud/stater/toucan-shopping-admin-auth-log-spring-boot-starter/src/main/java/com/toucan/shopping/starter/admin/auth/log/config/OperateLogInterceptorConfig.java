package com.toucan.shopping.starter.admin.auth.log.config;

import com.toucan.shopping.starter.admin.auth.log.interceptor.OperateLogInterceptor;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@ConditionalOnProperty(
        prefix = "toucan.admin-auth.operate-log",
        name = "enabled",
        havingValue = "true"
)
public class OperateLogInterceptorConfig implements WebMvcConfigurer {

    @Autowired
    private OperateLogInterceptor operateLogInterceptor;

    @Value("${toucan.admin-auth.operate-log.path-patterns}")
    private String pathPatterns;

    @Value("${toucan.admin-auth.operate-log.exclude-path-patterns}")
    private String excludePathPatterns;

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        logger.info("初始化权限中台操作日志拦截器 拦截路径"+pathPatterns+
                " 忽略拦截路径"+excludePathPatterns);

        InterceptorRegistration interceptorRegistration = registry.addInterceptor(operateLogInterceptor);
        interceptorRegistration.addPathPatterns(pathPatterns);
        if(StringUtils.isNotEmpty(excludePathPatterns)) {
            interceptorRegistration.excludePathPatterns(excludePathPatterns.split(","));
        }
    }


}
