package com.toucan.shopping.modules.admin.auth.log.vo;


import com.toucan.shopping.modules.admin.auth.log.entity.OperateLog;
import lombok.Data;

/**
 * 请求日志
 */
@Data
public class OperateLogVO extends OperateLog {


    private String appName; //应用名称

}
