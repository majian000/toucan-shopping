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
            if(toucan.getShoppingPC()!=null&&toucan.getShoppingPC().getFreemarker()!=null) {
                logger.info("freemarker资源映射 {} to {}",toucan.getShoppingPC().getFreemarker().getReleaseLocation(),
                        toucan.getShoppingPC().getFreemarker().getReleaseMappingUrl());
                //配置资源映射
                registry.addResourceHandler(toucan.getShoppingPC().getFreemarker().getReleaseMappingUrl())
                        .addResourceLocations("file:"+toucan.getShoppingPC().getFreemarker().getReleaseLocation());

                logger.info("freemarker资源映射 {} to {}",toucan.getShoppingPC().getFreemarker().getPreviewLocation(),
                        toucan.getShoppingPC().getFreemarker().getPreviewMappingUrl());
                //配置资源映射
                registry.addResourceHandler(toucan.getShoppingPC().getFreemarker().getPreviewMappingUrl())
                        .addResourceLocations("file:"+toucan.getShoppingPC().getFreemarker().getPreviewLocation());

            }

            if(toucan.getShoppingSellerWebPC()!=null&&toucan.getShoppingSellerWebPC().getFreemarker()!=null) {
                logger.info("freemarker资源映射 {} to {}",toucan.getShoppingSellerWebPC().getFreemarker().getReleaseLocation(),
                        toucan.getShoppingSellerWebPC().getFreemarker().getReleaseMappingUrl());
                //配置资源映射
                registry.addResourceHandler(toucan.getShoppingSellerWebPC().getFreemarker().getReleaseMappingUrl())
                        .addResourceLocations("file:"+toucan.getShoppingSellerWebPC().getFreemarker().getReleaseLocation());

                logger.info("freemarker资源映射 {} to {}",toucan.getShoppingSellerWebPC().getFreemarker().getPreviewLocation(),
                        toucan.getShoppingSellerWebPC().getFreemarker().getPreviewMappingUrl());
                //配置资源映射
                registry.addResourceHandler(toucan.getShoppingSellerWebPC().getFreemarker().getPreviewMappingUrl())
                        .addResourceLocations("file:"+toucan.getShoppingSellerWebPC().getFreemarker().getPreviewLocation());

            }
        }
    }


}
