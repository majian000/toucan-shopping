package com.toucan.shopping.modules.order.service;

import com.toucan.shopping.modules.common.page.PageInfo;
import com.toucan.shopping.modules.order.entity.OrderRefund;
import com.toucan.shopping.modules.order.page.OrderRefundPageInfo;
import com.toucan.shopping.modules.order.vo.OrderRefundVO;

public interface OrderRefundService {

    int save(OrderRefund orderRefund);

    /**
     * 查询列表页
     * @param pageInfo
     * @return
     */
    PageInfo<OrderRefundVO> queryOrderRefundListPage(OrderRefundPageInfo pageInfo);


}
