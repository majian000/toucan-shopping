package com.toucan.shopping.common.vo;

import lombok.Data;

@Data
public class RequestVO {

    /**
     * 请求签名MD5=应用编码+盐+entity JSON字符串
     */
    private String sign;

    /**
     * 应用编码
     */
    private String appCode;


    /**
     * 操作人ID
     */
    private String adminId;

    /**
     * 用户ID
     */
    private String userId;

    /**
     * 默认版本1.0
     */
    private Integer version = 1;

}
