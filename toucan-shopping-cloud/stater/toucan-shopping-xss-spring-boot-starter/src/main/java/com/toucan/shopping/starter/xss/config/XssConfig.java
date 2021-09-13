package com.toucan.shopping.starter.xss.config;

import com.toucan.shopping.modules.common.properties.Toucan;
import com.toucan.shopping.starter.xss.filter.RequestXssWrapperFilter;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.support.ConfigurableWebBindingInitializer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;

import javax.annotation.PostConstruct;
import javax.servlet.Filter;


@Configuration
@ConditionalOnProperty(
        prefix = "toucan.plugins.xss-filter",
        name = "enabled",
        havingValue = "true"
)
public class XssConfig implements WebMvcConfigurer {

    private final Logger logger = LoggerFactory.getLogger(getClass());


    @Autowired
    private Toucan toucan;

    /**
     * 注册替换request xss对象过滤器
     * @return
     */
    @Bean
    public FilterRegistrationBean filterRegistrationBean() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(requestXssWrapperFilter());
        registration.addUrlPatterns("/*");
        registration.setName("requestXssWrapperFilterRegistrationBean");
        return registration;
    }

    @Bean(name = "requestXssWrapperFilter")
    public Filter requestXssWrapperFilter() {
        RequestXssWrapperFilter requestXssWrapperFilter = new RequestXssWrapperFilter();
        String excludePaths = toucan.getPlugins().getXssFilter().getExcludePaths();
        if(StringUtils.isNotEmpty(excludePaths)) {
            String[] excludePathArray = excludePaths.split(",");
            if(excludePathArray!=null&&excludePathArray.length>0)
            {
                for(String excludePath:excludePathArray)
                {
                    logger.info("XSS过滤器忽略路径{}.........",excludePath);
                }
            }
            requestXssWrapperFilter.setExcludePaths(excludePathArray);
        }
        return requestXssWrapperFilter;
    }


}
