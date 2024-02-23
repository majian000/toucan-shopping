package com.toucan.shopping.modules.admin.auth.service;


import com.toucan.shopping.modules.admin.auth.entity.Dict;
import com.toucan.shopping.modules.admin.auth.page.DictPageInfo;
import com.toucan.shopping.modules.admin.auth.vo.DictVO;
import com.toucan.shopping.modules.common.page.PageInfo;

import java.util.List;

public interface DictService {

    /**
     * 根据查询对象返回列表
     * @param entity
     * @return
     */
    List<DictVO> findListByEntity(Dict entity);

    /**
     * 保存
     * @param entity
     * @return
     */
    int save(Dict entity);

    /**
     * 更新
     * @param entity
     * @return
     */
    int update(Dict entity);

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
    List<DictVO> queryListByCodeAndAppCodes(String code, List<String> appCodes);

    /**
     * 查询列表页
     * @param pageInfo
     * @return
     */
    PageInfo<DictVO> queryListPage(DictPageInfo pageInfo);

    /**
     *  根据ID删除
     * @param id
     * @return
     */
    int deleteById(Long id);

    /**
     * 根据分类ID删除
     * @param categoryId
     * @return
     */
    int deleteByCategoryId(Integer categoryId);


    /**
     * 根据应用编码查询列表
     * @param appCode
     * @return
     */
    List<DictVO> queryListByAppCode(String appCode);


    /**
     * 根据查询对象查询列表
     * @param query
     * @return
     */
    List<DictVO> queryList(DictVO query);

    /**
     * 根据查询对象查询列表数量
     * @param entity
     * @return
     */
    Long queryListCount(DictVO entity);



}
