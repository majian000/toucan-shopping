package com.toucan.shopping.modules.admin.auth.vo;

import com.toucan.shopping.modules.admin.auth.entity.Function;
import com.toucan.shopping.modules.admin.auth.entity.Role;
import lombok.Data;

/**
 * 功能项
 */
@Data
public class FunctionVO extends Function {


    /**
     * 上级节点名称
     */
    private String parentName;

    /**
     * 创建人
     */
    private String createAdminUsername;


    /**
     * 修改人
     */
    private String updateAdminUsername;

}
