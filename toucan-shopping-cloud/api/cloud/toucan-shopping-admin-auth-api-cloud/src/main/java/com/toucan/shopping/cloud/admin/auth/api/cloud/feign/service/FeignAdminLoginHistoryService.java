package com.toucan.shopping.cloud.admin.auth.api.cloud.feign.service;

import com.toucan.shopping.cloud.admin.auth.api.AdminLoginHistoryServiceAPI;
import com.toucan.shopping.cloud.admin.auth.api.cloud.feign.fallback.FeignAdminLoginHistoryServiceFallbackFactory;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(value = "toucan-shopping-gateway", path = "/toucan-shopping-admin-auth-proxy/adminLoginHistory", fallbackFactory = FeignAdminLoginHistoryServiceFallbackFactory.class)
public interface FeignAdminLoginHistoryService extends AdminLoginHistoryServiceAPI {

    /**
     * 查询列表分页
     * @param requestVo
     * @return
     */
    @RequestMapping(value = "/list/page", method = RequestMethod.POST)
    ResultObjectVO listPage(@RequestBody RequestJsonVO requestVo);

    /**
     * 根据ID查询
     * @param requestVo
     * @return
     */
    @RequestMapping(value = "/find/id", method = RequestMethod.POST)
    ResultObjectVO findById(@RequestBody RequestJsonVO requestVo);
}
