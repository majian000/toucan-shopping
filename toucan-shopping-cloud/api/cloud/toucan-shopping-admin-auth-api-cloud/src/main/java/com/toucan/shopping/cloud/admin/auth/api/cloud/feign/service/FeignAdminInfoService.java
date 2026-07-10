package com.toucan.shopping.cloud.admin.auth.api.cloud.feign.service;

import com.toucan.shopping.cloud.admin.auth.api.AdminInfoServiceAPI;
import com.toucan.shopping.cloud.admin.auth.api.cloud.feign.fallback.FeignAdminInfoServiceFallbackFactory;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(value = "toucan-shopping-gateway", path = "/toucan-shopping-admin-auth-proxy/adminInfo", fallbackFactory = FeignAdminInfoServiceFallbackFactory.class)
public interface FeignAdminInfoService extends AdminInfoServiceAPI {

    @RequestMapping(value = "/saveOrUpdate", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    ResultObjectVO saveOrUpdate(@RequestBody RequestJsonVO requestVo);

    @RequestMapping(value = "/findByAdminId", method = RequestMethod.POST)
    ResultObjectVO findByAdminId(@RequestBody RequestJsonVO requestVo);
}
