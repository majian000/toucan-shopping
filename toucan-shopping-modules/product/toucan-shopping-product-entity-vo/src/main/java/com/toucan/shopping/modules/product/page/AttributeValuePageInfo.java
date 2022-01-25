package com.toucan.shopping.modules.product.page;

import com.toucan.shopping.modules.common.page.PageInfo;
import com.toucan.shopping.modules.product.vo.AttributeKeyVO;
import com.toucan.shopping.modules.product.vo.AttributeValueVO;
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
public class AttributeValuePageInfo extends PageInfo<AttributeValueVO> {


    // ===============查询条件===================

    private Integer id;


    private Long attributeKeyId; //属性名ID

    private Long categoryId; //所属类别

    private String attributeValue;


    private Long[] idArray; //ID数组

    //==============================================

}
