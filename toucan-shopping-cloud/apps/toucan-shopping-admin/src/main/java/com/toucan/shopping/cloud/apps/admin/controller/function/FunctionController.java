package com.toucan.shopping.cloud.apps.admin.controller.function;


import com.alibaba.fastjson2.JSONObject;
import com.toucan.shopping.cloud.admin.auth.api.AppServiceAPI;
import com.toucan.shopping.cloud.admin.auth.api.FunctionServiceAPI;
import com.toucan.shopping.cloud.admin.auth.api.RoleFunctionServiceAPI;
import com.toucan.shopping.modules.admin.auth.entity.App;
import com.toucan.shopping.modules.admin.auth.entity.Function;
import com.toucan.shopping.modules.admin.auth.entity.RoleFunction;
import com.toucan.shopping.modules.admin.auth.holder.AdminLoginHolder;
import com.toucan.shopping.modules.admin.auth.page.FunctionTreeInfo;
import com.toucan.shopping.modules.admin.auth.vo.AppFunctionTreeVO;
import com.toucan.shopping.modules.admin.auth.vo.FunctionTreeVO;
import com.toucan.shopping.modules.admin.auth.vo.FunctionVO;
import com.toucan.shopping.modules.admin.auth.vo.RoleFunctionVO;
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
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 功能项控制器
 */
