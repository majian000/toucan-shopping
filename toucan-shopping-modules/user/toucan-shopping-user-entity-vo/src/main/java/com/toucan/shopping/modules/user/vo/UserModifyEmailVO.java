package com.toucan.shopping.modules.user.vo;

import lombok.Data;

@Data
public class UserModifyEmailVO extends UserVO {



    /**
     * 要登录的应用
     */
    private String appCode;


    private String oldEmail; //旧邮箱

    private String newEmail; //新邮箱

}
