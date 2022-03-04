package com.toucan.shopping.modules.category.vo;

import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.ToStringSerializer;
import com.fasterxml.jackson.annotation.JsonFormat;
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
    @JsonFormat(shape=JsonFormat.Shape.STRING)
    @JSONField(serializeUsing = ToStringSerializer.class)
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
    private CheckArr checkArr = new CheckArr();

    /**
     * 节点属性
     */
    private State state = new State();



    /**
     * 是否有子节点
     */
    private Boolean haveChild = false;


    /**
     * 是否展开节点
     */
    private Boolean open=true;

    /**
     * 是否是父节点
     */
    private Boolean isParent;


}
