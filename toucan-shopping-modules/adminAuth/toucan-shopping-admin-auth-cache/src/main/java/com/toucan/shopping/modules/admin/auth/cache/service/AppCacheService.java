package com.toucan.shopping.modules.admin.auth.cache.service;

import com.toucan.shopping.modules.admin.auth.vo.AppVO;

public interface AppCacheService {


    /**
     * 保存对象
     * @param appVO
     */
    void save(AppVO appVO) throws Exception;

    /**
     * 根据应用编码移除
     * @param appCode
     * @return
     * @throws Exception
     */
    boolean deleteByAppCode(String appCode)  throws Exception;


    /**
     * 根据应用编码查询
     * @param appCode
     * @return
     * @throws Exception
     */
    AppVO findByAppCode(String appCode)  throws Exception;
}
