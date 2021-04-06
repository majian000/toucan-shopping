package com.toucan.shopping.admin.auth.page;

import com.toucan.shopping.admin.auth.entity.Role;
import lombok.Data;

/**
 * 角色列表查询页
 */
@Data
public class RolePageInfo extends Role {

    /**
     * 页码
     */
    private int page;

    /**
     * 每页显示数量
     */
    private int size;

    /**
     * 每页显示数量
     */
    private int limit;






}
