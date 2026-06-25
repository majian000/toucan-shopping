package com.toucan.shopping.cloud.order.api;

import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;

public interface MainOrderServiceAPI {

    /**
     * 创建订单
     * @param requestJsonVO
     * @return
     */
    ResultObjectVO create(RequestJsonVO requestJsonVO);

    /**
     * 取消订单
     * @param requestJsonVO
     * @return
     */
    ResultObjectVO cancel(RequestJsonVO requestJsonVO);

    /**
     * 查询支付超时的订单
     * @param requestJsonVO
     * @return
     */
    ResultObjectVO queryOrderByPayTimeOut(RequestJsonVO requestJsonVO);

    /**
     * 查询主订单
     * @param requestJsonVO
     * @return
     */
    ResultObjectVO queryMainOrderByOrderNoAndUserId(RequestJsonVO requestJsonVO);

    /**
     * 查询支付超时订单页
     * @param requestJsonVO
     * @return
     */
    ResultObjectVO queryOrderByPayTimeOutPage(RequestJsonVO requestJsonVO);

    /**
     * 批量取消支付超时订单
     * @param requestJsonVO
     * @return
     */
    ResultObjectVO batchCancelPayTimeout(RequestJsonVO requestJsonVO);

}
