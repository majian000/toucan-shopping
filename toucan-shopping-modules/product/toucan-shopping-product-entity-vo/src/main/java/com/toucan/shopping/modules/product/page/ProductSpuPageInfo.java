package com.toucan.shopping.modules.product.page;

import com.toucan.shopping.modules.common.page.PageInfo;
import com.toucan.shopping.modules.product.vo.ProductSkuVO;
import com.toucan.shopping.modules.product.vo.ProductSpuVO;
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
public class ProductSpuPageInfo extends PageInfo<ProductSpuVO> {

    // ===============查询条件===================

    private Long id;

    /**
     * 品牌ID
     */
    private Long brandId;

    /**
     * 分类ID
     */
    private Long categoryId;

    /**
     * 是否上架 -1全部 0:未上架 1:已上架
     */
    private Integer status;

    //==============================================

}
