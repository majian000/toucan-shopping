package com.toucan.shopping.modules.common.properties;


import lombok.Data;
import org.springframework.beans.factory.annotation.Value;

/**
 * redis相关配置
 */
@Data
public class Redis {

    /**
     * 选择类型 # single:单实例 cluster:集群
     */
    @Value("${toucan.modules.areaCache.redis.select}")
    private String select;

    /**
     * 连接超时
     */
    @Value("${toucan.modules.areaCache.redis.single.timeout}")
    private Long timeout;

    /**
     * 密码
     */
    @Value("${toucan.modules.areaCache.redis.password:#{null}}")
    private String password;

    // =============================单实例===========================================
    /**
     * 地址
     */
    @Value("${toucan.modules.areaCache.redis.single.host:#{null}}")
    private String host;

    /**
     * 端口
     */
    @Value("${toucan.modules.areaCache.redis.single.port:6379}}")
    private Integer port;



    /**
     * 数据库
     */
    @Value("${toucan.modules.areaCache.redis.single.database:0}}")
    private Integer database;

    // ========================================================================




    // =============================集群===========================================
    //活跃数
    @Value("${toucan.modules.areaCache.redis.jedis.pool.max-active:8}")
    private int maxActive;

    //等待数
    @Value("${toucan.modules.areaCache.redis.jedis.pool.max-wait:-1}")
    private int maxWait;

    //最大核心线程数
    @Value("${toucan.modules.areaCache.redis.jedis.pool.max-idle:8}")
    private int maxIdle;

    //最小核心线程数
    @Value("${toucan.modules.areaCache.redis.jedis.pool.min-idle:0}")
    private int minIdle;

    //节点配置
    @Value("${toucan.modules.areaCache.redis.cluster.hosts:#{null}}")
    private String hosts;

    //最大连接转移数
    @Value("${toucan.modules.areaCache.redis.cluster.maxRedirects:3}")
    private Long maxRedirects;

    // ========================================================================

}
