package com.toucan.shopping.app.api.feign.service;

import com.toucan.shopping.app.api.feign.fallback.FeignAppServiceFallbackFactory;
import com.toucan.shopping.app.api.feign.fallback.FeignAppServiceFallbackFactory;
import com.toucan.shopping.common.vo.RequestJsonVO;
import com.toucan.shopping.common.vo.ResultObjectVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(value = "toucan-shopping-gateway",path = "/toucan-shopping-admin-auth-proxy/toucan-shopping-admin-auth/app",fallbackFactory = FeignAppServiceFallbackFactory.class)
public interface FeignAppService {

    /**
     * 保存应用
     * @param bbsUserCenterAtuh
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/save",method = RequestMethod.POST)
    ResultObjectVO save(@RequestHeader("toucan-admin-auth-atuh") String bbsUserCenterAtuh, @RequestBody RequestJsonVO requestVo);


    /**
     * 编辑应用
     * @param bbsUserCenterAtuh
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/update",method = RequestMethod.POST)
    ResultObjectVO update(@RequestHeader("toucan-admin-auth-atuh") String bbsUserCenterAtuh, @RequestBody RequestJsonVO requestVo);


    /**
     * 查询应用列表
     * @param bbsUserCenterAtuh
     * @param requestVo
     * @return
     */
    @GetMapping("/list/page")
    ResultObjectVO listPage(@RequestHeader("toucan-admin-auth-atuh") String bbsUserCenterAtuh, @RequestBody RequestJsonVO requestVo);


    /**
     * 根据ID删除指定应用
     * @param bbsUserCenterAtuh
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/delete/id",method = RequestMethod.DELETE)
    ResultObjectVO deleteById(@RequestHeader("toucan-admin-auth-atuh") String bbsUserCenterAtuh, @RequestBody RequestJsonVO requestVo);

    /**
     * 批量删除应用
     * @param bbsUserCenterAtuh
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/delete/ids",method = RequestMethod.DELETE)
    ResultObjectVO deleteByIds(@RequestHeader("toucan-admin-auth-atuh") String bbsUserCenterAtuh, @RequestBody RequestJsonVO requestVo);



}
