package com.toucan.shopping.standard.apps.web.app;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceAutoConfigure;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication(exclude = DruidDataSourceAutoConfigure.class)
@MapperScan({"com.toucan.shopping.modules.common.persistence.event.mapper",
                "com.toucan.shopping.modules.admin.auth.mapper",
                "com.toucan.shopping.modules.area.mapper",
                "com.toucan.shopping.modules.category.mapper",
                "com.toucan.shopping.modules.column.mapper",
                "com.toucan.shopping.modules.order.mapper",
                "com.toucan.shopping.modules.product.mapper",
                "com.toucan.shopping.modules.stock.mapper",
                "com.toucan.shopping.modules.user.mapper"})
@ComponentScan("com.toucan.shopping")
public class StandardWebApplication {

    public static void main(String[] args) {
        SpringApplication.run(StandardWebApplication.class, args);
    }



}
