package com.toucan.shopping.modules.admin.auth.page;

import com.toucan.shopping.modules.admin.auth.vo.DictVO;
import com.toucan.shopping.modules.common.page.PageInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;


/**
 * 字典分类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
public class DictPageInfo extends PageInfo<DictVO> {




    // ===============查询条件===================

    /**
     * 名称
     */
    private String name;

    /**
     * 编码
     */
    private String code;

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
