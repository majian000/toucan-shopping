package com.toucan.shopping.cloud.order.api.feign.fallback;

import com.alibaba.fastjson.JSONObject;
import com.toucan.shopping.cloud.order.api.feign.service.FeignOrderExpressDeliveryService;
import com.toucan.shopping.cloud.order.api.feign.service.FeignOrderService;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import feign.hystrix.FallbackFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * 订单快递信息服务
 */
@Component
public class FeignOrderExpressDeliveryServiceFallbackFactory implements FallbackFactory<FeignOrderExpressDeliveryService> {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public FeignOrderExpressDeliveryService create(Throwable throwable) {

        logger.warn(throwable.getMessage(),throwable);
        return new FeignOrderExpressDeliveryService(){

            @Override
            public ResultObjectVO saveOrUpdate(RequestJsonVO requestJsonVO) {
                ResultObjectVO resultObjectVO = new ResultObjectVO();
                if(requestJsonVO==null)
                {
                    resultObjectVO.setCode(ResultObjectVO.FAILD);
                    resultObjectVO.setMsg("请重试");
                    return resultObjectVO;
                }
                logger.warn("FeignOrderItemServiceFallbackFactory saveOrUpdate  params{}:",JSONObject.toJSONString(requestJsonVO));
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("保存订单快递信息失败");
                return resultObjectVO;
            }
        };
    }
}
