package com.toucan.shopping.cloud.message.api.feign.service;

import com.toucan.shopping.cloud.message.api.feign.fallback.FeignMessageTypeServiceFallbackFactory;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 消息类型服务
 */
@FeignClient(value = "toucan-shopping-gateway",path = "/toucan-shopping-message-proxy/messageType",fallbackFactory = FeignMessageTypeServiceFallbackFactory.class)
public interface FeignMessageTypeService {

    @RequestMapping(value="/save",produces = "application/json;charset=UTF-8")
    ResultObjectVO save(@RequestBody RequestJsonVO requestJsonVO);

}
