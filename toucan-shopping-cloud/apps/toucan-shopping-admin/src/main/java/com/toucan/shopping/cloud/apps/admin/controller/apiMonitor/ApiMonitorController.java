package com.toucan.shopping.cloud.apps.admin.controller.apiMonitor;

import com.toucan.shopping.cloud.admin.auth.api.FunctionServiceAPI;
import com.toucan.shopping.cloud.apiMonitor.api.ApiMonitorDashboardServiceAPI;
import com.toucan.shopping.cloud.apps.admin.auth.web.controller.base.UIController;
import com.toucan.shopping.modules.auth.admin.AdminAuth;
import com.toucan.shopping.modules.common.properties.Toucan;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 接口监控看板
 */
@Controller
@RequestMapping("/apiMonitor")
public class ApiMonitorController extends UIController {

    @Autowired
    private Toucan toucan;

    @Autowired
    private FunctionServiceAPI functionServiceAPI;

    @Autowired
    private ApiMonitorDashboardServiceAPI apiMonitorDashboardServiceAPI;

    /** 实时大盘页面 */
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH, requestType = AdminAuth.REQUEST_FORM, responseType = AdminAuth.RESPONSE_FORM)
    @RequestMapping(value = "/dashboardPage", method = RequestMethod.GET)
    public String dashboardPage(HttpServletRequest request) {
        super.initButtons(request, toucan, "/apiMonitor/dashboardPage", functionServiceAPI);
        return "pages/apiMonitor/dashboard.html";
    }

    /** 耗时趋势页面 */
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH, requestType = AdminAuth.REQUEST_FORM, responseType = AdminAuth.RESPONSE_FORM)
    @RequestMapping(value = "/trendPage", method = RequestMethod.GET)
    public String trendPage(HttpServletRequest request) {
        super.initButtons(request, toucan, "/apiMonitor/trendPage", functionServiceAPI);
        return "pages/apiMonitor/trend.html";
    }

    /** 慢请求列表页面 */
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH, requestType = AdminAuth.REQUEST_FORM, responseType = AdminAuth.RESPONSE_FORM)
    @RequestMapping(value = "/slowListPage", method = RequestMethod.GET)
    public String slowListPage(HttpServletRequest request) {
        super.initButtons(request, toucan, "/apiMonitor/slowListPage", functionServiceAPI);
        return "pages/apiMonitor/slowList.html";
    }

    /** 概要统计 API */
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH)
    @RequestMapping(value = "/summary", method = RequestMethod.GET)
    @ResponseBody
    public ResultObjectVO summary(@RequestParam(defaultValue = "5") int minutes,
                                  @RequestParam(defaultValue = "1") int page,
                                  @RequestParam(defaultValue = "30") int limit) {
        return apiMonitorDashboardServiceAPI.getSummary(minutes, page, limit);
    }

    /** 趋势 API */
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH)
    @RequestMapping(value = "/trend", method = RequestMethod.GET)
    @ResponseBody
    public ResultObjectVO trend(@RequestParam String apiUrl,
                                @RequestParam(required = false) String appName,
                                @RequestParam(defaultValue = "60") int range,
                                @RequestParam(defaultValue = "1") int page,
                                @RequestParam(defaultValue = "30") int limit) {
        return apiMonitorDashboardServiceAPI.getTrend(apiUrl, appName, range, page, limit);
    }

    /** 慢请求列表 API */
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH)
    @RequestMapping(value = "/slowList", method = RequestMethod.GET)
    @ResponseBody
    public ResultObjectVO slowList(@RequestParam(defaultValue = "3000") int minElapsed,
                                   @RequestParam(defaultValue = "1") int page,
                                   @RequestParam(name = "limit", defaultValue = "50") int size) {
        return apiMonitorDashboardServiceAPI.getSlowList(minElapsed, page, size);
    }
}
