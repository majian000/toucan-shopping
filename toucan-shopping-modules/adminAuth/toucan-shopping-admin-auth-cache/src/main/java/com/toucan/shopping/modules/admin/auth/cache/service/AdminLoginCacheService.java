package com.toucan.shopping.modules.admin.auth.cache.service;


/**
 * 账号登录缓存
 * @author majian
 * @date 2022-7-2 02:30:52
 */
public interface AdminLoginCacheService {

    /**
     * 保存登录缓存
     * @param adminId
     * @param appCode
     * @param loginToken
     */
    void loginToken(String adminId,String appCode,String loginToken);

    /**
     * 拿到登录缓存
     * @param adminId
     * @param appCode
     * @return
     */
    Object getLoginToken(String adminId,String appCode);

    /**
     * 删除登录会话
     * @param adminId
     * @param appCode
     */
    void deleteLoginToken(String adminId,String appCode);

    /**
     * 登录缓存超时时间延长
     * @param adminId
     */
    void loginTokenDelay(String adminId);

}
