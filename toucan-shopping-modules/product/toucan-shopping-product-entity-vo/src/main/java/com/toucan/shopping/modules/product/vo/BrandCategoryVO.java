package com.toucan.shopping.modules.product.vo;

import com.toucan.shopping.modules.product.entity.Brand;
import com.toucan.shopping.modules.product.entity.BrandCategory;
import lombok.Data;

/**
 * 品牌与分类关联
 *
 * @author majian
 */
@Data
public class BrandCategoryVO extends BrandCategory {


    /**
     * 关联的品牌名称,以类目为主表查询出这个关联对应的品牌名称
     */
    private String brandName;

}
