package com.toucan.shopping.cloud.order.api;

import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.common.vo.ResultTypeObjectVO;

public interface OrderServiceAPI {

    /**
     * 查询订单下所有skuuuid
     * @param requestJsonVO
     * @return
     */
    ResultObjectVO querySkuUuidsByOrderNo(RequestJsonVO requestJsonVO);

    /**
     * 完成订单
     * @param requestJsonVO
     * @return
     */
    ResultObjectVO finish(RequestJsonVO requestJsonVO);

    /**
     * 查询支付超时的订单
     * @param requestJsonVO
     * @return
     */
    ResultObjectVO queryOrderByPayTimeOut(RequestJsonVO requestJsonVO);

    /**
     * 查询列表页
     * @param requestJsonVO
     * @return
     */
    ResultObjectVO queryListPage(RequestJsonVO requestJsonVO);

    /**
     * 查询子订单
     * @param requestJsonVO
     * @return
     */
    ResultObjectVO queryByOrderNoAndUserId(RequestJsonVO requestJsonVO);

    /**
     * 根据ID查询
     * @param requestJsonVO
     * @return
     */
    ResultObjectVO findById(RequestJsonVO requestJsonVO);

    /**
     * 取消订单
     * @param requestJsonVO
     * @return
     */
    ResultObjectVO cancel(RequestJsonVO requestJsonVO);

    /**
     * 更新订单
     * @param requestJsonVO
     * @return
     */
    ResultObjectVO update(RequestJsonVO requestJsonVO);

    /**
     * 查询已完成订单数量
     * @param requestJsonVO
     * @return
     */
    ResultTypeObjectVO<Long> queryFinishCountByShopId(RequestJsonVO requestJsonVO);

}
