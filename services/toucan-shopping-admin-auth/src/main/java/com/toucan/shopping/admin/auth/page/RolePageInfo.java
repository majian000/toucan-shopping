package com.toucan.shopping.admin.auth.page;

import com.toucan.shopping.admin.auth.entity.Role;
import com.toucan.shopping.common.page.PageInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * 角色列表查询页
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
public class RolePageInfo extends PageInfo<Role> {




    // ===============查询条件===================

    /**
     * 角色名
     */
    private String name;

    /**
     * 启用状态 0:禁用 1启用
     */
    private Short enableStatus;
    /**
     * 所属应用
     */
    private String appCode;


    //==============================================


}
