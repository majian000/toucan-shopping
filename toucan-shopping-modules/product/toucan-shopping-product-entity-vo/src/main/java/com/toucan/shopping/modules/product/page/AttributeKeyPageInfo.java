package com.toucan.shopping.modules.product.page;

import com.toucan.shopping.modules.common.page.PageInfo;
import com.toucan.shopping.modules.product.vo.AttributeKeyVO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * 列表查询页对象
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
public class AttributeKeyPageInfo extends PageInfo<AttributeKeyVO> {


    // ===============查询条件===================

    private Integer id;


    /**
     * 标题
     */
    private String title;


    private Long[] idArray; //ID数组

    //==============================================

}
