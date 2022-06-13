package com.toucan.shopping.modules.admin.auth.log.service.impl;

import com.toucan.shopping.modules.admin.auth.log.mapper.OperateLogMapper;
import com.toucan.shopping.modules.admin.auth.log.service.OperateLogService;
import com.toucan.shopping.modules.admin.auth.log.vo.OperateLogVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OperateLogServiceImpl implements OperateLogService {

    @Autowired
    private OperateLogMapper operateLogMapper;


    @Override
    public int saves(List<OperateLogVO> requestLogs) {
        return operateLogMapper.inserts(requestLogs);
    }
}
