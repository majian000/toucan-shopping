package com.toucan.shopping.cloud.order.api.single;

import com.toucan.shopping.cloud.order.api.feign.service.FeignOrderService;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.common.vo.ResultTypeObjectVO;
import com.toucan.shopping.modules.order.business.service.OrderBusinessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderServiceSingleImpl implements FeignOrderService {

    @Autowired
    private OrderBusinessService orderBusinessService;

    @Override
    public ResultObjectVO querySkuUuidsByOrderNo(String signHeader, RequestJsonVO requestJsonVO) {
        return orderBusinessService.querySkuUuidsByOrderNo(requestJsonVO);
    }

    @Override
    public ResultObjectVO finish(String signHeader, RequestJsonVO requestJsonVO) {
        return orderBusinessService.finish(requestJsonVO);
    }

    @Override
    public ResultObjectVO queryOrderByPayTimeOut(String signHeader, RequestJsonVO requestJsonVO) {
        return orderBusinessService.queryOrderByPayTimeOut(requestJsonVO);
    }

    @Override
    public ResultObjectVO queryListPage(RequestJsonVO requestJsonVO) {
        return orderBusinessService.queryListPage(requestJsonVO);
    }

    @Override
    public ResultObjectVO queryByOrderNoAndUserId(RequestJsonVO requestJsonVO) {
        return orderBusinessService.queryByOrderNoAndUserId(requestJsonVO);
    }

    @Override
    public ResultObjectVO findById(RequestJsonVO requestJsonVO) {
        return orderBusinessService.findById(requestJsonVO);
    }

    @Override
    public ResultObjectVO cancel(RequestJsonVO requestJsonVO) {
        return orderBusinessService.cancel(requestJsonVO);
    }

    @Override
    public ResultObjectVO update(RequestJsonVO requestJsonVO) {
        return orderBusinessService.update(requestJsonVO);
    }

    @Override
    public ResultTypeObjectVO<Long> queryFinishCountByShopId(RequestJsonVO requestJsonVO) {
        return orderBusinessService.queryFinishCountByShopId(requestJsonVO);
    }
}
