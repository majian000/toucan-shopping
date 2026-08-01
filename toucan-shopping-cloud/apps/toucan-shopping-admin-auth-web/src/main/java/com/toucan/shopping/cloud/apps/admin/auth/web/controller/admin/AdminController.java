package com.toucan.shopping.cloud.apps.admin.auth.web.controller.admin;


import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.toucan.shopping.cloud.admin.auth.api.*;
import com.toucan.shopping.cloud.apps.admin.auth.web.controller.base.UIController;
import com.toucan.shopping.modules.admin.auth.entity.Admin;
import com.toucan.shopping.modules.admin.auth.entity.AdminApp;
import com.toucan.shopping.modules.admin.auth.holder.AdminLoginHolder;
import com.toucan.shopping.modules.admin.auth.page.AdminPageInfo;
import com.toucan.shopping.modules.admin.auth.vo.*;
import com.toucan.shopping.modules.auth.admin.AdminAuth;
import com.toucan.shopping.modules.common.generator.IdGenerator;
import com.toucan.shopping.modules.layui.vo.TableVO;
import com.toucan.shopping.modules.common.generator.RequestJsonVOGenerator;
import com.toucan.shopping.modules.common.util.AuthHeaderUtil;
import com.toucan.shopping.modules.common.util.SignUtil;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.common.properties.Toucan;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController extends UIController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Value("${toucan.app-code}")
    private String appCode;

    @Autowired
    private Toucan toucan;

    @Autowired
    private AdminServiceAPI adminServiceAPI;

    @Autowired
    private AdminAppServiceAPI adminAppServiceAPI;

    @Autowired
    private FunctionServiceAPI functionServiceAPI;

    @Autowired
    private AdminRoleServiceAPI adminRoleServiceAPI;

    @Autowired
    private AppServiceAPI appServiceAPI;

    @Autowired
    private IdGenerator idGenerator;

    @Autowired
    private AdminOrgnazitionServiceAPI adminOrgnazitionServiceAPI;




    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH, verifyType = AdminAuth.VERIFY_TYPE_ANY, permissions = {"pms:system:user:org"})
    @RequestMapping(value = "/apps",method = RequestMethod.POST)
    @ResponseBody
    public ResultObjectVO appsByAdminId(HttpServletRequest request, @RequestBody AdminApp adminApp)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(), adminApp);
            resultObjectVO = adminAppServiceAPI.queryAppListByAdminId(requestJsonVO);
        } catch(Exception e) {
            logger.warn(e.getMessage(),e);
        }
        return resultObjectVO;
    }


















    /**
     * 保存
     * @param entity
     * @return
     */
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH, verifyType = AdminAuth.VERIFY_TYPE_ANY, permissions = {"pms:system:user:save"})
    @RequestMapping(value = "/save",method = RequestMethod.POST)
    @ResponseBody
    public ResultObjectVO save(HttpServletRequest request, @RequestBody AdminVO entity)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            entity.setId(idGenerator.id());
            entity.setCreateAdminId(AdminLoginHolder.getCurrentAdminId());
            if(!CollectionUtils.isEmpty(entity.getAppCodes()))
            {
                entity.setAdminApps(new ArrayList<AdminApp>());
                for(String appCode:entity.getAppCodes()){
                    AdminApp adminApp = new AdminApp();
                    adminApp.setAppCode(appCode);
                    adminApp.setCreateAdminId(entity.getCreateAdminId());
                    adminApp.setCreateDate(new Date());
                    entity.getAdminApps().add(adminApp);
                }
            }
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(appCode, entity);
            resultObjectVO = adminServiceAPI.save(requestJsonVO);
        }catch(Exception e)
        {
            resultObjectVO.setMsg("请重试");
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            logger.warn(e.getMessage(),e);
        }
        return resultObjectVO;
    }





    /**
     * 关联角色
     * @param adminRoleVO
     * @return
     */
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH, verifyType = AdminAuth.VERIFY_TYPE_ANY, permissions = {"pms:system:user:connect-roles"})
    @RequestMapping(value = "/connect/roles",method = RequestMethod.POST)
    @ResponseBody
    public ResultObjectVO connectRoles(HttpServletRequest request, @RequestBody AdminRoleVO adminRoleVO)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            adminRoleVO.setCreateAdminId(AdminLoginHolder.getCurrentAdminId());
            adminRoleVO.setCreateDate(new Date());
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(appCode, adminRoleVO);
            resultObjectVO = adminRoleServiceAPI.saveRoles(requestJsonVO);
        }catch(Exception e)
        {
            resultObjectVO.setMsg("请重试");
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            logger.warn(e.getMessage(),e);
        }
        return resultObjectVO;
    }


    /**
     * 关联组织机构
     * @param adminOrgnazitionVO
     * @return
     */
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH, verifyType = AdminAuth.VERIFY_TYPE_ANY, permissions = {"pms:system:user:connect-orgs"})
    @RequestMapping(value = "/connect/orgnazitions",method = RequestMethod.POST)
    @ResponseBody
    public ResultObjectVO connectOrgnazitions(HttpServletRequest request, @RequestBody AdminOrgnazitionVO adminOrgnazitionVO)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            adminOrgnazitionVO.setCreateAdminId(AdminLoginHolder.getCurrentAdminId());
            adminOrgnazitionVO.setCreateDate(new Date());
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(appCode, adminOrgnazitionVO);
            resultObjectVO = adminOrgnazitionServiceAPI.saveOrgnazitions(requestJsonVO);
        }catch(Exception e)
        {
            resultObjectVO.setMsg("请重试");
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            logger.warn(e.getMessage(),e);
        }
        return resultObjectVO;
    }

    /**
     * 修改密码
     * @param entity
     * @return
     */
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH, verifyType = AdminAuth.VERIFY_TYPE_ANY, permissions = {"pms:system:user:update-pwd"})
    @RequestMapping(value = "/update/password",method = RequestMethod.POST)
    @ResponseBody
    public ResultObjectVO updatePassword(HttpServletRequest request,@RequestBody AdminVO entity)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            entity.setUpdateAdminId(AdminLoginHolder.getCurrentAdminId());
            entity.setUpdateDate(new Date());
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(appCode, entity);
            resultObjectVO = adminServiceAPI.updatePassword(requestJsonVO);
        }catch(Exception e)
        {
            resultObjectVO.setMsg("请重试");
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            logger.warn(e.getMessage(),e);
        }
        return resultObjectVO;
    }


    /**
     * 修改我的密码
     * @param entity
     * @return
     */
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH)
    @RequestMapping(value = "/update/mypassword",method = RequestMethod.POST)
    @ResponseBody
    public ResultObjectVO updateMyPassword(HttpServletRequest request,@RequestBody AdminVO entity)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            String adminId= AdminLoginHolder.getCurrentAdminId();
            entity.setAdminId(adminId);
            entity.setUpdateAdminId(adminId);
            entity.setUpdateDate(new Date());
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(appCode, entity);
            resultObjectVO = adminServiceAPI.updatePassword(requestJsonVO);
        }catch(Exception e)
        {
            resultObjectVO.setMsg("请重试");
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            logger.warn(e.getMessage(),e);
        }
        return resultObjectVO;
    }




    /**
     * 修改
     * @param entity
     * @return
     */
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH, verifyType = AdminAuth.VERIFY_TYPE_ANY, permissions = {"pms:system:user:update"})
    @RequestMapping(value = "/update",method = RequestMethod.POST)
    @ResponseBody
    public ResultObjectVO update(HttpServletRequest request,@RequestBody AdminVO entity)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            entity.setUpdateAdminId(AdminLoginHolder.getCurrentAdminId());
            entity.setUpdateDate(new Date());
            if(!CollectionUtils.isEmpty(entity.getAppCodes()))
            {
                entity.setAdminApps(new ArrayList<AdminApp>());
                for(String appCode:entity.getAppCodes()){
                    AdminApp adminApp = new AdminApp();
                    adminApp.setAppCode(appCode);
                    entity.getAdminApps().add(adminApp);
                }
            }
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(appCode, entity);
            resultObjectVO = adminServiceAPI.update(requestJsonVO);
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
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH, verifyType = AdminAuth.VERIFY_TYPE_ANY, permissions = {"pms:system:user:list"}, requestType = AdminAuth.REQUEST_JSON, responseType = AdminAuth.RESPONSE_JSON)
    @RequestMapping(value = "/list",method = RequestMethod.POST)
    @ResponseBody
    public TableVO list(HttpServletRequest request, @RequestBody AdminPageInfo pageInfo)
    {
        TableVO tableVO = new TableVO();
        try {
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(),pageInfo);
            ResultObjectVO resultObjectVO = adminServiceAPI.list(requestJsonVO);
            if(resultObjectVO.getCode() == ResultObjectVO.SUCCESS)
            {
                if(resultObjectVO.getData()!=null)
                {
                    fillTableVOPageData(tableVO, resultObjectVO.getData());
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
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH, verifyType = AdminAuth.VERIFY_TYPE_ANY, permissions = {"pms:system:user:delete"}, requestType = AdminAuth.REQUEST_JSON, responseType = AdminAuth.RESPONSE_JSON)
    @RequestMapping(value = "/delete",method = RequestMethod.POST)
    @ResponseBody
    public ResultObjectVO deleteById(HttpServletRequest request, @RequestBody Admin admin)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            if(admin.getId() == null)
            {
                resultObjectVO.setMsg("请传入ID");
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                return resultObjectVO;
            }
                        admin.setUpdateAdminId(AdminLoginHolder.getCurrentAdminId());

            String entityJson = JSONObject.toJSONString(admin);
            RequestJsonVO requestVo = new RequestJsonVO();
            requestVo.setAppCode(appCode);
            requestVo.setEntityJson(entityJson);
            resultObjectVO = adminServiceAPI.deleteById(requestVo);
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
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH, verifyType = AdminAuth.VERIFY_TYPE_ANY, permissions = {"pms:system:user:batch-delete-api"}, requestType = AdminAuth.REQUEST_JSON, responseType = AdminAuth.RESPONSE_JSON)
    @RequestMapping(value = "/delete/ids",method = RequestMethod.POST)
    @ResponseBody
    public ResultObjectVO deleteByIds(HttpServletRequest request, @RequestBody List<AdminVO> adminVos)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            if(CollectionUtils.isEmpty(adminVos))
            {
                resultObjectVO.setMsg("请传入ID");
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                return resultObjectVO;
            }
            String entityJson = JSONObject.toJSONString(adminVos);
            RequestJsonVO requestVo = new RequestJsonVO();
            requestVo.setAppCode(appCode);
            requestVo.setEntityJson(entityJson);
            resultObjectVO = adminServiceAPI.deleteByIds( requestVo);
        }catch(Exception e)
        {
            resultObjectVO.setMsg("请重试");
            resultObjectVO.setCode(TableVO.FAILD);
            logger.warn(e.getMessage(),e);
        }
        return resultObjectVO;
    }

}

