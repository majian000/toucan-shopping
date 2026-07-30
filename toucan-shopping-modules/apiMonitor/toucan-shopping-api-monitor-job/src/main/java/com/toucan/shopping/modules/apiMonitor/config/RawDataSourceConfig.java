package com.toucan.shopping.modules.apiMonitor.config;

import com.alibaba.druid.pool.DruidDataSource;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

/**
 * 绕过 ShardingSphere 的原生数据源，专用于 DDL 操作（TRUNCATE 等）
 */
@Configuration
public class RawDataSourceConfig {

    @Bean(name = "rawDataSource")
    @ConfigurationProperties(prefix = "spring.shardingsphere.datasource.ds0")
    public DataSource rawDataSource() {
        return new DruidDataSource();
    }
}
