package com.toucan.shopping.modules.admin.auth.mapper;

import com.toucan.shopping.modules.admin.auth.entity.DictCategoryApp;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


@Mapper
public interface DictCategoryAppMapper {


    /**
     * 保存关联
     * @param entity
     * @return
     */
    int insert(DictCategoryApp entity);

    /**
     * 根据实体查询列表
     * @param entity
     * @return
     */
    List<DictCategoryApp> findListByEntity(DictCategoryApp entity);

    /**
     * 删除指定应用编码下所有关联
     * @param appCode
     * @return
     */
    int deleteByAppCode(String appCode);


    /**
     * 根据机构ID删除所有关联
     * @param dictCategoryId
     * @return
     */
    int deleteByDictCategoryId(Integer dictCategoryId);



}
