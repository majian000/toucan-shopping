package com.toucan.shopping.modules.order.vo;


import com.alibaba.fastjson2.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 订单热销统计
 * @author majian
 */
@Data
public class OrderHotSellStatisticVO {


    @JsonFormat(shape=JsonFormat.Shape.STRING)
    private Long skuId; //主键

    private String productName; //商品名称

    @JsonFormat(shape=JsonFormat.Shape.STRING)
    private Long shopId; //店铺ID


    /**
     * 销售数量
     */
    @JsonFormat(shape=JsonFormat.Shape.STRING)
    private Long sellCount;

    /**
     * 销售总金额
     */
    @JsonFormat(shape=JsonFormat.Shape.STRING)
    private BigDecimal sellTotal;



}
