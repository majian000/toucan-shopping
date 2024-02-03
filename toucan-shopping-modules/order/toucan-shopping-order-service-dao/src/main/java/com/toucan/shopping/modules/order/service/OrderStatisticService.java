package com.toucan.shopping.modules.order.service;

import com.toucan.shopping.modules.common.page.PageInfo;
import com.toucan.shopping.modules.order.page.OrderHotSellPageInfo;
import com.toucan.shopping.modules.order.vo.OrderHotSellStatisticVO;
import com.toucan.shopping.modules.order.vo.OrderStatisticVO;

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


    /**
     * 查询热销列表页
     * @param queryPageInfo
     * @return
     */
    PageInfo<OrderHotSellStatisticVO> queryHotSellListPage(OrderHotSellPageInfo queryPageInfo);

}
