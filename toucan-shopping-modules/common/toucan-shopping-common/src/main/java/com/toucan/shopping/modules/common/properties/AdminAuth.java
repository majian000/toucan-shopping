package com.toucan.shopping.modules.common.properties;

import lombok.Data;

/**
 * 自定义配置
 */
@Data
public class AdminAuth {

    /**
     * 权限中台名称
     */
    private String name;

    /**
     * 是否开启用户中心接管权限控制
     */
    private boolean enabled = false;

    /**
     * HTTP权限请求头,默认值为toucan-atuh-header
     * 后台管理端 toucan-atuh-header:adminId=d10590b318544049ba2104d1f5517a7d&loginToken=d10590b318544049ba2104d1f5517a7d
     * 或商城C端 toucan-atuh-header:userId=d10590b318544049ba2104d1f5517a7d&loginToken=d10590b318544049ba2104d1f5517a7d
     */
    private String httpToucanAuthHeader = "toucan-atuh-header";

    /**
     * 默认登录页
     */
    private String loginPage;

    /**
     * 没有权限跳转页面
     */
    private String page403;

    /**
     * 拦截指定路径
     */
    private String pathPatterns;

    /**
     * 忽略拦截的后缀
     */
    private String excludePathPatterns;
}
