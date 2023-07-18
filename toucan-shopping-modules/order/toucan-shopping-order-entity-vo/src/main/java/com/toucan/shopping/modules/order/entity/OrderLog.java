package com.toucan.shopping.modules.order.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.ToStringSerializer;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 子订单日志
 *
 * @author majian
 */
@Data
public class OrderLog {

    @JsonFormat(shape=JsonFormat.Shape.STRING)
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long id; //主键
    private String orderNo; //订单编号
    private String operateUserId; //用户ID

    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date createDate; //创建时间

    private String remark; //取消订单备注

    private String appCode; //所属应用

    /**
     * 删除状态 0未删除 1已删除
     */
    private Short deleteStatus;
}
