package com.toucan.shopping.cloud.apps.admin.controller.role;


import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.toucan.shopping.cloud.admin.auth.api.AppServiceAPI;
import com.toucan.shopping.cloud.admin.auth.api.FunctionServiceAPI;
import com.toucan.shopping.cloud.admin.auth.api.RoleServiceAPI;
import com.toucan.shopping.cloud.apps.admin.controller.base.UIController;
import com.toucan.shopping.modules.admin.auth.entity.Role;
import com.toucan.shopping.modules.auth.admin.AdminAuth;
import com.toucan.shopping.modules.common.generator.RequestJsonVOGenerator;
import com.toucan.shopping.modules.common.properties.Toucan;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class RolePageController extends UIController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Value("${toucan.app-code}")
    private String appCode;

    @Autowired
    private Toucan toucan;

    @Autowired
    private RoleServiceAPI roleServiceAPI;

    @Autowired
    private AppServiceAPI appServiceAPI;

    @Autowired
    private FunctionServiceAPI functionServiceAPI;


    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH,requestType = AdminAuth.REQUEST_FORM,responseType =AdminAuth.RESPONSE_FORM )
    @RequestMapping(value = "/role/listPage",method = RequestMethod.GET)
    public String page(HttpServletRequest request)
    {
        //初始化选择应用控件
        super.initSelectApp(request,toucan, appServiceAPI);

        //初始化工具条按钮、操作按钮
        super.initButtons(request,toucan,"/role/listPage", functionServiceAPI);

        return "pages/role/list.html";
    }






    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH,requestType = AdminAuth.REQUEST_FORM,responseType =AdminAuth.RESPONSE_FORM )
    @RequestMapping(value = "/role/addPage",method = RequestMethod.GET)
    public String addPage(HttpServletRequest request)
    {
        //初始化选择应用控件
        super.initSelectApp(request,toucan, appServiceAPI);
        return "pages/role/add.html";
    }



    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH,requestType = AdminAuth.REQUEST_FORM,responseType =AdminAuth.RESPONSE_FORM )
    @RequestMapping(value = "/role/editPage/{id}",method = RequestMethod.GET)
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

}
