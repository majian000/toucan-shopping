package com.toucan.shopping.stock.export.vo;

import com.toucan.shopping.product.export.entity.ProductSku;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

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
