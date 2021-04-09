package com.toucan.shopping.admin.auth.export.page;

import com.toucan.shopping.admin.auth.export.entity.Function;
import com.toucan.shopping.common.page.PageInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * 功能项列表分页查询
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
public class FunctionPageInfo extends PageInfo<Function> {




    // ===============查询条件===================


    /**
     * 名称
     */
    private String name;

    /**
     * 功能项ID
     */
    private String functionId;

    /**
     * 连接
     */
    private String link;


    /**
     * 菜单类型 0目录 1菜单 2按钮
     */
    private Short type;


    /**
     * 启用状态 0:禁用 1启用
     */
    private Short enableStatus;

    /**
     * 所属应用
     */
    private String appCode;


    //==============================================


}
