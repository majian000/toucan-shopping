package com.toucan.shopping.modules.apiMonitor.controller;

import com.toucan.shopping.modules.apiMonitor.service.DashboardService;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Dashboard 查询控制器
 */
@RestController
@RequestMapping("/api-monitor")
public class DashboardController {

    @Autowired
    private DashboardService dashboardService;

    /** 实时概要统计 */
    @RequestMapping(value = "/dashboard/summary", method = RequestMethod.GET)
    public ResultObjectVO summary(
            @RequestParam(defaultValue = "5") int minutes,
            @RequestParam(required = false) String apiUrl,
            @RequestParam(required = false) String appName) {
        ResultObjectVO result = new ResultObjectVO();
        try {
            result.setData(dashboardService.getSummary(apiUrl, appName, minutes));
            result.setCode(ResultObjectVO.SUCCESS);
        } catch (Exception e) {
            result.setCode(ResultObjectVO.FAILD);
            result.setMsg(e.getMessage());
        }
        return result;
    }

    /** 趋势查询 */
    @RequestMapping(value = "/dashboard/trend", method = RequestMethod.GET)
    public ResultObjectVO trend(
            @RequestParam String apiUrl,
            @RequestParam(required = false) String appName,
            @RequestParam(defaultValue = "60") int range) {
        ResultObjectVO result = new ResultObjectVO();
        try {
            result.setData(dashboardService.getTrend(apiUrl, appName, range));
            result.setCode(ResultObjectVO.SUCCESS);
        } catch (Exception e) {
            result.setCode(ResultObjectVO.FAILD);
            result.setMsg(e.getMessage());
        }
        return result;
    }

    /** 慢请求列表 */
    @RequestMapping(value = "/dashboard/slow-list", method = RequestMethod.GET)
    public ResultObjectVO slowList(
            @RequestParam(defaultValue = "3000") int minElapsed,
            @RequestParam(required = false) String apiUrl,
            @RequestParam(required = false) String appName,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size) {
        ResultObjectVO result = new ResultObjectVO();
        try {
            result.setData(dashboardService.getSlowList(apiUrl, appName, minElapsed, page, size));
            result.setCode(ResultObjectVO.SUCCESS);
        } catch (Exception e) {
            result.setCode(ResultObjectVO.FAILD);
            result.setMsg(e.getMessage());
        }
        return result;
    }
}
