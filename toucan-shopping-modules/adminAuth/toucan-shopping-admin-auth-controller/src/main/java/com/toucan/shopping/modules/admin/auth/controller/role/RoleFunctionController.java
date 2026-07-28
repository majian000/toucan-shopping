package com.toucan.shopping.modules.admin.auth.controller.role;


import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.toucan.shopping.modules.admin.auth.business.service.RoleFunctionBusinessService;
import com.toucan.shopping.modules.admin.auth.cache.service.RoleFunctionCacheService;
import com.toucan.shopping.modules.admin.auth.entity.AdminRole;
import com.toucan.shopping.modules.admin.auth.entity.Function;
import com.toucan.shopping.modules.admin.auth.entity.Role;
import com.toucan.shopping.modules.admin.auth.entity.RoleFunction;
import com.toucan.shopping.modules.admin.auth.helper.AdminAuthCacheHelper;
import com.toucan.shopping.modules.admin.auth.page.RoleFunctionPageInfo;
import com.toucan.shopping.modules.admin.auth.page.RolePageInfo;
import com.toucan.shopping.modules.admin.auth.service.AdminRoleService;
import com.toucan.shopping.modules.admin.auth.service.FunctionService;
import com.toucan.shopping.modules.admin.auth.service.RoleFunctionService;
import com.toucan.shopping.modules.admin.auth.service.RoleService;
import com.toucan.shopping.modules.admin.auth.vo.*;
import com.toucan.shopping.modules.common.page.PageInfo;
import com.toucan.shopping.modules.common.util.GlobalUUID;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.common.vo.ResultVO;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * 角色功能项管理
 */
@RestController
@RequestMapping("/roleFunction")
public class RoleFunctionController {


    @Autowired
    private RoleFunctionBusinessService roleFunctionBusinessService;




    /**
     * 查询指定角色的所有功能项
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value = "/query/list",method = RequestMethod.POST)
    @ResponseBody
    public ResultObjectVO queryRoleFunctionList(@RequestBody RequestJsonVO requestJsonVO)
    {
        return roleFunctionBusinessService.queryRoleFunctionList(requestJsonVO);
    }





    /**
     * 保存角色功能项
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value = "/save/functions",method = RequestMethod.POST)
    @ResponseBody
    public ResultObjectVO saveFunctions(@RequestBody RequestJsonVO requestJsonVO)
    {
        return roleFunctionBusinessService.saveFunctions(requestJsonVO);
    }



    /**
     * 刷新缓存
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value = "/refresh/cache",method = RequestMethod.POST)
    @ResponseBody
    public ResultObjectVO refreshCache(@RequestBody RequestJsonVO requestJsonVO) {
        return roleFunctionBusinessService.refreshCache(requestJsonVO);
    }



    /**
     * 查询列表分页
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/list", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO list(@RequestBody RequestJsonVO requestVo){
        return roleFunctionBusinessService.list(requestVo);
    }



    @RequestMapping(value="/query/function/tree/by/roleId/parentId", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO queryFunctionTreeByRoleIdAndParentId(@RequestBody RequestJsonVO requestVo){
        return roleFunctionBusinessService.queryFunctionTreeByRoleIdAndParentId(requestVo);
    }




}
