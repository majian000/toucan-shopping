package com.toucan.shopping.starter.admin.auth.config;

import com.toucan.shopping.starter.admin.auth.interceptor.AuthInterceptor;
import org.apache.commons.lang.StringUtils;
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
        prefix = "toucan.admin-auth",
        name = "enabled",
        havingValue = "true"
)
public class AuthInterceptorConfig implements WebMvcConfigurer {

    @Autowired
    private AuthInterceptor authInterceptor;

    @Value("${toucan.admin-auth.path-patterns}")
    private String pathPatterns;

    @Value("${toucan.admin-auth.exclude-path-patterns}")
    private String excludePathPatterns;

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        logger.info("初始化权限中心拦截器 拦截路径"+pathPatterns+
                " 忽略拦截路径"+excludePathPatterns);

        InterceptorRegistration interceptorRegistration = registry.addInterceptor(authInterceptor);
        interceptorRegistration.addPathPatterns(pathPatterns);
        if(StringUtils.isNotEmpty(excludePathPatterns)) {
            interceptorRegistration.excludePathPatterns(excludePathPatterns.split(","));
        }
    }


}
