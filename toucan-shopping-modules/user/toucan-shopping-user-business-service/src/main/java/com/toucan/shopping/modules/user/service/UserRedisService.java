package com.toucan.shopping.modules.user.service;

public interface UserRedisService {

    /**
     * 刷新最新用户信息到缓存，如果redis中存在这个key的话就刷新(这个用户在登录状态),不存在就忽略
     * @param userMainId
     * @return
     */
    boolean flushLoginCache(String userMainId,String appCode) throws Exception;

    /**
     * 清空登录缓存(直接删除该用户会话)
     * @param userMainId
     * @return
     * @throws Exception
     */
    void clearLoginCache(String userMainId,String appCode) throws Exception;
}
