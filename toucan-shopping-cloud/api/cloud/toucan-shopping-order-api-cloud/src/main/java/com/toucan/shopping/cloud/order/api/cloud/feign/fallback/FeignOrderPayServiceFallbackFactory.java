package com.toucan.shopping.cloud.order.api.cloud.feign.fallback;

import com.alibaba.fastjson2.JSONObject;
import com.toucan.shopping.cloud.order.api.cloud.feign.service.FeignOrderPayService;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.common.vo.ResultPageInfoVO;
import com.toucan.shopping.modules.order.vo.OrderPayVO;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * 订单支付流水
 */
@Component
public class FeignOrderPayServiceFallbackFactory implements FallbackFactory<FeignOrderPayService> {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public FeignOrderPayService create(Throwable throwable) {

        logger.warn(throwable.getMessage(),throwable);
        return new FeignOrderPayService(){

            @Override
            public ResultPageInfoVO<OrderPayVO> queryListPage(RequestJsonVO requestJsonVO) {
                ResultPageInfoVO<OrderPayVO> resultObjectVO = new ResultPageInfoVO<OrderPayVO>();
                if(requestJsonVO==null)
                {
                    resultObjectVO.setCode(ResultObjectVO.FAILD);
                    resultObjectVO.setMsg("请重试");
                    return resultObjectVO;
                }
                logger.warn("FeignOrderPayService queryListPage  params{}:",JSONObject.toJSONString(requestJsonVO));
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("查询订单支付流水失败");
                return resultObjectVO;
            }
        };
    }
}
