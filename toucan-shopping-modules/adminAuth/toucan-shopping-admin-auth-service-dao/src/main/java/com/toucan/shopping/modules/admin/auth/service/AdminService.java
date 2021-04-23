package com.toucan.shopping.modules.admin.auth.service;

import com.toucan.shopping.modules.admin.auth.entity.Admin;
import com.toucan.shopping.modules.admin.auth.page.AdminPageInfo;
import com.toucan.shopping.modules.admin.auth.vo.AdminVO;
import com.toucan.shopping.modules.common.page.PageInfo;

import java.util.List;

public interface AdminService {

    List<Admin> findListByEntity(Admin admin);

    /**
     * 保存账号
     * @param admin
     * @return
     */
    int save(Admin admin);

    /**
     * 修改账号
     * @param admin
     * @return
     */
    int update(Admin admin);

    /**
     * 判断账号是否存在(无关应用)
     * @param username
     * @return
     */
    boolean exists(String username);


    /**
     * 修改密码
     * @param adminVO
     * @return
     */
    int updatePassword(AdminVO adminVO);



    /**
     * 查询列表页
     * @param appPageInfo
     * @return
     */
    PageInfo<AdminVO> queryListPage(AdminPageInfo appPageInfo);


}
