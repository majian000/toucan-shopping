package com.toucan.shopping.modules.product.page;

import com.toucan.shopping.modules.common.page.PageInfo;
import com.toucan.shopping.modules.product.vo.BrandCategoryVO;
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
public class BrandCategoryPageInfo extends PageInfo<BrandCategoryVO> {

    // ===============查询条件===================


    private Long categoryId;


    //==============================================

}
