package com.toucan.shopping.modules.freemarker.config;

import com.toucan.shopping.modules.common.properties.Toucan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class FreemarkerStaticPathMappingConfig implements WebMvcConfigurer {

    private final Logger logger = LoggerFactory.getLogger(getClass());


    @Autowired
    private Toucan toucan;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        if(toucan.getShoppingPC()!=null) {
            if(toucan.getShoppingPC().getFreemarker()!=null) {
                logger.info("freemarker资源映射 {} to {}",toucan.getShoppingPC().getFreemarker().getStaticLocation(),
                        toucan.getShoppingPC().getFreemarker().getMappingUrl());
                //配置资源映射
                registry.addResourceHandler(toucan.getShoppingPC().getFreemarker().getMappingUrl())
                        .addResourceLocations(toucan.getShoppingPC().getFreemarker().getStaticLocation());
            }
        }
    }


}
