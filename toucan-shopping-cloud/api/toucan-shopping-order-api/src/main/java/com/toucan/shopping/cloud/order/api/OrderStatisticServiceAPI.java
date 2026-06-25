package com.toucan.shopping.cloud.order.api;

import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;

public interface OrderStatisticServiceAPI {

    /**
     * 总金额
     * @param requestVo
     * @return
     */
    ResultObjectVO queryTotalAndTodayAndCurrentMonthAndCurrentYear(RequestJsonVO requestVo);

    /**
     * 查询热销列表
     * @param requestJsonVO
     * @return
     */
    ResultObjectVO queryHotSellListPage(RequestJsonVO requestJsonVO);

}
