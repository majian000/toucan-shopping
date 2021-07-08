package com.toucan.shopping.modules.user.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.ToStringSerializer;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 用户实名审核
 */
@Data
public class UserTrueNameApprove {


    /**
     * 主键 雪花算法生成
     */
    @JsonFormat(shape=JsonFormat.Shape.STRING)
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long id;

    /**
     * 用户ID,用该字段分库分表
     */
    @JsonFormat(shape=JsonFormat.Shape.STRING)
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long userMainId;


    /**
     * 真实姓名
     */
    private String trueName;


    /**
     * 身份证
     */
    private String idCard;


    /**
     * 身份证照片正面
     */
    private String idcardImg1;

    /**
     * 身份证照片背面
     */
    private String idcardImg2;

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
    private Date createDate;



    /**
     * 删除状态 0未删除 1已删除
     */
    private Short deleteStatus;



}
