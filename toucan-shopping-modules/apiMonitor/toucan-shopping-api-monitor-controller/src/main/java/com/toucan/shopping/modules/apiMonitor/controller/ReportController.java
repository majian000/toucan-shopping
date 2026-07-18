package com.toucan.shopping.modules.apiMonitor.controller;

import com.toucan.shopping.modules.apiMonitor.service.PersistService;
import com.toucan.shopping.modules.apiMonitor.vo.ApiMonitorRecordVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 上报接口控制器
 */
@RestController
@RequestMapping("/api-monitor")
public class ReportController {

    @Autowired
    private PersistService persistService;

    @RequestMapping(value = "/reports/batch", method = RequestMethod.POST)
    public ResultObjectVO sendBatch(@RequestBody List<ApiMonitorRecordVO> records) {
        ResultObjectVO result = new ResultObjectVO();
        try {
            persistService.batchInsert(records);
            result.setCode(ResultObjectVO.SUCCESS);
        } catch (Exception e) {
            result.setCode(ResultObjectVO.FAILD);
            result.setMsg(e.getMessage());
        }
        return result;
    }
}
