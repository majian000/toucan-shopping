package com.toucan.shopping.cloud.order.api.feign.fallback;

import com.alibaba.fastjson.JSONObject;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.cloud.order.api.feign.service.FeignOrderService;
import feign.hystrix.FallbackFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * 订单服务
 */
@Component
public class FeignOrderServiceFallbackFactory implements FallbackFactory<FeignOrderService> {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public FeignOrderService create(Throwable throwable) {

        logger.warn(throwable.getMessage(),throwable);
        return new FeignOrderService(){

            @Override
            public ResultObjectVO create( String signHeader,RequestJsonVO requestJsonVO) {
                ResultObjectVO resultObjectVO = new ResultObjectVO();
                if(requestJsonVO==null)
                {
                    resultObjectVO.setCode(ResultObjectVO.FAILD);
                    resultObjectVO.setMsg("请重试");
                    return resultObjectVO;
                }
                logger.warn("FeignOrderServiceFallbackFactory create header {}  params{}:",signHeader,JSONObject.toJSONString(requestJsonVO));
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("订单创建失败");
                return resultObjectVO;
            }

            @Override
            public ResultObjectVO querySkuUuidsByOrderNo(String signHeader,RequestJsonVO requestJsonVO) {
                ResultObjectVO resultObjectVO = new ResultObjectVO();
                if(requestJsonVO==null)
                {
                    resultObjectVO.setCode(ResultObjectVO.FAILD);
                    resultObjectVO.setMsg("请重试");
                    return resultObjectVO;
                }
                logger.warn("FeignOrderServiceFallbackFactory queryskuIdsByOrderNo  header {}  params{}:",signHeader,JSONObject.toJSONString(requestJsonVO));
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("子订单查询失败");
                return resultObjectVO;
            }

            @Override
            public ResultObjectVO finish(String signHeader,RequestJsonVO requestJsonVO) {
                ResultObjectVO resultObjectVO = new ResultObjectVO();
                if(requestJsonVO==null)
                {
                    resultObjectVO.setCode(ResultObjectVO.FAILD);
                    resultObjectVO.setMsg("请重试");
                    return resultObjectVO;
                }
                logger.warn("FeignOrderServiceFallbackFactory finish  header {}  params{}:",signHeader,JSONObject.toJSONString(requestJsonVO));
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("完成订单失败");
                return resultObjectVO;
            }

            @Override
            public ResultObjectVO cancel(String signHeader,RequestJsonVO requestJsonVO) {
                ResultObjectVO resultObjectVO = new ResultObjectVO();
                if(requestJsonVO==null)
                {
                    resultObjectVO.setCode(ResultObjectVO.FAILD);
                    resultObjectVO.setMsg("请重试");
                    return resultObjectVO;
                }
                logger.warn("FeignOrderServiceFallbackFactory finish  header {}  params{}:",signHeader,JSONObject.toJSONString(requestJsonVO));
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("取消订单失败");
                return resultObjectVO;
            }

            @Override
            public ResultObjectVO queryOrderByPayTimeOut(String signHeader,RequestJsonVO requestJsonVO) {
                ResultObjectVO resultObjectVO = new ResultObjectVO();
                if(requestJsonVO==null)
                {
                    resultObjectVO.setCode(ResultObjectVO.FAILD);
                    resultObjectVO.setMsg("请重试");
                    return resultObjectVO;
                }
                logger.warn("FeignOrderServiceFallbackFactory queryOrderByPayTimeOut header {}  params{}:",signHeader,JSONObject.toJSONString(requestJsonVO));
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("查询支付超时订单失败");
                return resultObjectVO;
            }
        };
    }
}
