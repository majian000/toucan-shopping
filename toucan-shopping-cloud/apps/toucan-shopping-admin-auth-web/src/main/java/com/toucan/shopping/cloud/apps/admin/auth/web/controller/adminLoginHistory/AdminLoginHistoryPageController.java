package com.toucan.shopping.cloud.apps.admin.auth.web.controller.adminLoginHistory;

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
public class AdminLoginHistoryPageController extends UIController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Value("${toucan.app-code}")
    private String appCode;

    @Autowired
    private Toucan toucan;

    @Autowired
    private AppServiceAPI appServiceAPI;

    @Autowired
    private FunctionServiceAPI functionServiceAPI;

    @Autowired
    private AdminLoginHistoryServiceAPI adminLoginHistoryServiceAPI;



    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH, requestType = AdminAuth.REQUEST_FORM, responseType = AdminAuth.RESPONSE_FORM)
        @RequestMapping(value = "/adminLoginHistory/listPage", method = RequestMethod.GET)
        public String page(HttpServletRequest request) {
            super.initSelectApp(request, toucan, appServiceAPI);
            // 初始化工具条按钮、操作按钮
            super.initButtons(request, toucan, "/adminLoginHistory/listPage", functionServiceAPI);
            return "pages/adminLoginHistory/list.html";
        }

    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH, requestType = AdminAuth.REQUEST_FORM, responseType = AdminAuth.RESPONSE_FORM)
        @RequestMapping(value = "/adminLoginHistory/showPage/{id}", method = RequestMethod.GET)
        public String showPage(HttpServletRequest request, @PathVariable Long id) {
            try {
                AdminLoginHistoryVO adminLoginHistoryVO = new AdminLoginHistoryVO();
                adminLoginHistoryVO.setId(id);
                RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(appCode, adminLoginHistoryVO);
                ResultObjectVO resultObjectVO = adminLoginHistoryServiceAPI.findById(requestJsonVO);
                if (resultObjectVO.getCode().intValue() == ResultObjectVO.SUCCESS.intValue()) {
                    if (resultObjectVO.getData() != null) {
                        List<AdminLoginHistoryVO> adminLoginHistoryVOS = JSONArray.parseArray(JSONObject.toJSONString(resultObjectVO.getData()), AdminLoginHistoryVO.class);
                        if (!CollectionUtils.isEmpty(adminLoginHistoryVOS)) {
                            request.setAttribute("model", adminLoginHistoryVOS.get(0));
                        }
                    }
                }
            } catch (Exception e) {
                logger.warn(e.getMessage(), e);
            }
            return "pages/adminLoginHistory/show.html";
        }

}
