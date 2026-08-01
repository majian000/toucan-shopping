package com.toucan.shopping.cloud.apps.admin.auth.web.controller.app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import com.toucan.shopping.cloud.apps.admin.auth.web.controller.base.UIController;
import com.toucan.shopping.cloud.admin.auth.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.toucan.shopping.modules.auth.admin.AdminAuth;
import com.toucan.shopping.modules.common.properties.Toucan;
import jakarta.servlet.http.HttpServletRequest;
import com.toucan.shopping.modules.admin.auth.entity.*;
import com.toucan.shopping.modules.admin.auth.vo.*;
import com.toucan.shopping.modules.common.generator.RequestJsonVOGenerator;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import org.springframework.util.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import java.util.ArrayList;
import java.util.List;

@Controller
public class AppPageController extends UIController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Value("${toucan.app-code}")
    private String appCode;

    @Autowired
    private Toucan toucan;

    @Autowired
    private AppServiceAPI appServiceAPI;

    @Autowired
    private FunctionServiceAPI functionServiceAPI;



    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH,requestType = AdminAuth.REQUEST_FORM,responseType=AdminAuth.RESPONSE_FORM, permissions = {"pms:system:app:list"})
        @RequestMapping(value = "/app/listPage",method = RequestMethod.GET)
        public String page(HttpServletRequest request)
        {
            //初始化工具条按钮、操作按钮
            super.initButtons(request,toucan,"/app/listPage", functionServiceAPI);
    
            return "pages/app/list.html";
        }

    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH,requestType = AdminAuth.REQUEST_FORM,responseType=AdminAuth.RESPONSE_FORM)
        @RequestMapping(value = "/app/addPage",method = RequestMethod.GET)
        public String addPage()
        {
            return "pages/app/add.html";
        }

    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH,requestType = AdminAuth.REQUEST_FORM,responseType=AdminAuth.RESPONSE_FORM)
        @RequestMapping(value = "/app/editPage/{id}",method = RequestMethod.GET)
        public String editPage(HttpServletRequest request,@PathVariable Long id)
        {
            try {
                App app = new App();
                app.setId(id);
                RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(appCode, app);
                ResultObjectVO resultObjectVO = appServiceAPI.findById(requestJsonVO);
                if(resultObjectVO.getCode().intValue()==ResultObjectVO.SUCCESS.intValue())
                {
                    if(resultObjectVO.getData()!=null) {
                        List<App> apps = JSONArray.parseArray(JSONObject.toJSONString(resultObjectVO.getData()),App.class);
                        if(!CollectionUtils.isEmpty(apps))
                        {
                            request.setAttribute("model",apps.get(0));
                        }
                    }
    
                }
            }catch(Exception e)
            {
                logger.warn(e.getMessage(),e);
            }
            return "pages/app/edit.html";
        }

}
