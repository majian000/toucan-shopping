package com.toucan.shopping.modules.admin.auth.vo;

import com.toucan.shopping.modules.admin.auth.entity.RoleFunction;
import lombok.Data;

/**
 * 角色功能列表VO (关联t_sa_function)
 */
@Data
public class RoleFunctionListVO extends RoleFunction {

    /** 功能名称 */
    private String functionName;

    /** 功能路径 */
    private String functionUrl;

    /** 权限标识 */
    private String functionPermission;

    /** 功能类型 0目录 1菜单 2按钮 3工具条按钮 4:API 5页面控件 */
    private Short functionType;

}
