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

    /** 查询已聚合的最新时间窗口（ShardingSphere 分表返回多行，由 Service 层取最大值），用于补偿追赶 */
    List<Date> selectMaxTimeWindow();

    /** 批量插入聚合结果 */
    int batchInsert(List<ApiMonitorMetricsPO> list);

    /** 查询已存在的 (api_url, app_name, time_window) 组合，用于去重 */
    List<ApiMonitorMetricsPO> selectExistingKeys(Date startTime, Date endTime);

    /** 删除过期数据 */
    int deleteByCreateDate(Date beforeDate);
}
