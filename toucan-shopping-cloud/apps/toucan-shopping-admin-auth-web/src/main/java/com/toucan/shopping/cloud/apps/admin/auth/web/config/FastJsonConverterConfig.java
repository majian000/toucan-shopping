package com.toucan.shopping.cloud.apps.admin.auth.web.config;

import com.alibaba.fastjson2.JSONWriter;
import com.alibaba.fastjson2.support.config.FastJsonConfig;
import com.alibaba.fastjson2.support.spring6.http.converter.FastJsonHttpMessageConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * 解决long丢失精度问题,都转成字符串
 */
@Configuration
public class FastJsonConverterConfig implements WebMvcConfigurer {

    private final Logger logger = LoggerFactory.getLogger(getClass());


    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        logger.info("初始化fastjson2转换器.");
        FastJsonHttpMessageConverter fastJsonConverter = new FastJsonHttpMessageConverter();
        FastJsonConfig fastJsonConfig = new FastJsonConfig();
        // Long类型全局转String，解决JS精度丢失
        fastJsonConfig.setWriterFeatures(JSONWriter.Feature.WriteLongAsString);
        fastJsonConverter.setFastJsonConfig(fastJsonConfig);
        converters.add(fastJsonConverter);
    }
}
