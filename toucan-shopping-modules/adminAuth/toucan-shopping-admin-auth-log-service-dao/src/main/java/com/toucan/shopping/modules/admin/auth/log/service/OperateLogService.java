package com.toucan.shopping.modules.admin.auth.log.service;


import com.toucan.shopping.modules.admin.auth.log.vo.OperateLogChartVO;
import com.toucan.shopping.modules.admin.auth.log.vo.OperateLogVO;

import java.util.Date;
import java.util.List;

/**
 * 请求日志
 */
public interface OperateLogService {

    /**
     * 批量保存
     * @param requestLogs
     * @return
     */
    int saves(List<OperateLogVO> requestLogs);

    /**
     * 查询指定时间段 指定应用的操作数量
     * @param startDate
     * @param endDate
     * @param appCode
     * @return
     */
    List<OperateLogChartVO> queryOperateLogCountList(Date startDate, Date endDate,String appCode);

}
