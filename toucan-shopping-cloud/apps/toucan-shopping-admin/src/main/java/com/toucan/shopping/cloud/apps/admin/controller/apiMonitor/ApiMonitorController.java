package com.toucan.shopping.cloud.apps.admin.controller.apiMonitor;

import com.toucan.shopping.cloud.apiMonitor.api.ApiMonitorDashboardServiceAPI;
import com.toucan.shopping.modules.apiMonitor.vo.DashboardQueryVO;
import com.toucan.shopping.modules.auth.admin.AdminAuth;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/apiMonitor")
public class ApiMonitorController {

    @Autowired
    private ApiMonitorDashboardServiceAPI apiMonitorDashboardServiceAPI;

    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH, verifyType = AdminAuth.VERIFY_TYPE_ANY, permissions = {"toucan:monitor:dashboard:summary"})
    @RequestMapping(value = "/summary", method = RequestMethod.POST)
    public ResultObjectVO summary(@RequestBody DashboardQueryVO query) {
        return apiMonitorDashboardServiceAPI.getSummary(query);
    }

    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH, verifyType = AdminAuth.VERIFY_TYPE_ANY, permissions = {"toucan:monitor:dashboard:requestLog"})
    @RequestMapping(value = "/requestLog", method = RequestMethod.POST)
    public ResultObjectVO requestLog(@RequestBody DashboardQueryVO query) {
        return apiMonitorDashboardServiceAPI.getRequestLog(query);
    }
}
