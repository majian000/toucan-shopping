package com.toucan.shopping.cloud.order.api;

import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.common.vo.ResultTypeObjectVO;
import com.toucan.shopping.modules.order.vo.OrderExpressDeliveryVO;

public interface OrderExpressDeliveryServiceAPI {

    /**
     * 保存或修改订单快递信息
     * @param requestJsonVO
     * @return
     */
    ResultObjectVO saveOrUpdate(RequestJsonVO requestJsonVO);

    /**
     * 根据订单ID删除
     * @param requestJsonVO
     * @return
     */
    ResultObjectVO removeByOrderId(RequestJsonVO requestJsonVO);

    /**
     * 根据订单ID和店铺ID查询
     * @param requestJsonVO
     * @return
     */
    ResultTypeObjectVO<OrderExpressDeliveryVO> findOneByOrderIdAndShopId(RequestJsonVO requestJsonVO);

}
