package com.toucan.shopping.modules.user.vo;

import lombok.Data;

@Data
public class UserModifyMobilePhoneVO extends UserVO {



    /**
     * 要登录的应用
     */
    private String appCode;

    /**
     * 安全码
     */
    private String securityCode;

}
