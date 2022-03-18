package com.toucan.shopping.modules.product.vo;

import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.ToStringSerializer;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.toucan.shopping.modules.common.vo.bootstrap.State;
import com.toucan.shopping.modules.common.vo.layui.dtree.CheckArr;
import com.toucan.shopping.modules.product.entity.AttributeKey;
import lombok.Data;

import java.util.List;

/**
 * 属性键
 *
 * @author majian
 */
@Data
public class AttributeKeyVO extends AttributeKey {


    private String categoryName; //类别名称
    private String categoryPath; //分类路径

    private String createAdminName; //创建人ID
    private String updateAdminName; //修改人ID


    private Long[] idArray; //主键列表

    private List<Long> idList; //主键集合

    private String parentName; //上级节点名称

    /**
     * 可选值列表
     */
    private List<AttributeValueVO> values;


    /**
     * 子节点列表
     */
    private List<AttributeKeyVO> children;

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
    private List<AttributeKeyVO> nodes;


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
     * 属性路径
     */
    private String path;

}
