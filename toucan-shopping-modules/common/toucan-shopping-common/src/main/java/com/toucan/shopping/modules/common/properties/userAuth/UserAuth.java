package com.toucan.shopping.modules.common.properties.userAuth;

import lombok.Data;

/**
 * 用户中心权限
 */
@Data
public class UserAuth {

    /**
     * 权限中台名称
     */
    private String name;

    /**
     * 是否开启用户中心接管权限控制
     */
    private boolean enabled = false;

    private String loginSalt; //登录token加盐

    /**
     * HTTP权限请求头,默认值为Cookie
     * 后台管理端 Cookie:adminId=d10590b318544049ba2104d1f5517a7d&loginToken=d10590b318544049ba2104d1f5517a7d
     * 或商城C端 Cookie:userId=d10590b318544049ba2104d1f5517a7d&loginToken=d10590b318544049ba2104d1f5517a7d
     */
    private String httpToucanAuthHeader = "Cookie";

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

    /**
     * 共享会话的所有域名多个用，分割
     */
    private String ssoSetCookieUrlList;

}
