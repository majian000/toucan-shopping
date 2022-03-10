package com.toucan.shopping.modules.product.vo;

import com.toucan.shopping.modules.product.entity.ProductSpu;
import lombok.Data;

import java.util.Date;

/**
 * 商品
 *
 * @author majian
 */
@Data
public class ProductSpuVO extends ProductSpu {

    private String brandChineseName; //品牌中文名称
    private String brandEnglishName; //品牌英文名称
    private String brandLogo; //品牌logo
    private String brandHttpLogo; //品牌LOGO地址


}
