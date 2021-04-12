package com.toucan.shopping.admin.auth.service;

import com.toucan.shopping.admin.auth.entity.Admin;
import com.toucan.shopping.admin.auth.page.AdminPageInfo;
import com.toucan.shopping.admin.auth.vo.AdminVO;
import com.toucan.shopping.modules.common.page.PageInfo;

import java.util.List;

public interface AdminService {

    List<Admin> findListByEntity(Admin admin);

    int save(Admin admin);

    /**
     * 判断账号是否存在(无关应用)
     * @param username
     * @return
     */
    boolean exists(String username);



    /**
     * 查询列表页
     * @param appPageInfo
     * @return
     */
    PageInfo<AdminVO> queryListPage(AdminPageInfo appPageInfo);


}
