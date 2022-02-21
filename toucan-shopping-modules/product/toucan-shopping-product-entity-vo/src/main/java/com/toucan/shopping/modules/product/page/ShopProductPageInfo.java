package com.toucan.shopping.modules.product.page;

import com.toucan.shopping.modules.common.page.PageInfo;
import com.toucan.shopping.modules.product.vo.ShopProductVO;
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
public class ShopProductPageInfo extends PageInfo<ShopProductVO> {

    // ===============查询条件===================
    private Long categoryId; //分类ID

    private Integer approveStatus; //审批状态

    private Long shopId; //店铺ID

    private Long brandId; //品牌ID

    private String name; //商品名称
    //==============================================

}
