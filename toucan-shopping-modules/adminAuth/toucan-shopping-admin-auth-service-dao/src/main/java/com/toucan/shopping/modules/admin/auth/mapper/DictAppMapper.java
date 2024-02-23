package com.toucan.shopping.modules.admin.auth.mapper;

import com.toucan.shopping.modules.admin.auth.entity.DictApp;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


@Mapper
public interface DictAppMapper {


    /**
     * 保存关联
     * @param entity
     * @return
     */
    int insert(DictApp entity);

    /**
     * 根据实体查询列表
     * @param entity
     * @return
     */
    List<DictApp> findListByEntity(DictApp entity);

    /**
     * 删除指定应用编码下所有关联
     * @param appCode
     * @return
     */
    int deleteByAppCode(String appCode);


    /**
     * 根据ID删除所有关联
     * @param dictId
     * @return
     */
    int deleteByDictId(Long dictId);


    /**
     * 根据分类ID删除
     * @param categoryId
     * @return
     */
    int deleteByCategoryId(Integer categoryId);

}
