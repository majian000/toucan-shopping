package com.toucan.shopping.modules.category.page;

import com.toucan.shopping.modules.category.vo.CategoryHotVO;
import com.toucan.shopping.modules.category.vo.CategoryVO;
import com.toucan.shopping.modules.common.page.PageInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * 热门类别列表分页查询
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
public class CategoryHotTreeInfo extends PageInfo<CategoryHotVO> {




    // ===============查询条件===================

    private String name;


    private Long parentId; //上级类别

    /**
     * 启用状态 0:禁用 1启用
     */
    private Short enableStatus;


    /**
     * 管理员ID
     */
    private String adminId;

    //==============================================


    private String appCode; //所属应用

}
