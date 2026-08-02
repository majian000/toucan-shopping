package com.toucan.shopping.modules.admin.auth.vo;

import lombok.Data;
import java.util.List;

/**
 * 角色权限树节点（精简版，只含前端需要的字段）
 */
@Data
public class RoleFunctionTreeVO {

    /** 节点ID */
    private Long id;

    /** 功能项ID */
    private String functionId;

    /** 上级节点ID */
    private Long pid;

    /** 名称 */
    private String name;

    /** 类型 0目录 1菜单 2按钮 3工具条按钮 4API 5页面控件 */
    private Integer type;

    /** 是否有子节点 */
    private Boolean isParent;

    /** 自身是否已关联 */
    private Boolean checked;

    /** 子孙总数（含自身） */
    private Integer descendantCount;

    /** 已勾选的子孙数（含自身） */
    private Integer checkedDescendantCount;

    /** 是否级联模式 */
    private Boolean cascaded;

    /** 子节点 */
    private List<RoleFunctionTreeVO> children;
}
