package com.toucan.shopping.modules.common.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 自定义配置
 */
@Data
@ConfigurationProperties(prefix = "toucan")
@Component
public class Toucan {

    /**
     * 应用编码
     */
    private String appCode;

    /**
     * 应用名称
     */
    private String appName;

    /**
     * 机器编码 用于生成订单号,可以通过订单号定位某个机器的某个时间阶段下的订单
     */
    private String machineId;

    //工作ID 0~31
    private Integer workerId;

    //数据中心ID (0~31)
    private Integer datacenterId;

    /**
     * 权限中台
     */
    private AdminAuth adminAuth;

    /**
     * 用户中心
     */
    private UserAuth userAuth;

    /**
     * 用户中心调度
     */
    private UserCenterScheduler userCenterScheduler;



    /**
     * 权限中台调度
     */
    private AdminAuthScheduler adminAuthScheduler;


    /**
     * 用户配置
     */
    private User user;

    /**
     * 第三方模块
     */
    private Modules modules;


}
