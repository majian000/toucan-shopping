package com.toucan.shopping.cloud.message.api.feign.service;

import com.toucan.shopping.cloud.message.api.feign.fallback.FeignMessageServiceFallbackFactory;
import com.toucan.shopping.cloud.message.api.feign.fallback.FeignMessageTypeServiceFallbackFactory;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 消息服务
 */
@FeignClient(value = "toucan-shopping-gateway",path = "/toucan-shopping-message-proxy/message",fallbackFactory = FeignMessageServiceFallbackFactory.class)
public interface FeignMessageService {

    @RequestMapping(value="/send",produces = "application/json;charset=UTF-8")
    ResultObjectVO send(@RequestBody RequestJsonVO requestJsonVO);

}
