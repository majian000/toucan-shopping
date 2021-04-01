package com.toucan.shopping.center.user.service;


import com.toucan.shopping.center.user.export.vo.UserElasticSearchVO;
import com.toucan.shopping.center.user.vo.SearchAfterPage;

import java.util.List;

public interface UserElasticSearchService {

    /**
     * 保存对象
     * @param esUserVO
     */
    void save(UserElasticSearchVO esUserVO);


    /**
     * 查询列表 from size方式
     * @param esUserVo
     * @return
     */
    List<UserElasticSearchVO> queryListForFormSize(UserElasticSearchVO esUserVo,int size)  throws Exception;


    /**
     * 查询列表 search after方式
     * @param esUserVo
     * @return
     */
    SearchAfterPage queryListForSearchAfter(UserElasticSearchVO esUserVo, int size, Object[] searchAfter) throws Exception ;
}
