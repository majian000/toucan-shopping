package com.toucan.shopping.modules.product.vo;

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
public class InventoryReductionVO {


    private String appCode;
    private String userId;
    private Long productSkuId; //SKU ID
    private Integer stockNum; //减库存数量

    private Short type; //扣库存方式 1:预扣库存 2:实际扣库存

}
