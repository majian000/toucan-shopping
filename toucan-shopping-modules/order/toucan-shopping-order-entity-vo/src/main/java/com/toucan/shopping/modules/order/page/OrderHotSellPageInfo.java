package com.toucan.shopping.modules.order.page;

import com.toucan.shopping.modules.common.page.PageInfo;
import com.toucan.shopping.modules.order.vo.OrderHotSellStatisticVO;
import com.toucan.shopping.modules.product.vo.ProductSkuVO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

/**
 * 订单热卖列表查询页对象
 */
@Data
@AllArgsConstructor
@ToString(callSuper = true)
public class OrderHotSellPageInfo extends PageInfo<OrderHotSellStatisticVO> {


}
