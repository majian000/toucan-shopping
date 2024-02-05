package com.toucan.shopping.modules.log.vo;

import lombok.Data;

/**
 * 日志参数对象
 */
@Data
public class LogParam {

    private LogParamRequest request; //请求对象

    private LogParamResponse response; //响应对象

}
