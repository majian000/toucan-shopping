package com.toucan.shopping.modules.apiMonitor.controller;

import com.toucan.shopping.modules.apiMonitor.service.DashboardService;
import com.toucan.shopping.modules.apiMonitor.vo.DashboardQueryVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/dashboard")
public class DashboardController {

    @Autowired
    private DashboardService dashboardService;

    private static ResultObjectVO fail(String msg) {
        ResultObjectVO vo = new ResultObjectVO();
        vo.setCode(ResultObjectVO.FAILD);
        vo.setMsg(msg);
        return vo;
    }

    @RequestMapping(value = "/summary", method = RequestMethod.GET)
    public ResultObjectVO summary(DashboardQueryVO query) {
        if (!StringUtils.hasText(query.getStartTime()) || !StringUtils.hasText(query.getEndTime())) {
            return fail("请选择时间范围");
        }
        return dashboardService.getSummary(query);
    }

    @RequestMapping(value = "/trend", method = RequestMethod.GET)
    public ResultObjectVO trend(DashboardQueryVO query) {
        if (!StringUtils.hasText(query.getStartTime()) || !StringUtils.hasText(query.getEndTime())) {
            return fail("请选择时间范围");
        }
        return dashboardService.getTrend(query);
    }

    @RequestMapping(value = "/slow-list", method = RequestMethod.GET)
    public ResultObjectVO slowList(DashboardQueryVO query) {
        if (!StringUtils.hasText(query.getStartTime()) || !StringUtils.hasText(query.getEndTime())) {
            return fail("请选择时间范围");
        }
        return dashboardService.getSlowList(query);
    }
}
