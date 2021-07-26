package com.toucan.shopping.modules.admin.auth.mapper;

import com.toucan.shopping.modules.admin.auth.entity.AdminRole;
import com.toucan.shopping.modules.admin.auth.page.AdminRolePageInfo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


@Mapper
public interface AdminRoleMapper {

    int insert(AdminRole AdminRole);

    int inserts(AdminRole[] entitys);

    List<AdminRole> findListByEntity(AdminRole adminRole);

    int deleteByRoleId(String roleId);

    List<AdminRole> findListByAdminId(String adminId);

    int deleteByAdminId(String adminId);

    int deleteByAdminIdAndAppCodes(String adminId,String[] appCodes);


    List<AdminRole> listByAdminIdAndAppCode(String adminId,String appCode);



    /**
     * 查询列表页
     * @param queryPageInfo
     * @return
     */
    List<AdminRole> queryListPage(AdminRolePageInfo queryPageInfo);

    /**
     * 返回列表页数量
     * @param queryPageInfo
     * @return
     */
    Long queryListPageCount(AdminRolePageInfo queryPageInfo);


}
