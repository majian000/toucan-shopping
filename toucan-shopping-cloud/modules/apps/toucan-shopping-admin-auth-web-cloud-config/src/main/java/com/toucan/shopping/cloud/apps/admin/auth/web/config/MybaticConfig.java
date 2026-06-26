package com.toucan.shopping.cloud.apps.admin.auth.web.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan({"com.toucan.shopping.modules.admin.auth.mapper",
        "com.toucan.shopping.modules.admin.auth.log.mapper",
        "com.toucan.shopping.modules.common.persistence.mapper"})
public class MybaticConfig {
}
