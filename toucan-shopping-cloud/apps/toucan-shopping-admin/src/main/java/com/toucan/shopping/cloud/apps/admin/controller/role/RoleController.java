package com.toucan.shopping.cloud.apps.admin.controller.role;


import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.toucan.shopping.cloud.admin.auth.api.AdminRoleServiceAPI;
import com.toucan.shopping.cloud.admin.auth.api.RoleFunctionServiceAPI;
import com.toucan.shopping.cloud.admin.auth.api.RoleServiceAPI;
import com.toucan.shopping.modules.admin.auth.entity.AdminRole;
import com.toucan.shopping.modules.admin.auth.entity.Role;
import com.toucan.shopping.modules.admin.auth.holder.AdminLoginHolder;
import com.toucan.shopping.modules.admin.auth.page.RoleFunctionPageInfo;
import com.toucan.shopping.modules.admin.auth.page.RolePageInfo;
import com.toucan.shopping.modules.admin.auth.vo.AdminAppVO;
import com.toucan.shopping.modules.admin.auth.vo.RoleFunctionVO;
import com.toucan.shopping.modules.admin.auth.vo.RoleTreeVO;
import com.toucan.shopping.modules.admin.auth.vo.RoleVO;
import com.toucan.shopping.modules.auth.admin.AdminAuth;
import com.toucan.shopping.modules.common.generator.RequestJsonVOGenerator;
import com.toucan.shopping.modules.common.properties.Toucan;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.layui.vo.TableVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

