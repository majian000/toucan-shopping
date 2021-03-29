package com.toucan.shopping.order.export.vo;

import com.toucan.shopping.product.export.entity.ProductBuy;
import com.toucan.shopping.product.export.entity.ProductSku;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class QueryOrderVo {

    private String appCode;
    private String userId;
    private String orderNo;

    private List<ProductSku> productSkuList;
    private Map<String, ProductBuy> buyMap;

}
