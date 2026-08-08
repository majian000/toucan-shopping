package com.toucan.shopping.cloud.apps.admin.controller.role;


import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.toucan.shopping.cloud.admin.auth.api.*;
import com.toucan.shopping.cloud.apps.admin.controller.base.UIController;
import com.toucan.shopping.modules.admin.auth.holder.AdminLoginHolder;
import com.toucan.shopping.modules.layui.vo.TableVO;
import com.toucan.shopping.modules.admin.auth.entity.AdminRole;
import com.toucan.shopping.modules.admin.auth.entity.Role;
import com.toucan.shopping.modules.admin.auth.page.RoleFunctionPageInfo;
import com.toucan.shopping.modules.admin.auth.page.RolePageInfo;
import com.toucan.shopping.modules.admin.auth.vo.AdminAppVO;
import com.toucan.shopping.modules.admin.auth.vo.RoleFunctionVO;
import com.toucan.shopping.modules.admin.auth.vo.RoleTreeVO;
import com.toucan.shopping.modules.admin.auth.vo.RoleVO;
import com.toucan.shopping.modules.auth.admin.AdminAuth;
import com.toucan.shopping.modules.common.generator.RequestJsonVOGenerator;
import com.toucan.shopping.modules.common.properties.Toucan;
import com.toucan.shopping.modules.common.util.AuthHeaderUtil;
import com.toucan.shopping.modules.common.util.SignUtil;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
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
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/role")
public class RoleController extends UIController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Value("${toucan.app-code}")
    private String appCode;

    @Autowired
    private Toucan toucan;

    @Autowired
    private RoleServiceAPI roleServiceAPI;

    @Autowired
    private AdminAppServiceAPI adminAppServiceAPI;

    @Autowired
    private RoleFunctionServiceAPI roleFunctionService;

    @Autowired
    private AdminRoleServiceAPI adminRoleServiceAPI;

    @Autowired
    private FunctionServiceAPI functionServiceAPI;


    @Autowired
    private AppServiceAPI appServiceAPI;


    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH,requestType = AdminAuth.REQUEST_FORM,responseType =AdminAuth.RESPONSE_FORM )
    @RequestMapping(value = "/listPage",method = RequestMethod.GET)
    public String page(HttpServletRequest request)
    {
        //初始化选择应用控件
        super.initSelectApp(request,toucan, appServiceAPI);

        //初始化工具条按钮、操作按钮
        super.initButtons(request,toucan,"/role/listPage", functionServiceAPI);

        return "pages/role/list.html";
    }






    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH,requestType = AdminAuth.REQUEST_FORM,responseType =AdminAuth.RESPONSE_FORM )
    @RequestMapping(value = "/addPage",method = RequestMethod.GET)
    public String addPage(HttpServletRequest request)
    {
        //初始化选择应用控件
        super.initSelectApp(request,toucan, appServiceAPI);
        return "pages/role/add.html";
    }



    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH,requestType = AdminAuth.REQUEST_FORM,responseType =AdminAuth.RESPONSE_FORM )
    @RequestMapping(value = "/editPage/{id}",method = RequestMethod.GET)
    public String editPage(HttpServletRequest request,@PathVariable Long id)
    {
        try {

            //初始化选择应用控件
            super.initSelectApp(request,toucan, appServiceAPI);

            Role entity = new Role();
            entity.setId(id);
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(appCode, entity);
            ResultObjectVO resultObjectVO = roleServiceAPI.findById(requestJsonVO);
            if(resultObjectVO.getCode().intValue()==ResultObjectVO.SUCCESS.intValue())
            {
                if(resultObjectVO.getData()!=null) {
                    List<Role> roles = JSONArray.parseArray(JSONObject.toJSONString(resultObjectVO.getData()),Role.class);
                    if(!CollectionUtils.isEmpty(roles))
                    {
                        request.setAttribute("model",roles.get(0));
                    }
                }

            }
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);
        }
        return "pages/role/edit.html";
    }



    /**
     * 修改
     * @param role
     * @return
     */
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH, verifyType = AdminAuth.VERIFY_TYPE_ANY, permissions = {"pms:system:role:update"})
    @RequestMapping(value = "/update",method = RequestMethod.POST)
    @ResponseBody
    public ResultObjectVO update(HttpServletRequest request,@RequestBody Role role)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            role.setAppCode(toucan.getAppCode());
            role.setUpdateAdminId(AdminLoginHolder.getCurrentAdminId());
            role.setUpdateDate(new Date());
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(appCode, role);
            resultObjectVO = roleServiceAPI.update(requestJsonVO);
        }catch(Exception e)
        {
            resultObjectVO.setMsg("请重试");
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            logger.warn(e.getMessage(),e);
        }
        return resultObjectVO;
    }





    public void setTreeNodeSelect(AtomicLong id,RoleTreeVO parentTree,List<RoleTreeVO> roleTreeVOS,RoleTreeVO parentNode,List<AdminRole> adminRoles)
    {
        for(RoleTreeVO roleTreeVO:roleTreeVOS)
        {
            roleTreeVO.setId(id.incrementAndGet());
            roleTreeVO.setNodeId(roleTreeVO.getId());
            roleTreeVO.setParentId(parentTree.getId());
            for(AdminRole adminRole:adminRoles) {
                if(adminRole.getRoleId().equals(roleTreeVO.getRoleId())) {
                    parentNode.getState().setChecked(true);
                    roleTreeVO.getState().setChecked(true);
                }
            }
        }
    }




    /**
     * 查询当前账号可管理的所有角色树
     * @param request
     * @return
     */
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH,requestType = AdminAuth.REQUEST_JSON,responseType =AdminAuth.RESPONSE_JSON )
    @RequestMapping(value = "/query/admin/role/tree",method = RequestMethod.POST)
    @ResponseBody
    public ResultObjectVO queryRoleTree(HttpServletRequest request,@RequestBody AdminAppVO entity)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            //查询对应账户的应用
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(),entity);
            resultObjectVO = roleServiceAPI.queryAdminRoleTree(requestJsonVO);
            if(resultObjectVO.isSuccess())
            {
                //拿到角色树
                List<RoleTreeVO> roleTreeVOS = JSONArray.parseArray(JSONObject.toJSONString(resultObjectVO.getData()), RoleTreeVO.class);

                //重新设置ID,由于这个树是多个表合并而成,可能会存在ID重复,layui不支持id重复
                AtomicLong id = new AtomicLong();
                //查询要操作账户的所有角色关联
                AdminRole queryAdminRole = new AdminRole();
                queryAdminRole.setAdminId(entity.getAdminId());
                requestJsonVO = RequestJsonVOGenerator.generator(appCode,queryAdminRole);
                resultObjectVO = adminRoleServiceAPI.queryListByEntity(requestJsonVO);
                if(resultObjectVO.isSuccess())
                {
                    List<AdminRole> adminRoles = JSONArray.parseArray(JSONObject.toJSONString(resultObjectVO.getData()), AdminRole.class);
                    if(!CollectionUtils.isEmpty(adminRoles)) {
                        for(RoleTreeVO roleTreeVO:roleTreeVOS) {
                            roleTreeVO.setId(id.incrementAndGet());
                            roleTreeVO.setNodeId(roleTreeVO.getId());
                            setTreeNodeSelect(id,roleTreeVO,roleTreeVO.getChildren(),roleTreeVO, adminRoles);
                        }
                    }
                }
                resultObjectVO.setData(roleTreeVOS);
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
     * 保存
     * @param entity
     * @return
     */
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH, verifyType = AdminAuth.VERIFY_TYPE_ANY, permissions = {"pms:system:role:save"})
    @RequestMapping(value = "/save",method = RequestMethod.POST)
    @ResponseBody
    public ResultObjectVO save(HttpServletRequest request, @RequestBody Role entity)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            entity.setAppCode(toucan.getAppCode());
            entity.setCreateAdminId(AdminLoginHolder.getCurrentAdminId());
            entity.setCreateDate(new Date());
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(appCode, entity);
            resultObjectVO = roleServiceAPI.save(requestJsonVO);
        }catch(Exception e)
        {
            resultObjectVO.setMsg("请重试");
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            logger.warn(e.getMessage(),e);
        }
        return resultObjectVO;
    }



    /**
     * 关联功能项
     * @param roleFunctionVO
     * @return
     */
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH, verifyType = AdminAuth.VERIFY_TYPE_ANY, permissions = {"pms:system:role:connect-menus"})
    @RequestMapping(value = "/connect/functions",method = RequestMethod.POST)
    @ResponseBody
    public ResultObjectVO connectFunctions(HttpServletRequest request, @RequestBody RoleFunctionVO roleFunctionVO)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            roleFunctionVO.setCreateAdminId(AdminLoginHolder.getCurrentAdminId());
            roleFunctionVO.setCreateDate(new Date());
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(appCode, roleFunctionVO);
            resultObjectVO = roleFunctionService.saveFunctions(requestJsonVO);
        }catch(Exception e)
        {
            resultObjectVO.setMsg("请重试");
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            logger.warn(e.getMessage(),e);
        }
        return resultObjectVO;
    }



    /**
     * 刷新缓存
     * @param roleFunctionVO
     * @return
     */
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH)
    @RequestMapping(value = "/refresh/cache/functions",method = RequestMethod.POST)
    @ResponseBody
    public ResultObjectVO refreshFunctionsCache(HttpServletRequest request, @RequestBody RoleFunctionVO roleFunctionVO)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            roleFunctionVO.setCreateAdminId(AdminLoginHolder.getCurrentAdminId());
            roleFunctionVO.setCreateDate(new Date());
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(appCode, roleFunctionVO);
            resultObjectVO = roleFunctionService.refreshCache(requestJsonVO);
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
     * @param pageInfo
     * @return
     */
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH, verifyType = AdminAuth.VERIFY_TYPE_ANY, permissions = {"pms:system:role:list"}, requestType = AdminAuth.REQUEST_JSON, responseType = AdminAuth.RESPONSE_JSON)
    @RequestMapping(value = "/list",method = RequestMethod.POST)
    @ResponseBody
    public TableVO listPage(RolePageInfo pageInfo)
    {
        TableVO tableVO = new TableVO();
        try {
            pageInfo.setAppCode(toucan.getAppCode());
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(),pageInfo);
            ResultObjectVO resultObjectVO = roleServiceAPI.listPage(requestJsonVO);
            if(resultObjectVO.getCode() == ResultObjectVO.SUCCESS)
            {
                if(resultObjectVO.getData()!=null)
                {
                    Map<String,Object> resultObjectDataMap = (Map<String,Object>)resultObjectVO.getData();
                    tableVO.setCount(Long.parseLong(String.valueOf(resultObjectDataMap.get("total")!=null?resultObjectDataMap.get("total"):"0")));
                    if(tableVO.getCount()>0) {
                        tableVO.setData((List<Object>) resultObjectDataMap.get("list"));
                    }
                }
            }
        }catch(Exception e)
        {
            tableVO.setMsg("请重试");
            tableVO.setCode(TableVO.FAILD);
            logger.warn(e.getMessage(),e);
        }
        return tableVO;
    }



    /**
     * 删除
     * @param request
     * @return
     */
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH, verifyType = AdminAuth.VERIFY_TYPE_ANY, permissions = {"pms:system:role:delete"}, requestType = AdminAuth.REQUEST_JSON, responseType = AdminAuth.RESPONSE_JSON)
    @RequestMapping(value = "/delete",method = RequestMethod.POST)
    @ResponseBody
    public ResultObjectVO deleteById(HttpServletRequest request, @RequestBody Role role)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            if(role.getId() == null)
            {
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
        }catch(Exception e)
        {
            resultObjectVO.setMsg("请重试");
            resultObjectVO.setCode(TableVO.FAILD);
            logger.warn(e.getMessage(),e);
        }
        return resultObjectVO;
    }



    /**
     * 删除
     * @param request
     * @return
     */
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH, verifyType = AdminAuth.VERIFY_TYPE_ANY, permissions = {"pms:system:role:batch-delete"}, requestType = AdminAuth.REQUEST_JSON, responseType = AdminAuth.RESPONSE_JSON)
    @RequestMapping(value = "/delete/ids",method = RequestMethod.POST)
    @ResponseBody
    public ResultObjectVO deleteByIds(HttpServletRequest request, @RequestBody List<RoleVO> roleVOS)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            if(CollectionUtils.isEmpty(roleVOS))
            {
                resultObjectVO.setMsg("请传入ID");
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                return resultObjectVO;
            }
            String entityJson = JSONObject.toJSONString(roleVOS);
            RequestJsonVO requestVo = new RequestJsonVO();
            requestVo.setAppCode(appCode);
            requestVo.setEntityJson(entityJson);
            resultObjectVO = roleServiceAPI.deleteByIds(requestVo);
        }catch(Exception e)
        {
            resultObjectVO.setMsg("请重试");
            resultObjectVO.setCode(TableVO.FAILD);
            logger.warn(e.getMessage(),e);
        }
        return resultObjectVO;
    }




    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH, verifyType = AdminAuth.VERIFY_TYPE_ANY, permissions = {"pms:system:role:show"}, requestType = AdminAuth.REQUEST_JSON, responseType = AdminAuth.RESPONSE_JSON)
    @RequestMapping(value="/queryDetail", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO queryDetail(HttpServletRequest request, @RequestBody Role role){
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(appCode, role);
            resultObjectVO = roleServiceAPI.queryDetail(requestJsonVO);
        } catch(Exception e) {
            logger.warn(e.getMessage(), e);
        }
        return resultObjectVO;
    }


    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH, verifyType = AdminAuth.VERIFY_TYPE_ANY, permissions = {"pms:system:role:show"}, requestType = AdminAuth.REQUEST_JSON, responseType = AdminAuth.RESPONSE_JSON)
    @RequestMapping(value="/function/list/page", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO queryRoleFunctionListPage(HttpServletRequest request, @RequestBody RoleFunctionPageInfo pageInfo){
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(appCode, pageInfo);
            resultObjectVO = roleServiceAPI.queryRoleFunctionListPage(requestJsonVO);
        } catch(Exception e) {
            logger.warn(e.getMessage(), e);
        }
        return resultObjectVO;
    }


}
