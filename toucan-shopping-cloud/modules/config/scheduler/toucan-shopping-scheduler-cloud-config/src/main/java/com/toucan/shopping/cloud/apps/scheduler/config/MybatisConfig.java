package com.toucan.shopping.cloud.apps.scheduler.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@MapperScan({"com.toucan.shopping.modules.common.persistence.mapper",
             "com.toucan.shopping.modules.common.persistence.event.mapper"})
@Configuration
public class MybatisConfig {
}
