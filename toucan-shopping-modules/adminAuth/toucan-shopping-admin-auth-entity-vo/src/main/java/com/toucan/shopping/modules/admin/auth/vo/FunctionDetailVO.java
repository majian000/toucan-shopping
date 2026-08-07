package com.toucan.shopping.modules.admin.auth.vo;

import lombok.Data;

/**
 * 功能项详情
 */
@Data
public class FunctionDetailVO {

    /**
     * 基本信息
     */
    private FunctionVO basicInfo;

    /**
     * 应用名称
     */
    private String appName;

}
