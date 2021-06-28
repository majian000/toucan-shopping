package com.toucan.shopping.cloud.seller.app.config;

/**
 * 数据源健康监控重写实现
 */
//@Configuration
public class DataSourceHealthConfig /*extends DataSourceHealthContributorAutoConfiguration*/ {


//
//    private final Logger logger = LoggerFactory.getLogger(getClass());
//
//    @Value("select 1")
//    private String defaultQuery;
//
//
//    public DataSourceHealthConfig(Map<String, DataSource> dataSources, ObjectProvider<DataSourcePoolMetadataProvider> metadataProviders) {
//        super(dataSources, metadataProviders);
//    }
//
//    @Override
//    protected AbstractHealthIndicator createIndicator(DataSource source) {
//
//        logger.info("健康监控数据源执行 "+defaultQuery);
//        DataSourceHealthIndicator indicator = (DataSourceHealthIndicator) super.createIndicator(source);
//        if (!StringUtils.hasText(indicator.getQuery())) {
//            indicator.setQuery(defaultQuery);
//        }
//        return indicator;
//    }
}
