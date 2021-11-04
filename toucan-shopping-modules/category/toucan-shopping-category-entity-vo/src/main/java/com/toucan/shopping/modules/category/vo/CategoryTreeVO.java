package com.toucan.shopping.modules.category.vo;

import com.toucan.shopping.modules.common.vo.bootstrap.State;
import lombok.Data;

import java.util.List;

/**
 * 类别
 */
@Data
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
     * 节点ID
     */
    private Long nodeId;

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
    private State state = new State();



    /**
     * 是否有子节点
     */
    private Boolean haveChild = false;

}
