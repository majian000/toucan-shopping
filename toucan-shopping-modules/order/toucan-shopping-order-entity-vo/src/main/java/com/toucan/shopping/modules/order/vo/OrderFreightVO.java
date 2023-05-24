package com.toucan.shopping.modules.order.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 订单运费对象
 */
@Data
public class OrderFreightVO {

    private Short valuationMethod; //计价方式 1:按件数 2:按重量 3:按体积

    private String transportModel; //运送方式 1:快递 2:EMS 3:平邮

    private BigDecimal firstWeight; //首重量 单位KG/件

    private BigDecimal firstWeightMoney; //首重金额

    private BigDecimal appendWeight; //续重量 单位KG/件

    private BigDecimal appendWeightMoney; //续重金额
}
