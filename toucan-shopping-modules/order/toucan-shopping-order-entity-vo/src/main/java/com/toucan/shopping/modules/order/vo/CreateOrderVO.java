package com.toucan.shopping.modules.order.vo;

import com.toucan.shopping.modules.order.entity.Order;
import com.toucan.shopping.modules.user.vo.ConsigneeAddressVO;
import com.toucan.shopping.modules.user.vo.UserBuyCarItemVO;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 创建订单对象
 *
 * @author majian
 */
@Data
public class CreateOrderVO {

    private List<UserBuyCarItemVO> buyCarItems; //购物车中的所有项


    private BigDecimal moneyTotal;  //总金额

    private ConsigneeAddressVO consigneeAddress; //收货地址


    private List<OrderVO> orders; //所有拆分后的订单(根据店铺、运费模板拆分订单)

}
