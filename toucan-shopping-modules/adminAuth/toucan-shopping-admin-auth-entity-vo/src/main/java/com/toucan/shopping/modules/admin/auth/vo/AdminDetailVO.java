package com.toucan.shopping.modules.admin.auth.vo;

import lombok.Data;

import java.util.List;

/**
 * 管理员详情(含基本信息、角色、组织机构)
 */
@Data
public class AdminDetailVO {

    /** 基本信息 */
    private AdminVO basicInfo;

    /** 角色列表(含角色名称) */
    private List<AdminRoleVO> roles;

    /** 组织机构列表 */
    private List<OrgnazitionVO> orgs;
}
