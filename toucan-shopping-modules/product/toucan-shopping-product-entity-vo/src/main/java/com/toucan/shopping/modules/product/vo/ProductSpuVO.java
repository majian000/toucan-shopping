package com.toucan.shopping.modules.product.vo;

import com.toucan.shopping.modules.product.entity.ProductSpu;
import lombok.Data;

import java.util.Date;
import java.util.List;

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

    private String attributes; //这个SPU的属性

    private String categoryName; //分类名称
    private String categoryPath; //类别路径

    private String brandName; //品牌名称

    private List<ProductSpuAttributeKeyVO> attributeKeys; //所有属性名
    private List<ProductSpuAttributeValueVO> attributeValues; //所有属性值

    private List<ProductSpuAttributeKeyValueVO> attributeKeyValues; //所有属性名和属性值(都在一个数组中)

    private List<ProductSpuAttributeKeyValueTreeVO> attributeTree; //属性树

    private String attributeKeyValuesJson; //属性名和属性值 对象JSON字符串

}
