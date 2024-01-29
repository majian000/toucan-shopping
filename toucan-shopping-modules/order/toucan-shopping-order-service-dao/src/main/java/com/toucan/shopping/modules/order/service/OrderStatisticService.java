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
     * 查询 总数/今日新增/本月新增/本年新增
     * @return
     */
    OrderStatisticVO queryTotalAndTodayAndCurrentMonthAndCurrentYear();


}
