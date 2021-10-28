package com.toucan.shopping.modules.common.properties.modules.toucanRedis;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;

@Data
public class ToucanRedis {
    /**
     * 选择类型 # single:单实例 cluster:集群
     */
    @Value("${toucan.modules.toucanRedis.select}")
    private String select;

    /**
     * 连接超时,默认20秒超时
     */
    @Value("${toucan.modules.toucanRedis.timeout:-1}")
    private Long timeout;

    /**
     * 密码
     */
    @Value("${toucan.modules.toucanRedis.password}")
    private String password;

    // =============================单实例===========================================
    /**
     * 地址
     */
    @Value("${toucan.modules.toucanRedis.host}")
    private String host;

    /**
     * 端口
     */
    @Value("${toucan.modules.toucanRedis.port:6379}")
    private Integer port;



    /**
     * 数据库
     */
    @Value("${toucan.modules.toucanRedis.database:0}")
    private Integer database;

    // ========================================================================




    // =============================集群===========================================
    //活跃数
    @Value("${toucan.modules.toucanRedis.maxActive:8}")
    private int maxActive;

    //等待数
    @Value("${toucan.modules.toucanRedis.maxWaitMillis:-1}")
    private int maxWaitMillis;

    //最大核心线程数
    @Value("${toucan.modules.toucanRedis.maxIdle:8}")
    private int maxIdle;

    //最小核心线程数
    @Value("${toucan.modules.toucanRedis.minIdle:0}")
    private int minIdle;

    //节点配置
    @Value("${toucan.modules.toucanRedis.hosts}")
    private String hosts;

    //最大连接转移数
    @Value("${toucan.modules.toucanRedis.maxRedirects:3}")
    private Long maxRedirects;

    // ========================================================================
    
}
