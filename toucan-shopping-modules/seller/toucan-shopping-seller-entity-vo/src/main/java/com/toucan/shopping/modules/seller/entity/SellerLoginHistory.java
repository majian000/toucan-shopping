package com.toucan.shopping.modules.seller.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.ToStringSerializer;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 商户店铺登录历史
 */
@Data
public class SellerLoginHistory {
    /**
     * 主键 雪花算法生成
     */
    @JsonFormat(shape=JsonFormat.Shape.STRING)
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long id;

    /**
     * 所属用户ID,用该字段分库分表
     */
    @JsonFormat(shape=JsonFormat.Shape.STRING)
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long userMainId;

    /**
     * 登录IP地址
     */
    private String ip;


    /**
     * 登录源头 1:PC
     */
    private Integer loginSrcType;


    /**
     * 创建时间
     */
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date createDate;


    /**
     * 删除状态 0未删除 1已删除
     */
    private Short deleteStatus;

}
