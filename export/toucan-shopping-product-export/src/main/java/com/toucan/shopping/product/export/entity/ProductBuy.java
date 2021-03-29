package com.toucan.shopping.product.export.entity;

import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * 用户购买商品时封装的实体
 *
 * @author majian
 */
@Data
public class ProductBuy {


    private final Logger logger = LoggerFactory.getLogger(getClass());

    private Long skuId; //主键
    private Integer buyNum; //购买数量



}
