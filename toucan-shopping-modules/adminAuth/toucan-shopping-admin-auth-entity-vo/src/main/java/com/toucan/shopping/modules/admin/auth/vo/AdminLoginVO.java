package com.toucan.shopping.modules.admin.auth.vo;


import lombok.Data;

@Data
public class AdminLoginVO extends AdminVO{

    /**
     * 登录状态 0:未登录 1:登录中
     */
    public int loginStatus=0;

    private String loginToken;
}