@RestController
@RequestMapping("/role")
public class RoleController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Value("${toucan.app-code}")
    private String appCode;

    @Autowired
    private Toucan toucan;

    @Autowired
    private RoleServiceAPI roleServiceAPI;

    @Autowired
    private RoleFunctionServiceAPI roleFunctionService;

    @Autowired
    private AdminRoleServiceAPI adminRoleServiceAPI;


    /**
     * 修改
     */
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH, verifyType = AdminAuth.VERIFY_TYPE_ANY, permissions = {"toucan:admin:role:update"})
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public ResultObjectVO update(HttpServletRequest request, @RequestBody Role role) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            role.setAppCode(toucan.getAppCode());
            role.setUpdateAdminId(AdminLoginHolder.getCurrentAdminId());
            role.setUpdateDate(new Date());
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(appCode, role);
            resultObjectVO = roleServiceAPI.update(requestJsonVO);
        } catch (Exception e) {
            resultObjectVO.setMsg("请重试");
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            logger.warn(e.getMessage(), e);
        }
        return resultObjectVO;
    }


    public void setTreeNodeSelect(AtomicLong id, RoleTreeVO parentTree, List<RoleTreeVO> roleTreeVOS, RoleTreeVO parentNode, List<AdminRole> adminRoles) {
        for (RoleTreeVO roleTreeVO : roleTreeVOS) {
            roleTreeVO.setId(id.incrementAndGet());
            roleTreeVO.setNodeId(roleTreeVO.getId());
            roleTreeVO.setParentId(parentTree.getId());
            for (AdminRole adminRole : adminRoles) {
                if (adminRole.getRoleId().equals(roleTreeVO.getRoleId())) {
                    parentNode.getState().setChecked(true);
                    roleTreeVO.getState().setChecked(true);
                }
            }
        }
    }


    /**
     * 查询当前账号可管理的所有角色树
     */
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH, verifyType = AdminAuth.VERIFY_TYPE_ANY, permissions = {"toucan:admin:admin:tree:list"})
    @RequestMapping(value = "/query/admin/role/tree", method = RequestMethod.POST)
    public ResultObjectVO queryRoleTree(HttpServletRequest request, @RequestBody AdminAppVO entity) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(), entity);
            resultObjectVO = roleServiceAPI.queryAdminRoleTree(requestJsonVO);
            if (resultObjectVO.isSuccess()) {
                List<RoleTreeVO> roleTreeVOS = JSONArray.parseArray(JSONObject.toJSONString(resultObjectVO.getData()), RoleTreeVO.class);

                AtomicLong id = new AtomicLong();
                AdminRole queryAdminRole = new AdminRole();
                queryAdminRole.setAdminId(entity.getAdminId());
                requestJsonVO = RequestJsonVOGenerator.generator(appCode, queryAdminRole);
                resultObjectVO = adminRoleServiceAPI.queryListByEntity(requestJsonVO);
                if (resultObjectVO.isSuccess()) {
                    List<AdminRole> adminRoles = JSONArray.parseArray(JSONObject.toJSONString(resultObjectVO.getData()), AdminRole.class);
                    if (!CollectionUtils.isEmpty(adminRoles)) {
                        for (RoleTreeVO roleTreeVO : roleTreeVOS) {
                            roleTreeVO.setId(id.incrementAndGet());
                            roleTreeVO.setNodeId(roleTreeVO.getId());
                            setTreeNodeSelect(id, roleTreeVO, roleTreeVO.getChildren(), roleTreeVO, adminRoles);
                        }
                    }
                }
                resultObjectVO.setData(roleTreeVOS);
            }
            return resultObjectVO;
        } catch (Exception e) {
            resultObjectVO.setMsg("请求失败");
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            logger.warn(e.getMessage(), e);
        }
        return resultObjectVO;
    }


    /**
     * 保存
     */
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH, verifyType = AdminAuth.VERIFY_TYPE_ANY, permissions = {"toucan:admin:role:save"})
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public ResultObjectVO save(HttpServletRequest request, @RequestBody Role entity) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            entity.setAppCode(toucan.getAppCode());
            entity.setCreateAdminId(AdminLoginHolder.getCurrentAdminId());
            entity.setCreateDate(new Date());
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(appCode, entity);
            resultObjectVO = roleServiceAPI.save(requestJsonVO);
        } catch (Exception e) {
            resultObjectVO.setMsg("请重试");
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            logger.warn(e.getMessage(), e);
        }
        return resultObjectVO;
    }


    /**
     * 关联功能项
     */
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH, verifyType = AdminAuth.VERIFY_TYPE_ANY, permissions = {"toucan:admin:role:connect:functions"})
    @RequestMapping(value = "/connect/functions", method = RequestMethod.POST)
    public ResultObjectVO connectFunctions(HttpServletRequest request, @RequestBody RoleFunctionVO roleFunctionVO) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            roleFunctionVO.setCreateAdminId(AdminLoginHolder.getCurrentAdminId());
            roleFunctionVO.setCreateDate(new Date());
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(appCode, roleFunctionVO);
            resultObjectVO = roleFunctionService.saveFunctions(requestJsonVO);
        } catch (Exception e) {
            resultObjectVO.setMsg("请重试");
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            logger.warn(e.getMessage(), e);
        }
        return resultObjectVO;
    }


    /**
     * 刷新缓存
     */
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH, verifyType = AdminAuth.VERIFY_TYPE_ANY, permissions = {"toucan:admin:role:cache:functions"})
    @RequestMapping(value = "/refresh/cache/functions", method = RequestMethod.POST)
    public ResultObjectVO refreshFunctionsCache(HttpServletRequest request, @RequestBody RoleFunctionVO roleFunctionVO) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            roleFunctionVO.setCreateAdminId(AdminLoginHolder.getCurrentAdminId());
            roleFunctionVO.setCreateDate(new Date());
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(appCode, roleFunctionVO);
            resultObjectVO = roleFunctionService.refreshCache(requestJsonVO);
        } catch (Exception e) {
            resultObjectVO.setMsg("请重试");
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            logger.warn(e.getMessage(), e);
        }
        return resultObjectVO;
    }


    /**
     * 查询列表
     */
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH, verifyType = AdminAuth.VERIFY_TYPE_ANY, permissions = {"toucan:admin:role:list"})
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    public TableVO list(@RequestBody RolePageInfo pageInfo) {
        TableVO tableVO = new TableVO();
        try {
            pageInfo.setAppCode(toucan.getAppCode());
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(), pageInfo);
            ResultObjectVO resultObjectVO = roleServiceAPI.listPage(requestJsonVO);
            if (resultObjectVO.getCode() == ResultObjectVO.SUCCESS) {
                if (resultObjectVO.getData() != null) {
                    Map<String, Object> resultObjectDataMap = (Map<String, Object>) resultObjectVO.getData();
                    tableVO.setCount(Long.parseLong(String.valueOf(resultObjectDataMap.get("total") != null ? resultObjectDataMap.get("total") : "0")));
                    if (tableVO.getCount() > 0) {
                        tableVO.setData((List<Object>) resultObjectDataMap.get("list"));
                    }
                }
            }
        } catch (Exception e) {
            tableVO.setMsg("请重试");
            tableVO.setCode(TableVO.FAILD);
            logger.warn(e.getMessage(), e);
        }
        return tableVO;
    }


    /**
     * 删除
     */
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH, verifyType = AdminAuth.VERIFY_TYPE_ANY, permissions = {"toucan:admin:role:delete"})
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public ResultObjectVO deleteById(HttpServletRequest request, @RequestBody Role role) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            if (role.getId() == null) {
                resultObjectVO.setMsg("请传入ID");
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                return resultObjectVO;
            }
            role.setUpdateAdminId(AdminLoginHolder.getCurrentAdminId());

            String entityJson = JSONObject.toJSONString(role);
            RequestJsonVO requestVo = new RequestJsonVO();
            requestVo.setAppCode(appCode);
            requestVo.setEntityJson(entityJson);
            resultObjectVO = roleServiceAPI.deleteById(requestVo);
        } catch (Exception e) {
            resultObjectVO.setMsg("请重试");
            resultObjectVO.setCode(TableVO.FAILD);
            logger.warn(e.getMessage(), e);
        }
        return resultObjectVO;
    }


    /**
     * 批量删除
     */
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH, verifyType = AdminAuth.VERIFY_TYPE_ANY, permissions = {"toucan:admin:role:deletes"})
    @RequestMapping(value = "/delete/ids", method = RequestMethod.POST)
    public ResultObjectVO deleteByIds(HttpServletRequest request, @RequestBody List<RoleVO> roleVOS) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            if (CollectionUtils.isEmpty(roleVOS)) {
                resultObjectVO.setMsg("请传入ID");
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                return resultObjectVO;
            }
            String entityJson = JSONObject.toJSONString(roleVOS);
            RequestJsonVO requestVo = new RequestJsonVO();
            requestVo.setAppCode(appCode);
            requestVo.setEntityJson(entityJson);
            resultObjectVO = roleServiceAPI.deleteByIds(requestVo);
        } catch (Exception e) {
            resultObjectVO.setMsg("请重试");
            resultObjectVO.setCode(TableVO.FAILD);
            logger.warn(e.getMessage(), e);
        }
        return resultObjectVO;
    }


    /**
     * 查询详情
     */
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH, verifyType = AdminAuth.VERIFY_TYPE_ANY, permissions = {"toucan:admin:role:detail"})
    @RequestMapping(value = "/queryDetail", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResultObjectVO queryDetail(HttpServletRequest request, @RequestBody Role role) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(appCode, role);
            resultObjectVO = roleServiceAPI.queryDetail(requestJsonVO);
        } catch (Exception e) {
            logger.warn(e.getMessage(), e);
        }
        return resultObjectVO;
    }


    /**
     * 查询角色功能列表
     */
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH, verifyType = AdminAuth.VERIFY_TYPE_ANY, permissions = {"toucan:admin:role:function:list"})
    @RequestMapping(value = "/function/list/page", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResultObjectVO queryRoleFunctionListPage(HttpServletRequest request, @RequestBody RoleFunctionPageInfo pageInfo) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(appCode, pageInfo);
            resultObjectVO = roleServiceAPI.queryRoleFunctionListPage(requestJsonVO);
        } catch (Exception e) {
            logger.warn(e.getMessage(), e);
        }
        return resultObjectVO;
    }

}
