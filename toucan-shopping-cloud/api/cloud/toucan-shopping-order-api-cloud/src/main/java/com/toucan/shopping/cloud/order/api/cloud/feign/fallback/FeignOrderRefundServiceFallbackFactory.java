package com.toucan.shopping.cloud.order.api.cloud.feign.fallback;

import com.alibaba.fastjson2.JSONObject;
import com.toucan.shopping.cloud.order.api.cloud.feign.service.FeignOrderRefundService;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.common.vo.ResultPageInfoVO;
import com.toucan.shopping.modules.order.vo.OrderRefundVO;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * 订单退款流水
 */
@Component
public class FeignOrderRefundServiceFallbackFactory implements FallbackFactory<FeignOrderRefundService> {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public FeignOrderRefundService create(Throwable throwable) {

        logger.warn(throwable.getMessage(),throwable);
        return new FeignOrderRefundService(){

            @Override
            public ResultPageInfoVO<OrderRefundVO> queryListPage(RequestJsonVO requestJsonVO) {
                ResultPageInfoVO<OrderRefundVO> resultObjectVO = new ResultPageInfoVO<OrderRefundVO>();
                if(requestJsonVO==null)
                {
                    resultObjectVO.setCode(ResultObjectVO.FAILD);
                    resultObjectVO.setMsg("请重试");
                    return resultObjectVO;
                }
                logger.warn("FeignOrderRefundService queryListPage  params{}:",JSONObject.toJSONString(requestJsonVO));
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("查询订单退款流水失败");
                return resultObjectVO;
            }
        };
    }
}
