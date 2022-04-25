package com.toucan.shopping.modules.product.page;

import com.toucan.shopping.modules.common.page.PageInfo;
import com.toucan.shopping.modules.product.vo.ProductSkuVO;
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
public class ProductSkuPageInfo extends PageInfo<ProductSkuVO> {

    // ===============查询条件===================

    private Long shopProductId;

    private Long categoryId; //分类ID

    private List<Long> categoryIdList; //分类ID列表

    private Long shopCategoryId; //店铺分类ID

    private String orderColumn; //排序列

    private String orderSort; //升序还是降序

    //==============================================

}
