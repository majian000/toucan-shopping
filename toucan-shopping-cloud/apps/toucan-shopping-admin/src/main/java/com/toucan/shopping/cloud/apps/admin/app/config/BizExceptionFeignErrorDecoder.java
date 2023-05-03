package com.toucan.shopping.cloud.apps.admin.app.config;

import com.netflix.hystrix.exception.HystrixBadRequestException;
import feign.Response;
import feign.codec.ErrorDecoder;
import org.springframework.context.annotation.Configuration;

/**
 * 防止400触发熔断
 */
@Configuration
public class BizExceptionFeignErrorDecoder implements ErrorDecoder {

    @Override
    public Exception decode(String methodKey, Response response) {
        if(response.status() >= 400 && response.status() <= 499){
            return new HystrixBadRequestException("服务调用失败 status: "+response.status()+" methodKey:"+methodKey);
        }
        return feign.FeignException.errorStatus(methodKey, response);
    }
}
