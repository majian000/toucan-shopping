package com.toucan.shopping.modules.order.vo;

import com.toucan.shopping.modules.order.entity.MainOrder;
import lombok.Data;

import java.util.List;

@Data
public class MainOrderVO extends MainOrder {

    private List<OrderVO> orders; //所有拆分后的订单(根据店铺、运费模板拆分订单)

    private Long createDateLong; //订单创建时间

    private Long systemDateLong; //系统时间

    private Long timeRemaining; //剩余时间=系统时间-订单创建时间

    private Long maxPayTime; //最大允许支付的时间

}
