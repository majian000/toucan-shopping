package com.toucan.shopping.modules.admin.auth.vo;

import com.toucan.shopping.modules.admin.auth.entity.Function;
import com.toucan.shopping.modules.common.vo.bootstrap.State;
import com.toucan.shopping.modules.common.vo.layui.dtree.CheckArr;
import lombok.Data;

import java.util.List;

/**
 * 功能项
 */
@Data
public class FunctionTreeVO extends Function {

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
     * 节点属性
     */
    private State state = new State();

    /**
     * 节点属性
     */
    private CheckArr checkArr = new CheckArr();

    /**
     * 标签
     */
    private List<String> tags;

    /**
     * 子节点
     */
    private List<FunctionTreeVO> children;

    /**
     * 子节点
     */
    private List<FunctionTreeVO> nodes;

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
    private Boolean checked = false;

    /**
     * 是否支持展开
     */
    private Boolean open = true;

    /**
     * 是否是父节点
     */
    private Boolean isParent=true;

    /**
     * 是否是半勾选 true:该节点半勾选 false:自动计算勾选(全选)
     */
    private Boolean halfCheck;


    /**
     * 是否是应用节点
     */
    private Boolean isAppNode = false;

    /**
     * 子孙节点总数
     */
    private Integer descendantCount = 0;

    /**
     * 已勾选的子孙节点数
     */
    private Integer checkedDescendantCount = 0;

    /**
     * 是否级联模式（已勾选所有子孙）
     */
    private Boolean cascaded = false;

}
