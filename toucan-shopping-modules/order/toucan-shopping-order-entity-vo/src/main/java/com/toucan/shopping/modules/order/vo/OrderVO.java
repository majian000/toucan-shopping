package com.toucan.shopping.modules.order.vo;

import com.toucan.shopping.modules.order.entity.Order;
import com.toucan.shopping.modules.user.vo.UserBuyCarItemVO;
import com.toucan.shopping.modules.user.vo.freightTemplate.UBCIFreightTemplateVO;
import jdk.nashorn.internal.objects.annotations.Constructor;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


/**
 * 订单对象
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderVO extends Order {

    List<UserBuyCarItemVO> buyCarItems; //这个订单购买的商品

    OrderFreightVO orderFreight; //订单运费规则

    List<OrderItemVO> orderItems; //订单项

    OrderConsigneeAddressVO orderConsigneeAddress; //收货人

    Long createDateLong; //订单创建时间

    Long systemDateLong; //系统时间

    Long timeRemaining; //剩余时间=系统时间-订单创建时间

    Long maxPayTime; //最大允许支付的时间

}
