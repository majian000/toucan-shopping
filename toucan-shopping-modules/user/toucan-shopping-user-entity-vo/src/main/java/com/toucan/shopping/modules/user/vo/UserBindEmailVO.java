package com.toucan.shopping.modules.user.vo;

import lombok.Data;

@Data
public class UserBindEmailVO extends UserVO {



    /**
     * 要登录的应用
     */
    private String appCode;


}
