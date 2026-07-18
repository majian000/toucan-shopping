package com.toucan.shopping.modules.apiMonitor.mapper;

import com.toucan.shopping.modules.apiMonitor.entity.ApiMonitorMetricsPO;
import org.apache.ibatis.annotations.Mapper;

import java.util.Date;
import java.util.List;

/**
 * 接口监控分钟聚合 Mapper
 */
@Mapper
public interface ApiMonitorMetricsMapper {

    /** 查询概要统计 */
    List<ApiMonitorMetricsPO> selectSummary(String apiUrl, String appName, Date startTime, Date endTime);

    /** 趋势查询 */
    List<ApiMonitorMetricsPO> selectTrend(String apiUrl, String appName, Date startTime, Date endTime);

    /** 删除过期数据 */
    int deleteByCreateDate(Date beforeDate);
}
