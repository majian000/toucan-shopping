package com.toucan.shopping.cloud.order.api.single;

import com.toucan.shopping.cloud.order.api.feign.service.FeignOrderExpressDeliveryService;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.common.vo.ResultTypeObjectVO;
import com.toucan.shopping.modules.order.vo.OrderExpressDeliveryVO;
import org.springframework.stereotype.Service;

@Service
public class FeignOrderExpressDeliveryServiceSingleImpl implements FeignOrderExpressDeliveryService {

    @Override
    public ResultObjectVO saveOrUpdate(RequestJsonVO requestJsonVO) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        resultObjectVO.setCode(ResultObjectVO.FAILD);
        resultObjectVO.setMsg("请稍后重试!");
        return resultObjectVO;
    }

    @Override
    public ResultObjectVO removeByOrderId(RequestJsonVO requestJsonVO) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        resultObjectVO.setCode(ResultObjectVO.FAILD);
        resultObjectVO.setMsg("请稍后重试!");
        return resultObjectVO;
    }

    @Override
    public ResultTypeObjectVO<OrderExpressDeliveryVO> findOneByOrderIdAndShopId(RequestJsonVO requestJsonVO) {
        ResultTypeObjectVO resultObjectVO = new ResultTypeObjectVO();
        resultObjectVO.setCode(ResultObjectVO.FAILD);
        resultObjectVO.setMsg("请稍后重试!");
        return resultObjectVO;
    }
}
