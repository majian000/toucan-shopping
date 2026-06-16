package com.toucan.shopping.standard.apps.admin.auth.web.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@ComponentScan("com.toucan.shopping")
@MapperScan({"com.toucan.shopping.modules.admin.auth.mapper",
             "com.toucan.shopping.modules.common.persistence.event.mapper",
             "com.toucan.shopping.modules.user.mapper",
             "com.toucan.shopping.modules.area.mapper",
             "com.toucan.shopping.modules.category.mapper",
             "com.toucan.shopping.modules.order.mapper",
             "com.toucan.shopping.modules.product.mapper",
             "com.toucan.shopping.modules.stock.mapper"})
public class StandardAdminAuthJarWebApplication {

    public static void main(String[] args) {
        SpringApplication.run(StandardAdminAuthJarWebApplication.class, args);
    }

}
