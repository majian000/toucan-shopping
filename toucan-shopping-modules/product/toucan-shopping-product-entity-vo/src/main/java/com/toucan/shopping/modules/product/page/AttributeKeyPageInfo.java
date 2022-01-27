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


    private String attributeName; //属性名
    private Short showStatus; //显示状态 1显示 0隐藏

    private Long categoryId; //所属类别


    private Long[] idArray; //ID数组

    //==============================================

}
