package com.toucan.shopping.center.admin.auth.service;




import com.toucan.shopping.center.admin.auth.entity.Admin;

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




}
