package com.toucan.shopping.cloud.order.api.single;

import com.toucan.shopping.cloud.order.api.OrderRefundServiceAPI;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultPageInfoVO;
import com.toucan.shopping.modules.order.business.service.OrderRefundBusinessService;
import com.toucan.shopping.modules.order.vo.OrderRefundVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderRefundServiceSingleImpl implements OrderRefundServiceAPI {

    @Autowired
    private OrderRefundBusinessService orderRefundBusinessService;

    @Override
    public ResultPageInfoVO<OrderRefundVO> queryListPage(RequestJsonVO requestJsonVO) {
        return orderRefundBusinessService.queryListPage(requestJsonVO);
    }
}
