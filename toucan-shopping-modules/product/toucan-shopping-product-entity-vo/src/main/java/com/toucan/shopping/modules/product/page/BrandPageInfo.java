package com.toucan.shopping.modules.product.page;

import com.toucan.shopping.modules.common.page.PageInfo;
import com.toucan.shopping.modules.product.vo.AttributeKeyVO;
import com.toucan.shopping.modules.product.vo.BrandVO;
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
public class BrandPageInfo extends PageInfo<BrandVO> {

    // ===============查询条件===================

    /**
     * 每页显示数量
     */
    private Integer pageSize = 10;

    /**
     * 页码
     */
    private Integer pageNumber = 1;

    private Integer id;

    private Long categoryId;


    /**
     * 查询指定类别下的品牌列表
     */
    private List<Long> categoryIdList;

    /**
     * 品牌ID列表
     */
    private List<Long> brandIdList;

    /**
     * 品牌名称
     */
    private String name;

    /**
     * selectPage查询条件
     */
    private String[] q_word;

    //==============================================

}
