package com.toucan.shopping.modules.order.service;

import com.toucan.shopping.modules.common.page.PageInfo;
import com.toucan.shopping.modules.order.entity.OrderConsigneeAddress;

import java.util.List;

/**
 * 订单收货人
 * @author majian
 * @date 2022-12-28 11:08:01
 */
public interface OrderConsigneeAddressService {

    int save(OrderConsigneeAddress entity);


}
