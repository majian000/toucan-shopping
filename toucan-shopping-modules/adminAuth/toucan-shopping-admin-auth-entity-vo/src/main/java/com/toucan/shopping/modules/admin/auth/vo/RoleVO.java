package com.toucan.shopping.modules.admin.auth.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.toucan.shopping.modules.admin.auth.entity.Role;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 角色表
 */
@Data
public class RoleVO extends Role {


    /**
     * 创建人
     */
    private String createAdminUsername;


    /**
     * 修改人
     */
    private String updateAdminUsername;

    /**
     * 应用名称
     */
    private String appName;

    /**
     * 操作来源 1:中台 2:应用
     */
    private Integer operateSourceType = 1;

    /**
     * 操作应用
     */
    private String operateAppCode;

}
