package com.toucan.shopping.modules.stock.vo;

import com.toucan.shopping.modules.product.entity.ProductSku;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * 删减库存对象
 *
 * @author majian
 */
@Data
public class InventoryReductionVo {


    private final Logger logger = LoggerFactory.getLogger(getClass());

    private String appCode;
    private String userId;

    private List<ProductSku> productSkuList;



}
