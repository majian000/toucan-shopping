package com.toucan.shopping.modules.admin.auth.page;

import com.toucan.shopping.modules.admin.auth.entity.Dict;
import com.toucan.shopping.modules.admin.auth.entity.Orgnazition;
import com.toucan.shopping.modules.common.page.PageInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * 字典列表分页查询
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
public class DictTreeInfo extends PageInfo<Dict> {




    // ===============查询条件===================

    /**
     * 机构名称
     */
    private String name;

    /**
     * 启用状态 0:禁用 1启用
     */
    private Short enableStatus;

    /**
     * 所属应用
     */
    private String appCode;

    /**
     * 管理员ID
     */
    private String adminId;

    //==============================================


}
