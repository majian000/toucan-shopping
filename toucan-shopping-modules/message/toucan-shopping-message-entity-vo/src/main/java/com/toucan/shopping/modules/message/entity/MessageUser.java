package com.toucan.shopping.modules.message.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.ToStringSerializer;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 消息用户 实体
 *
 * @author majian
 */
@Data
public class MessageUser {


    @JsonFormat(shape=JsonFormat.Shape.STRING)
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long id; //主键

    /**
     * 消息主体ID
     */
    private Long messageBodyId;


    /**
     * 接收用户ID
     */
    private Long userMainId;

    /**
     * 发送时间
     */
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date sendDate;

    /**
     * 是否读取 0:未读 1:已读
     */
    private Integer status;


    /**
     * 消息类型ID(用于消息历史)
     */
    private Long messageTypeId;

    /**
     * 消息类型编码(用于消息历史)
     */
    private String messageTypeCode;

    /**
     * 消息类型名称(用于消息历史)
     */
    private String messageTypeName;


    /**
     * 所属应用 (软关联,直接存储应用编码符串)
     */
    private String messageTypeAppCode;

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
     * 删除状态 0未删除 1已删除
     */
    private Short deleteStatus;
}
