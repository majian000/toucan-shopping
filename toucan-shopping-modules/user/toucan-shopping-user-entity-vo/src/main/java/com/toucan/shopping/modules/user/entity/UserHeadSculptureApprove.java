package com.toucan.shopping.modules.user.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.ToStringSerializer;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 用户头像审核
 */
@Data
public class UserHeadSculptureApprove {

    /**
     * 主键
     */
    @JsonFormat(shape=JsonFormat.Shape.STRING)
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long id;

    /**
     * 头像
     */
    private String headSculpture;


    /**
     * 用户ID,用该字段分库分表
     */
    @JsonFormat(shape=JsonFormat.Shape.STRING)
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long userMainId;

    /**
     * 审核状态 1审核中 2审核通过 3审核驳回
     */
    private Integer approveStatus;


    /**
     * 驳回原因
     */
    private String rejectText;

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
