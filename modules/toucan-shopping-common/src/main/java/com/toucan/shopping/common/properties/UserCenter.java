package com.toucan.shopping.common.properties;

import lombok.Data;

/**
 * 自定义配置
 */
@Data
public class UserCenter {

    /**
     * 用户中心名称
     */
    private String name;

    /**
     * 是否开启用户中心接管权限控制
     */
    private boolean enabled = false;

    /**
     * HTTP权限请求头,默认值为bbs-atuh-header
     * 后台管理端 bbs-atuh-header:adminId=d10590b318544049ba2104d1f5517a7d&loginToken=d10590b318544049ba2104d1f5517a7d
     * 或商城C端 bbs-atuh-header:userId=d10590b318544049ba2104d1f5517a7d&loginToken=d10590b318544049ba2104d1f5517a7d
     */
    private String httpBbsAuthHeader = "bbs-atuh-header";

    /**
     * 默认登录页
     */
    private String loginPage;

    /**
     * 拦截指定路径
     */
    private String pathPatterns;

    /**
     * 忽略拦截的后缀
     */
    private String excludePathPatterns;
}
