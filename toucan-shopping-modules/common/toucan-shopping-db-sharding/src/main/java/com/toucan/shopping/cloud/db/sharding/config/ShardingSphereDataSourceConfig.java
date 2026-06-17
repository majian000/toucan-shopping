package com.toucan.shopping.cloud.db.sharding.config;

import com.alibaba.druid.pool.DruidDataSource;
import org.apache.shardingsphere.driver.api.ShardingSphereDataSourceFactory;
import org.apache.shardingsphere.infra.config.rule.RuleConfiguration;
import org.apache.shardingsphere.sharding.api.config.ShardingRuleConfiguration;
import org.apache.shardingsphere.sharding.api.config.rule.ShardingTableRuleConfiguration;
import org.apache.shardingsphere.sharding.api.config.strategy.sharding.StandardShardingStrategyConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.*;

@Configuration
public class ShardingSphereDataSourceConfig {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private Environment environment;

    @Bean
    @Primary
    public DataSource shardingSphereDataSource() throws SQLException {
        // Check at runtime (config server properties loaded after @ConditionalOnProperty evaluation)
        String dsNames = environment.getProperty("spring.shardingsphere.datasource.names");
        if (dsNames == null || dsNames.isEmpty()) {
            logger.info("No spring.shardingsphere.datasource.names configured, skipping DataSource creation");
            return null;
        }

        // 1. Build data source map
        Map<String, DataSource> dataSourceMap = createDataSourceMap();

        // 2. Build sharding rules - check if any actual sharding tables are configured
        Collection<RuleConfiguration> ruleConfigs = new ArrayList<>();
        ShardingRuleConfiguration shardingRuleConfig = createShardingRuleConfiguration();
        if (shardingRuleConfig != null && !shardingRuleConfig.getTables().isEmpty()) {
            ruleConfigs.add(shardingRuleConfig);
        } else {
            logger.info("No sharding table rules configured, skipping ShardingSphere wrapper, using plain DataSource");
            // Return the first (master) data source directly without ShardingSphere wrapper
            String firstName = dsNames.split(",")[0].trim();
            return dataSourceMap.get(firstName);
        }

        // 3. Create ShardingSphere DataSource
        Properties props = new Properties();
        props.setProperty("sql-show", environment.getProperty("spring.shardingsphere.props.sql-show", "false"));

        logger.info("Creating ShardingSphere DataSource with {} data sources and {} sharding tables",
            dataSourceMap.size(), shardingRuleConfig.getTables().size());
        return ShardingSphereDataSourceFactory.createDataSource(dataSourceMap, ruleConfigs, props);
    }

    private Map<String, DataSource> createDataSourceMap() {
        Map<String, DataSource> dataSourceMap = new LinkedHashMap<>();
        String namesStr = environment.getProperty("spring.shardingsphere.datasource.names");
        if (namesStr == null || namesStr.isEmpty()) {
            return dataSourceMap;
        }

        String[] names = namesStr.split(",");
        for (String name : names) {
            name = name.trim();
            String prefix = "spring.shardingsphere.datasource." + name + ".";
            String url = environment.getProperty(prefix + "url");
            String username = environment.getProperty(prefix + "username");
            String password = environment.getProperty(prefix + "password");
            String driverClassName = environment.getProperty(prefix + "driver-class-name",
                    environment.getProperty("spring.shardingsphere.datasource.common.driver-class-name", "com.mysql.cj.jdbc.Driver"));

            DruidDataSource ds = new DruidDataSource();
            ds.setUrl(url);
            ds.setUsername(username);
            ds.setPassword(password);
            ds.setDriverClassName(driverClassName);

            // Optional Druid configs
            String initialSize = environment.getProperty(prefix + "initial-size");
            if (initialSize != null) ds.setInitialSize(Integer.parseInt(initialSize));
            String minIdle = environment.getProperty(prefix + "min-idle");
            if (minIdle != null) ds.setMinIdle(Integer.parseInt(minIdle));
            String maxActive = environment.getProperty(prefix + "maxActive");
            if (maxActive != null) ds.setMaxActive(Integer.parseInt(maxActive));
            String maxWait = environment.getProperty(prefix + "maxWait");
            if (maxWait != null) ds.setMaxWait(Long.parseLong(maxWait));
            String validationQuery = environment.getProperty(prefix + "validationQuery");
            if (validationQuery != null) ds.setValidationQuery(validationQuery);
            String testWhileIdle = environment.getProperty(prefix + "testWhileIdle");
            if (testWhileIdle != null) ds.setTestWhileIdle(Boolean.parseBoolean(testWhileIdle));
            String testOnBorrow = environment.getProperty(prefix + "testOnBorrow");
            if (testOnBorrow != null) ds.setTestOnBorrow(Boolean.parseBoolean(testOnBorrow));
            String testOnReturn = environment.getProperty(prefix + "testOnReturn");
            if (testOnReturn != null) ds.setTestOnReturn(Boolean.parseBoolean(testOnReturn));

            dataSourceMap.put(name, ds);
            logger.info("Configured ShardingSphere datasource: {}", name);
        }
        return dataSourceMap;
    }

