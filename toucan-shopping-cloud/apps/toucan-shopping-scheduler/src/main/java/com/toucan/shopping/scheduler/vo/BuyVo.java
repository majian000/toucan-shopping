package com.toucan.shopping.scheduler.vo;

import com.toucan.shopping.product.export.entity.ProductBuy;
import com.toucan.shopping.product.export.entity.ProductSku;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

/**
 * 购买对象
 *
 * @author majian
 */
@Data
public class BuyVo {


    private final Logger logger = LoggerFactory.getLogger(getClass());

    private String appCode;
    private String userId;

    private List<ProductSku> productSkuList;
    private Map<String, ProductBuy> buyMap;


}
