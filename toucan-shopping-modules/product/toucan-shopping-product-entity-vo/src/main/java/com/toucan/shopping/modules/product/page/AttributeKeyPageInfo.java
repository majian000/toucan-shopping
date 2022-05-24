package com.toucan.shopping.modules.product.page;

import com.toucan.shopping.modules.common.page.PageInfo;
import com.toucan.shopping.modules.product.vo.AttributeKeyVO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

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

    private Long parentId; //上级属性名

    private String attributeName; //属性名
    private Short showStatus; //显示状态 1显示 0隐藏
    private Short attributeType; //属性类型 1:普通属性 2:颜色属性
    private Short queryStatus; //搜索状态 1可搜索 0不可搜索
    private Short attributeScope; //属性范围 1:全局属性 2:SKU属性

    private Long categoryId; //所属类别


    private Long[] idArray; //ID数组


    private List<Long> categoryIdList; //分类ID列表

    //==============================================

}
