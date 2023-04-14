package com.toucan.shopping.modules.search.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 商品搜索
 */
@Data
public class ProductSearchVO extends SearchVO{

    private String keyword; //查询关键字

    private BigDecimal price; //价格

}
