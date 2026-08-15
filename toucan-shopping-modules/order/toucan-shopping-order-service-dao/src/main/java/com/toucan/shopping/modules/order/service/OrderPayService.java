package com.toucan.shopping.modules.order.service;

import com.toucan.shopping.modules.common.page.PageInfo;
import com.toucan.shopping.modules.order.entity.OrderPay;
import com.toucan.shopping.modules.order.page.OrderPayPageInfo;
import com.toucan.shopping.modules.order.vo.OrderPayVO;

public interface OrderPayService {

    int save(OrderPay orderPay);

    /**
     * 查询列表页
     * @param pageInfo
     * @return
     */
    PageInfo<OrderPayVO> queryOrderPayListPage(OrderPayPageInfo pageInfo);


}
