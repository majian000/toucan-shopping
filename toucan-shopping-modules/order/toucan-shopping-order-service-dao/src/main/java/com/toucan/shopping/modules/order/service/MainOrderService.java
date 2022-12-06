package com.toucan.shopping.modules.order.service;

import com.toucan.shopping.modules.order.entity.MainOrder;
import com.toucan.shopping.modules.order.vo.MainOrderVO;
import com.toucan.shopping.modules.product.entity.ProductBuy;
import com.toucan.shopping.modules.product.entity.ProductSku;

import java.util.List;
import java.util.Map;

public interface MainOrderService {

    int save(MainOrder mainOrder);

    /**
     * 创建订单
     * @param mainOrderVO
     * @return
     */
    int createOrder(MainOrderVO mainOrderVO);

}
