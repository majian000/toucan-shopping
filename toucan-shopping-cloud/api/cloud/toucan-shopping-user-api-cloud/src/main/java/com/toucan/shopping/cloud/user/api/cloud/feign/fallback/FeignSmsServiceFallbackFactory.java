package com.toucan.shopping.cloud.user.api.cloud.feign.fallback;

import com.alibaba.fastjson.JSONObject;
import com.toucan.shopping.cloud.user.api.cloud.feign.service.FeignSmsService;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * 短信服务
 */
@Component
public class FeignSmsServiceFallbackFactory implements FallbackFactory<FeignSmsService> {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public FeignSmsService create(Throwable throwable) {
        logger.warn(throwable.getMessage(),throwable);
        return new FeignSmsService(){

            @Override
            public ResultObjectVO send(RequestJsonVO requestJsonVO) {
                ResultObjectVO resultObjectVO = new ResultObjectVO();
                if(requestJsonVO==null)
                {
                    resultObjectVO.setCode(ResultObjectVO.FAILD);
                    resultObjectVO.setMsg("请求超时,请稍后重试");
                    return resultObjectVO;
                }
                logger.warn("调用FeignSmsService.send失败 params{}",JSONObject.toJSONString(requestJsonVO));
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("请求超时,请稍后重试");
                return resultObjectVO;
            }
        };
    }
}
