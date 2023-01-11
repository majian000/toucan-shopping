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

    private ConsigneeAddressVO consigneeAddress; //收货地址

    private MainOrderVO mainOrder; //主订单,包含了所有拆分订单后的总金额

    private Integer payType; //支付类型 1:微信 2:支付宝

    private String userId; //买家

    private Integer srcType = 1 ; //下单渠道 1:pc 2:app

}
