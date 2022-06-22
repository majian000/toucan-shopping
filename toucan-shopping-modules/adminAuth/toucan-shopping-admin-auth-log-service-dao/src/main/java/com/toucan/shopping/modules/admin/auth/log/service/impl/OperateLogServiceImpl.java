package com.toucan.shopping.modules.admin.auth.log.service.impl;

import com.toucan.shopping.modules.admin.auth.log.entity.OperateLog;
import com.toucan.shopping.modules.admin.auth.log.mapper.OperateLogMapper;
import com.toucan.shopping.modules.admin.auth.log.service.OperateLogService;
import com.toucan.shopping.modules.admin.auth.log.vo.OperateLogChartVO;
import com.toucan.shopping.modules.admin.auth.log.vo.OperateLogPageInfo;
import com.toucan.shopping.modules.admin.auth.log.vo.OperateLogVO;
import com.toucan.shopping.modules.common.page.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class OperateLogServiceImpl implements OperateLogService {

    @Autowired
    private OperateLogMapper operateLogMapper;


    @Override
    public int saves(List<OperateLogVO> requestLogs) {
        return operateLogMapper.inserts(requestLogs);
    }

    @Override
    public List<OperateLogChartVO> queryOperateLogCountList(Date startDate, Date endDate, String appCode) {
        return operateLogMapper.queryOperateLogCountList(startDate,endDate,appCode);
    }

    @Override
    public PageInfo<OperateLogVO> queryListPage(OperateLogPageInfo pageInfo) {
        pageInfo.setStart(pageInfo.getPage()*pageInfo.getLimit()-pageInfo.getLimit());
        pageInfo.setList(operateLogMapper.queryListPage(pageInfo));
        pageInfo.setTotal(operateLogMapper.queryListPageCount(pageInfo));
        return pageInfo;
    }

    @Override
    public List<OperateLog> findListByEntity(OperateLog query) {
        return operateLogMapper.findListByEntity(query);
    }
}
