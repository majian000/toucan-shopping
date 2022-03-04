package com.toucan.shopping.modules.admin.auth.vo;

import com.toucan.shopping.modules.admin.auth.entity.Function;
import com.toucan.shopping.modules.admin.auth.entity.RoleFunction;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 角色功能项表
 */
@Data
public class RoleFunctionVO extends RoleFunction {


    /**
     * 权限列表
     */
    private List<FunctionTreeVO> functions;


}
