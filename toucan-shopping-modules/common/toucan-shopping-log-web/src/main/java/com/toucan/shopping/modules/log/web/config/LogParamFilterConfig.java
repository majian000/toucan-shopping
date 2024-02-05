package com.toucan.shopping.modules.log.web.config;

import com.toucan.shopping.modules.common.properties.Toucan;
import com.toucan.shopping.modules.log.queue.LogParamQueue;
import com.toucan.shopping.modules.log.web.filter.LogParamFilter;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;
import java.util.LinkedList;
import java.util.List;

@Configuration
public class LogParamFilterConfig {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private Toucan toucan;

    @Autowired
    private LogParamQueue logParamQueue;

    /**
     * 注册替换request对象过滤器
     * @return
     */
    @Bean
    public FilterRegistrationBean filterRegistrationBean() {
        if(toucan.getModules()!=null
                &&toucan.getModules().getLog()!=null
                &&toucan.getModules().getLog().getRequest()!=null
                &&(toucan.getModules().getLog().getRequest().isEnabled()||toucan.getModules().getLog().getRequest().isEnabled())) {

            logger.info("初始化日志请求参数拦截器....");
            if(toucan.getModules().getLog().getRequest()!=null) {
                List<String> contentTypeList = toucan.getModules().getLog().getRequest().getContentTypeList();
                if (CollectionUtils.isNotEmpty(contentTypeList)) {
                    List<String> contentTypeLowerList = new LinkedList<>();
                    for (String contentType : contentTypeList) {
                        contentTypeLowerList.add(contentType.toLowerCase());
                    }
                    toucan.getModules().getLog().getRequest().setContentTypeList(contentTypeLowerList);
                }
            }
        }

        if(toucan.getModules()!=null
                &&toucan.getModules().getLog()!=null
                &&toucan.getModules().getLog().getResponse()!=null
                &&(toucan.getModules().getLog().getResponse().isEnabled()||toucan.getModules().getLog().getResponse().isEnabled())) {

            logger.info("初始化日志响应参数拦截器....");
            if(toucan.getModules().getLog().getResponse()!=null) {
                List<String> contentTypeList = toucan.getModules().getLog().getResponse().getContentTypeList();
                if (CollectionUtils.isNotEmpty(contentTypeList)) {
                    List<String> contentTypeLowerList = new LinkedList<>();
                    for (String contentType : contentTypeList) {
                        contentTypeLowerList.add(contentType.toLowerCase());
                    }
                    toucan.getModules().getLog().getResponse().setContentTypeList(contentTypeLowerList);
                }
            }
        }

        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(LogParamFilter());
        registration.addUrlPatterns("/*");
        registration.setName("logParamFilterRegistrationBean");
        return registration;
    }

    @Bean(name = "logParamFilter")
    public Filter LogParamFilter() {
        LogParamFilter logParamFilter = new LogParamFilter();
        logParamFilter.setToucan(toucan);
        logParamFilter.setLogParamQueue(logParamQueue);
        return logParamFilter;
    }


}
