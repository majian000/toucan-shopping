package com.toucan.shopping.modules.admin.auth.service;




import com.toucan.shopping.modules.admin.auth.entity.DictApp;

import java.util.List;

public interface DictAppService {

    /**
     * 根据实体查询列表
     * @param entity
     * @return
     */
    List<DictApp> findListByEntity(DictApp entity);

    /**
     * 保存关联
     * @param entity
     * @return
     */
    int save(DictApp entity);

    /**
     * 批量保存
     * @param entitys
     * @return
     */
    int saves(List<DictApp> entitys);
    /**
     * 删除指定应用编码下所有关联
     * @param appCode
     * @return
     */
    int deleteByAppCode(String appCode);


    /**
     * 删除指定应用编码下所有关联
     * @param dictId
     * @return
     */
    int deleteByDictId(Long dictId);


    /**
     * 删除指定应用编码下所有关联
     * @param dictIdList
     * @return
     */
    int deleteByDictIdList(List<Long> dictIdList);

    /**
     * 根据分类ID删除
     * @param categoryId
     * @return
     */
    int deleteByCategoryId(Integer categoryId);

    /**
     * 根据分类ID删除和应用编码删除
     * @param categoryId
     * @param appCode
     * @return
     */
    int deleteByCategoryId(Integer categoryId,String appCode);

}
