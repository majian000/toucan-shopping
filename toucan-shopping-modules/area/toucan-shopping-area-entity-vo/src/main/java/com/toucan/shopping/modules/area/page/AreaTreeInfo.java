package com.toucan.shopping.modules.area.page;

import com.toucan.shopping.modules.area.entity.Area;
import com.toucan.shopping.modules.common.page.PageInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * 组织机构列表分页查询
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
public class AreaTreeInfo extends PageInfo<Area> {




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
     * 管理员ID
     */
    private String adminId;

    //==============================================


}
