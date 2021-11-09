package com.toucan.shopping.modules.category.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.toucan.shopping.modules.common.vo.bootstrap.State;
import com.toucan.shopping.modules.common.vo.layui.dtree.CheckArr;
import lombok.Data;

import java.util.List;

/**
 * 类别
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CategoryTreeVO extends CategoryVO {


    /**
     * 节点标题
     */
    private String title;

    /**
     * 节点名
     */
    private String text;


    /**
     * 上级菜单 -1表示当前是顶级
     */
    private Long pid;

    /**
     * 节点ID
     */
    private Long nodeId;

    /**
     * 实体ID
     */
    private Long entityId;

    /**
     * 标签
     */
    private List<String> tags;

    /**
     * 子节点
     */
    private List<CategoryTreeVO> nodes;


    /**
     * 节点属性
     */
    private CheckArr checkArr = new CheckArr();

    /**
     * 节点属性
     */
    private State state = new State();



    /**
     * 是否有子节点
     */
    private Boolean haveChild = false;

}
