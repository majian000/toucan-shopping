package com.toucan.shopping.modules.admin.auth.controller.auth;


import com.alibaba.fastjson.JSONObject;
import com.toucan.shopping.modules.admin.auth.cache.service.AdminRoleCacheService;
import com.toucan.shopping.modules.admin.auth.cache.service.FunctionCacheService;
import com.toucan.shopping.modules.admin.auth.cache.service.RoleFunctionCacheService;
import com.toucan.shopping.modules.admin.auth.entity.AdminRole;
import com.toucan.shopping.modules.admin.auth.entity.Function;
import com.toucan.shopping.modules.admin.auth.entity.RoleFunction;
import com.toucan.shopping.modules.admin.auth.helper.AdminAuthCacheHelper;
import com.toucan.shopping.modules.admin.auth.redis.AdminAuthRedisKey;
import com.toucan.shopping.modules.admin.auth.service.*;
import com.toucan.shopping.modules.admin.auth.vo.*;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.common.vo.ResultVO;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 校验权限
 */
@RestController
@RequestMapping("/auth")
public class AuthController {


    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private RoleFunctionService roleFunctionService;

    @Autowired
    private AdminRoleService adminRoleService;


    @Autowired
    private FunctionService functionService;

    /**
     * 校验权限
     * 首先从es中查询权限关联,如果es中没有就查询数据库就进行一次同步,如果数据库也没有 就认为没有权限
     * 这样设计的好处是 让所有正常的用户请求全部走缓存,那些不正常的用户虽然最后也会查询到数据库层面,但是后续会做黑名单限制恶意用户的访问
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/verify",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO verify(@RequestBody RequestJsonVO requestVo){
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        resultObjectVO.setData(false);
        if(requestVo.getEntityJson()==null)
        {
            resultObjectVO.setCode(AdminResultVO.FAILD);
            resultObjectVO.setMsg("没有找到参数");
            return resultObjectVO;
        }
        try {
            //缓存是否可读取
            boolean cacheIsRead = true;
            AuthVerifyVO query = JSONObject.parseObject(requestVo.getEntityJson(), AuthVerifyVO.class);
            if(StringUtils.isEmpty(query.getAdminId()))
            {
                throw new IllegalArgumentException("adminId为空");
            }
            if(StringUtils.isEmpty(query.getAppCode())){
                throw new IllegalArgumentException("appCode为空");
            }
            if(StringUtils.isEmpty(query.getUrl()))
            {
                throw new IllegalArgumentException("url为空");
            }
            AdminRole queryAdminRole = new AdminRole();
            queryAdminRole.setAdminId(query.getAdminId());
            queryAdminRole.setAppCode(query.getAppCode());
            queryAdminRole.setDeleteStatus((short)0);

            List<AdminRole> adminRoles = null;
            //先查询缓存,为了缓解数据库的查询压力
            List<AdminRoleCacheVO> adminRoleCacheVOS = null;
            AdminRoleCacheService adminRoleCacheService = AdminAuthCacheHelper.getAdminRoleCacheService();
            FunctionCacheService functionCacheService = AdminAuthCacheHelper.getFunctionCacheService();
            RoleFunctionCacheService roleFunctionCacheService = AdminAuthCacheHelper.getRoleFunctionCacheService();
            try {
                if(adminRoleCacheService!=null) {
                    AdminRoleCacheVO queryAdminRoleCacheVO = new AdminRoleCacheVO();
                    BeanUtils.copyProperties(queryAdminRoleCacheVO, queryAdminRole);
                    //查询管理员角色关联
                    adminRoleCacheVOS = adminRoleCacheService.queryByEntity(queryAdminRoleCacheVO);
                }
            }catch(Exception e)
            {
                cacheIsRead = false;
                logger.warn(e.getMessage(),e);
            }
            if(CollectionUtils.isNotEmpty(adminRoleCacheVOS))
            {
                //将缓存数据进行格式化
                adminRoles = JSONObject.parseArray(JSONObject.toJSONString(adminRoleCacheVOS),AdminRole.class);
            }else{
                //查询这个账户下应用下的所有角色
                adminRoles = adminRoleService.findListByEntity(queryAdminRole);
                try {
                    if(adminRoleCacheService!=null) {
                        //同步缓存
                        if (CollectionUtils.isNotEmpty(adminRoles) && cacheIsRead) {
                            for (AdminRole adminRole : adminRoles) {
                                AdminRoleCacheVO adminRoleCacheVO = new AdminRoleCacheVO();
                                BeanUtils.copyProperties(adminRoleCacheVO, adminRole);
                                adminRoleCacheService.save(adminRoleCacheVO);
                            }
                        }
                    }
                }catch(Exception e)
                {
                    cacheIsRead = false;
                    logger.warn(e.getMessage(),e);
                }
            }

            if(CollectionUtils.isNotEmpty(adminRoles)) {
                Integer count = 0;
                //遍历每一个角色
                for(int i=0;i<adminRoles.size();i++) {
                    if(adminRoles.get(i)!=null) {
                        String roleId = adminRoles.get(i).getRoleId();

                        FunctionVO queryFunction = new FunctionVO();
                        queryFunction.setUrl(query.getUrl());
                        queryFunction.setAppCode(query.getAppCode());
                        queryFunction.setDeleteStatus((short)0);
                        queryFunction.setEnableStatus((short)1);
                        List<FunctionCacheVO> functionCacheVOS = null;
                        List<Function> functionList = new ArrayList<Function>();
                        try {
                            if(cacheIsRead) {
                                if(functionCacheService!=null) {
                                    //查询功能项
                                    FunctionCacheVO queryFunctionCacheVO = new FunctionCacheVO();
                                    BeanUtils.copyProperties(queryFunctionCacheVO, queryFunction);
                                    functionCacheVOS = functionCacheService.queryByEntity(queryFunctionCacheVO);
                                    if (CollectionUtils.isNotEmpty(functionCacheVOS)) {
                                        List<Function> functionCacheList = JSONObject.parseArray(JSONObject.toJSONString(functionCacheVOS), Function.class);
                                        if (CollectionUtils.isNotEmpty(functionCacheList)) {
                                            for (Function functioncache : functionCacheList) {
                                                //在进行一次过滤,因为elasticsearch查询url的时候,会把关联的都查询出来,这样查询不是eq查询
                                                //例如/role/list这个接口也会把role/listPage查询出来
                                                if (functioncache.getUrl().equals(query.getUrl())) {
                                                    functionList.add(functioncache);
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }catch(Exception e)
                        {
                            cacheIsRead = false;
                            logger.warn(e.getMessage(),e);
                        }
                        //从数据库查询
                        if(CollectionUtils.isEmpty(functionList))
                        {
                            functionList = functionService.findListByEntity(queryFunction);
                        }
                        if(CollectionUtils.isNotEmpty(functionList))
                        {
                            for(Function function:functionList) {
                                try {
                                    if(cacheIsRead) {
                                        if(functionCacheService!=null) {
                                            //同步到缓存
                                            FunctionCacheVO functionCacheVO = new FunctionCacheVO();
                                            BeanUtils.copyProperties(functionCacheVO, function);
                                            functionCacheService.save(functionCacheVO);
                                        }
                                    }
                                } catch (Exception e) {
                                    logger.warn(e.getMessage(), e);
                                    cacheIsRead = false;
                                }
                                if(function.getFunctionId()==null)
                                {
                                    resultObjectVO.setData(false);
                                    return resultObjectVO;
                                }
                                //查询这个功能项是否包含在这个管理员的角色与功能项的关联里
                                RoleFunctionVO queryRoleFunction = new RoleFunctionVO();
                                queryRoleFunction.setFunctionId(function.getFunctionId());
                                queryRoleFunction.setRoleId(roleId);
                                queryRoleFunction.setDeleteStatus((short) 0);
                                queryRoleFunction.setAppCode(query.getAppCode());

                                List<RoleFunctionCacheVO> roleFunctionCacheVOS = null;
                                try {
                                    if(cacheIsRead) {
                                        if(roleFunctionCacheService!=null) {
                                            RoleFunctionCacheVO queryRoleFunctionCacheVO = new RoleFunctionCacheVO();
                                            BeanUtils.copyProperties(queryRoleFunctionCacheVO, queryRoleFunction);
                                            roleFunctionCacheVOS = roleFunctionCacheService.queryByEntity(queryRoleFunctionCacheVO);
                                            if (CollectionUtils.isNotEmpty(roleFunctionCacheVOS)) {
                                                count = roleFunctionCacheVOS.size();
                                            }
                                        }
                                    }
                                }catch(Exception e)
                                {
                                    cacheIsRead = false;
                                    logger.warn(e.getMessage(),e);
                                }
                                //如果缓存为空,就查询数据库,如果数据库为空 才认为这个人没有权限
                                if(CollectionUtils.isEmpty(roleFunctionCacheVOS))
                                {
                                    List<RoleFunction> roleFunctions = roleFunctionService.findListByEntity(queryRoleFunction);
                                    if(CollectionUtils.isNotEmpty(roleFunctions))
                                    {
                                        count = roleFunctions.size();
                                    }
                                }
                            }
                        }
                        if(count!=null&&count.intValue()>0)
                        {
                            //每次操作都延长登录会话1小时
                            AdminAuthCacheHelper.getAdminLoginCacheService().loginTokenDelay(query.getAdminId());
                            resultObjectVO.setData(true);
                            return resultObjectVO;
                        }

                    }
                }
            }
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);

            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请稍后重试");
        }
        return resultObjectVO;
    }







    /**
     * 校验权限
     * 首先从es中查询权限关联,如果es中没有就查询数据库就进行一次同步,如果数据库也没有 就认为没有权限
     * 这样设计的好处是 让所有正常的用户请求全部走缓存,那些不正常的用户虽然最后也会查询到数据库层面,但是后续会做黑名单限制恶意用户的访问
     * @param requestVo
     * @return -1 登录超时 -2 权限校验失败 1成功
     */
    @RequestMapping(value="/verifyLoginAndUrl",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO verifyLoginAndUrl(@RequestBody RequestJsonVO requestVo){
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        resultObjectVO.setData(false);
        if(requestVo.getEntityJson()==null)
        {
            resultObjectVO.setCode(AdminResultVO.FAILD);
            resultObjectVO.setMsg("没有找到参数");
            return resultObjectVO;
        }
        try {
            //缓存是否可读取
            boolean cacheIsRead = true;
            AuthVerifyVO query = JSONObject.parseObject(requestVo.getEntityJson(), AuthVerifyVO.class);
            if(StringUtils.isEmpty(query.getAdminId()))
            {
                throw new IllegalArgumentException("adminId为空");
            }
            if(StringUtils.isEmpty(query.getAppCode())){
                throw new IllegalArgumentException("appCode为空");
            }
            if(StringUtils.isEmpty(query.getUrl()))
            {
                throw new IllegalArgumentException("url为空");
            }
            if(StringUtils.isEmpty(query.getLoginToken()))
            {
                throw new IllegalArgumentException("loginToken为空");
            }


            //校验登录会话
            Object loginTokenObject = AdminAuthCacheHelper.getAdminLoginCacheService().getLoginToken(query.getAdminId(),requestVo.getAppCode());
            if (loginTokenObject == null) {
                resultObjectVO.setData(-1);
                return resultObjectVO;
            }
            if(!StringUtils.equals(query.getLoginToken(),String.valueOf(loginTokenObject)))
            {
                resultObjectVO.setData(-1);
                return resultObjectVO;
            }


            //校验权限
            AdminRole queryAdminRole = new AdminRole();
            queryAdminRole.setAdminId(query.getAdminId());
            queryAdminRole.setAppCode(query.getAppCode());
            queryAdminRole.setDeleteStatus((short)0);

            List<AdminRole> adminRoles = null;
            //先查询缓存,为了缓解数据库的查询压力
            List<AdminRoleCacheVO> adminRoleCacheVOS = null;
            AdminRoleCacheService adminRoleCacheService = AdminAuthCacheHelper.getAdminRoleCacheService();
            FunctionCacheService functionCacheService = AdminAuthCacheHelper.getFunctionCacheService();
            RoleFunctionCacheService roleFunctionCacheService = AdminAuthCacheHelper.getRoleFunctionCacheService();
            try {
                if(adminRoleCacheService!=null) {
                    AdminRoleCacheVO queryAdminRoleCacheVO = new AdminRoleCacheVO();
                    BeanUtils.copyProperties(queryAdminRoleCacheVO, queryAdminRole);
                    //查询管理员角色关联
                    adminRoleCacheVOS = adminRoleCacheService.queryByEntity(queryAdminRoleCacheVO);
                }
            }catch(Exception e)
            {
                cacheIsRead = false;
                logger.warn(e.getMessage(),e);
            }
            if(CollectionUtils.isNotEmpty(adminRoleCacheVOS))
            {
                //将缓存数据进行格式化
                adminRoles = JSONObject.parseArray(JSONObject.toJSONString(adminRoleCacheVOS),AdminRole.class);
            }else{
                //查询这个账户下应用下的所有角色
                adminRoles = adminRoleService.findListByEntity(queryAdminRole);
                try {
                    if(adminRoleCacheService!=null) {
                        //同步缓存
                        if (CollectionUtils.isNotEmpty(adminRoles) && cacheIsRead) {
                            for (AdminRole adminRole : adminRoles) {
                                AdminRoleCacheVO adminRoleCacheVO = new AdminRoleCacheVO();
                                BeanUtils.copyProperties(adminRoleCacheVO, adminRole);
                                adminRoleCacheService.save(adminRoleCacheVO);
                            }
                        }
                    }
                }catch(Exception e)
                {
                    cacheIsRead = false;
                    logger.warn(e.getMessage(),e);
                }
            }

            //没有任何角色
            if(CollectionUtils.isEmpty(adminRoles)) {
                //权限校验失败
                resultObjectVO.setData(-2);
                return resultObjectVO;
            }
            Integer count = 0;
            //遍历每一个角色
            for(int i=0;i<adminRoles.size();i++) {
                if(adminRoles.get(i)!=null) {
                    String roleId = adminRoles.get(i).getRoleId();

                    FunctionVO queryFunction = new FunctionVO();
                    queryFunction.setUrl(query.getUrl());
                    queryFunction.setAppCode(query.getAppCode());
                    queryFunction.setDeleteStatus((short)0);
                    queryFunction.setEnableStatus((short)1);
                    List<FunctionCacheVO> functionCacheVOS = null;
                    List<Function> functionList = new ArrayList<Function>();
                    try {
                        if(cacheIsRead) {
                            if(functionCacheService!=null) {
                                //查询功能项
                                FunctionCacheVO queryFunctionCacheVO = new FunctionCacheVO();
                                BeanUtils.copyProperties(queryFunctionCacheVO, queryFunction);
                                functionCacheVOS = functionCacheService.queryByEntity(queryFunctionCacheVO);
                                if (CollectionUtils.isNotEmpty(functionCacheVOS)) {
                                    List<Function> functionCacheList = JSONObject.parseArray(JSONObject.toJSONString(functionCacheVOS), Function.class);
                                    if (CollectionUtils.isNotEmpty(functionCacheList)) {
                                        for (Function functioncache : functionCacheList) {
                                            //在进行一次过滤,因为elasticsearch查询url的时候,会把关联的都查询出来,这样查询不是eq查询
                                            //例如/role/list这个接口也会把role/listPage查询出来
                                            if (functioncache.getUrl().equals(query.getUrl())) {
                                                functionList.add(functioncache);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }catch(Exception e)
                    {
                        cacheIsRead = false;
                        logger.warn(e.getMessage(),e);
                    }
                    //从数据库查询
                    if(CollectionUtils.isEmpty(functionList))
                    {
                        functionList = functionService.findListByEntity(queryFunction);
                    }
                    if(CollectionUtils.isNotEmpty(functionList))
                    {
                        for(Function function:functionList) {
                            try {
                                if(cacheIsRead) {
                                    if(functionCacheService!=null) {
                                        //同步到缓存
                                        FunctionCacheVO functionCacheVO = new FunctionCacheVO();
                                        BeanUtils.copyProperties(functionCacheVO, function);
                                        functionCacheService.save(functionCacheVO);
                                    }
                                }
                            } catch (Exception e) {
                                logger.warn(e.getMessage(), e);
                                cacheIsRead = false;
                            }
                            if(function.getFunctionId()==null)
                            {
                                resultObjectVO.setData(false);
                                return resultObjectVO;
                            }
                            //查询这个功能项是否包含在这个管理员的角色与功能项的关联里
                            RoleFunctionVO queryRoleFunction = new RoleFunctionVO();
                            queryRoleFunction.setFunctionId(function.getFunctionId());
                            queryRoleFunction.setRoleId(roleId);
                            queryRoleFunction.setDeleteStatus((short) 0);
                            queryRoleFunction.setAppCode(query.getAppCode());

                            List<RoleFunctionCacheVO> roleFunctionCacheVOS = null;
                            try {
                                if(cacheIsRead) {
                                    if(roleFunctionCacheService!=null) {
                                        RoleFunctionCacheVO queryRoleFunctionCacheVO = new RoleFunctionCacheVO();
                                        BeanUtils.copyProperties(queryRoleFunctionCacheVO, queryRoleFunction);
                                        roleFunctionCacheVOS = roleFunctionCacheService.queryByEntity(queryRoleFunctionCacheVO);
                                        if (CollectionUtils.isNotEmpty(roleFunctionCacheVOS)) {
                                            count = roleFunctionCacheVOS.size();
                                        }
                                    }
                                }
                            }catch(Exception e)
                            {
                                cacheIsRead = false;
                                logger.warn(e.getMessage(),e);
                            }
                            //如果缓存为空,就查询数据库,如果数据库为空 才认为这个人没有权限
                            if(CollectionUtils.isEmpty(roleFunctionCacheVOS))
                            {
                                List<RoleFunction> roleFunctions = roleFunctionService.findListByEntity(queryRoleFunction);
                                if(CollectionUtils.isNotEmpty(roleFunctions))
                                {
                                    count = roleFunctions.size();

                                    try{
                                        //刷新缓存
                                        if(roleFunctionCacheService!=null) {
                                            RoleFunctionCacheVO[] roleFunctionCacheVOArray = new RoleFunctionCacheVO[count];
                                            for (int p = 0; p < count; p++) {
                                                RoleFunction roleFunction = roleFunctions.get(p);
                                                RoleFunctionCacheVO roleFunctionCacheVO = new RoleFunctionCacheVO();
                                                if (roleFunction != null) {
                                                    BeanUtils.copyProperties(roleFunctionCacheVO, roleFunction);
                                                }
                                                roleFunctionCacheVOArray[p] = roleFunctionCacheVO;
                                            }
                                            roleFunctionCacheService.saves(roleFunctionCacheVOArray);
                                        }
                                    }catch(Exception e){
                                        logger.warn(e.getMessage(),e);
                                    }
                                }
                            }
                        }
                    }
                    if(count!=null&&count.intValue()>0)
                    {
                        //每次操作都延长登录会话1小时
                        AdminAuthCacheHelper.getAdminLoginCacheService().loginTokenDelay(query.getAdminId());
                        resultObjectVO.setData(1);
                        return resultObjectVO;
                    }
                    //权限校验失败
                    resultObjectVO.setData(-2);
                }
            }
        }catch(Exception e)
        {
            resultObjectVO.setData(-1);
            logger.warn(e.getMessage(),e);

            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请稍后重试");
        }
        return resultObjectVO;
    }



}
