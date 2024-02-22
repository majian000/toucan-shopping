package com.toucan.shopping.modules.admin.auth.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.ToStringSerializer;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * 字典分类应用关联
 */
@Data
public class DictCategoryApp {


    @JsonFormat(shape=JsonFormat.Shape.STRING)
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Integer id;


    /**
     * 字典分类ID
     */
    private Integer dictCategoryId;


    /**
     * 所属应用
     */
    private String appCode;

    /**
     * 创建时间
     */
    private Date createDate;



    /**
     * 删除状态 0未删除 1已删除
     */
    private Short deleteStatus;

    /**
     * 创建人
     */
    private String createAdminId;


}