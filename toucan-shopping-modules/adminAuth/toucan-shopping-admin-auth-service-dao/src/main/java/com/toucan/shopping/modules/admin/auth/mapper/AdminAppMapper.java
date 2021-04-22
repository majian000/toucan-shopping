package com.toucan.shopping.modules.admin.auth.mapper;

import com.toucan.shopping.modules.admin.auth.entity.AdminApp;
import com.toucan.shopping.modules.admin.auth.vo.AdminAppVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


@Mapper
public interface AdminAppMapper {


    /**
     * 保存关联
     * @param adminApp
     * @return
     */
    int insert(AdminApp adminApp);

    /**
     * 根据实体查询列表
     * @param adminApp
     * @return
     */
    List<AdminApp> findListByEntity(AdminApp adminApp);

    /**
     * 删除指定应用编码下所有关联
     * @param appCode
     * @return
     */
    int deleteByAppCode(String appCode);


    /**
     * 查询账号应用关联列表
     * @param query
     * @return
     */
    List<AdminAppVO> findAppListByAdminAppEntity(AdminApp query);


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
