package com.toucan.shopping.modules.order.vo;

import com.toucan.shopping.modules.order.entity.Order;
import com.toucan.shopping.modules.user.vo.UserBuyCarItemVO;
import lombok.Data;

import java.util.List;


/**
 * 订单对象
 */
@Data
public class OrderVO extends Order {

    List<UserBuyCarItemVO> buyCarItems; //这个订单购买的商品

}
