package com.toucan.shopping.modules.admin.auth.mapper;

import com.toucan.shopping.modules.admin.auth.entity.OrgnazitionApp;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


@Mapper
public interface OrgnazitionAppMapper {


    /**
     * 保存关联
     * @param entity
     * @return
     */
    int insert(OrgnazitionApp entity);

    /**
     * 根据实体查询列表
     * @param entity
     * @return
     */
    List<OrgnazitionApp> findListByEntity(OrgnazitionApp entity);

    /**
     * 删除指定应用编码下所有关联
     * @param appCode
     * @return
     */
    int deleteByAppCode(String appCode);


    /**
     * 根据机构ID删除所有关联
     * @param orgnazitionId
     * @return
     */
    int deleteByOrgnazitionId(String orgnazitionId);



}
