package com.toucan.shopping.cloud.order.api;

import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;

public interface OrderItemServiceAPI {

    /**
     * 查询列表页
     * @param requestJsonVO
     * @return
     */
    ResultObjectVO queryListPage(RequestJsonVO requestJsonVO);

    /**
     * 查询所有订单项
     * @param requestJsonVO
     * @return
     */
    ResultObjectVO queryAllListByOrderId(RequestJsonVO requestJsonVO);

    /**
     * 修改订单项(从订单列表)
     * @param requestJsonVO
     * @return
     */
    ResultObjectVO updatesFromOrderList(RequestJsonVO requestJsonVO);

}
