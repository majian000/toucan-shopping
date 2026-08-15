package com.toucan.shopping.cloud.order.api;

import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultPageInfoVO;
import com.toucan.shopping.modules.order.vo.OrderRefundVO;

public interface OrderRefundServiceAPI {

    /**
     * 查询列表页
     * @param requestJsonVO
     * @return
     */
    ResultPageInfoVO<OrderRefundVO> queryListPage(RequestJsonVO requestJsonVO);

}
