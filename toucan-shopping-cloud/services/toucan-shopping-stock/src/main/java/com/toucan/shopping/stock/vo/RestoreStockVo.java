package com.toucan.shopping.stock.vo;

import com.toucan.shopping.product.export.entity.ProductSku;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * 还原库存对象
 *
 * @author majian
 */
@Data
public class RestoreStockVo {


    private final Logger logger = LoggerFactory.getLogger(getClass());

    private String appCode;
    private String userId;

    private List<ProductSku> productSkuList;



}
