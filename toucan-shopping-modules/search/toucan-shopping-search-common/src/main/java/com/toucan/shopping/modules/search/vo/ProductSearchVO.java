package com.toucan.shopping.modules.search.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * 商品搜索
 */
@Data
public class ProductSearchVO extends SearchVO{

    private String keyword; //查询关键字

    private BigDecimal price; //价格

    private String cid; //分类ID

    private List<Long> brandIds; //品牌ID

    private String bid; //品牌ID

    private String ebids; //将这些品牌ID排除出查询条件

    private String qbs="t"; //是否先查询品牌 f:不查询 t:查询

    private String productName; //商品名称

    private String brandName; //品牌名称

    private String categoryName; //分类名称


}
