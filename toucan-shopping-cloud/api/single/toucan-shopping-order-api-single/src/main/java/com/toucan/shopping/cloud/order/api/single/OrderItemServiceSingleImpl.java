package com.toucan.shopping.cloud.order.api.single;

import com.toucan.shopping.cloud.order.api.feign.service.FeignOrderItemService;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.order.business.service.OrderItemBusinessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderItemServiceSingleImpl implements FeignOrderItemService {

    @Autowired
    private OrderItemBusinessService orderItemBusinessService;

    @Override
    public ResultObjectVO queryListPage(RequestJsonVO requestJsonVO) {
        return orderItemBusinessService.queryListPage(requestJsonVO);
    }

    @Override
    public ResultObjectVO queryAllListByOrderId(RequestJsonVO requestJsonVO) {
        return orderItemBusinessService.queryAllListByOrderId(requestJsonVO);
    }

    @Override
    public ResultObjectVO updatesFromOrderList(RequestJsonVO requestJsonVO) {
        return orderItemBusinessService.updatesFromOrderList(requestJsonVO);
    }
}
