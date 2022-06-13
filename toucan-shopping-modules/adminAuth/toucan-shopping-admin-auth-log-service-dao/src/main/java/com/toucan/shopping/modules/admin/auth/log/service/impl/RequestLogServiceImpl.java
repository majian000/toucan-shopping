package com.toucan.shopping.modules.admin.auth.log.service.impl;

import com.toucan.shopping.modules.admin.auth.log.entity.RequestLog;
import com.toucan.shopping.modules.admin.auth.log.mapper.RequestLogMapper;
import com.toucan.shopping.modules.admin.auth.log.service.RequestLogService;
import com.toucan.shopping.modules.admin.auth.log.vo.RequestLogVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RequestLogServiceImpl implements RequestLogService {

    @Autowired
    private RequestLogMapper requestLogMapper;


    @Override
    public int saves(List<RequestLogVO> requestLogs) {
        return requestLogMapper.inserts(requestLogs);
    }
}
