package com.toucan.shopping.cloud.admin.auth.api.feign.service;

import com.toucan.shopping.cloud.admin.auth.api.feign.fallback.FeignAdminAppServiceFallbackFactory;
import com.toucan.shopping.cloud.admin.auth.api.feign.fallback.FeignAdminRoleServiceFallbackFactory;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(value = "toucan-shopping-gateway",path = "/toucan-shopping-admin-auth-proxy/adminRole",fallbackFactory = FeignAdminRoleServiceFallbackFactory.class)
public interface FeignAdminRoleService {

    @RequestMapping(value = "/save/roles",method = RequestMethod.POST)
    public ResultObjectVO saveRoles(@RequestHeader("toucan-sign-header") String signHeader,@RequestBody RequestJsonVO requestJsonVO);



    @RequestMapping(value="/queryListByEntity",produces = "application/json;charset=UTF-8")
    ResultObjectVO queryListByEntity(@RequestHeader("toucan-sign-header") String signHeader, @RequestBody RequestJsonVO requestVo);


    /**
     * 列表分页
     * @param signHeader
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/list",produces = "application/json;charset=UTF-8",method = RequestMethod.POST)
    ResultObjectVO list(@RequestHeader("toucan-sign-header") String signHeader,@RequestBody RequestJsonVO requestVo);


}
