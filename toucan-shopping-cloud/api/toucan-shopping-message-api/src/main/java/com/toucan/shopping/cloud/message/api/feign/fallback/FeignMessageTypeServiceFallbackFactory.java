package com.toucan.shopping.cloud.message.api.feign.fallback;

import com.alibaba.fastjson.JSONObject;
import com.toucan.shopping.cloud.message.api.feign.service.FeignMessageTypeService;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import feign.hystrix.FallbackFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * 消息类型服务
 */
@Component
public class FeignMessageTypeServiceFallbackFactory implements FallbackFactory<FeignMessageTypeService> {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public FeignMessageTypeService create(Throwable throwable) {
        logger.warn(throwable.getMessage(),throwable);
        return new FeignMessageTypeService(){

        };
    }
}
