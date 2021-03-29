package com.toucan.shopping.common.message;

public enum MessageTopicConstant {
    //秒杀扣库存消息
    sk_inventory_reduction,
    //秒杀创建订单消息
    sk_create_order,
    //扣库存消息
    inventory_reduction,
    //预扣库存消息
    inventory_reduction_redis,
    //还原库存消息
    restore_stock,
    //还原预扣库存消息
    restore_redis_stock,
    //创建订单消息
    create_order;
}
