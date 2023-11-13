package com.toucan.shopping.cloud.apps.web.app.config;


import com.toucan.shopping.modules.qlexpress.service.QLExpressService;
import com.toucan.shopping.modules.qlexpress.service.impl.QLExpressServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * QL表达式配置
 * @author majian
 */
@Configuration
public class QLExpressConfig {


    @Bean
    public QLExpressService qlExpressService()
    {
        return new QLExpressServiceImpl();
    }

}
