package com.toucan.shopping.modules.log.web.config;

import com.toucan.shopping.modules.common.properties.Toucan;
import com.toucan.shopping.modules.common.properties.modules.Modules;
import com.toucan.shopping.modules.common.properties.modules.log.Log;
import com.toucan.shopping.modules.common.properties.modules.log.Param;
import com.toucan.shopping.modules.log.queue.LogParamQueue;
import com.toucan.shopping.modules.log.web.filter.LogParamFilter;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;

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
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(LogParamFilter());
        registration.addUrlPatterns("/*");
        registration.setName("logParamFilterRegistrationBean");

        Modules toucanModules = toucan.getModules();
        if(toucanModules!=null&&toucanModules.getLog()!=null) {
            Log toucanLog = toucanModules.getLog();
            Param toucanParam = toucanLog.getParam();
            if(toucanParam!=null)
            {
                for(String ingoreUrlPattern:toucanParam.getIgnoreUrlPatterns()) {
                    Pattern pattern = Pattern.compile(ingoreUrlPattern, Pattern.CASE_INSENSITIVE);
                    ((LogParamFilter) registration.getFilter()).getIgnoreUrlPatterns().add(pattern);
                }
            }
            if(toucanParam.getRequest()!=null&&toucanParam.getRequest().isEnabled()) {
                logger.info("初始化日志请求参数拦截器....");
                if (toucanParam.getRequest() != null) {
                    List<String> contentTypeList = toucanParam.getRequest().getContentTypeList();
                    if (CollectionUtils.isNotEmpty(contentTypeList)) {
                        List<String> contentTypeLowerList = new LinkedList<>();
                        for (String contentType : contentTypeList) {
                            contentTypeLowerList.add(contentType.toLowerCase());
                        }
                        toucanParam.getRequest().setContentTypeList(contentTypeLowerList);
                    }
                }
            }

            if(toucanParam.getResponse()!=null
                    &&toucanParam.getResponse().isEnabled())
            {
                logger.info("初始化日志响应参数拦截器....");
                if(toucanParam.getResponse()!=null) {
                    List<String> contentTypeList = toucanParam.getResponse().getContentTypeList();
                    if (CollectionUtils.isNotEmpty(contentTypeList)) {
                        List<String> contentTypeLowerList = new LinkedList<>();
                        for (String contentType : contentTypeList) {
                            contentTypeLowerList.add(contentType.toLowerCase());
                        }
                        toucanParam.getResponse().setContentTypeList(contentTypeLowerList);
                    }
                }
            }
        }

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
