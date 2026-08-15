package com.toucan.shopping.cloud.order.api;

import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultPageInfoVO;
import com.toucan.shopping.modules.order.vo.OrderPayVO;

public interface OrderPayServiceAPI {

    /**
     * 查询列表页
     * @param requestJsonVO
     * @return
     */
    ResultPageInfoVO<OrderPayVO> queryListPage(RequestJsonVO requestJsonVO);

}
