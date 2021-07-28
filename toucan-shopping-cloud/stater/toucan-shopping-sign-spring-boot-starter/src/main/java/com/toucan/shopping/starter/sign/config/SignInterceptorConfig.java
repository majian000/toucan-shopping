package com.toucan.shopping.starter.sign.config;

import com.toucan.shopping.starter.user.auth.interceptor.SignInterceptor;
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
        prefix = "toucan.sign",
        name = "enabled",
        havingValue = "true"
)
public class SignInterceptorConfig implements WebMvcConfigurer {

    @Autowired
    private SignInterceptor signInterceptor;

    @Value("${toucan.sign.path-patterns}")
    private String pathPatterns;

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        logger.info("初始化签名拦截器 拦截路径"+pathPatterns);

        InterceptorRegistration interceptorRegistration = registry.addInterceptor(signInterceptor);
        interceptorRegistration.addPathPatterns(pathPatterns);
    }


}
