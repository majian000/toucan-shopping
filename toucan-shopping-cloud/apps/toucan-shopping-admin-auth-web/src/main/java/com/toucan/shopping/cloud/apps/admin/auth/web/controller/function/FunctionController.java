package com.toucan.shopping.cloud.apps.admin.auth.web.controller.function;


import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.toucan.shopping.cloud.admin.auth.api.*;
import com.toucan.shopping.cloud.apps.admin.auth.web.controller.base.UIController;
import com.toucan.shopping.modules.admin.auth.holder.AdminLoginHolder;
import com.toucan.shopping.modules.admin.auth.vo.AppFunctionTreeVO;
import com.toucan.shopping.modules.admin.auth.vo.RoleFunctionVO;
import com.toucan.shopping.modules.common.generator.IdGenerator;
import com.toucan.shopping.modules.layui.vo.TableVO;
import com.toucan.shopping.modules.admin.auth.entity.App;
import com.toucan.shopping.modules.admin.auth.entity.Function;
import com.toucan.shopping.modules.admin.auth.entity.RoleFunction;
import com.toucan.shopping.modules.admin.auth.page.FunctionTreeInfo;
import com.toucan.shopping.modules.admin.auth.vo.FunctionTreeVO;
import com.toucan.shopping.modules.admin.auth.vo.FunctionVO;
import com.toucan.shopping.modules.auth.admin.AdminAuth;
import com.toucan.shopping.modules.common.generator.RequestJsonVOGenerator;
import com.toucan.shopping.modules.common.properties.Toucan;
import com.toucan.shopping.modules.common.util.AuthHeaderUtil;
import com.toucan.shopping.modules.common.util.SignUtil;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
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
@Controller
@RequestMapping("/function")
public class FunctionController extends UIController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Value("${toucan.app-code}")
    private String appCode;

    @Autowired
    private Toucan toucan;

    @Autowired
    private FunctionServiceAPI functionServiceAPI;

    @Autowired
    private AdminAppServiceAPI adminAppServiceAPI;

    @Autowired
    private AppServiceAPI appServiceAPI;

    @Autowired
    private RoleFunctionServiceAPI roleFunctionServiceAPI;

    @Autowired
    private RoleServiceAPI roleServiceAPI;
    @Autowired
    private IdGenerator idGenerator;













    /**
     * 修改
     * @param entity
     * @return
     */
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH, verifyType = AdminAuth.VERIFY_TYPE_ANY, permissions = {"pms:system:menu:update"})
    @RequestMapping(value = "/update",method = RequestMethod.POST)
    @ResponseBody
    public ResultObjectVO update(HttpServletRequest request,@RequestBody Function entity)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            entity.setUpdateAdminId(AdminLoginHolder.getCurrentAdminId());
            entity.setUpdateDate(new Date());
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(appCode, entity);
            resultObjectVO = functionServiceAPI.update(requestJsonVO);
        }catch(Exception e)
        {
            resultObjectVO.setMsg("请重试");
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            logger.warn(e.getMessage(),e);
        }
        return resultObjectVO;
    }



    /**
     * 保存
     * @param entity
     * @return
     */
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH, verifyType = AdminAuth.VERIFY_TYPE_ANY, permissions = {"pms:system:menu:save"})
    @RequestMapping(value = "/save",method = RequestMethod.POST)
    @ResponseBody
    public ResultObjectVO save(HttpServletRequest request, @RequestBody Function entity)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            entity.setCreateAdminId(AdminLoginHolder.getCurrentAdminId());
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(appCode, entity);
            resultObjectVO = functionServiceAPI.save(requestJsonVO);
        }catch(Exception e)
        {
            resultObjectVO.setMsg("请重试");
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            logger.warn(e.getMessage(),e);
        }
        return resultObjectVO;
    }


    /**
     * 批量保存
     * @param entitys
     * @return
     */
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH, verifyType = AdminAuth.VERIFY_TYPE_ANY, permissions = {"pms:system:menu:batch-save"})
    @RequestMapping(value = "/saves",method = RequestMethod.POST)
    @ResponseBody
    public ResultObjectVO saves(HttpServletRequest request, @RequestBody List<Function> entitys)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            if(CollectionUtils.isEmpty(entitys)){
                resultObjectVO.setMsg("功能项列表不能为空");
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                return resultObjectVO;
            }
            for(Function function:entitys){
                function.setCreateAdminId(AdminLoginHolder.getCurrentAdminId());
            }
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(appCode, entitys);
            resultObjectVO = functionServiceAPI.saves(requestJsonVO);
        }catch(Exception e)
        {
            resultObjectVO.setMsg("请重试");
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            logger.warn(e.getMessage(),e);
        }
        return resultObjectVO;
    }


    /**
     * 查询列表
     * @param queryPageInfo
     * @return
     */
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH, verifyType = AdminAuth.VERIFY_TYPE_ANY, permissions = {"pms:system:menu:tree"})
    @RequestMapping(value = "/tree/table",method = RequestMethod.GET)
    @ResponseBody
    public ResultObjectVO treeTable(HttpServletRequest request, FunctionTreeInfo queryPageInfo)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            queryPageInfo.setAdminId(AdminLoginHolder.getCurrentAdminId());
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(),queryPageInfo);
            resultObjectVO = functionServiceAPI.queryAppFunctionTreeTable(requestJsonVO);
            return resultObjectVO;
        }catch(Exception e)
        {
            resultObjectVO.setMsg("请重试");
            resultObjectVO.setCode(TableVO.FAILD);
            logger.warn(e.getMessage(),e);
        }
        return resultObjectVO;
    }


    /**
     * 查询列表
     * @param queryPageInfo
     * @return
     */
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH, verifyType = AdminAuth.VERIFY_TYPE_ANY, permissions = {"pms:system:menu:tree"})
    @RequestMapping(value = "/tree/table/by/pid",method = RequestMethod.GET)
    @ResponseBody
    public ResultObjectVO treeTableByPid(HttpServletRequest request, FunctionTreeInfo queryPageInfo)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            queryPageInfo.setAdminId(AdminLoginHolder.getCurrentAdminId());
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(),queryPageInfo);
            resultObjectVO = functionServiceAPI.queryAppFunctionTreeTableByPid(requestJsonVO);
            return resultObjectVO;
        }catch(Exception e)
        {
            resultObjectVO.setMsg("请重试");
            resultObjectVO.setCode(TableVO.FAILD);
            logger.warn(e.getMessage(),e);
        }
        return resultObjectVO;
    }


    /**
     * 删除功能项
     * @param request
     * @return
     */
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH, verifyType = AdminAuth.VERIFY_TYPE_ANY, permissions = {"pms:system:menu:delete"})
    @RequestMapping(value = "/delete",method = RequestMethod.POST)
    @ResponseBody
    public ResultObjectVO deleteById(HttpServletRequest request, @RequestBody Function function)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            if(function.getId() == null)
            {
                resultObjectVO.setMsg("请传入ID");
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                return resultObjectVO;
            }
            function.setUpdateAdminId(AdminLoginHolder.getCurrentAdminId());
                        String entityJson = JSONObject.toJSONString(function);
            RequestJsonVO requestVo = new RequestJsonVO();
            requestVo.setAppCode(appCode);
            requestVo.setEntityJson(entityJson);
            resultObjectVO = functionServiceAPI.deleteById(requestVo);
        }catch(Exception e)
        {
            resultObjectVO.setMsg("请重试");
            resultObjectVO.setCode(TableVO.FAILD);
            logger.warn(e.getMessage(),e);
        }
        return resultObjectVO;
    }



    /**
     * 清空该应用下所有功能项
     * @param request
     * @return
     */
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH)
    @RequestMapping(value = "/delete/by/app/code",method = RequestMethod.POST)
    @ResponseBody
    public ResultObjectVO deleteByAppCode(HttpServletRequest request, @RequestBody FunctionVO functionVO)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            if(StringUtils.isEmpty(functionVO.getAppCode()))
            {
                resultObjectVO.setMsg("请传入应用编码");
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                return resultObjectVO;
            }
            String entityJson = JSONObject.toJSONString(functionVO);
            RequestJsonVO requestVo = new RequestJsonVO();
            requestVo.setAppCode(appCode);
            requestVo.setEntityJson(entityJson);
            resultObjectVO = functionServiceAPI.deleteByAppCode(requestVo);
        }catch(Exception e)
        {
            resultObjectVO.setMsg("请重试");
            resultObjectVO.setCode(TableVO.FAILD);
            logger.warn(e.getMessage(),e);
        }
        return resultObjectVO;
    }



    /**
     * 删除应用
     * @param request
     * @return
     */
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH)
    @RequestMapping(value = "/delete/ids",method = RequestMethod.POST)
    @ResponseBody
    public ResultObjectVO deleteByIds(HttpServletRequest request, @RequestBody List<FunctionVO> functionVOS)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            if(CollectionUtils.isEmpty(functionVOS))
            {
                resultObjectVO.setMsg("请传入ID");
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                return resultObjectVO;
            }
            String entityJson = JSONObject.toJSONString(functionVOS);
            RequestJsonVO requestVo = new RequestJsonVO();
            requestVo.setAppCode(appCode);
            requestVo.setEntityJson(entityJson);
            resultObjectVO = functionServiceAPI.deleteByIds( requestVo);
        }catch(Exception e)
        {
            resultObjectVO.setMsg("请重试");
            resultObjectVO.setCode(TableVO.FAILD);
            logger.warn(e.getMessage(),e);
        }
        return resultObjectVO;
    }



    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH,requestType = AdminAuth.REQUEST_JSON,responseType=AdminAuth.RESPONSE_JSON)
    @RequestMapping(value = "/query/app/function/tree")
    @ResponseBody
    public ResultObjectVO queryAppFunctionTree(HttpServletRequest request,FunctionTreeVO functionTreeVO)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            //默认查询根节点
            if(functionTreeVO.getId()==null)
            {
                App query = new App();
                RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(appCode,query);
                resultObjectVO = appServiceAPI.list(requestJsonVO);
                if(resultObjectVO.isSuccess())
                {
                    List<App> rootNodes = resultObjectVO.formatDataList(App.class);
                    List<AppFunctionTreeVO> appFunctionTreeVOS = new LinkedList<>();
                    for(App rootNode:rootNodes) {
                        //随机生成一个应用节点ID,必须是负数
                        AppFunctionTreeVO appFunctionTreeVO = new AppFunctionTreeVO();
                        appFunctionTreeVO.setId((0-idGenerator.id()));
                        appFunctionTreeVO.setPid(-2L);
                        appFunctionTreeVO.setParentId(-2L);
                        appFunctionTreeVO.setAppCode(rootNode.getCode());
                        appFunctionTreeVO.setTitle(rootNode.getCode() + " " + rootNode.getName());
                        appFunctionTreeVO.setName(rootNode.getCode() + " " + rootNode.getName());
                        appFunctionTreeVO.setEnableStatus((short) 1);
                        appFunctionTreeVO.setIsParent(true);
                        appFunctionTreeVO.setIsAppNode(true); //是应用节点
                        appFunctionTreeVOS.add(appFunctionTreeVO);
                    }
                    resultObjectVO.setData(appFunctionTreeVOS);
                }
            }else{
                //如果展开的是应用下面的第一级节点
                if(functionTreeVO.getIsAppNode()!=null&&functionTreeVO.getIsAppNode().booleanValue()==true)
                {
                    functionTreeVO.setParentId(-1L);
                }else { //就查询这个节点下的子节点(当前选择节点深度大于1)
                    functionTreeVO.setParentId(functionTreeVO.getId());
                }
                RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(appCode,functionTreeVO);
                resultObjectVO = functionServiceAPI.queryAppFunctionTreeByPid(requestJsonVO);
                if(resultObjectVO.isSuccess())
                {
                    List<FunctionTreeVO> functionTreeVOS = resultObjectVO.formatDataList(FunctionTreeVO.class);
                    for(FunctionTreeVO tree:functionTreeVOS)
                    {
                        //手动设置第一级节点的父节点ID为应用编码
                        if(String.valueOf(functionTreeVO.getId()).equals(functionTreeVO.getAppCode()))
                        {
                            tree.setParentId(functionTreeVO.getId());
                        }
                        tree.setIsAppNode(false);
                    }
                    resultObjectVO.setData(functionTreeVOS);
                }
            }
        }catch(Exception e)
        {
            resultObjectVO.setMsg("请求失败");
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            logger.warn(e.getMessage(),e);
        }
        return resultObjectVO;
    }


    public void setTreeNodeSelect(AtomicLong id,FunctionTreeVO parentTreeVO,List<FunctionTreeVO> functionTreeVOList,List<RoleFunction> roleFunctions)
    {
        for(FunctionTreeVO functionTreeVO:functionTreeVOList)
        {
            functionTreeVO.setId(id.incrementAndGet());
            functionTreeVO.setNodeId(functionTreeVO.getId());
            functionTreeVO.setPid(parentTreeVO.getId());
            functionTreeVO.setParentId(functionTreeVO.getPid());
            for(RoleFunction roleFunction:roleFunctions) {
                if(functionTreeVO.getFunctionId().equals(roleFunction.getFunctionId())) {
                    //设置节点被选中
                    functionTreeVO.getState().setChecked(true);
                }
            }
            if(!CollectionUtils.isEmpty(functionTreeVO.getChildren()))
            {
                setTreeNodeSelect(id,functionTreeVO,functionTreeVO.getChildren(),roleFunctions);
            }
        }
    }


    /**
     * 返回指定角色下的功能树
     * @param request
     * @param appCode
     * @param roleId
     * @return
     */





    /**
     * 返回指定角色下的功能树
     * @param request
     * @param appCode
     * @param roleId
     * @return
     */
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH,requestType = AdminAuth.REQUEST_JSON,responseType=AdminAuth.RESPONSE_JSON)
    @RequestMapping(value = "/query/role/function/tree",method = RequestMethod.POST)
    @ResponseBody
    public ResultObjectVO queryRoleFunctionTree(HttpServletRequest request, @RequestBody RoleFunctionVO roleFunctionVO)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            if(roleFunctionVO.getPid() == null)
            {
                roleFunctionVO.setPid(-1L);
            }
            //查询权限树
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(),roleFunctionVO);
            resultObjectVO = roleFunctionServiceAPI.queryFunctionTreeByRoleIdAndParentId(requestJsonVO);
            if(resultObjectVO.isSuccess())
            {
                List<FunctionTreeVO> fucntionTreeVOS = resultObjectVO.formatDataList(FunctionTreeVO.class);
                if(!CollectionUtils.isEmpty(fucntionTreeVOS))
                {
                    for(FunctionTreeVO functionTreeVO:fucntionTreeVOS)
                    {
                        functionTreeVO.setUrl(null);
                    }
                }
                resultObjectVO.setData(fucntionTreeVOS);
            }
            return resultObjectVO;
        }catch(Exception e)
        {
            resultObjectVO.setMsg("请求失败");
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            logger.warn(e.getMessage(),e);
        }
        return resultObjectVO;
    }


    /**
     * 查询指定节点的所有子孙节点(functionId列表)
     */
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH, verifyType = AdminAuth.VERIFY_TYPE_ANY,
            permissions = {"pms:system:role:permission"})
    @RequestMapping(value = "/query/descendants",method = RequestMethod.POST)
    @ResponseBody
    public ResultObjectVO queryDescendants(HttpServletRequest request, @RequestBody Function function)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            if(function.getId() == null) {
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("id为空");
                return resultObjectVO;
            }
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(), function);
            resultObjectVO = functionServiceAPI.queryDescendants(requestJsonVO);
        }catch(Exception e)
        {
            resultObjectVO.setMsg("请求失败");
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            logger.warn(e.getMessage(),e);
        }
        return resultObjectVO;
    }

}

