package com.toucan.shopping.modules.common.properties.modules.userLoginCache;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;

@Data
public class UserLoginRedis {

    /**
     * 通过用户ID取余得到索引位置
     */
    private String index;

    /**
     * 数据库数量
     */
    private Integer dbCount;

    /**
     * 选择类型 # single:单实例 cluster:集群
     */
    private String select;

    /**
     * 连接超时,默认20秒超时
     */
    private Long timeout = -1L;

    /**
     * 密码
     */
    private String password;

    // =============================单实例===========================================
    /**
     * 地址
     */
    private String host;

    /**
     * 端口
     */
    private Integer port = 6379;



    /**
     * 数据库
     */
    private Integer database = 0;

    // ========================================================================




    // =============================集群===========================================
    //活跃数
    private int maxActive = 8;

    //等待数
    private int maxWaitMillis = -1;

    //最大核心线程数
    private int maxIdle = 8;

    //最小核心线程数
    private int minIdle = 0;

    //最大连接转移数
    private Long maxRedirects = 3L;

    // ========================================================================
    
}
