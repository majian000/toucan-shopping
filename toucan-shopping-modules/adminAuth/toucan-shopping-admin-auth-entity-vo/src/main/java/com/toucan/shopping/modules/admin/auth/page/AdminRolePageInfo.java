package com.toucan.shopping.modules.admin.auth.page;

import com.toucan.shopping.modules.admin.auth.entity.Admin;
import com.toucan.shopping.modules.admin.auth.entity.AdminApp;
import com.toucan.shopping.modules.admin.auth.entity.AdminRole;
import com.toucan.shopping.modules.common.page.PageInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;
import java.util.List;

/**
 * 账号角色列表分页
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
public class AdminRolePageInfo extends PageInfo<AdminRole> {


    // ===============查询条件===================
    /**
     * 所属应用
     */
    private String appCode;

    /**
     * 所属用户
     */
    private String adminId;


    /**
     * 所属角色
     */
    private String roleId;

    //==============================================


}
