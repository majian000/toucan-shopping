package com.toucan.shopping.modules.admin.auth.business.service;


import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.JSON;
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
import com.toucan.shopping.modules.common.annotation.RequestCheck;
import com.toucan.shopping.modules.common.exception.BusinessValidationException;
import com.toucan.shopping.modules.common.util.Check;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.common.vo.ResultVO;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * 校验权限
 */
@Service
public class AuthBusinessService {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private RoleFunctionService roleFunctionService;

    @Autowired
    private AdminRoleService adminRoleService;

    @Autowired
    private FunctionService functionService;

    /**
     * 统一权限校验(公共方法)
     *
     * @param query        校验参数
     * @param requireLogin 是否强制校验登录
     * @param appCode      应用编码
     * @return >0 校验通过, -1 登录超时, 0 不通过
     */
    private Integer doVerify(AuthVerifyVO query, boolean requireLogin, String appCode) {
        // 1. 登录校验(仅 verifyLoginAndUrl 需要)
        if (requireLogin) {
            try {
                Object loginTokenObject = AdminAuthCacheHelper.getAdminLoginCacheService()
                        .getLoginToken(query.getAdminId(), appCode);
                if (loginTokenObject == null) {
                    return -1;
                }
                if (!StringUtils.equals(query.getLoginToken(), String.valueOf(loginTokenObject))) {
                    return -1;
                }
            } catch (Exception e) {
                logger.warn("Redis login check failed, pass through adminId={}", query.getAdminId());
            }
        }

        boolean isPermissionMode = (query.getPermissions() != null && query.getPermissions().length > 0);

        // 2. 查询用户角色
        AdminRole queryAdminRole = new AdminRole();
        queryAdminRole.setAdminId(query.getAdminId());
        queryAdminRole.setAppCode(appCode);
        queryAdminRole.setDeleteStatus((short) 0);

        boolean cacheIsRead = true;
        List<AdminRole> adminRoles = null;
        AdminRoleCacheService adminRoleCacheService = AdminAuthCacheHelper.getAdminRoleCacheService();
        try {
            if (adminRoleCacheService != null) {
                AdminRoleCacheVO vo = new AdminRoleCacheVO();
                BeanUtils.copyProperties(vo, queryAdminRole);
                List<AdminRoleCacheVO> vos = adminRoleCacheService.queryByEntity(vo);
                if (CollectionUtils.isNotEmpty(vos)) {
                    adminRoles = JSON.parseArray(JSONObject.toJSONString(vos), AdminRole.class);
                }
            }
        } catch (Exception e) {
            cacheIsRead = false;
            logger.warn(e.getMessage(), e);
        }
        if (adminRoles == null) {
            adminRoles = adminRoleService.findListByEntity(queryAdminRole);
        }
        if (CollectionUtils.isEmpty(adminRoles)) {
            return 0;
        }

        Integer count = 0;

        // 3. 权限标识模式: 批量查Function + RoleFunction, 内存匹配
        if (isPermissionMode) {
            Set<String> roleIdSet = new HashSet<>();
            Set<String> permSet = new HashSet<>();
            for (String p : query.getPermissions()) {
                if (StringUtils.isNotEmpty(p)) {
                    permSet.add(p);
                }
            }
            for (AdminRole ar : adminRoles) {
                if (ar != null && StringUtils.isNotEmpty(ar.getRoleId())) {
                    roleIdSet.add(ar.getRoleId());
                }
            }
            if (!roleIdSet.isEmpty() && !permSet.isEmpty()) {
                FunctionVO permQuery = new FunctionVO();
                permQuery.setAppCode(appCode);
                permQuery.setDeleteStatus((short) 0);
                permQuery.setEnableStatus((short) 1);
                List<Function> allFunctions = functionService.findListByEntity(permQuery);
                Set<String> functionIdSet = new HashSet<>();
                if (CollectionUtils.isNotEmpty(allFunctions)) {
                    for (Function f : allFunctions) {
                        if (StringUtils.isNotEmpty(f.getPermission()) && permSet.contains(f.getPermission())
                                && StringUtils.isNotEmpty(f.getFunctionId())) {
                            functionIdSet.add(f.getFunctionId());
                        }
                    }
                }
                if (!functionIdSet.isEmpty()) {
                    List<RoleFunction> roleFunctions = roleFunctionService.findListByRoleIdsAndAppCode(
                            roleIdSet.toArray(new String[0]), appCode);
                    if (CollectionUtils.isNotEmpty(roleFunctions)) {
                        for (RoleFunction rf : roleFunctions) {
                            if (functionIdSet.contains(rf.getFunctionId())) {
                                count++;
                            }
                        }
                    }
                }
            }
        } else {
            // 4. URL模式: 逐角色查缓存+DB
            FunctionCacheService functionCacheService = AdminAuthCacheHelper.getFunctionCacheService();
            RoleFunctionCacheService roleFunctionCacheService = AdminAuthCacheHelper.getRoleFunctionCacheService();
            for (int i = 0; i < adminRoles.size(); i++) {
                if (adminRoles.get(i) == null) {
                    continue;
                }
                String roleId = adminRoles.get(i).getRoleId();

                FunctionVO qf = new FunctionVO();
                qf.setUrl(query.getUrl());
                qf.setAppCode(appCode);
                qf.setDeleteStatus((short) 0);
                qf.setEnableStatus((short) 1);

                List<Function> functionList = new ArrayList<>();
                try {
                    if (cacheIsRead && functionCacheService != null) {
                        FunctionCacheVO fvo = new FunctionCacheVO();
                        BeanUtils.copyProperties(fvo, qf);
                        List<FunctionCacheVO> fvos = functionCacheService.queryByEntity(fvo);
                        if (CollectionUtils.isNotEmpty(fvos)) {
                            List<Function> cacheList = JSON.parseArray(
                                    JSONObject.toJSONString(fvos), Function.class);
                            for (Function fc : cacheList) {
                                if (fc.getUrl() != null && fc.getUrl().equals(query.getUrl())) {
                                    functionList.add(fc);
                                }
                            }
                        }
                    }
                } catch (Exception e) {
                    cacheIsRead = false;
                    logger.warn(e.getMessage(), e);
                }
                if (CollectionUtils.isEmpty(functionList)) {
                    functionList = functionService.findListByEntity(qf);
                }
                for (Function function : functionList) {
                    if (function.getFunctionId() == null) {
                        continue;
                    }
                    RoleFunctionVO qrf = new RoleFunctionVO();
                    qrf.setFunctionId(function.getFunctionId());
                    qrf.setRoleId(roleId);
                    qrf.setDeleteStatus((short) 0);
                    qrf.setAppCode(appCode);

                    try {
                        if (cacheIsRead && roleFunctionCacheService != null) {
                            RoleFunctionCacheVO rfvo = new RoleFunctionCacheVO();
                            BeanUtils.copyProperties(rfvo, qrf);
                            List<RoleFunctionCacheVO> rfvos = roleFunctionCacheService.queryByEntity(rfvo);
                            if (CollectionUtils.isNotEmpty(rfvos)) {
                                count = rfvos.size();
                            }
                        }
                    } catch (Exception e) {
                        cacheIsRead = false;
                        logger.warn(e.getMessage(), e);
                    }
                    if (count == 0) {
                        List<RoleFunction> rfs = roleFunctionService.findListByEntity(qrf);
                        if (CollectionUtils.isNotEmpty(rfs)) {
                            count = rfs.size();
                        }
                    }
                }
                if (count > 0) {
                    break;
                }
            }
        }

        // 5. 校验通过, 延长登录会话
        if (count != null && count > 0) {
            AdminAuthCacheHelper.getAdminLoginCacheService().loginTokenDelay(query.getAdminId());
        }
        return count;
    }


