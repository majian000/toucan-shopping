package com.toucan.shopping.standard.apps.admin.auth.web.controller.admin;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.toucan.shopping.cloud.admin.auth.api.feign.service.*;
import com.toucan.shopping.modules.admin.auth.entity.Admin;
import com.toucan.shopping.modules.admin.auth.entity.AdminApp;
import com.toucan.shopping.modules.admin.auth.entity.AdminRole;
import com.toucan.shopping.modules.admin.auth.page.AdminPageInfo;
import com.toucan.shopping.modules.admin.auth.service.*;
import com.toucan.shopping.modules.admin.auth.vo.AdminResultVO;
import com.toucan.shopping.modules.admin.auth.vo.AdminRoleVO;
import com.toucan.shopping.modules.admin.auth.vo.AdminVO;
import com.toucan.shopping.modules.admin.auth.vo.AppVO;
import com.toucan.shopping.modules.auth.admin.AdminAuth;
import com.toucan.shopping.modules.common.page.PageInfo;
import com.toucan.shopping.modules.common.util.*;
import com.toucan.shopping.modules.common.vo.ResultVO;
import com.toucan.shopping.modules.layui.vo.TableVO;
import com.toucan.shopping.modules.common.generator.RequestJsonVOGenerator;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.common.properties.Toucan;
import com.toucan.shopping.standard.apps.admin.auth.web.controller.base.UIController;
import org.apache.commons.lang3.StringUtils;
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
                request.setAttribute("model",admin);
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
            List<Admin>  admins = adminService.findListByEntity(admin);
            if(!CollectionUtils.isEmpty(admins))
            {
                admin = admins.get(0);
                request.setAttribute("model",admin);
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
            if(StringUtils.isEmpty(entity.getUsername()))
            {
                resultObjectVO.setCode(AdminResultVO.NOT_FOUND_USERNAME);
                resultObjectVO.setMsg("添加失败,请输入账号");
                return resultObjectVO;
            }

            if(entity.getUsername().length()>20)
            {
                resultObjectVO.setCode(AdminResultVO.FAILD);
                resultObjectVO.setMsg("登录失败,账号长度不能大与20位");
                return resultObjectVO;
            }
            if(StringUtils.isEmpty(entity.getPassword()))
            {
                resultObjectVO.setCode(AdminResultVO.PASSWORD_NOT_FOUND);
                resultObjectVO.setMsg("添加失败,请输入密码");
                return resultObjectVO;
            }

            if(!UserRegistUtil.checkPwd(entity.getPassword()))
            {
                resultObjectVO.setCode(AdminResultVO.PASSWORD_ERROR);
                resultObjectVO.setMsg("添加失败,请输入6至15位的密码");
                return resultObjectVO;
            }


            Admin query=new Admin();
            query.setUsername(entity.getUsername());
            query.setDeleteStatus((short)0);
            if(!CollectionUtils.isEmpty(adminService.findListByEntity(query)))
            {
                resultObjectVO.setCode(AdminResultVO.FAILD);
                resultObjectVO.setMsg("账号已注册!");
                return resultObjectVO;
            }
            entity.setCreateDate(new Date());
            entity.setPassword(MD5Util.md5(entity.getPassword()));
            entity.setEnableStatus((short)1);
            entity.setDeleteStatus((short)0);
            entity.setAdminId(GlobalUUID.uuid());
            int row = adminService.save(entity);
            if (row < 1) {

                resultObjectVO.setCode(AdminResultVO.FAILD);
                resultObjectVO.setMsg("添加失败,请重试!");
                return resultObjectVO;
            }
            if(!CollectionUtils.isEmpty(entity.getAdminApps())) {
                for (AdminApp adminApp : entity.getAdminApps()) {
                    adminApp.setAdminId(entity.getAdminId());
                    adminApp.setDeleteStatus((short) 0);
                    adminApp.setCreateDate(new Date());
                    adminApp.setCreateAdminId(entity.getCreateAdminId());
                    adminAppService.save(adminApp);
                }
            }

            resultObjectVO.setData(entity);

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

            if(StringUtils.isEmpty(adminRoleVO.getAdminId()))
            {
                throw new IllegalArgumentException("adminId为空");
            }
            if(org.apache.commons.collections4.CollectionUtils.isEmpty(adminRoleVO.getRoles()))
            {
                throw new IllegalArgumentException("roles为空");
            }

            AdminApp queryAdminApp = new AdminApp();
            queryAdminApp.setAdminId(adminRoleVO.getCreateAdminId());

            List<AdminApp> adminApps = adminAppService.findListByEntity(queryAdminApp);

            if(org.apache.commons.collections4.CollectionUtils.isEmpty(adminApps))
            {
                throw new IllegalArgumentException("当前登录用户,关联应用列表为空");
            }

            //拿到当前这个账户的操作人可管理的所有应用
            String[] appCodes = new String[adminApps.size()];
            for(int i=0;i<adminApps.size();i++)
            {
                appCodes[i] = adminApps.get(i).getAppCode();
            }
            //创建账户角色的前提是这个账户和操作这个账户的操作人属于同一个应用,这个操作人也只能操作他俩所属同一应用下面的所有角色
            adminRoleService.deleteByAdminIdAndAppCodes(adminRoleVO.getAdminId(),appCodes);

            int length=0;
            for(AdminRole adminRole:adminRoleVO.getRoles())
            {
                //-1为应用节点
                if(!"-1".equals(adminRole.getRoleId())) {
                    adminRole.setAdminId(adminRoleVO.getAdminId());
                    adminRole.setCreateAdminId(adminRoleVO.getCreateAdminId());
                    adminRole.setCreateDate(new Date());
                    adminRole.setDeleteStatus((short) 0);
                    length++;
                }
            }
            AdminRole[] adminRoles = new AdminRole[length];
            int pos = 0;
            for(AdminRole adminRole:adminRoleVO.getRoles()) {
                //-1为应用节点
                if (!"-1".equals(adminRole.getRoleId())) {
                    adminRoles[pos] = adminRole;
                    pos++;
                }
            }
            adminRoleService.saves(adminRoles);
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
            if(StringUtils.isEmpty(entity.getPassword()))
            {
                resultObjectVO.setCode(AdminResultVO.PASSWORD_NOT_FOUND);
                resultObjectVO.setMsg("请求失败,请输入密码");
                return resultObjectVO;
            }

            if(!UserRegistUtil.checkPwd(entity.getPassword()))
            {
                resultObjectVO.setCode(AdminResultVO.PASSWORD_ERROR);
                resultObjectVO.setMsg("请求失败,请输入6至15位的密码");
                return resultObjectVO;
            }
            entity.setPassword(MD5Util.md5(entity.getPassword()));
            int row = adminService.updatePassword(entity);
            if (row < 1) {

                resultObjectVO.setCode(AdminResultVO.FAILD);
                resultObjectVO.setMsg("请求失败,请重试!");
                return resultObjectVO;
            }

            resultObjectVO.setData(entity);

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
            if(StringUtils.isEmpty(entity.getPassword()))
            {
                resultObjectVO.setCode(AdminResultVO.PASSWORD_NOT_FOUND);
                resultObjectVO.setMsg("请求失败,请输入密码");
                return resultObjectVO;
            }

            if(!UserRegistUtil.checkPwd(entity.getPassword()))
            {
                resultObjectVO.setCode(AdminResultVO.PASSWORD_ERROR);
                resultObjectVO.setMsg("请求失败,请输入6至15位的密码");
                return resultObjectVO;
            }
            entity.setPassword(MD5Util.md5(entity.getPassword()));
            int row = adminService.updatePassword(entity);
            if (row < 1) {

                resultObjectVO.setCode(AdminResultVO.FAILD);
                resultObjectVO.setMsg("请求失败,请重试!");
                return resultObjectVO;
            }

            resultObjectVO.setData(entity);
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
            if(StringUtils.isEmpty(entity.getAdminId()))
            {
                resultObjectVO.setCode(AdminResultVO.FAILD);
                resultObjectVO.setMsg("修改失败,adminId为空");
                return resultObjectVO;
            }
            if(StringUtils.isEmpty(entity.getUsername()))
            {
                resultObjectVO.setCode(AdminResultVO.NOT_FOUND_USERNAME);
                resultObjectVO.setMsg("修改失败,请输入账号");
                return resultObjectVO;
            }

            if(entity.getUsername().length()>20)
            {
                resultObjectVO.setCode(AdminResultVO.FAILD);
                resultObjectVO.setMsg("修改失败,账号长度不能大与20位");
                return resultObjectVO;
            }

            Admin query=new Admin();
            query.setUsername(entity.getUsername());
            query.setDeleteStatus((short)0);
            List<Admin> queryAdmins = adminService.findListByEntity(query);
            if(!CollectionUtils.isEmpty(queryAdmins))
            {
                if(!queryAdmins.get(0).getAdminId().equals(entity.getAdminId()))
                {
                    resultObjectVO.setCode(AdminResultVO.FAILD);
                    resultObjectVO.setMsg("账号已存在!");
                    return resultObjectVO;
                }
            }
            int row = adminService.update(entity);
            if (row < 1) {

                resultObjectVO.setCode(AdminResultVO.FAILD);
                resultObjectVO.setMsg("修改失败,请重试!");
                return resultObjectVO;
            }

            //查询出当前账号数据库中保存的应用关联
            AdminApp queryAdminApp = new AdminApp();
            queryAdminApp.setAdminId(entity.getAdminId());
            List<AdminApp> adminAppPersistentList = adminAppService.findListByEntity(queryAdminApp);
            if(!CollectionUtils.isEmpty(adminAppPersistentList))
            {
                //这次修改将所有应用都取消关联了
                if(CollectionUtils.isEmpty(entity.getAdminApps()))
                {

                    //将数据库中保存的关联全部删除
                    for(AdminApp adminAppPersistent:adminAppPersistentList)
                    {
                        adminAppService.deleteByAdminIdAndAppCode(entity.getAdminId(),adminAppPersistent.getAppCode());
                        adminRoleService.deleteByAdminIdAndAppCodes(entity.getAdminId(),new String[]{adminAppPersistent.getAppCode()});
                    }

                }else{
                    //找到这次取消掉的关联,把那个关联删除掉
                    for(AdminApp adminAppPersistent:adminAppPersistentList)
                    {
                        boolean find=false;
                        for(AdminApp adminApp : entity.getAdminApps())
                        {
                            //如果这次修改数据库中保存的关联没有任何操作,那么就什么都不做
                            if(adminAppPersistent.getAppCode().equals(adminApp.getAppCode()))
                            {
                                find=true;
                            }
                        }
                        if(!find)
                        {
                            adminAppService.deleteByAdminIdAndAppCode(entity.getAdminId(),adminAppPersistent.getAppCode());
                            adminRoleService.deleteByAdminIdAndAppCodes(entity.getAdminId(),new String[]{adminAppPersistent.getAppCode()});
                        }
                    }
                }
            }

            if(!CollectionUtils.isEmpty(entity.getAdminApps())) {
                //重新保存关联
                for (AdminApp adminApp : entity.getAdminApps()) {
                    boolean find=false;
                    for(AdminApp adminAppPersistent:adminAppPersistentList) {
                        if(adminAppPersistent.getAppCode().equals(adminApp.getAppCode()))
                        {
                            find=true;
                        }
                    }
                    //如果这次保存传过来的应用已经存在关联 就什么都不做
                    if(!find) {
                        adminApp.setAdminId(entity.getAdminId());
                        adminApp.setDeleteStatus((short) 0);
                        adminApp.setCreateDate(new Date());
                        adminApp.setCreateAdminId(entity.getUpdateAdminId());
                        adminAppService.save(adminApp);
                    }
                }
            }


            resultObjectVO.setData(entity);
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
     * @param queryPageInfo
     * @return
     */
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH,requestType = AdminAuth.REQUEST_FORM)
    @RequestMapping(value = "/list",method = RequestMethod.POST)
    @ResponseBody
    public TableVO listPage(HttpServletRequest request, AdminPageInfo queryPageInfo)
    {
        TableVO tableVO = new TableVO();
        try {
            //查询用户主表
            PageInfo<AdminVO> pageInfo =  adminService.queryListPage(queryPageInfo);
            tableVO.setCount(pageInfo.getTotal());
            if(tableVO.getCount()>0) {
                List list = pageInfo.getList();
                tableVO.setData(list);
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

