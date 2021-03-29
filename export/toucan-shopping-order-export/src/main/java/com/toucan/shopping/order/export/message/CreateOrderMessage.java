package com.toucan.shopping.order.export.message;

import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * 创建订单消息
 */
@Data
public class CreateOrderMessage {

    /**
     * 全局事务ID
     */
    private String globalTransactionId;

    private String orderNo;

    private String skuId;

    private String skuUuid;

    private String appCode;

    private String userId;

    /**
     * 购买所有商品JSON
     */
    private String productSkuListJson;

    /**
     *  购买商品数量
     */
    private String buyMap;

    /**
     * 本地事件ID
     */
    private String localEventPublishId;

}
