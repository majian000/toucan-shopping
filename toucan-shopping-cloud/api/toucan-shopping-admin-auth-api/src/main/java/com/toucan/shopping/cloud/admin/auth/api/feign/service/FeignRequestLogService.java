package com.toucan.shopping.cloud.admin.auth.api.feign.service;

import com.toucan.shopping.cloud.admin.auth.api.feign.fallback.FeignAppServiceFallbackFactory;
import com.toucan.shopping.cloud.admin.auth.api.feign.fallback.FeignRequestServiceFallbackFactory;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(value = "toucan-shopping-gateway",path = "/toucan-shopping-admin-auth-proxy/requestLog",fallbackFactory = FeignRequestServiceFallbackFactory.class)
public interface FeignRequestLogService {


    /**
     * 批量保存
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/saves",produces = "application/json;charset=UTF-8")
    ResultObjectVO saves(@RequestBody RequestJsonVO requestJsonVO);

}
