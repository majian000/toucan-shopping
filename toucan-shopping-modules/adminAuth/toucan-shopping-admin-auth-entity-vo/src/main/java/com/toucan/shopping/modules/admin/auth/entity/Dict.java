package com.toucan.shopping.modules.admin.auth.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.ToStringSerializer;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 字典
 * 注意
 *      修改字典编码以及名称会生成历史快照，然后创建一个新的字典来设置为活动字典(用于回显历史数据)
 *      启用、禁用、删除，不会生成快照
 */
@Data
public class Dict {


    @JsonFormat(shape=JsonFormat.Shape.STRING)
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long id;

    private String name; //名称

    private String code; //编码

    private Long pid; //父级ID

    private Integer categoryId; //字典分类ID

    private Integer dictSort; //排序

    private String dictVersion; //版本号

    private String batchId; //批次ID

    private String extendProperty; //扩展属性

    /**
     * 启用状态 0:禁用 1启用
     */
    private Short enableStatus;

    /**
     * 是否活动版本(0否 1是)
     */
    private Short isActive;

    /**
     * 删除状态 0未删除 1已删除
     */
    private Short deleteStatus;

    private String remark;

    /**
     * 所属应用
     */
    private String appCode;

    /**
     * 创建时间
     */
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date createDate;


    /**
     * 创建人
     */
    private String createAdminId;



    /**
     * 修改时间
     */
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date updateDate;


    /**
     * 修改人
     */
    private String updateAdminId;

}
