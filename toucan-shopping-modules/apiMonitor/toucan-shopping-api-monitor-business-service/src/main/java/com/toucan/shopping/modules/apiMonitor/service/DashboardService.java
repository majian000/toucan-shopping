package com.toucan.shopping.modules.apiMonitor.service;

import com.toucan.shopping.modules.apiMonitor.entity.ApiMonitorSummaryPO;
import com.toucan.shopping.modules.apiMonitor.vo.DashboardQueryVO;
import com.toucan.shopping.modules.common.page.PageInfo;
import com.toucan.shopping.modules.common.util.DateUtils;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

@Service
public class DashboardService {

    @Autowired
    private ApiMonitorRecordService apiMonitorRecordService;

    public ResultObjectVO getSummary(DashboardQueryVO query) {
        ResultObjectVO result = new ResultObjectVO();
        try {
            Date start = DateUtils.parse(query.getStartTime(), DateUtils.FORMATTER_SS.get());
            Date end = DateUtils.parse(query.getEndTime(), DateUtils.FORMATTER_SS.get());
            List<ApiMonitorSummaryPO> list = apiMonitorRecordService.selectSummary(
                    query.getApiUrl(), query.getAppName(), start, end, query.getMinElapsed());
            int total = list.size();
            int from = (query.getPage() - 1) * query.getLimit();
            int to = Math.min(from + query.getLimit(), total);
            PageInfo<ApiMonitorSummaryPO> pageInfo = new PageInfo<>();
            pageInfo.setList(list.subList(Math.min(from, total), to));
            pageInfo.setTotal((long) total);
            result.setData(pageInfo);
            result.setCode(ResultObjectVO.SUCCESS);
        } catch (ParseException e) {
            result.setCode(ResultObjectVO.FAILD);
            result.setMsg("时间格式错误，请使用 yyyy-MM-dd HH:mm:ss");
        } catch (Exception e) {
            result.setCode(ResultObjectVO.FAILD);
            result.setMsg(e.getMessage());
        }
        return result;
    }

    public ResultObjectVO getRequestLog(DashboardQueryVO query) {
        ResultObjectVO result = new ResultObjectVO();
        try {
            Date start = DateUtils.parse(query.getStartTime(), DateUtils.FORMATTER_SS.get());
            Date end = DateUtils.parse(query.getEndTime(), DateUtils.FORMATTER_SS.get());
            int elapsed = query.getMinElapsed() != null ? query.getMinElapsed() : 0;
            int offset = (query.getPage() - 1) * query.getLimit();
            List<?> items = apiMonitorRecordService.selectSlowList(
                    query.getApiUrl(), query.getAppName(), elapsed, start, end, query.getTraceId(), offset, query.getLimit());
            long total = apiMonitorRecordService.countSlowList(
                    query.getApiUrl(), query.getAppName(), elapsed, start, end, query.getTraceId());
            PageInfo<Object> pageInfo = new PageInfo<>();
            pageInfo.setList((List<Object>) items);
            pageInfo.setTotal(total);
            result.setData(pageInfo);
            result.setCode(ResultObjectVO.SUCCESS);
        } catch (ParseException e) {
            result.setCode(ResultObjectVO.FAILD);
            result.setMsg("时间格式错误，请使用 yyyy-MM-dd HH:mm:ss");
        } catch (Exception e) {
            result.setCode(ResultObjectVO.FAILD);
            result.setMsg(e.getMessage());
        }
        return result;
    }
}
