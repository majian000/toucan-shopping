package com.toucan.shopping.modules.admin.auth.log.service;


import com.toucan.shopping.modules.admin.auth.log.vo.OperateLogVO;

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
}
