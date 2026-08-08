package com.toucan.shopping.modules.admin.auth.log.vo;

import com.toucan.shopping.modules.admin.auth.log.entity.OperateLog;
import lombok.Data;

/**
 * 操作日志详情
 */
@Data
public class OperateLogDetailVO extends OperateLog {

    /**
     * 应用名称
     */
    private String appName;

}
