package com.toucan.shopping.modules.order.vo;

import com.toucan.shopping.modules.order.entity.Order;
import com.toucan.shopping.modules.order.entity.OrderItem;
import com.toucan.shopping.modules.user.vo.UserBuyCarItemVO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


/**
 * 订单项对象
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemVO extends OrderItem {

    private String httpProductPreviewPath; //商品主图HTTP路径

    String operateUserId; //操作用户

}
