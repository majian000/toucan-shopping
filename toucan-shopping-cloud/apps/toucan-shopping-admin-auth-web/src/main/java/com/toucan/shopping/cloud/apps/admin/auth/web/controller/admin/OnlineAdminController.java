package com.toucan.shopping.cloud.apps.admin.auth.web.controller.admin;


import com.toucan.shopping.cloud.admin.auth.api.*;
import com.toucan.shopping.cloud.apps.admin.auth.web.controller.base.UIController;
import com.toucan.shopping.modules.admin.auth.entity.AdminApp;
import com.toucan.shopping.modules.admin.auth.page.AdminAppPageInfo;
import com.toucan.shopping.modules.auth.admin.AdminAuth;
import com.toucan.shopping.modules.common.generator.RequestJsonVOGenerator;
import com.toucan.shopping.modules.common.properties.Toucan;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.layui.vo.TableVO;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

@Controller
@RequestMapping("/admin/online")
public class OnlineAdminController extends UIController {

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
    private AdminAppServiceAPI adminAppServiceAPI;








    /**
     * 查询列表
     * @param pageInfo
     * @return
     */
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH,requestType = AdminAuth.REQUEST_JSON,responseType=AdminAuth.RESPONSE_JSON)
    @RequestMapping(value = "/list",method = RequestMethod.POST)
    @ResponseBody
    public TableVO list(HttpServletRequest request, @RequestBody(required = false) AdminAppPageInfo pageInfo)
    {
        TableVO tableVO = new TableVO();
        try {
            if (pageInfo == null) {
                pageInfo = new AdminAppPageInfo();
            }
            pageInfo.setLoginStatus((short)1);
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(),pageInfo);
            ResultObjectVO resultObjectVO = adminAppServiceAPI.onlineList(requestJsonVO);
            if(resultObjectVO.isSuccess())
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
     * 登出
     * @param request
     * @return
     */
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH,requestType = AdminAuth.REQUEST_JSON,responseType=AdminAuth.RESPONSE_JSON)
    @RequestMapping(value = "/logout",method = RequestMethod.POST)
    @ResponseBody
    public ResultObjectVO logout(HttpServletRequest request, @RequestBody AdminApp adminApp)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            if(adminApp.getId() == null)
            {
                resultObjectVO.setMsg("请传入ID");
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                return resultObjectVO;
            }
            RequestJsonVO requestJsonVO=RequestJsonVOGenerator.generator(toucan.getAppCode(),adminApp);
            resultObjectVO = adminAppServiceAPI.logout(requestJsonVO);
        }catch(Exception e)
        {
            resultObjectVO.setMsg("请重试");
            resultObjectVO.setCode(TableVO.FAILD);
            logger.warn(e.getMessage(),e);
        }
        return resultObjectVO;
    }


}

