package com.toucan.shopping.modules.order.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.ToStringSerializer;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 订单子表
 *
 * @author majian
 */
@Data
public class OrderItem {

    @JsonFormat(shape=JsonFormat.Shape.STRING)
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long id; //主键
    private String orderNo; //订单编号
    private String userId; //用户ID
    @JsonFormat(shape=JsonFormat.Shape.STRING)
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long orderId; //订单表ID
    private Long skuId; //商品SKUID
    private Integer deliveryStatus; //配送状态 0未收货 1送货中 2已收货
    private Integer sellerStatus; //卖家备货状态 0备货中 1备货完成 2缺货
    private Integer buyerStatus; //买家状态 0待收货 1已收货 2换货 3退货
    private Integer productNum; //购买商品数量
    private BigDecimal productPrice; //商品单价
    private BigDecimal productRoughWeight; //商品毛重
    private BigDecimal orderItemAmount; //订单单项总金额
    private String productPreviewPath; //商品主图路径
    private String productSkuName; //商品SKU名称


    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date deliveryReceiveTime; //收货时间

    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date deliveryFinishTime; //配送人员完成时间

    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date sellerFinishTime; //卖家完成时间

    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date buyerFinishTime; //买家完成时间

    private String remark; //订单备注 买家填写

    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date createDate; //创建时间
    private String appCode; //所属应用

    @JsonFormat(shape=JsonFormat.Shape.STRING)
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long freightTemplateId; //运费模板ID

    /**
     * 删除状态 0未删除 1已删除
     */
    private Short deleteStatus;
}
