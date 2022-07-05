package com.toucan.shopping.modules.admin.auth.page;

import com.toucan.shopping.modules.admin.auth.entity.Admin;
import com.toucan.shopping.modules.admin.auth.entity.AdminApp;
import com.toucan.shopping.modules.common.page.PageInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;
import java.util.List;

/**
 * 账号应用列表页
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
public class AdminAppPageInfo extends PageInfo<AdminApp> {


    // ===============查询条件===================

    private Integer id;



    /**
     * 管理员ID
     */
    private String adminId;

    /**
     * 所属应用
     */
    private String appCode;


    /**
     * 登录状态(0未登录 1登陆中 -1或null查询全部)
     */
    private Short loginStatus;

    /**
     * 账号名称
     */
    private String username;
}
