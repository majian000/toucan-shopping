package com.toucan.shopping.modules.admin.auth.vo;

import lombok.Data;

@Data
public class AuthVerifyVO {

    /**
     * 管理员账号ID
     */
    private String adminId;

    /**
     * 访问路径
     */
    private String url;

    /**
     * 应用编码
     */
    private String appCode;

    /**
     * 登录会话
     */
    private String loginToken;


}
