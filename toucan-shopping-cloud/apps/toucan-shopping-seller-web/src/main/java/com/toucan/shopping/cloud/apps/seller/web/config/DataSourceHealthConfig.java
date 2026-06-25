package com.toucan.shopping.cloud.apps.seller.web.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.jdbc.DataSourceHealthIndicator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

/**
 * 数据源健康监控重写实现
 */
@Configuration
public class DataSourceHealthConfig {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Value("select 1")
    private String defaultQuery;

    @Bean
    public DataSourceHealthIndicator dataSourceHealthIndicator(DataSource dataSource) {
        logger.info("健康监控数据源执行 " + defaultQuery);
        DataSourceHealthIndicator indicator = new DataSourceHealthIndicator(dataSource);
        indicator.setQuery(defaultQuery);
        return indicator;
    }
}
