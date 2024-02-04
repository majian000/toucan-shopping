package com.toucan.shopping.modules.order.entity;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.ToStringSerializer;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.toucan.shopping.modules.order.vo.OrderLogDataBodyVO;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

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

    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date shardingDate; //分片日期

    private String dataBody; //数据主体

    private String remark; //日志备注

    private String batchId; //操作批次ID

    private String appCode; //所属应用

    /**
     * 删除状态 0未删除 1已删除
     */
    private Short deleteStatus;

    private OrderLogDataBodyVO orderLogDataBody = new OrderLogDataBodyVO();

    public OrderLog loadOldData(Object oldData){
        this.orderLogDataBody.setOldData(oldData);
        return this;
    }


    public OrderLog loadUpdateData(Object updateData){
        this.orderLogDataBody.setUpdateData(updateData);
        return this;
    }


    public OrderLog setDataBodyType(Integer type){
        this.orderLogDataBody.setType(type);
        return this;
    }

    public void loadDataBody()
    {
        this.setDataBody(JSONObject.toJSONString(this.orderLogDataBody));
    }

}
