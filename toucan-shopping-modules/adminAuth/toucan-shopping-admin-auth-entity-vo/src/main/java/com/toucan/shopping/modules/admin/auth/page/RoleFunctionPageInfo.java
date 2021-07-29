package com.toucan.shopping.modules.admin.auth.page;

import com.toucan.shopping.modules.admin.auth.entity.AdminRole;
import com.toucan.shopping.modules.admin.auth.entity.RoleFunction;
import com.toucan.shopping.modules.common.page.PageInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;

/**
 * 角色功能项列表分页
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
public class RoleFunctionPageInfo extends PageInfo<RoleFunction> {


    // ===============查询条件===================
    /**
     * 所属角色
     */
    private String roleId;

    /**
     * 所属菜单
     */
    private String functionId;


    /**
     * 所属应用
     */
    private String appCode;

    /**
     * 创建时间
     */
    private Date createDate;


    //==============================================


}
