package com.toucan.shopping.web.vo;

import com.toucan.shopping.modules.product.entity.ProductBuy;
import com.toucan.shopping.modules.product.entity.ProductSku;
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
