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
     * 前台传入
     */
    private String vcode;

    /**
     * 关联所有应用
     */
    private String appNames;

}
