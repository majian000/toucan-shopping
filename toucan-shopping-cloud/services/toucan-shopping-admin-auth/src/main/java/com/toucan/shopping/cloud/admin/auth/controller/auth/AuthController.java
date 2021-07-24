package com.toucan.shopping.cloud.admin.auth.controller.auth;


import com.alibaba.fastjson.JSONObject;
import com.toucan.shopping.modules.admin.auth.entity.AdminApp;
import com.toucan.shopping.modules.admin.auth.entity.AdminRole;
import com.toucan.shopping.modules.admin.auth.entity.Function;
import com.toucan.shopping.modules.admin.auth.entity.RoleFunction;
import com.toucan.shopping.modules.admin.auth.es.service.AdminRoleElasticSearchService;
import com.toucan.shopping.modules.admin.auth.es.service.FunctionElasticSearchService;
import com.toucan.shopping.modules.admin.auth.es.service.RoleFunctionElasticSearchService;
import com.toucan.shopping.modules.admin.auth.redis.AdminCenterRedisKey;
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
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
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
    private StringRedisTemplate redisTemplate;

    @Autowired
    private AdminRoleElasticSearchService adminRoleElasticSearchService;

    @Autowired
    private FunctionElasticSearchService functionElasticSearchService;

    @Autowired
    private RoleFunctionElasticSearchService roleFunctionElasticSearchService;

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
            resultObjectVO.setMsg("请求失败,没有找到参数");
            return resultObjectVO;
        }
        try {
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
            List<AdminRoleElasticSearchVO> adminRoleElasticSearchVOS = null;
            try {
                //查询管理员角色关联
                adminRoleElasticSearchVOS = adminRoleElasticSearchService.queryByEntity((AdminRoleElasticSearchVO) queryAdminRole);
            }catch(Exception e)
            {
                logger.warn(e.getMessage(),e);
            }
            if(CollectionUtils.isNotEmpty(adminRoleElasticSearchVOS))
            {
                //将缓存数据进行格式化
                adminRoles = JSONObject.parseArray(JSONObject.toJSONString(adminRoleElasticSearchVOS),AdminRole.class);
            }else{
                //查询这个账户下应用下的所有角色
                adminRoles = adminRoleService.findListByEntity(queryAdminRole);
                try {
                    //同步缓存
                    if (CollectionUtils.isNotEmpty(adminRoles)) {
                        for (AdminRole adminRole : adminRoles) {
                            AdminRoleElasticSearchVO adminRoleElasticSearchVO = new AdminRoleElasticSearchVO();
                            BeanUtils.copyProperties(adminRoleElasticSearchVO,adminRole);
                            adminRoleElasticSearchService.save(adminRoleElasticSearchVO);
                        }
                    }
                }catch(Exception e)
                {
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
                        List<FunctionElasticSearchVO> functionElasticSearchVOS = null;
                        List<Function> functionList = null;
                        try {
                            //查询功能项
                            functionElasticSearchVOS = functionElasticSearchService.queryByEntity((FunctionElasticSearchVO) queryFunction);
                            if(CollectionUtils.isNotEmpty(functionElasticSearchVOS))
                            {
                                functionList = JSONObject.parseArray(JSONObject.toJSONString(functionElasticSearchVOS),Function.class);
                            }
                        }catch(Exception e)
                        {
                            logger.warn(e.getMessage(),e);
                        }
                        //从数据库查询
                        if(CollectionUtils.isEmpty(functionList))
                        {
                            functionList = functionService.findListByEntity(queryFunction);
                            if(CollectionUtils.isNotEmpty(functionList))
                            {
                                for(Function function:functionList) {
                                    try {
                                        //同步到缓存
                                        FunctionElasticSearchVO functionElasticSearchVO = new FunctionElasticSearchVO();
                                        BeanUtils.copyProperties(functionElasticSearchVO, function);
                                        functionElasticSearchService.save(functionElasticSearchVO);
                                    } catch (Exception e) {
                                        logger.warn(e.getMessage(), e);
                                    }
                                    //查询这个功能项是否包含在这个管理员的角色与功能项的关联里
                                    RoleFunctionVO queryRoleFunction = new RoleFunctionVO();
                                    queryRoleFunction.setFunctionId(function.getFunctionId());
                                    queryRoleFunction.setRoleId(roleId);
                                    queryRoleFunction.setDeleteStatus((short) 0);
                                    queryRoleFunction.setAppCode(query.getAppCode());

                                    List<RoleFunctionElasticSearchVO> roleFunctionElasticSearchVOS = null;
                                    try {
                                        roleFunctionElasticSearchVOS = roleFunctionElasticSearchService.queryByEntity((RoleFunctionElasticSearchVO) queryRoleFunction);
                                        if(CollectionUtils.isNotEmpty(roleFunctionElasticSearchVOS))
                                        {
                                            count = roleFunctionElasticSearchVOS.size();
                                        }
                                    }catch(Exception e)
                                    {
                                        logger.warn(e.getMessage(),e);
                                    }
                                    //如果缓存为空,就查询数据库,如果数据库为空 才认为这个人没有权限
                                    if(CollectionUtils.isEmpty(roleFunctionElasticSearchVOS))
                                    {
                                        List<RoleFunction> roleFunctions = roleFunctionService.findListByEntity(queryRoleFunction);
                                        if(CollectionUtils.isNotEmpty(roleFunctions))
                                        {
                                            count = roleFunctions.size();
                                        }
                                    }
                                }
                            }
                        }
                        if(count!=null&&count.intValue()>0)
                        {
                            //每次操作都延长登录会话1小时
                            redisTemplate.expire(AdminCenterRedisKey.getLoginTokenGroupKey(query.getAdminId()),
                                    AdminCenterRedisKey.LOGIN_TIMEOUT_SECOND, TimeUnit.SECONDS);
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
            resultObjectVO.setMsg("请求失败,请稍后重试");
        }
        return resultObjectVO;
    }



}
