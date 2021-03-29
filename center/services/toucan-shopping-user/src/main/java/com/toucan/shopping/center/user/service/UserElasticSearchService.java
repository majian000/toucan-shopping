package com.toucan.shopping.center.user.service;

import com.toucan.shopping.center.user.vo.UserElasticSearchVO;

import java.util.List;

public interface UserElasticSearchService {

    /**
     * 保存对象
     * @param esUserVO
     */
    void save(UserElasticSearchVO esUserVO);


    /**
     * 查询列表
     * @param esUserVo
     * @return
     */
    List<UserElasticSearchVO> queryList(UserElasticSearchVO esUserVo,int size)  throws Exception;
}
