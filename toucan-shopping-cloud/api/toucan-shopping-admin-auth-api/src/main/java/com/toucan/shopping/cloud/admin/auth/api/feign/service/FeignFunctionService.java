package com.toucan.shopping.cloud.admin.auth.api.feign.service;

import com.toucan.shopping.cloud.admin.auth.api.feign.fallback.FeignFunctionServiceFallbackFactory;
import com.toucan.shopping.cloud.admin.auth.api.feign.fallback.FeignRoleServiceFallbackFactory;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(value = "toucan-shopping-gateway",path = "/toucan-shopping-admin-auth-proxy/function",fallbackFactory = FeignFunctionServiceFallbackFactory.class)
public interface FeignFunctionService {

    /**
     * 保存
     * @param signHeader
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/save",method = RequestMethod.POST)
    ResultObjectVO save(@RequestHeader("toucan-sign-header") String signHeader, @RequestBody RequestJsonVO requestVo);


    /**
     * 编辑
     * @param signHeader
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/update",method = RequestMethod.POST)
    ResultObjectVO update(@RequestHeader("toucan-sign-header") String signHeader, @RequestBody RequestJsonVO requestVo);


    /**
     * 查询树表格
     * @param signHeader
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/query/app/function/tree/table",produces = "application/json;charset=UTF-8",method = RequestMethod.POST)
    public ResultObjectVO queryAppFunctionTreeTable(@RequestHeader("toucan-sign-header") String signHeader,@RequestBody RequestJsonVO requestJsonVO);
    
    
    /**
     * 根据ID删除指定角色
     * @param signHeader
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/delete/id",method = RequestMethod.DELETE)
    ResultObjectVO deleteById(@RequestHeader("toucan-sign-header") String signHeader, @RequestBody RequestJsonVO requestVo);




    /**
     * 根据ID查询
     * @param signHeader
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/find/id",method = RequestMethod.POST)
    ResultObjectVO findById(@RequestHeader("toucan-sign-header") String signHeader, @RequestBody RequestJsonVO requestVo);


    /**
     * 批量删除
     * @param signHeader
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/delete/ids",method = RequestMethod.DELETE)
    ResultObjectVO deleteByIds(@RequestHeader("toucan-sign-header") String signHeader, @RequestBody RequestJsonVO requestVo);


    /**
     * 查询应用以及下面所有功能树
     * @param signHeader
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value = "/query/app/function/tree",method = RequestMethod.POST)
    public ResultObjectVO queryAppFunctionTree(@RequestHeader("toucan-sign-header") String signHeader, @RequestBody RequestJsonVO requestJsonVO);

}
