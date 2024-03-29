package com.toucan.shopping.modules.admin.auth.vo;

import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.ToStringSerializer;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.toucan.shopping.modules.common.vo.bootstrap.State;
import com.toucan.shopping.modules.common.vo.layui.dtree.CheckArr;
import lombok.Data;

import java.util.List;

/**
 * 字典树
 */
@Data
public class DictTreeVO extends DictVO {

    /**
     * 节点ID
     */
    private Long nodeId;

    /**
     * 上级节点ID
     */
    @JsonFormat(shape=JsonFormat.Shape.STRING)
    @JSONField(serializeUsing = ToStringSerializer.class)
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
    private List<DictTreeVO> nodes;



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
     * 是否是父节点
     */
    private Boolean isParent=true;

}
