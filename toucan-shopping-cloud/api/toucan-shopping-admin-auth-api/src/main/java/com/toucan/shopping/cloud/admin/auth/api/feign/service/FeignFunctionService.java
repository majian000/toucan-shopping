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
    ResultObjectVO queryAppFunctionTreeTable(@RequestHeader("toucan-sign-header") String signHeader,@RequestBody RequestJsonVO requestJsonVO);



    /**
     * 根据PID查询树表格
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/query/app/function/tree/table/by/pid",produces = "application/json;charset=UTF-8",method = RequestMethod.POST)
    @ResponseBody
    ResultObjectVO queryAppFunctionTreeTableByPid(@RequestHeader("toucan-sign-header") String signHeader,@RequestBody RequestJsonVO requestJsonVO);
    
    /**
     * 根据ID删除
     * @param signHeader
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/delete/id",method = RequestMethod.DELETE)
    ResultObjectVO deleteById(@RequestHeader("toucan-sign-header") String signHeader, @RequestBody RequestJsonVO requestVo);




    /**
     * 删除指定功能项
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/delete/by/app/code",produces = "application/json;charset=UTF-8",method = RequestMethod.DELETE)
    ResultObjectVO deleteByAppCode(@RequestBody RequestJsonVO requestVo);


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
    ResultObjectVO queryAppFunctionTree(@RequestHeader("toucan-sign-header") String signHeader, @RequestBody RequestJsonVO requestJsonVO);



    /**
     * 查询应用权限列表
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value = "/query/app/function/tree/pid",method = RequestMethod.POST)
    ResultObjectVO queryAppFunctionTreeByPid(@RequestBody RequestJsonVO requestJsonVO);

    /**
     * 查询指定用户和应用的权限树
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value = "/query/function/tree",method = RequestMethod.POST)
    ResultObjectVO queryFunctionTree(@RequestHeader("toucan-sign-header") String signHeader,@RequestBody RequestJsonVO requestJsonVO);



    /**
     * 查询指定管理员应用所有角色的功能项
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value = "/query/admin/app/functions",method = RequestMethod.POST)
    ResultObjectVO queryAdminAppFunctions(@RequestHeader("toucan-sign-header") String signHeader,@RequestBody RequestJsonVO requestJsonVO);


    /**
     * 查询下一级子节点
     * @param signHeader
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value = "/query/one/children",method = RequestMethod.POST)
    ResultObjectVO queryChildren(@RequestHeader("toucan-sign-header") String signHeader,@RequestBody RequestJsonVO requestJsonVO);



    /**
     * 返回指定人的指定应用的某个上级功能项下的按钮列表
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value = "/query/admin/app/parent/url/one/child",method = RequestMethod.POST)
    ResultObjectVO queryOneChildsByAdminIdAndAppCodeAndParentUrl(@RequestHeader("toucan-sign-header") String signHeader,@RequestBody RequestJsonVO requestJsonVO);


    /**
     * 列表分页
     * @param signHeader
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/list",produces = "application/json;charset=UTF-8",method = RequestMethod.POST)
    ResultObjectVO list(@RequestHeader("toucan-sign-header") String signHeader,@RequestBody RequestJsonVO requestVo);


}
