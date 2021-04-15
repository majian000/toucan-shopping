package com.toucan.shopping.modules.product.message;

import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * 减少库存消息
 */
@Data
public class InventoryReductionMessage {

    /**
     * 全局事务ID
     */
    private String globalTransactionId;

    private String orderNo;

    private String skuId;

    private String skuUuid;

    private String appCode;

    private String userId;

    private String productSkuListJson;


    /**
     * 本地事件ID
     */
    private String localEventPublishId;

}
