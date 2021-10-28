package com.toucan.shopping.modules.common.properties.modules.categoryCache;


import lombok.Data;
import org.springframework.beans.factory.annotation.Value;

/**
 * 类别缓存 redis相关配置
 */
@Data
public class CategoryCacheRedis {

    /**
     * 选择类型 # single:单实例 cluster:集群
     */
    @Value("${toucan.modules.categoryCache.redis.select}")
    private String select;

    /**
     * 连接超时,默认20秒超时
     */
    @Value("${toucan.modules.categoryCache.redis.timeout:-1}")
    private Long timeout;

    /**
     * 密码
     */
    @Value("${toucan.modules.categoryCache.redis.password}")
    private String password;

    // =============================单实例===========================================
    /**
     * 地址
     */
    @Value("${toucan.modules.categoryCache.redis.host}")
    private String host;

    /**
     * 端口
     */
    @Value("${toucan.modules.categoryCache.redis.port:6379}")
    private Integer port;



    /**
     * 数据库
     */
    @Value("${toucan.modules.categoryCache.redis.database:0}")
    private Integer database;

    // ========================================================================




    // =============================集群===========================================
    //活跃数
    @Value("${toucan.modules.categoryCache.redis.maxActive:8}")
    private int maxActive;

    //等待数
    @Value("${toucan.modules.categoryCache.redis.maxWaitMillis:-1}")
    private int maxWaitMillis;

    //最大核心线程数
    @Value("${toucan.modules.categoryCache.redis.maxIdle:8}")
    private int maxIdle;

    //最小核心线程数
    @Value("${toucan.modules.categoryCache.redis.minIdle:0}")
    private int minIdle;

    //节点配置
    @Value("${toucan.modules.categoryCache.redis.hosts}")
    private String hosts;

    //最大连接转移数
    @Value("${toucan.modules.categoryCache.redis.maxRedirects:3}")
    private Long maxRedirects;

    // ========================================================================

}
