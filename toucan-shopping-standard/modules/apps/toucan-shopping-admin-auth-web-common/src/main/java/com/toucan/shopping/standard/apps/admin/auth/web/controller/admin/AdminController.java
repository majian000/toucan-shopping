package com.toucan.shopping.standard.apps.admin.auth.web.controller.admin;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.toucan.shopping.cloud.admin.auth.api.feign.service.*;
import com.toucan.shopping.modules.admin.auth.entity.Admin;
import com.toucan.shopping.modules.admin.auth.entity.AdminApp;
import com.toucan.shopping.modules.admin.auth.page.AdminPageInfo;
import com.toucan.shopping.modules.admin.auth.service.*;
import com.toucan.shopping.modules.admin.auth.vo.AdminRoleVO;
import com.toucan.shopping.modules.admin.auth.vo.AdminVO;
import com.toucan.shopping.modules.admin.auth.vo.AppVO;
import com.toucan.shopping.modules.auth.admin.AdminAuth;
import com.toucan.shopping.modules.layui.vo.TableVO;
import com.toucan.shopping.modules.common.generator.RequestJsonVOGenerator;
import com.toucan.shopping.modules.common.util.AuthHeaderUtil;
import com.toucan.shopping.modules.common.util.SignUtil;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.common.properties.Toucan;
import com.toucan.shopping.standard.apps.admin.auth.web.controller.base.UIController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin")
public class AdminController extends UIController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Value("${toucan.app-code}")
    private String appCode;

    @Autowired
    private Toucan toucan;

    @Autowired
    private AdminService adminService;

    @Autowired
    private AdminAppService adminAppService;

    @Autowired
    private FunctionService functionService;

    @Autowired
    private AdminRoleService adminRoleService;

    @Autowired
    private AppService appService;



    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH,requestType = AdminAuth.REQUEST_FORM)
    @RequestMapping(value = "/addPage",method = RequestMethod.GET)
    public String addPage(HttpServletRequest request)
    {
        super.initSelectApp(request,toucan, appService);

        //初始化工具条按钮、操作按钮
        super.initButtons(request,toucan,"/admin/listPage", functionService);

        return "pages/admin/add.html";
    }




    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH,requestType = AdminAuth.REQUEST_FORM)
    @RequestMapping(value = "/editPage/{id}",method = RequestMethod.GET)
    public String editPage(HttpServletRequest request,@PathVariable Long id)
    {
        try {
            super.initSelectApp(request,toucan, appService);

            Admin admin = new Admin();
            admin.setId(id);
            List<Admin> adminList = adminService.findListByEntity(admin);
            for(Admin adminEntity:adminList)
            {
                AdminApp queryAdminApp = new AdminApp();
                queryAdminApp.setAdminId(adminEntity.getAdminId());
                queryAdminApp.setDeleteStatus((short)0);
                adminEntity.setAdminApps(adminAppService.findListByEntity(queryAdminApp));
            }
            if(!CollectionUtils.isEmpty(adminList))
            {
                admin = adminList.get(0);
                //设置复选框选中状态
                Object appsObject = request.getAttribute("apps");
                if(appsObject!=null) {
                    List<AppVO> appVos = (List<AppVO>) appsObject;
                    if(!CollectionUtils.isEmpty(admin.getAdminApps()))
                    {
                        for(AdminApp adminAppVO:admin.getAdminApps())
                        {
                            for(AppVO aa:appVos)
                            {
                                if(adminAppVO.getAppCode().equals(aa.getCode()))
                                {
                                    aa.setChecked(true);
                                }
                            }
                        }
                    }
                }
                request.setAttribute("model",admin);
            }

        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);
        }
        return "pages/admin/edit.html";
    }



    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH,requestType = AdminAuth.REQUEST_FORM)
    @RequestMapping(value = "/passwordPage/{id}",method = RequestMethod.GET)
    public String passwordPage(HttpServletRequest request,@PathVariable Long id)
    {
        try {
            Admin admin = new Admin();
            admin.setId(id);
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(appCode, admin);
            ResultObjectVO resultObjectVO = adminService.findById(SignUtil.sign(requestJsonVO),requestJsonVO);
            if(resultObjectVO.isSuccess())
            {
                if(resultObjectVO.getData()!=null) {
                    List<Admin> admins = JSONArray.parseArray(JSONObject.toJSONString(resultObjectVO.getData()),Admin.class);
                    if(!CollectionUtils.isEmpty(admins))
                    {
                        admin = admins.get(0);
                        request.setAttribute("model",admin);
                    }
                }
            }
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);
        }
        return "pages/admin/password.html";
    }




    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH,requestType = AdminAuth.REQUEST_FORM)
    @RequestMapping(value = "/myPasswordPage",method = RequestMethod.GET)
    public String myPasswordPage(HttpServletRequest request)
    {
        try {
            Admin admin = new Admin();
            admin.setAdminId(AuthHeaderUtil.getAdminId(request.getHeader(toucan.getAdminAuth().getHttpToucanAuthHeader())));
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(appCode, admin);
            ResultObjectVO resultObjectVO = adminService.queryListByEntity(SignUtil.sign(requestJsonVO),requestJsonVO);
            if(resultObjectVO.isSuccess())
            {
                if(resultObjectVO.getData()!=null) {
                    List<Admin> admins = JSONArray.parseArray(JSONObject.toJSONString(resultObjectVO.getData()),Admin.class);
                    if(!CollectionUtils.isEmpty(admins))
                    {
                        admin = admins.get(0);
                        request.setAttribute("model",admin);
                    }
                }
            }
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);
        }
        return "pages/admin/mypassword.html";
    }



    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH,requestType = AdminAuth.REQUEST_FORM)
    @RequestMapping(value = "/listPage",method = RequestMethod.GET)
    public String page(HttpServletRequest request)
    {

        //初始化选择应用控件
        super.initSelectApp(request,toucan, appService);

        //初始化工具条按钮、操作按钮
        super.initButtons(request,toucan,"/admin/listPage", functionService);
        return "pages/admin/list.html";
    }



    /**
     * 保存
     * @param entity
     * @return
     */
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH)
    @RequestMapping(value = "/save",method = RequestMethod.POST)
    @ResponseBody
    public ResultObjectVO save(HttpServletRequest request, @RequestBody AdminVO entity)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            entity.setCreateAdminId(AuthHeaderUtil.getAdminId(request.getHeader(toucan.getAdminAuth().getHttpToucanAuthHeader())));
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
            resultObjectVO = adminService.save(SignUtil.sign(requestJsonVO),requestJsonVO);
        }catch(Exception e)
        {
            resultObjectVO.setMsg("请求失败,请重试");
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
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH)
    @RequestMapping(value = "/connect/roles",method = RequestMethod.POST)
    @ResponseBody
    public ResultObjectVO connectRoles(HttpServletRequest request, @RequestBody AdminRoleVO adminRoleVO)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            adminRoleVO.setCreateAdminId(AuthHeaderUtil.getAdminId(request.getHeader(toucan.getAdminAuth().getHttpToucanAuthHeader())));
            adminRoleVO.setCreateDate(new Date());
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(appCode, adminRoleVO);
            resultObjectVO = adminRoleService.saveRoles(SignUtil.sign(requestJsonVO),requestJsonVO);
        }catch(Exception e)
        {
            resultObjectVO.setMsg("请求失败,请重试");
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
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH)
    @RequestMapping(value = "/update/password",method = RequestMethod.POST)
    @ResponseBody
    public ResultObjectVO updatePassword(HttpServletRequest request,@RequestBody AdminVO entity)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            entity.setUpdateAdminId(AuthHeaderUtil.getAdminId(request.getHeader(toucan.getAdminAuth().getHttpToucanAuthHeader())));
            entity.setUpdateDate(new Date());
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(appCode, entity);
            resultObjectVO = adminService.updatePassword(SignUtil.sign(requestJsonVO),requestJsonVO);
        }catch(Exception e)
        {
            resultObjectVO.setMsg("请求失败,请重试");
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
            entity.setUpdateAdminId(AuthHeaderUtil.getAdminId(request.getHeader(toucan.getAdminAuth().getHttpToucanAuthHeader())));
            entity.setUpdateDate(new Date());
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(appCode, entity);
            resultObjectVO = adminService.updatePassword(SignUtil.sign(requestJsonVO),requestJsonVO);
        }catch(Exception e)
        {
            resultObjectVO.setMsg("请求失败,请重试");
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
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH)
    @RequestMapping(value = "/update",method = RequestMethod.POST)
    @ResponseBody
    public ResultObjectVO update(HttpServletRequest request,@RequestBody AdminVO entity)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            entity.setUpdateAdminId(AuthHeaderUtil.getAdminId(request.getHeader(toucan.getAdminAuth().getHttpToucanAuthHeader())));
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
            resultObjectVO = adminService.update(SignUtil.sign(requestJsonVO),requestJsonVO);
        }catch(Exception e)
        {
            resultObjectVO.setMsg("请求失败,请重试");
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
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH,requestType = AdminAuth.REQUEST_FORM)
    @RequestMapping(value = "/list",method = RequestMethod.POST)
    @ResponseBody
    public TableVO listPage(HttpServletRequest request, AdminPageInfo pageInfo)
    {
        TableVO tableVO = new TableVO();
        try {
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(),pageInfo);
            ResultObjectVO resultObjectVO = adminService.list(SignUtil.sign(requestJsonVO),requestJsonVO);
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
            tableVO.setMsg("请求失败,请重试");
            tableVO.setCode(TableVO.FAILD);
            logger.warn(e.getMessage(),e);
        }
        return tableVO;
    }



}

