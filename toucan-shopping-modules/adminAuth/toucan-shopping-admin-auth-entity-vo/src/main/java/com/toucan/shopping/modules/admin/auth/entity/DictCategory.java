package com.toucan.shopping.modules.admin.auth.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.ToStringSerializer;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 字典分类
 */
@Data
public class DictCategory {


    @JsonFormat(shape=JsonFormat.Shape.STRING)
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Integer id;

    private String name; //名称

    private String code; //编码

    private Integer dictCategorySort; //排序

    /**
     * 启用状态 0:禁用 1启用
     */
    private Short enableStatus;

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
