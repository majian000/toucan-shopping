package com.toucan.shopping.modules.apiMonitor.service;

import com.toucan.shopping.modules.apiMonitor.entity.ApiMonitorRecordPO;
import com.toucan.shopping.modules.apiMonitor.vo.ApiMonitorRecordVO;
import com.toucan.shopping.modules.common.generator.IdGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * 持久化服务（VO→PO转换 + 调用 service-dao 层）
 */
@Service
public class PersistService {

    @Autowired
    private ApiMonitorRecordService apiMonitorRecordService;

    @Autowired
    private IdGenerator idGenerator;

    /**
     * 批量保存上报记录
     */
    public void batchInsert(List<ApiMonitorRecordVO> voList) {
        if (voList == null || voList.isEmpty()) {
            return;
        }
        List<ApiMonitorRecordPO> poList = new ArrayList<>(voList.size());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
        for (ApiMonitorRecordVO vo : voList) {
            ApiMonitorRecordPO po = new ApiMonitorRecordPO();
            po.setId(idGenerator.id());
            po.setApiUrl(vo.getApiUrl());
            po.setHttpMethod(vo.getMethod());
            po.setElapsedMs((int) vo.getElapsedMs());
            po.setStatusCode(vo.getStatusCode());
            po.setTraceId(vo.getTraceId());
            po.setAppName(vo.getAppName());
            po.setServerIp(vo.getServerIp());
            if (vo.getRequestTime() != null) {
                try {
                    java.util.Date requestTime = sdf.parse(vo.getRequestTime());
                    po.setRequestTime(requestTime);
                    po.setShardingDate(requestTime);
                } catch (ParseException e) {
                    java.util.Date now = new java.util.Date();
                    po.setRequestTime(now);
                    po.setShardingDate(now);
                }
            }
            poList.add(po);
        }
        apiMonitorRecordService.batchInsert(poList);
    }
}
