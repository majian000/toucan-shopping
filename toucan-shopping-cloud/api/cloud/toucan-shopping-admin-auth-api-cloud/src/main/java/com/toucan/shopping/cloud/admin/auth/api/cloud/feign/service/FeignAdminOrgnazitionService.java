package com.toucan.shopping.cloud.admin.auth.api.cloud.feign.service;

import com.toucan.shopping.cloud.admin.auth.api.AdminOrgnazitionServiceAPI;
import com.toucan.shopping.cloud.admin.auth.api.cloud.feign.fallback.FeignAdminOrgnazitionServiceFallbackFactory;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(value = "toucan-shopping-gateway",path = "/toucan-shopping-admin-auth-proxy/adminOrgnazition",fallbackFactory = FeignAdminOrgnazitionServiceFallbackFactory.class)
public interface FeignAdminOrgnazitionService extends AdminOrgnazitionServiceAPI {

    @RequestMapping(value="/save",produces = "application/json;charset=UTF-8")
    ResultObjectVO save( @RequestBody RequestJsonVO requestVo);


    @RequestMapping(value="/queryListByEntity",produces = "application/json;charset=UTF-8")
    ResultObjectVO queryListByEntity( @RequestBody RequestJsonVO requestVo);


    @RequestMapping(value="/deleteByAppCode",produces = "application/json;charset=UTF-8")
    ResultObjectVO deleteByAppCode( @RequestBody RequestJsonVO requestVo);


    @RequestMapping(value="/queryAppListByAdminId",produces = "application/json;charset=UTF-8")
    ResultObjectVO queryAppListByAdminId( @RequestBody RequestJsonVO requestVo);


    /**
     * 保存组织机构关联
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value = "/save/orgnazition",method = RequestMethod.POST)
    ResultObjectVO saveOrgnazitions(@RequestBody RequestJsonVO requestJsonVO);

}