    /**
     * 校验权限(/auth/verify)
     */
    @RequestCheck(requireEntity = true)
    public ResultObjectVO verify(RequestJsonVO requestVo) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        resultObjectVO.setData(false);
        try {
            AuthVerifyVO query = JSONObject.parseObject(requestVo.getEntityJson(), AuthVerifyVO.class);
            Check.notEmpty(query.getAdminId(), AdminResultVO.FAILD, "adminId为空");
            Check.notEmpty(query.getAppCode(), AdminResultVO.FAILD, "appCode为空");
            boolean isPerm = (query.getPermissions() != null && query.getPermissions().length > 0);
            if (!isPerm) {
                Check.notEmpty(query.getUrl(), AdminResultVO.FAILD, "url为空");
            }
            Integer c = doVerify(query, false, requestVo.getAppCode());
            if (c != null && c > 0) {
                resultObjectVO.setData(true);
            }
        } catch (BusinessValidationException e) {
            return ResultObjectVO.fail(e.getCode(), e.getMessage());
        } catch (Exception e) {
            logger.warn(e.getMessage(), e);
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请稍后重试");
        }
        return resultObjectVO;
    }


    /**
     * 校验登录+URL权限(/auth/verifyLoginAndUrl)
     *
     * @return -1登录超时 -2权限校验失败 1成功
     */
    @RequestCheck(requireEntity = true)
    public ResultObjectVO verifyLoginAndUrl(RequestJsonVO requestVo) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            AuthVerifyVO query = JSONObject.parseObject(requestVo.getEntityJson(), AuthVerifyVO.class);

            Check.notEmpty(query.getAdminId(), AdminResultVO.FAILD, "adminId为空");
            Check.notEmpty(query.getAppCode(), AdminResultVO.FAILD, "appCode为空");
            Check.notEmpty(query.getUrl(), AdminResultVO.FAILD, "url为空");
            Check.notEmpty(query.getLoginToken(), AdminResultVO.FAILD, "loginToken为空");

            Integer c = doVerify(query, true, requestVo.getAppCode());
            if (c == -1) {
                resultObjectVO.setData(-1);
                return resultObjectVO;
            }
            if (c == null || c == 0) {
                resultObjectVO.setData(-2);
                return resultObjectVO;
            }
            resultObjectVO.setData(1);
        } catch (BusinessValidationException e) {
            return ResultObjectVO.fail(e.getCode(), e.getMessage());
        } catch (Exception e) {
            resultObjectVO.setData(-1);
            logger.warn(e.getMessage(), e);
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请稍后重试");
        }
        return resultObjectVO;
    }

}
