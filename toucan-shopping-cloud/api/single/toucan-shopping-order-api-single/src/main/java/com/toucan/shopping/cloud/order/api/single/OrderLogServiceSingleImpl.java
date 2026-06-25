package com.toucan.shopping.cloud.order.api.single;

import com.toucan.shopping.cloud.order.api.feign.service.FeignOrderLogService;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultPageInfoVO;
import com.toucan.shopping.modules.order.business.service.OrderLogBusinessService;
import com.toucan.shopping.modules.order.vo.OrderLogVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderLogServiceSingleImpl implements FeignOrderLogService {

    @Autowired
    private OrderLogBusinessService orderLogBusinessService;

    @Override
    public ResultPageInfoVO<OrderLogVO> queryListPage(RequestJsonVO requestJsonVO) {
        return orderLogBusinessService.queryListPage(requestJsonVO);
    }
}
