package com.toucan.shopping.modules.stock.kafka.constant;

public enum StockMessageTopicConstant {
    //秒杀扣库存消息
    sk_inventory_reduction,
    //扣库存消息
    inventory_reduction,
    //预扣库存消息
    inventory_reduction_redis,
    //还原库存消息
    restore_stock,
    //还原预扣库存消息
    restore_redis_stock,
}