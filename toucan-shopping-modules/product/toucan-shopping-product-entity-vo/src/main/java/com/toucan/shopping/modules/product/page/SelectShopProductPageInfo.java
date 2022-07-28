package com.toucan.shopping.modules.product.page;


import com.toucan.shopping.modules.product.vo.ShopProductVO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

/**
 * 选择商品组件
 * 列表查询页对象
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
public class SelectShopProductPageInfo extends ShopProductPageInfo  {

    /**
     * 已选商品列表 多个用,分割
     * 因为layui reload where存在数组缓存问题 所以改成字符串接收
     */
    private String selectProductIds;
}
