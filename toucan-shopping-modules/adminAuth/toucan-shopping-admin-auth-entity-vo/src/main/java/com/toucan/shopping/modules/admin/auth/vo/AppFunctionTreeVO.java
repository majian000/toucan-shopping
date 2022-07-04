package com.toucan.shopping.modules.admin.auth.vo;

import com.toucan.shopping.modules.admin.auth.entity.Function;
import com.toucan.shopping.modules.common.vo.bootstrap.State;
import lombok.Data;

import java.util.List;

/**
 * 应用功能项
 */
@Data
public class AppFunctionTreeVO extends Function {


    /**
     * 节点标题
     */
    private String title;

    /**
     * 节点名
     */
    private String text;

    /**
     * 标签
     */
    private List<String> tags;

    /**
     * 子节点
     */
    private List<FunctionTreeVO> nodes;

    /**
     * 节点属性
     */
    private State state = new State();


    /**
     * 子节点
     */
    private List<FunctionTreeVO> children;

    /**
     * 是否有子节点
     */
    private Boolean haveChild = false;

    /**
     * 父节点ID
     */
    private Long parentId;


    /**
     * 是否是父节点
     */
    private Boolean isParent=true;

    /**
     * 是否展开
     */
    private Boolean open = false;

    /**
     * 是否是应用节点
     */
    private Boolean isAppNode = false;


}
