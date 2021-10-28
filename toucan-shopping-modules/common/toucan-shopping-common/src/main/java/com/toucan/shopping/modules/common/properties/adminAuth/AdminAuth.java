package com.toucan.shopping.modules.common.properties.adminAuth;

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
     * 后台管理端 Cookie:10001002_aid=d10590b318544049ba2104d1f5517a7d;10001002_lt=d10590b318544049ba2104d1f5517a7d
     */
    private String httpToucanAuthHeader = "Cookie";

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
