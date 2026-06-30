package com.toucan.shopping.cloud.apps.admin.auth.scheduler.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@MapperScan({"com.toucan.shopping.modules.common.persistence.mapper",
             "com.toucan.shopping.modules.common.persistence.event.mapper",
             "com.toucan.shopping.modules.admin.auth.mapper"})
@Configuration
public class MybatisConfig {
}
