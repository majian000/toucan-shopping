package com.toucan.shopping.modules.admin.auth.vo;

import com.toucan.shopping.modules.admin.auth.entity.Function;
import com.toucan.shopping.modules.admin.auth.entity.Role;
import com.toucan.shopping.modules.common.vo.bootstrap.State;
import com.toucan.shopping.modules.common.vo.layui.dtree.CheckArr;
import lombok.Data;

import java.util.List;

/**
 * 角色树
 */
@Data
public class RoleTreeVO extends Role {

    /**
     * 节点ID
     */
    private Long nodeId;

    /**
     * 上级节点ID
     */
    private Long parentId;

    /**
     * 节点名
     */
    private String title;

    /**
     * 节点名
     */
    private String text;


    /**
     * 子节点
     */
    private List<RoleTreeVO> children;




    /**
     * 标签
     */
    private List<String> tags;

    /**
     * 节点属性
     */
    private State state = new State();

    /**
     * 节点属性
     */
    private CheckArr checkArr = new CheckArr();


    /**
     * 创建人
     */
    private String createAdminUsername;


    /**
     * 修改人
     */
    private String updateAdminUsername;

    /**
     * 是否支持checkbox
     */
    private boolean checked;

}
