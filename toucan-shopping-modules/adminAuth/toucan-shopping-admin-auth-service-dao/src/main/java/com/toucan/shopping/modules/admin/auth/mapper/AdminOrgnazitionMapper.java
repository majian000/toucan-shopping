package com.toucan.shopping.modules.admin.auth.mapper;

import com.toucan.shopping.modules.admin.auth.entity.AdminOrgnazition;
import com.toucan.shopping.modules.admin.auth.vo.AdminOrgnazitionVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


@Mapper
public interface AdminOrgnazitionMapper {


    /**
     * 保存关联
     * @param entity
     * @return
     */
    int insert(AdminOrgnazition entity);

    /**
     * 根据实体查询列表
     * @param entity
     * @return
     */
    List<AdminOrgnazition> findListByEntity(AdminOrgnazition entity);

    /**
     * 删除指定应用编码下所有关联
     * @param appCode
     * @return
     */
    int deleteByAppCode(String appCode);


    /**
     * 根据组织机构ID删除所有关联
     * @param roleId
     * @return
     */
    int deleteByOrgnazitionId(String orgnazitionId);


    /**
     * 删除指定账号下所有关联
     * @param adminId
     * @return
     */
    int deleteByAdminId(String adminId);


    /**
     * 删除指定账号下指定应用的关联
     * @param adminId
     * @return
     */
    int deleteByAdminIdAndAppCode(String adminId, String appCode);

}
