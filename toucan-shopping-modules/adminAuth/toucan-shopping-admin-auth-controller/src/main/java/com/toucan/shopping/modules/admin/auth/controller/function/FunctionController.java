package com.toucan.shopping.modules.admin.auth.controller.function;


import com.alibaba.fastjson2.JSONObject;
import com.toucan.shopping.modules.admin.auth.business.service.FunctionBusinessService;
import com.toucan.shopping.modules.admin.auth.cache.service.AdminRoleCacheService;
import com.toucan.shopping.modules.admin.auth.cache.service.FunctionCacheService;
import com.toucan.shopping.modules.admin.auth.cache.service.RoleFunctionCacheService;
import com.toucan.shopping.modules.admin.auth.entity.*;
import com.toucan.shopping.modules.admin.auth.helper.AdminAuthCacheHelper;
import com.toucan.shopping.modules.admin.auth.page.FunctionTreeInfo;
import com.toucan.shopping.modules.admin.auth.service.*;
import com.toucan.shopping.modules.admin.auth.vo.*;
import com.toucan.shopping.modules.common.generator.RequestJsonVOGenerator;
import com.toucan.shopping.modules.common.page.PageInfo;
import com.toucan.shopping.modules.common.util.GlobalUUID;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.common.vo.ResultVO;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * 功能项管理
 */
@RestController
@RequestMapping("/function")
public class FunctionController {



    @Autowired
    private FunctionBusinessService functionBusinessService;


    /**
     * 添加功能项
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/save", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO save(@RequestBody RequestJsonVO requestVo){
        return functionBusinessService.save(requestVo);
    }



    /**
     * 添加功能项
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/saves", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO saves(@RequestBody RequestJsonVO requestVo){
        return functionBusinessService.saves(requestVo);
    }


    /**
     * 查询应用权限列表
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value = "/query/app/function/tree",method = RequestMethod.POST)
    @ResponseBody
    public ResultObjectVO queryAppFunctionTree(@RequestBody RequestJsonVO requestJsonVO)
    {
        return functionBusinessService.queryAppFunctionTree(requestJsonVO);
    }




    /**
     * 查询应用权限列表
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value = "/query/app/function/tree/pid",method = RequestMethod.POST)
    @ResponseBody
    public ResultObjectVO queryAppFunctionTreeByPid(@RequestBody RequestJsonVO requestJsonVO)
    {
        return functionBusinessService.queryAppFunctionTreeByPid(requestJsonVO);
    }


    /**
     * 查询指定用户和应用的权限树
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value = "/query/function/tree",method = RequestMethod.POST)
    @ResponseBody
    public ResultObjectVO queryFunctionTree(@RequestBody RequestJsonVO requestJsonVO)
    {
        return functionBusinessService.queryFunctionTree(requestJsonVO);
    }



    /**
     * 編輯功能项
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/update", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO update(@RequestBody RequestJsonVO requestVo){
        return functionBusinessService.update(requestVo);
    }



    /**
     * 查询树表格
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/query/app/function/tree/table", produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
    @ResponseBody
    public ResultObjectVO queryAppFunctionTreeTable(@RequestBody RequestJsonVO requestJsonVO){
        return functionBusinessService.queryAppFunctionTreeTable(requestJsonVO);
    }


    /**
     * 根据PID查询树表格
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/query/app/function/tree/table/by/pid", produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
    @ResponseBody
    public ResultObjectVO queryAppFunctionTreeTableByPid(@RequestBody RequestJsonVO requestJsonVO){
        return functionBusinessService.queryAppFunctionTreeTableByPid(requestJsonVO);
    }


    /**
     * 根据ID查询
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/find/id", produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
    @ResponseBody
    public ResultObjectVO findById(@RequestBody RequestJsonVO requestVo){
        return functionBusinessService.findById(requestVo);
    }




    /**
     * 删除指定功能项
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/delete/id", produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
    @ResponseBody
    public ResultObjectVO deleteById(@RequestBody RequestJsonVO requestVo){
        return functionBusinessService.deleteById(requestVo);
    }




    /**
     * 清空该应用下所有功能项
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/delete/by/app/code", produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
    @ResponseBody
    public ResultObjectVO deleteByAppCode(@RequestBody RequestJsonVO requestVo){
        return functionBusinessService.deleteByAppCode(requestVo);
    }


    /**
     * 批量删除功能项
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/delete/ids", produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
    @ResponseBody
    public ResultObjectVO deleteByIds(@RequestBody RequestJsonVO requestVo){
        return functionBusinessService.deleteByIds(requestVo);
    }




    /**
     * 查询指定管理员应用所有角色的功能项
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value = "/query/admin/app/functions",method = RequestMethod.POST)
    @ResponseBody
    public ResultObjectVO queryAdminAppFunctions(@RequestBody RequestJsonVO requestJsonVO)
    {
        return functionBusinessService.queryAdminAppFunctions(requestJsonVO);
    }





    /**
     * 返回一级子节点
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value = "/query/one/children",method = RequestMethod.POST)
    @ResponseBody
    public ResultObjectVO queryChildren(@RequestBody RequestJsonVO requestJsonVO)
    {
        return functionBusinessService.queryChildren(requestJsonVO);
    }




    /**
     * 返回指定人的指定应用的某个上级功能项下的按钮列表
     * 首先从es中查询权限关联,如果es中没有就查询数据库就进行一次同步,如果数据库也没有 就认为没有数据
     * 这样设计的好处是 让所有正常的用户请求全部走缓存,那些不正常的用户虽然最后也会查询到数据库层面,但是后续会做黑名单限制恶意用户的访问
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value = "/query/admin/app/parent/url/one/child",method = RequestMethod.POST)
    @ResponseBody
    public ResultObjectVO queryOneChildsByAdminIdAndAppCodeAndParentUrl(@RequestBody RequestJsonVO requestJsonVO)
    {
        return functionBusinessService.queryOneChildsByAdminIdAndAppCodeAndParentUrl(requestJsonVO);
    }



    /**
     * 查询列表分页
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/list", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO list(@RequestBody RequestJsonVO requestVo){
        return functionBusinessService.list(requestVo);
    }


    @RequestMapping(value="/queryListByAppCode", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO queryListByAppCode(@RequestBody RequestJsonVO requestVo){
        return functionBusinessService.queryListByAppCode(requestVo);
    }


    /**
     * 查询功能项详情
     */
    @RequestMapping(value = "/detail",method = RequestMethod.POST)
    @ResponseBody
    public ResultObjectVO queryDetail(HttpServletRequest request, @RequestBody RequestJsonVO requestVo)
    {
        return functionBusinessService.queryDetail(requestVo);
    }


}
