package com.toucan.shopping.cloud.order.api.single;

import com.toucan.shopping.cloud.order.api.OrderExpressDeliveryServiceAPI;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.common.vo.ResultTypeObjectVO;
import com.toucan.shopping.modules.order.business.service.OrderExpressDeliveryBusinessService;
import com.toucan.shopping.modules.order.vo.OrderExpressDeliveryVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderExpressDeliveryServiceSingleImpl implements OrderExpressDeliveryServiceAPI {

    @Autowired
    private OrderExpressDeliveryBusinessService orderExpressDeliveryBusinessService;

    @Override
    public ResultObjectVO saveOrUpdate(RequestJsonVO requestJsonVO) {
        return orderExpressDeliveryBusinessService.saveOrUpdate(requestJsonVO);
    }

    @Override
    public ResultObjectVO removeByOrderId(RequestJsonVO requestJsonVO) {
        return orderExpressDeliveryBusinessService.removeByOrderId(requestJsonVO);
    }

    @Override
    public ResultTypeObjectVO<OrderExpressDeliveryVO> findOneByOrderIdAndShopId(RequestJsonVO requestJsonVO) {
        return orderExpressDeliveryBusinessService.findOneByOrderIdAndShopId(requestJsonVO);
    }
}
