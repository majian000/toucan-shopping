package com.toucan.shopping.second.kill.service;

import com.toucan.shopping.order.export.message.CreateOrderMessage;
import com.toucan.shopping.product.export.message.InventoryReductionMessage;
import com.toucan.shopping.common.persistence.entity.EventPublish;
import com.toucan.shopping.product.export.entity.ProductSku;

import java.util.List;

public interface SecondKillService {



    /**
     * 发送扣库存消息
     */
    public EventPublish saveInventoryReductionMessagePublishEvent(InventoryReductionMessage inventoryReductionMessage, String orderNo,
                                                                  String globalTransactionId, String skuId,
                                                                  List<ProductSku> productSkuList);

    /**
     * 发送创建订单消息
     */
    public EventPublish saveCreateOrderMessagePublishEvent(CreateOrderMessage createOrderMessage,
                                                           String orderNo, String globalTransactionId);

}
