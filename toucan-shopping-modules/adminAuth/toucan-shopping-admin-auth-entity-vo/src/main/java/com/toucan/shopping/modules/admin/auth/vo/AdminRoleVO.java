package com.toucan.shopping.modules.admin.auth.vo;

import com.toucan.shopping.modules.admin.auth.entity.AdminRole;
import com.toucan.shopping.modules.admin.auth.entity.Function;
import com.toucan.shopping.modules.admin.auth.entity.RoleFunction;
import lombok.Data;

import java.util.List;

/**
 * 管理员角色表
 */
@Data
public class AdminRoleVO extends AdminRole {


    /**
     * 角色列表
     */
    private List<AdminRole> roles;



}
