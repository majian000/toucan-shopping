package com.toucan.shopping.modules.admin.auth.vo;

import com.toucan.shopping.modules.admin.auth.entity.Function;
import com.toucan.shopping.modules.admin.auth.entity.Orgnazition;
import com.toucan.shopping.modules.admin.auth.entity.OrgnazitionApp;
import lombok.Data;

import java.util.List;

/**
 * 组织机构
 */
@Data
public class OrgnazitionVO extends Orgnazition {


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

    /**
     * 功能所属人
     */
    private String adminId;


    /**
     * 应用编码
     */
    private List<String> appCodes;

    /**
     * 机构应用关联
     */
    private List<OrgnazitionApp> orgnazitionApps;


}
