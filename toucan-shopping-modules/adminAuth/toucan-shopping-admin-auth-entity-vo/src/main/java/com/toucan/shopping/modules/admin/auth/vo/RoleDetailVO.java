package com.toucan.shopping.modules.admin.auth.vo;

import lombok.Data;

import java.util.List;

/**
 * 角色详情
 */
@Data
public class RoleDetailVO {

    private RoleVO basicInfo;

    private List<FunctionVO> functions;
}
