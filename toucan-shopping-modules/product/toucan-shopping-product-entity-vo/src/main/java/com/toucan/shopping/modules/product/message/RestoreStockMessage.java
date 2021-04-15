package com.toucan.shopping.modules.product.message;

import lombok.Data;

import java.util.Map;

/**
 * 重置库存消息
 */
@Data
public class RestoreStockMessage {


    /**
     * 全局事务ID
     */
    private String globalTransactionId;

    private String orderNo;

    private String skuId;

    private String appCode;

    private String userId;

}
