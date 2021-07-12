package com.toucan.shopping.modules.message.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.ToStringSerializer;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * 站内信
 *
 * @author majian
 */
@Data
public class InternalMessage {


    @JsonFormat(shape=JsonFormat.Shape.STRING)
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long id; //主键

    private Date createDate; //创建时间

    /**
     * 删除状态 0未删除 1已删除
     */
    private Short deleteStatus;
}
