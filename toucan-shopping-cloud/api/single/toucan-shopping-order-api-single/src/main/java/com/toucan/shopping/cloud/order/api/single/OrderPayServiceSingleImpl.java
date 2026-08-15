package com.toucan.shopping.cloud.order.api.single;

import com.toucan.shopping.cloud.order.api.OrderPayServiceAPI;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultPageInfoVO;
import com.toucan.shopping.modules.order.business.service.OrderPayBusinessService;
import com.toucan.shopping.modules.order.vo.OrderPayVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderPayServiceSingleImpl implements OrderPayServiceAPI {

    @Autowired
    private OrderPayBusinessService orderPayBusinessService;

    @Override
    public ResultPageInfoVO<OrderPayVO> queryListPage(RequestJsonVO requestJsonVO) {
        return orderPayBusinessService.queryListPage(requestJsonVO);
    }
}
