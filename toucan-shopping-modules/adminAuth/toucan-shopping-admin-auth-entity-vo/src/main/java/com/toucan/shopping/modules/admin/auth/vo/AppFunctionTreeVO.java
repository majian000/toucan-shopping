package com.toucan.shopping.modules.admin.auth.vo;

import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.ToStringSerializer;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.toucan.shopping.modules.admin.auth.entity.Function;
import com.toucan.shopping.modules.common.vo.bootstrap.State;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

/**
 * 应用功能项
 */
@Data
public class AppFunctionTreeVO {


    private String id;

    /**
     * 名称
     */
    private String name;

    /**
     * 功能项ID
     */
    private String functionId;

    /**
     * 连接
     */
    private String url;

    /**
     * 权限标识
     */
    private String permission;


    /**
     * 功能项类型 0目录 1菜单 2按钮 3工具条按钮 4:API 5页面控件
     */
    private Short type;

    /**
     * 功能内容
     */
    private String functionText;

    /**
     * 上级菜单 -1表示当前是顶级
     */
    private Long pid;

    /**
     * 启用状态 0:禁用 1启用
     */
    private Short enableStatus;

    /**
     * 图标
     */
    private String icon;

    /**
     * 创建时间
     */
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date createDate;

    /**
     * 修改时间
     */
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date updateDate;

    /**
     * 备注
     */
    private String remark;


    /**
     * 所属应用
     */
    private String appCode;

    /**
     * 排序
     */
    private Long functionSort;



    /**
     * 删除状态 0未删除 1已删除
     */
    private Short deleteStatus;

    /**
     * 创建人
     */
    private String createAdminId;


    /**
     * 修改人
     */
    private String updateAdminId;


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
