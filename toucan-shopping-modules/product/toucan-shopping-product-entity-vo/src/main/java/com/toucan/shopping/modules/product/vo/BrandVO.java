package com.toucan.shopping.modules.product.vo;

import com.toucan.shopping.modules.product.entity.AttributeKey;
import com.toucan.shopping.modules.product.entity.Brand;
import lombok.Data;

import java.util.List;

/**
 * 品牌
 *
 * @author majian
 */
@Data
public class BrandVO extends Brand {

    /**
     * 品牌分类关联
     */
    private List<BrandCategoryVO> brandCategorys;

}
