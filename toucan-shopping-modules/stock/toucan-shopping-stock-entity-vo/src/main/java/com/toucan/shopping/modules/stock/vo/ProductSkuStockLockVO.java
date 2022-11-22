package com.toucan.shopping.modules.stock.vo;

import com.toucan.shopping.modules.stock.entity.ProductSkuStockLock;
import lombok.Data;

import java.util.List;


/**
 * 商品库存锁定
 * @author majian
 */
@Data
public class ProductSkuStockLockVO extends ProductSkuStockLock {

    private List<Long> productSkuIdList; //商品SKU ID集合

}
