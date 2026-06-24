package com.toucan.shopping.cloud.admin.auth.api.single;

import com.toucan.shopping.cloud.admin.auth.api.OperateLogServiceAPI;
import com.toucan.shopping.modules.admin.auth.log.controller.requestLog.OperateLogController;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OperateLogServiceAPISingleImpl implements OperateLogServiceAPI {

    @Autowired
    private OperateLogController operateLogController;

    @Override
    public ResultObjectVO saves(RequestJsonVO requestJsonVO) {
        return null;
    }

    @Override
    public ResultObjectVO queryOperateChart(RequestJsonVO requestJsonVO) {
        return null;
    }

    @Override
    public ResultObjectVO listPage(RequestJsonVO requestVo) {
        return null;
    }

    @Override
    public ResultObjectVO findById(RequestJsonVO requestVo) {
        return null;
    }
}
