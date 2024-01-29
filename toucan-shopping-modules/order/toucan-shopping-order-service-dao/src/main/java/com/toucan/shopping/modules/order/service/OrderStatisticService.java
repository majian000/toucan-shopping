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
     * 总数
     * 今日完成 / 本月完成 / 本年完成 / 完成数
     * 今日取消 / 本月取消 / 本年取消 / 取消数
     * @return
     */
    OrderStatisticVO queryTotalAndTodayAndCurrentMonthAndCurrentYear();


}
