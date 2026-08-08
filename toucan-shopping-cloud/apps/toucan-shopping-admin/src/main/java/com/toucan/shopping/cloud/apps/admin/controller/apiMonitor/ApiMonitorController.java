package com.toucan.shopping.cloud.apps.admin.controller.apiMonitor;

import com.toucan.shopping.cloud.admin.auth.api.FunctionServiceAPI;
import com.toucan.shopping.cloud.apiMonitor.api.ApiMonitorDashboardServiceAPI;
import com.toucan.shopping.cloud.apps.admin.controller.base.UIController;
import com.toucan.shopping.modules.apiMonitor.vo.DashboardQueryVO;
import com.toucan.shopping.modules.auth.admin.AdminAuth;
import com.toucan.shopping.modules.common.properties.Toucan;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/apiMonitor")
public class ApiMonitorController extends UIController {

    @Autowired
    private Toucan toucan;

    @Autowired
    private FunctionServiceAPI functionServiceAPI;

    @Autowired
    private ApiMonitorDashboardServiceAPI apiMonitorDashboardServiceAPI;

    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH, requestType = AdminAuth.REQUEST_FORM, responseType = AdminAuth.RESPONSE_FORM)
    @RequestMapping(value = "/dashboardPage", method = RequestMethod.GET)
    public String dashboardPage(HttpServletRequest request) {
        super.initButtons(request, toucan, "/apiMonitor/dashboardPage", functionServiceAPI);
        return "pages/apiMonitor/dashboard.html";
    }

    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH, requestType = AdminAuth.REQUEST_FORM, responseType = AdminAuth.RESPONSE_FORM)
    @RequestMapping(value = "/requestLogPage", method = RequestMethod.GET)
    public String requestLogPage(HttpServletRequest request) {
        super.initButtons(request, toucan, "/apiMonitor/requestLogPage", functionServiceAPI);
        return "pages/apiMonitor/requestLog.html";
    }

    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH, requestType = AdminAuth.REQUEST_FORM, responseType = AdminAuth.RESPONSE_FORM)
    @RequestMapping(value = "/dashboardRequestLogPage", method = RequestMethod.GET)
    public String dashboardRequestLogPage(HttpServletRequest request) {
        super.initButtons(request, toucan, "/apiMonitor/dashboardRequestLogPage", functionServiceAPI);
        return "pages/apiMonitor/dashboardRequestLog.html";
    }

    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH)
    @RequestMapping(value = "/summary", method = RequestMethod.GET)
    @ResponseBody
    public ResultObjectVO summary(DashboardQueryVO query) {
        return apiMonitorDashboardServiceAPI.getSummary(query);
    }

    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH)
    @RequestMapping(value = "/requestLog", method = RequestMethod.GET)
    @ResponseBody
    public ResultObjectVO requestLog(DashboardQueryVO query) {
        return apiMonitorDashboardServiceAPI.getRequestLog(query);
    }
}
