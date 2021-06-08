package com.toucan.shopping.modules.area.vo;

import com.toucan.shopping.modules.common.vo.bootstrap.State;
import lombok.Data;

import java.util.List;

/**
 * 应用功能项
 */
@Data
public class AreaTreeVO extends AreaVO {

    /**
     * 节点ID
     */
    private Long nodeId;

    /**
     * 上级节点ID
     */
    private Long parentId;

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
    private List<AreaTreeVO> nodes;



    /**
     * 节点属性
     */
    private State state = new State();



}
