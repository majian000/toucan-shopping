package com.toucan.shopping.cloud.apps.admin.controller.admin;


import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.toucan.shopping.cloud.admin.auth.api.AdminAppServiceAPI;
import com.toucan.shopping.cloud.admin.auth.api.AdminServiceAPI;
import com.toucan.shopping.cloud.admin.auth.api.AppServiceAPI;
import com.toucan.shopping.cloud.admin.auth.api.FunctionServiceAPI;
import com.toucan.shopping.cloud.apps.admin.controller.base.UIController;
import com.toucan.shopping.modules.admin.auth.entity.Admin;
import com.toucan.shopping.modules.admin.auth.entity.AdminApp;
import com.toucan.shopping.modules.admin.auth.holder.AdminLoginHolder;
import com.toucan.shopping.modules.admin.auth.vo.AdminAppVO;
import com.toucan.shopping.modules.admin.auth.vo.AppVO;
import com.toucan.shopping.modules.auth.admin.AdminAuth;
import com.toucan.shopping.modules.common.generator.RequestJsonVOGenerator;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.common.properties.Toucan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class AdminPageController extends UIController {

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
    private AppServiceAPI appServiceAPI;


    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH,requestType = AdminAuth.REQUEST_FORM,responseType =AdminAuth.RESPONSE_FORM )
    @RequestMapping(value = "/admin/addPage",method = RequestMethod.GET)
    public String addPage(HttpServletRequest request)
    {
        super.initSelectApp(request,toucan, appServiceAPI);

        //初始化工具条按钮、操作按钮
        super.initButtons(request,toucan,"/admin/listPage", functionServiceAPI);

        return "pages/admin/add.html";
    }


    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH,requestType = AdminAuth.REQUEST_FORM,responseType =AdminAuth.RESPONSE_FORM )
    @RequestMapping(value = "/admin/selectOrgnazitionPage/{adminId}",method = RequestMethod.GET)
    public String orgnazitionPage(HttpServletRequest request,@PathVariable String adminId)
    {
        try {
            AdminApp adminApp = new AdminApp();
            adminApp.setAdminId(adminId);
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(), adminApp);
            ResultObjectVO resultObjectVO = adminAppServiceAPI.queryAppListByAdminId(requestJsonVO);
            if(resultObjectVO.isSuccess())
            {
                List<AdminAppVO> adminAppVOS = JSONArray.parseArray(JSONObject.toJSONString(resultObjectVO.getData()), AdminAppVO.class);
                request.setAttribute("apps",adminAppVOS);
            }
        }catch (Exception e){
            logger.warn(e.getMessage(),e);
            request.setAttribute("apps",new ArrayList<AdminAppVO>());
        }
        request.setAttribute("adminId",adminId);
        return "pages/admin/selectorgnazition.html";
    }



    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH,requestType = AdminAuth.REQUEST_FORM,responseType =AdminAuth.RESPONSE_FORM )
    @RequestMapping(value = "/admin/editPage/{id}",method = RequestMethod.GET)
    public String editPage(HttpServletRequest request,@PathVariable Long id)
    {
        try {
            super.initSelectApp(request,toucan, appServiceAPI);

            Admin admin = new Admin();
            admin.setId(id);
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(appCode, admin);
            ResultObjectVO resultObjectVO = adminServiceAPI.findById(requestJsonVO);
            if(resultObjectVO.getCode().intValue()==ResultObjectVO.SUCCESS.intValue())
            {
                if(resultObjectVO.getData()!=null) {
                    List<Admin> admins = JSONArray.parseArray(JSONObject.toJSONString(resultObjectVO.getData()),Admin.class);
                    if(!CollectionUtils.isEmpty(admins))
                    {
                        admin = admins.get(0);
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
                }

            }
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);
        }
        return "pages/admin/edit.html";
    }



    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH,requestType = AdminAuth.REQUEST_FORM,responseType =AdminAuth.RESPONSE_FORM )
    @RequestMapping(value = "/admin/passwordPage/{id}",method = RequestMethod.GET)
    public String passwordPage(HttpServletRequest request,@PathVariable Long id)
    {
        try {
            Admin admin = new Admin();
            admin.setId(id);
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(appCode, admin);
            ResultObjectVO resultObjectVO = adminServiceAPI.findById(requestJsonVO);
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




    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH,requestType = AdminAuth.REQUEST_FORM,responseType =AdminAuth.RESPONSE_FORM )
    @RequestMapping(value = "/admin/myPasswordPage",method = RequestMethod.GET)
    public String myPasswordPage(HttpServletRequest request)
    {
        try {
            Admin admin = new Admin();
            admin.setAdminId(AdminLoginHolder.getCurrentAdminId());
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(appCode, admin);
            ResultObjectVO resultObjectVO = adminServiceAPI.queryListByEntity(requestJsonVO);
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



    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH,requestType = AdminAuth.REQUEST_FORM,responseType =AdminAuth.RESPONSE_FORM )
    @RequestMapping(value = "/admin/listPage",method = RequestMethod.GET)
    public String listPage(HttpServletRequest request)
    {

        //初始化选择应用控件
        super.initSelectApp(request,toucan, appServiceAPI);

        //初始化工具条按钮、操作按钮
        super.initButtons(request,toucan,"/admin/listPage", functionServiceAPI);
        return "pages/admin/list.html";
    }

}
