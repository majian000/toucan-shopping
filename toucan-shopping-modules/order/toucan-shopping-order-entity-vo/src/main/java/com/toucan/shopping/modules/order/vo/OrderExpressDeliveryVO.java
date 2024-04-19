package com.toucan.shopping.modules.order.vo;

import com.toucan.shopping.modules.order.entity.OrderExpressDelivery;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 订单快递信息
 */
@Data
public class OrderExpressDeliveryVO extends OrderExpressDelivery {

    /**
     * 店铺ID
     */
    private Long shopId;


}
