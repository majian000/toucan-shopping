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

}
