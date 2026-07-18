package com.toucan.shopping.cloud.apiMonitor.api.single;

import com.toucan.shopping.cloud.apiMonitor.api.ApiMonitorReportServiceAPI;
import com.toucan.shopping.modules.apiMonitor.service.PersistService;
import com.toucan.shopping.modules.apiMonitor.vo.ApiMonitorRecordVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 单服务版接口监控上报实现
 */
@Service
public class ApiMonitorReportServiceAPISingleImpl implements ApiMonitorReportServiceAPI {

    @Autowired
    private PersistService persistService;

    @Override
    public ResultObjectVO sendBatch(List<ApiMonitorRecordVO> records) {
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