    private ShardingRuleConfiguration createShardingRuleConfiguration() {
        ShardingRuleConfiguration config = new ShardingRuleConfiguration();

        // Configure sharding algorithms
        Map<String, Properties> algorithmProps = loadAlgorithmProperties();
        for (Map.Entry<String, Properties> entry : algorithmProps.entrySet()) {
            config.getShardingAlgorithms().put(entry.getKey(),
                new org.apache.shardingsphere.infra.algorithm.core.config.AlgorithmConfiguration(
                    entry.getValue().getProperty("type"),
                    filterAlgorithmProps(entry.getValue())));
        }

        // Configure table rules
        Map<String, Map<String, String>> tableRules = loadTableRules();
        for (Map.Entry<String, Map<String, String>> entry : tableRules.entrySet()) {
            String tableName = entry.getKey();
            Map<String, String> rule = entry.getValue();

            // Per-table database sharding strategy
            String dbShardingColumn = rule.get("database-strategy.standard.sharding-column");
            String dbShardingAlgorithm = rule.get("database-strategy.standard.sharding-algorithm-name");

            // Per-table table sharding strategy
            String tableShardingColumn = rule.get("table-strategy.standard.sharding-column");
            String tableShardingAlgorithm = rule.get("table-strategy.standard.sharding-algorithm-name");

            // Skip tables without any sharding strategy (non-sharded/single-node tables)
            if ((dbShardingColumn == null || dbShardingColumn.isEmpty() || dbShardingAlgorithm == null || dbShardingAlgorithm.isEmpty()) &&
                (tableShardingColumn == null || tableShardingColumn.isEmpty() || tableShardingAlgorithm == null || tableShardingAlgorithm.isEmpty())) {
                continue;
            }

            ShardingTableRuleConfiguration tableRuleConfig = new ShardingTableRuleConfiguration(
                tableName, rule.getOrDefault("actual-data-nodes", ""));

            if (dbShardingColumn != null && !dbShardingColumn.isEmpty()
                && dbShardingAlgorithm != null && !dbShardingAlgorithm.isEmpty()) {
                tableRuleConfig.setDatabaseShardingStrategy(
                    new StandardShardingStrategyConfiguration(dbShardingColumn, dbShardingAlgorithm));
            }

            if (tableShardingColumn != null && !tableShardingColumn.isEmpty()
                && tableShardingAlgorithm != null && !tableShardingAlgorithm.isEmpty()) {
                tableRuleConfig.setTableShardingStrategy(
                    new StandardShardingStrategyConfiguration(tableShardingColumn, tableShardingAlgorithm));
            }

            config.getTables().add(tableRuleConfig);
        }

        // Default database sharding strategy
        String dbShardingColumn = environment.getProperty("spring.shardingsphere.rules.sharding.default-database-strategy.standard.sharding-column");
        String dbShardingAlgorithm = environment.getProperty("spring.shardingsphere.rules.sharding.default-database-strategy.standard.sharding-algorithm-name");
        if (dbShardingColumn != null && dbShardingAlgorithm != null) {
            config.setDefaultDatabaseShardingStrategy(
                new StandardShardingStrategyConfiguration(dbShardingColumn, dbShardingAlgorithm));
        }

        // Key generators
        String keyGenType = environment.getProperty("spring.shardingsphere.rules.sharding.key-generators.snowflake.type");
        if (keyGenType != null) {
            Properties keyGenProps = new Properties();
            String workerId = environment.getProperty("spring.shardingsphere.rules.sharding.key-generators.snowflake.props.worker-id");
            if (workerId != null) keyGenProps.setProperty("worker-id", workerId);
            config.getKeyGenerators().put("snowflake",
                new org.apache.shardingsphere.infra.algorithm.core.config.AlgorithmConfiguration(keyGenType, keyGenProps));
        }

        return config;
    }

