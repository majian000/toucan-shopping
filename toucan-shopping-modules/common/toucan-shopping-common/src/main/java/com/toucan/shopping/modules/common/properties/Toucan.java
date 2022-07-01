package com.toucan.shopping.modules.common.properties;

import com.toucan.shopping.modules.common.properties.adminAuth.AdminAuth;
import com.toucan.shopping.modules.common.properties.adminAuthScheduler.AdminAuthScheduler;
import com.toucan.shopping.modules.common.properties.messageWebPc.MessageWebPC;
import com.toucan.shopping.modules.common.properties.modules.Modules;
import com.toucan.shopping.modules.common.properties.plugins.Plugins;
import com.toucan.shopping.modules.common.properties.seller.Seller;
import com.toucan.shopping.modules.common.properties.shoppingPc.ShoppingPC;
import com.toucan.shopping.modules.common.properties.shoppingPc.ShoppingSellerWebPC;
import com.toucan.shopping.modules.common.properties.sign.Sign;
import com.toucan.shopping.modules.common.properties.user.User;
import com.toucan.shopping.modules.common.properties.userAuth.UserAuth;
import com.toucan.shopping.modules.common.properties.userCenterScheduler.UserCenterScheduler;
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
     * 签名
     */
    private Sign sign;

    /**
     * 用户中心调度
     */
    private UserCenterScheduler userCenterScheduler;


    /**
     * 商城PC端
     */
    private ShoppingPC shoppingPC;

    /**
     * 消息web服务
     */
    private MessageWebPC messageWebPC;

    /**
     * 卖家中心PC端
     */
    private ShoppingSellerWebPC shoppingSellerWebPC;


    /**
     * 权限中台调度
     */
    private AdminAuthScheduler adminAuthScheduler;


    /**
     * 用户配置
     */
    private User user;

    /**
     * 卖家配置
     */
    private Seller seller;

    /**
     * 扩展模块
     */
    private Modules modules;

    /**
     * 插件列表
     */
    private Plugins plugins;


}
