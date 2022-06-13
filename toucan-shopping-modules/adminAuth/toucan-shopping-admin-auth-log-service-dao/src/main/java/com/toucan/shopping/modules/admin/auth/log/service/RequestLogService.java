package com.toucan.shopping.modules.admin.auth.log.service;


import com.toucan.shopping.modules.admin.auth.entity.App;
import com.toucan.shopping.modules.admin.auth.log.entity.RequestLog;
import com.toucan.shopping.modules.admin.auth.log.vo.RequestLogVO;
import com.toucan.shopping.modules.admin.auth.page.AppPageInfo;
import com.toucan.shopping.modules.admin.auth.vo.AppVO;
import com.toucan.shopping.modules.common.page.PageInfo;

import java.util.List;

/**
 * 请求日志
 */
public interface RequestLogService {

    /**
     * 批量保存
     * @param requestLogs
     * @return
     */
    int saves(List<RequestLogVO> requestLogs);
}
