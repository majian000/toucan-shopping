package com.toucan.shopping.admin.auth.api.feign.service;

import com.toucan.shopping.admin.auth.api.feign.fallback.FeignAppServiceFallbackFactory;
import com.toucan.shopping.admin.auth.api.feign.fallback.FeignRoleServiceFallbackFactory;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(value = "toucan-shopping-gateway",path = "/toucan-shopping-admin-auth-proxy/role",fallbackFactory = FeignRoleServiceFallbackFactory.class)
public interface FeignRoleService {

    /**
     * 保存
     * @param toucanAdminAuth
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/save",method = RequestMethod.POST)
    ResultObjectVO save(@RequestHeader("toucan-sign-header") String toucanAdminAuth, @RequestBody RequestJsonVO requestVo);


    /**
     * 编辑
     * @param toucanAdminAuth
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/update",method = RequestMethod.POST)
    ResultObjectVO update(@RequestHeader("toucan-sign-header") String toucanAdminAuth, @RequestBody RequestJsonVO requestVo);


    /**
     * 查询列表
     * @param toucanAdminAuth
     * @param requestVo
     * @return
     */
    @RequestMapping(value = "/list/page",method = RequestMethod.POST)
    ResultObjectVO listPage(@RequestHeader("toucan-sign-header") String toucanAdminAuth, @RequestBody RequestJsonVO requestVo);


    /**
     * 根据ID删除指定角色
     * @param toucanAdminAuth
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/delete/id",method = RequestMethod.DELETE)
    ResultObjectVO deleteById(@RequestHeader("toucan-sign-header") String toucanAdminAuth, @RequestBody RequestJsonVO requestVo);




    /**
     * 根据ID查询
     * @param toucanAdminAuth
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/find/id",method = RequestMethod.POST)
    ResultObjectVO findById(@RequestHeader("toucan-sign-header") String toucanAdminAuth, @RequestBody RequestJsonVO requestVo);


    /**
     * 批量删除
     * @param toucanAdminAuth
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/delete/ids",method = RequestMethod.DELETE)
    ResultObjectVO deleteByIds(@RequestHeader("toucan-sign-header") String toucanAdminAuth, @RequestBody RequestJsonVO requestVo);



}
