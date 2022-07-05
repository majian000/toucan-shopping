package com.toucan.shopping.cloud.admin.auth.api.feign.service;

import com.toucan.shopping.cloud.admin.auth.api.feign.fallback.FeignAdminAppServiceFallbackFactory;
import com.toucan.shopping.cloud.admin.auth.api.feign.fallback.FeignAdminServiceFallbackFactory;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(value = "toucan-shopping-gateway",path = "/toucan-shopping-admin-auth-proxy/adminApp",fallbackFactory = FeignAdminAppServiceFallbackFactory.class)
public interface FeignAdminAppService {

    @RequestMapping(value="/save",produces = "application/json;charset=UTF-8")
    ResultObjectVO save(@RequestHeader("toucan-sign-header") String signHeader,@RequestBody RequestJsonVO requestVo);


    @RequestMapping(value="/queryListByEntity",produces = "application/json;charset=UTF-8")
    ResultObjectVO queryListByEntity(@RequestHeader("toucan-sign-header") String signHeader, @RequestBody RequestJsonVO requestVo);


    @RequestMapping(value="/deleteByAppCode",produces = "application/json;charset=UTF-8")
    ResultObjectVO deleteByAppCode(@RequestHeader("toucan-sign-header") String signHeader,@RequestBody RequestJsonVO requestVo);


    @RequestMapping(value="/queryAppListByAdminId",produces = "application/json;charset=UTF-8")
    ResultObjectVO queryAppListByAdminId(@RequestHeader("toucan-sign-header") String signHeader,@RequestBody RequestJsonVO requestVo);


    /**
     * 查询列表分页
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/list",produces = "application/json;charset=UTF-8")
    ResultObjectVO list(@RequestBody RequestJsonVO requestVo);



    /**
     * 查询在线用户列表分页
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/online/list",produces = "application/json;charset=UTF-8")
    ResultObjectVO onlineList(@RequestBody RequestJsonVO requestVo);


    /**
     * 查询登录列表分页
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/login/list",produces = "application/json;charset=UTF-8")
    ResultObjectVO loginList(@RequestBody RequestJsonVO requestVo);



    /**
     * 修改账号登录状态
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/batchUpdateLoginStatus",produces = "application/json;charset=UTF-8")
    ResultObjectVO batchUpdateLoginStatus(@RequestBody RequestJsonVO requestVo);



    /**
     * 查询在线用户列表分页
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/logout",produces = "application/json;charset=UTF-8")
    ResultObjectVO logout(@RequestBody RequestJsonVO requestVo);


    /**
     * 查询APP登录用户信息
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/queryAppLoginUserCountList",produces = "application/json;charset=UTF-8")
    ResultObjectVO queryAppLoginUserCountList(@RequestBody RequestJsonVO requestVo);




}
