package com.toucan.shopping.modules.order.service;

import com.toucan.shopping.modules.order.vo.OrderStatisticVO;
import com.toucan.shopping.modules.product.vo.ProductSkuStatisticVO;

import java.util.List;

/**
 * 订单统计
 * @author majian
 */
public interface OrderStatisticService {


    /**
     * 总金额
     * @return
     */
    OrderStatisticVO queryTotalAndTodayAndCurrentMonthAndCurrentYear();


}
