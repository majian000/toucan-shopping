package com.toucan.shopping.modules.product.vo;

import com.toucan.shopping.modules.product.entity.ProductSku;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

/**
 * 还原库存对象
 *
 * @author majian
 */
@Data
public class RestoreStockVO {

    private String appCode;
    private String userId;

    private InventoryReductionVO inventoryReductionVO;  //扣库存对象



}
