package com.toucan.shopping.modules.admin.auth.vo;

import com.toucan.shopping.modules.admin.auth.entity.Admin;
import com.toucan.shopping.modules.admin.auth.entity.AdminApp;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 用户
 */
@Data
public class AdminVO extends Admin {

    /**
     * 应用登录状态
     */
    private String appLoginEnableStatus;


    /**
     * 前台传入
     */
    private String vcode;

}
