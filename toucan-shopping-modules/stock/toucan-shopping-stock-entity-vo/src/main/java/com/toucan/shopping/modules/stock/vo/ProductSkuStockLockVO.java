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

    private List<String> mainOrderNoList; //主订单编号集合


    private Short type; //库存计数 1:买家拍下减库存 2:买家付款减库存 -1:全部

}
