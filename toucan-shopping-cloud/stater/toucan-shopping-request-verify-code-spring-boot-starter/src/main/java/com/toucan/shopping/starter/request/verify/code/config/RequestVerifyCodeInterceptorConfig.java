package com.toucan.shopping.starter.request.verify.code.config;

import com.toucan.shopping.starter.request.verify.code.interceptor.RequestVerifyCodeInterceptor;
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
public class RequestVerifyCodeInterceptorConfig implements WebMvcConfigurer {

    @Autowired
    private RequestVerifyCodeInterceptor requestVerifyCodeInterceptor;

    @Value("${toucan.request-verify-code.path-patterns}")
    private String pathPatterns;

    @Value("${toucan.request-verify-code.exclude-path-patterns}")
    private String excludePathPatterns;

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        logger.info("初始化请求验证码拦截器 拦截路径"+pathPatterns+
                " 忽略拦截路径"+excludePathPatterns);

        InterceptorRegistration interceptorRegistration = registry.addInterceptor(requestVerifyCodeInterceptor);
        interceptorRegistration.addPathPatterns(pathPatterns);
        if(StringUtils.isNotEmpty(excludePathPatterns)) {
            interceptorRegistration.excludePathPatterns(excludePathPatterns.split(","));
        }
    }


}
