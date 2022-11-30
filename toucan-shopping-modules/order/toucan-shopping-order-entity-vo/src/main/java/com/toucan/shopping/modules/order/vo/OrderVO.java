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

    private UBCIFreightTemplateVO freightTemplate; //运费模板

}