@RestController
@RequestMapping("/function")
public class FunctionController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Value("${toucan.app-code}")
    private String appCode;

    @Autowired
    private Toucan toucan;

    @Autowired
    private FunctionServiceAPI functionServiceAPI;

    @Autowired
    private AppServiceAPI appServiceAPI;

    @Autowired
    private RoleFunctionServiceAPI roleFunctionServiceAPI;


    /**
     * 修改
     */
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH, verifyType = AdminAuth.VERIFY_TYPE_ANY, permissions = {"toucan:admin:function:update"})
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public ResultObjectVO update(HttpServletRequest request, @RequestBody Function entity) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            entity.setUpdateAdminId(AdminLoginHolder.getCurrentAdminId());
            entity.setUpdateDate(new Date());
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(appCode, entity);
            resultObjectVO = functionServiceAPI.update(requestJsonVO);
        } catch (Exception e) {
            resultObjectVO.setMsg("请重试");
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            logger.warn(e.getMessage(), e);
        }
        return resultObjectVO;
    }


    /**
     * 保存
     */
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH, verifyType = AdminAuth.VERIFY_TYPE_ANY, permissions = {"toucan:admin:function:save"})
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public ResultObjectVO save(HttpServletRequest request, @RequestBody Function entity) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            entity.setCreateAdminId(AdminLoginHolder.getCurrentAdminId());
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(appCode, entity);
            resultObjectVO = functionServiceAPI.save(requestJsonVO);
        } catch (Exception e) {
            resultObjectVO.setMsg("请重试");
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            logger.warn(e.getMessage(), e);
        }
        return resultObjectVO;
    }


    /**
     * 查询树表格
     */
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH, verifyType = AdminAuth.VERIFY_TYPE_ANY, permissions = {"toucan:admin:function:tree"})
    @RequestMapping(value = "/tree/table", method = RequestMethod.POST)
    public ResultObjectVO treeTable(HttpServletRequest request, @RequestBody FunctionTreeInfo queryPageInfo) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            queryPageInfo.setAppCode(toucan.getAppCode());
            queryPageInfo.setAdminId(AdminLoginHolder.getCurrentAdminId());
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(), queryPageInfo);
            resultObjectVO = functionServiceAPI.queryAppFunctionTreeTable(requestJsonVO);
            return resultObjectVO;
        } catch (Exception e) {
            resultObjectVO.setMsg("请重试");
            resultObjectVO.setCode(TableVO.FAILD);
            logger.warn(e.getMessage(), e);
        }
        return resultObjectVO;
    }


    /**
     * 查询树表格（按父ID）
     */
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH, verifyType = AdminAuth.VERIFY_TYPE_ANY, permissions = {"toucan:admin:function:tree"})
    @RequestMapping(value = "/tree/table/by/pid", method = RequestMethod.POST)
    public ResultObjectVO treeTableByPid(HttpServletRequest request, @RequestBody FunctionTreeInfo queryPageInfo) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            queryPageInfo.setAppCode(toucan.getAppCode());
            queryPageInfo.setAdminId(AdminLoginHolder.getCurrentAdminId());
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(), queryPageInfo);
            resultObjectVO = functionServiceAPI.queryAppFunctionTreeTableByPid(requestJsonVO);
            return resultObjectVO;
        } catch (Exception e) {
            resultObjectVO.setMsg("请重试");
            resultObjectVO.setCode(TableVO.FAILD);
            logger.warn(e.getMessage(), e);
        }
        return resultObjectVO;
    }


    /**
     * 删除功能项
     */
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH, verifyType = AdminAuth.VERIFY_TYPE_ANY, permissions = {"toucan:admin:function:delete"})
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public ResultObjectVO deleteById(HttpServletRequest request, @RequestBody Function function) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            if (function.getId() == null) {
                resultObjectVO.setMsg("请传入ID");
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                return resultObjectVO;
            }
            String entityJson = JSONObject.toJSONString(function);
            RequestJsonVO requestVo = new RequestJsonVO();
            requestVo.setAppCode(appCode);
            requestVo.setEntityJson(entityJson);
            resultObjectVO = functionServiceAPI.deleteById(requestVo);
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
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH)
    @RequestMapping(value = "/delete/ids", method = RequestMethod.POST)
    public ResultObjectVO deleteByIds(HttpServletRequest request, @RequestBody List<FunctionVO> functionVOS) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            if (CollectionUtils.isEmpty(functionVOS)) {
                resultObjectVO.setMsg("请传入ID");
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                return resultObjectVO;
            }
            String entityJson = JSONObject.toJSONString(functionVOS);
            RequestJsonVO requestVo = new RequestJsonVO();
            requestVo.setAppCode(appCode);
            requestVo.setEntityJson(entityJson);
            resultObjectVO = functionServiceAPI.deleteByIds(requestVo);
        } catch (Exception e) {
            resultObjectVO.setMsg("请重试");
            resultObjectVO.setCode(TableVO.FAILD);
            logger.warn(e.getMessage(), e);
        }
        return resultObjectVO;
    }


    /**
     * 查询应用功能树
     */
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH, verifyType = AdminAuth.VERIFY_TYPE_ANY, permissions = {"toucan:admin:function:query:app:function:tree"})
    @RequestMapping(value = "/query/app/function/tree", method = RequestMethod.POST)
    public ResultObjectVO queryAppFunctionTree(HttpServletRequest request, @RequestBody FunctionTreeVO functionTreeVO) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            if (functionTreeVO.getId() == null) {
                App query = new App();
                query.setCode(toucan.getAppCode());
                RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(appCode, query);
                resultObjectVO = appServiceAPI.findByCode(requestJsonVO);
                if (resultObjectVO.isSuccess()) {
                    App rootNode = resultObjectVO.formatData(App.class);
                    AppFunctionTreeVO appFunctionTreeVO = new AppFunctionTreeVO();
                    appFunctionTreeVO.setId(-1L);
                    appFunctionTreeVO.setPid(-2L);
                    appFunctionTreeVO.setParentId(-2L);
                    appFunctionTreeVO.setAppCode(rootNode.getCode());
                    appFunctionTreeVO.setTitle(toucan.getAppCode() + " " + rootNode.getName());
                    appFunctionTreeVO.setName(toucan.getAppCode() + " " + rootNode.getName());
                    appFunctionTreeVO.setEnableStatus((short) 1);
                    appFunctionTreeVO.setIsParent(true);
                    List<AppFunctionTreeVO> appFunctionTreeVOS = new LinkedList<>();
                    appFunctionTreeVOS.add(appFunctionTreeVO);
                    resultObjectVO.setData(appFunctionTreeVOS);
                }
            } else {
                functionTreeVO.setParentId(functionTreeVO.getId());
                functionTreeVO.setAppCode(toucan.getAppCode());
                RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(appCode, functionTreeVO);
                return functionServiceAPI.queryAppFunctionTreeByPid(requestJsonVO);
            }

        } catch (Exception e) {
            resultObjectVO.setMsg("请求失败");
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            logger.warn(e.getMessage(), e);
        }
        return resultObjectVO;
    }


    public void setTreeNodeSelect(AtomicLong id, FunctionTreeVO parentTreeVO, List<FunctionTreeVO> functionTreeVOList, List<RoleFunction> roleFunctions) {
        for (FunctionTreeVO functionTreeVO : functionTreeVOList) {
            functionTreeVO.setId(id.incrementAndGet());
            functionTreeVO.setNodeId(functionTreeVO.getId());
            functionTreeVO.setPid(parentTreeVO.getId());
            functionTreeVO.setParentId(functionTreeVO.getPid());
            for (RoleFunction roleFunction : roleFunctions) {
                if (functionTreeVO.getFunctionId().equals(roleFunction.getFunctionId())) {
                    functionTreeVO.getState().setChecked(true);
                }
            }
            if (!CollectionUtils.isEmpty(functionTreeVO.getChildren())) {
                setTreeNodeSelect(id, functionTreeVO, functionTreeVO.getChildren(), roleFunctions);
            }
        }
    }


    /**
     * 返回指定角色下的功能树
     */
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH, verifyType = AdminAuth.VERIFY_TYPE_ANY, permissions = {"toucan:admin:function:role:function:tree"})
    @RequestMapping(value = "/query/role/function/tree", method = RequestMethod.POST)
    public ResultObjectVO queryRoleFunctionTree(HttpServletRequest request, @RequestBody RoleFunctionVO roleFunctionVO) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            if (roleFunctionVO.getPid() == null) {
                roleFunctionVO.setPid(-1L);
            }
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(), roleFunctionVO);
            resultObjectVO = roleFunctionServiceAPI.queryFunctionTreeByRoleIdAndParentId(requestJsonVO);
            if (resultObjectVO.isSuccess()) {
                List<FunctionTreeVO> fucntionTreeVOS = resultObjectVO.formatDataList(FunctionTreeVO.class);
                if (!CollectionUtils.isEmpty(fucntionTreeVOS)) {
                    for (FunctionTreeVO functionTreeVO : fucntionTreeVOS) {
                        functionTreeVO.setUrl(null);
                    }
                }

                resultObjectVO.setData(fucntionTreeVOS);
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
     * 查询角色完整功能树（含嵌套children）
     */
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH, verifyType = AdminAuth.VERIFY_TYPE_ANY, permissions = {"toucan:admin:function:role:function:full:tree"})
    @RequestMapping(value = "/query/role/function/full/tree", method = RequestMethod.POST)
    public ResultObjectVO queryRoleFunctionFullTree(HttpServletRequest request, @RequestBody RoleFunctionVO roleFunctionVO) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(), roleFunctionVO);
            resultObjectVO = roleFunctionServiceAPI.queryRoleFunctionFullTree(requestJsonVO);
        } catch (Exception e) {
            resultObjectVO.setMsg("请求失败");
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            logger.warn(e.getMessage(), e);
        }
        return resultObjectVO;
    }


    /**
     * 查询功能项详情
     */
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH, verifyType = AdminAuth.VERIFY_TYPE_ANY, permissions = {"toucan:admin:function:detail"})
    @RequestMapping(value = "/detail", method = RequestMethod.POST)
    public ResultObjectVO queryDetail(HttpServletRequest request, @RequestBody Function entity) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(appCode, entity);
            resultObjectVO = functionServiceAPI.queryDetail(requestJsonVO);
        } catch (Exception e) {
            resultObjectVO.setMsg("请求失败");
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            logger.warn(e.getMessage(), e);
        }
        return resultObjectVO;
    }

}
