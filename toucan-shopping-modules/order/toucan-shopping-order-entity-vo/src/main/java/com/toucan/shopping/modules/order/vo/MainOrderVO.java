package com.toucan.shopping.modules.order.vo;

import com.toucan.shopping.modules.order.entity.MainOrder;
import lombok.Data;

import java.util.List;

@Data
public class MainOrderVO extends MainOrder {

    private List<OrderVO> orders; //所有拆分后的订单(根据店铺、运费模板拆分订单)

}