    private Map<String, Properties> loadAlgorithmProperties() {
        Map<String, Properties> result = new LinkedHashMap<>();
        String prefix = "spring.shardingsphere.rules.sharding.sharding-algorithms.";

        // Scan for algorithm names
        Set<String> algorithmNames = new HashSet<>();
        for (org.springframework.core.env.PropertySource<?> ps :
             ((org.springframework.core.env.AbstractEnvironment) environment).getPropertySources()) {
            if (ps.getName().contains("configserver") || ps.getName().contains("applicationConfig")) {
                if (ps.getSource() instanceof Map) {
                    for (Object key : ((Map<?, ?>) ps.getSource()).keySet()) {
                        String k = String.valueOf(key);
                        if (k.startsWith(prefix)) {
                            String suffix = k.substring(prefix.length());
                            int dotIdx = suffix.indexOf('.');
                            if (dotIdx > 0) {
                                algorithmNames.add(suffix.substring(0, dotIdx));
                            }
                        }
                    }
                }
            }
        }

        for (String algoName : algorithmNames) {
            Properties props = new Properties();
            String type = environment.getProperty(prefix + algoName + ".type");
            if (type != null) {
                props.setProperty("type", type);
                // Read props
                String propsPrefix = prefix + algoName + ".props.";
                for (org.springframework.core.env.PropertySource<?> ps :
                     ((org.springframework.core.env.AbstractEnvironment) environment).getPropertySources()) {
                    if (ps.getSource() instanceof Map) {
                        for (Object key : ((Map<?, ?>) ps.getSource()).keySet()) {
                            String k = String.valueOf(key);
                            if (k.startsWith(propsPrefix)) {
                                String propName = k.substring(propsPrefix.length());
                                props.setProperty(propName, environment.getProperty(k, ""));
                            }
                        }
                    }
                }
                result.put(algoName, props);
            }
        }

        // Fallback: read from standard property resolution
        if (result.isEmpty()) {
            String algoPrefix = "spring.shardingsphere.rules.sharding.sharding-algorithms";
            // Try to read algorithm names from database-sharding-algorithms and table-sharding-algorithms
            for (String suffix : Arrays.asList("database-sharding-algorithms", "table-sharding-algorithms")) {
                String type = environment.getProperty(algoPrefix + "." + suffix + ".type");
                if (type != null) {
                    Properties props = new Properties();
                    props.setProperty("type", type);
                    String psPrefix = algoPrefix + "." + suffix + ".props.";
                    String num = environment.getProperty(psPrefix + "num");
                    if (num != null) props.setProperty("num", num);
                    String instanceName = environment.getProperty(psPrefix + "instance-name");
                    if (instanceName != null) props.setProperty("instance-name", instanceName);
                    result.put(suffix, props);
                }
            }
        }

        return result;
    }

    private Map<String, Map<String, String>> loadTableRules() {
        Map<String, Map<String, String>> result = new LinkedHashMap<>();
        String prefix = "spring.shardingsphere.rules.sharding.tables.";

        Set<String> tableNames = new HashSet<>();
        for (org.springframework.core.env.PropertySource<?> ps :
             ((org.springframework.core.env.AbstractEnvironment) environment).getPropertySources()) {
            if (ps.getSource() instanceof Map) {
                for (Object key : ((Map<?, ?>) ps.getSource()).keySet()) {
                    String k = String.valueOf(key);
                    if (k.startsWith(prefix)) {
                        String suffix = k.substring(prefix.length());
                        int dotIdx = suffix.indexOf('.');
                        if (dotIdx > 0) {
                            tableNames.add(suffix.substring(0, dotIdx));
                        }
                    }
                }
            }
        }

        for (String tableName : tableNames) {
            Map<String, String> rule = new LinkedHashMap<>();
            String tablePrefix = prefix + tableName + ".";
            rule.put("actual-data-nodes", environment.getProperty(tablePrefix + "actual-data-nodes", ""));
            rule.put("database-strategy.standard.sharding-column",
                environment.getProperty(tablePrefix + "database-strategy.standard.sharding-column", ""));
            rule.put("database-strategy.standard.sharding-algorithm-name",
                environment.getProperty(tablePrefix + "database-strategy.standard.sharding-algorithm-name", ""));
            rule.put("table-strategy.standard.sharding-column",
                environment.getProperty(tablePrefix + "table-strategy.standard.sharding-column", ""));
            rule.put("table-strategy.standard.sharding-algorithm-name",
                environment.getProperty(tablePrefix + "table-strategy.standard.sharding-algorithm-name", ""));
            result.put(tableName, rule);
        }

        return result;
    }

    private Properties filterAlgorithmProps(Properties source) {
        Properties filtered = new Properties();
        for (String key : source.stringPropertyNames()) {
            if (!"type".equals(key)) {
                filtered.setProperty(key, source.getProperty(key));
            }
        }
        return filtered;
    }
}
