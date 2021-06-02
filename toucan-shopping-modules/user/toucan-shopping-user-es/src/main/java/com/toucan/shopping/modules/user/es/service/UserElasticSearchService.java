package com.toucan.shopping.modules.user.es.service;


import com.toucan.shopping.modules.user.vo.UserElasticSearchVO;
import com.toucan.shopping.modules.user.es.vo.SearchAfterPage;

import java.util.List;

public interface UserElasticSearchService {

    /**
     * 保存对象
     * @param esUserVO
     */
    void save(UserElasticSearchVO esUserVO);

    /**
     * 更新对象
     * @param esUserVO
     */
    void update(UserElasticSearchVO esUserVO);

    /**
     * 是否存在索引
     * @return
     */
    boolean existsIndex();

    /**
     * 创建索引
     */
    void createIndex();

    /**
     * 查询列表 from size方式
     * @param esUserVo
     * @return
     */
    List<UserElasticSearchVO> queryListForFormSize(UserElasticSearchVO esUserVo,int size)  throws Exception;


    /**
     * 根据ID查询
     * @param id
     * @return
     */
    List<UserElasticSearchVO> queryById(Long id) throws Exception;


    /**
     * 根据ID删除
     * @param id
     * @return
     */
    boolean deleteById(String id) throws Exception;

    /**
     * 根据ID查询
     * @param userMainId
     * @return
     */
    List<UserElasticSearchVO> queryByUserMainId(Long userMainId) throws Exception;

    /**
     * 查询列表 search after方式
     * @param esUserVo
     * @return
     */
    SearchAfterPage queryListForSearchAfter(UserElasticSearchVO esUserVo, int size, Object[] searchAfter) throws Exception ;

    /**
     * 查询记录总数
     * @param esUserVo
     * @return
     */
    Long queryCount(UserElasticSearchVO esUserVo)  throws Exception ;

}
