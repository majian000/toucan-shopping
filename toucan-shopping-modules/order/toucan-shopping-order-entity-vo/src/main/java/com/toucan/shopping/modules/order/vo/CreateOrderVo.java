package com.toucan.shopping.modules.order.vo;

import com.toucan.shopping.modules.product.entity.ProductBuy;
import com.toucan.shopping.modules.product.entity.ProductSku;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class CreateOrderVo {

    private String appCode;
    private String userId;
    private String orderNo;
    private Integer payMethod; //支付方式 1线上支付 2线下支付

    private BuyVO buyVo; //下单对象

}
