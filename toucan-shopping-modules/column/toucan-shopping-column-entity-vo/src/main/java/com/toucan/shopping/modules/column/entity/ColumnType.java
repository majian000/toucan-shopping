package com.toucan.shopping.modules.column.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.ToStringSerializer;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 栏目类型
 *
 * @author majian
 */
@Data
public class ColumnType {

    @JsonFormat(shape=JsonFormat.Shape.STRING)
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long id; //主键

    /**
     * 消息类型编码
     */
    private String code;

    /**
     * 消息类型名称
     */
    private String name;

    /**
     * 所属应用 (软关联,直接存储应用编码符串)
     */
    private String appCode;

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

    private String createAdminId;

    private String updateAdminId;

    /**
     * 删除状态 0未删除 1已删除
     */
    private Short deleteStatus;

}
