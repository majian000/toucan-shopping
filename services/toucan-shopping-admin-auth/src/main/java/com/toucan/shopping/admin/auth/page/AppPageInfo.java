package com.toucan.shopping.admin.auth.page;

import com.toucan.shopping.admin.auth.entity.App;
import com.toucan.shopping.common.page.PageInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * 应用列表查询页
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
public class AppPageInfo extends PageInfo<App> {


    // ===============查询条件===================

    /**
     * 所属管理员账号ID
     */
    private String adminId;


    /**
     * 启用状态 0:禁用 1启用
     */
    private Short enableStatus;


    /**
     * 删除状态 0未删除 1已删除
     */
    private Short deleteStatus;

    /**
     * 应用名称
     */
    private String name;

    /**
     * 应用编码
     */
    private String code;

    //==============================================
}
