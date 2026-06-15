package com.toucan.shopping.cloud.order.api.feign.fallback;

import com.alibaba.fastjson.JSONObject;
import com.toucan.shopping.cloud.order.api.feign.service.FeignOrderExpressDeliveryService;
import com.toucan.shopping.cloud.order.api.feign.service.FeignOrderService;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.common.vo.ResultTypeObjectVO;
import com.toucan.shopping.modules.order.entity.OrderExpressDelivery;
import com.toucan.shopping.modules.order.vo.OrderExpressDeliveryVO;
import org.springframework.cloud.openfeign.FallbackFactory;
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

            @Override
            public ResultObjectVO removeByOrderId(RequestJsonVO requestJsonVO) {
                ResultObjectVO resultObjectVO = new ResultObjectVO();
                if(requestJsonVO==null)
                {
                    resultObjectVO.setCode(ResultObjectVO.FAILD);
                    resultObjectVO.setMsg("请重试");
                    return resultObjectVO;
                }
                logger.warn("FeignOrderItemServiceFallbackFactory removeByOrderId  params{}:",JSONObject.toJSONString(requestJsonVO));
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("删除订单快递信息失败");
                return resultObjectVO;
            }

            @Override
            public ResultTypeObjectVO<OrderExpressDeliveryVO> findOneByOrderIdAndShopId(RequestJsonVO requestJsonVO) {
                ResultTypeObjectVO resultObjectVO = new ResultTypeObjectVO();
                if(requestJsonVO==null)
                {
                    resultObjectVO.setCode(ResultObjectVO.FAILD);
                    resultObjectVO.setMsg("请重试");
                    return resultObjectVO;
                }
                logger.warn("FeignOrderItemServiceFallbackFactory findOneByOrderIdAndShopId  params{}:",JSONObject.toJSONString(requestJsonVO));
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("查询订单快递信息失败");
                return resultObjectVO;
            }
        };
    }
}
