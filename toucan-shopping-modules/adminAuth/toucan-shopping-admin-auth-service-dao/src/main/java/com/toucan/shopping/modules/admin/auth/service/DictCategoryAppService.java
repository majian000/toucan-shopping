package com.toucan.shopping.modules.admin.auth.service;




import com.toucan.shopping.modules.admin.auth.entity.DictCategoryApp;

import java.util.List;

public interface DictCategoryAppService {

    /**
     * 根据实体查询列表
     * @param entity
     * @return
     */
    List<DictCategoryApp> findListByEntity(DictCategoryApp entity);

    /**
     * 保存关联
     * @param entity
     * @return
     */
    int save(DictCategoryApp entity);

    /**
     * 删除指定应用编码下所有关联
     * @param appCode
     * @return
     */
    int deleteByAppCode(String appCode);


    /**
     * 删除指定应用编码下所有关联
     * @param dictCategoryId
     * @return
     */
    int deleteByDictCategoryId(Integer dictCategoryId);


}
