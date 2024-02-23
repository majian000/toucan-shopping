package com.toucan.shopping.modules.admin.auth.service;


import com.toucan.shopping.modules.admin.auth.entity.DictCategory;
import com.toucan.shopping.modules.admin.auth.page.DictCategoryPageInfo;
import com.toucan.shopping.modules.admin.auth.vo.DictCategoryVO;
import com.toucan.shopping.modules.common.page.PageInfo;

import java.util.List;

public interface DictCategoryService {

    /**
     * 根据查询对象返回列表
     * @param entity
     * @return
     */
    List<DictCategoryVO> findListByEntity(DictCategory entity);

    /**
     * 保存
     * @param entity
     * @return
     */
    int save(DictCategory entity);

    /**
     * 更新
     * @param entity
     * @return
     */
    int update(DictCategory entity);

    /**
     * 查询最大排序
     * @return
     */
    Integer queryMaxSort();


    /**
     * 根据编码和应用编码查询集合
     * @param code
     * @return
     */
    List<DictCategoryVO> queryListByCodeAndAppCodes(String code,List<String> appCodes);

    /**
     * 查询列表页
     * @param pageInfo
     * @return
     */
    PageInfo<DictCategoryVO> queryListPage(DictCategoryPageInfo pageInfo);

    /**
     * 查询列表
     * @param query
     * @return
     */
    List<DictCategoryVO> queryList(DictCategoryVO query);

    /**
     *  根据ID删除
     * @param id
     * @return
     */
    int deleteById(Integer id);


    /**
     * 根据应用编码查询列表
     * @param appCode
     * @return
     */
    List<DictCategoryVO> queryListByAppCode(String appCode);



}
