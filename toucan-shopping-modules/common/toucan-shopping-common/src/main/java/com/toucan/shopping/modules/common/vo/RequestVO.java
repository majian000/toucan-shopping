package com.toucan.shopping.modules.common.vo;

import lombok.Data;

@Data
public class RequestVO {

    /**
     * 请求签名MD5=应用编码+盐+entity JSON字符串
     */
    private String sign;

    /**
     * 发起请求的应用编码(谁发的请求)
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
     * 默认版本1.0,可以通过这个字段兼容低版本应用
     */
    private Integer version = 1;

}
