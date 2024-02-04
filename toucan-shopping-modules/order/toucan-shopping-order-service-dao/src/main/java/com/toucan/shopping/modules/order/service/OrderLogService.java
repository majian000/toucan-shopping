package com.toucan.shopping.modules.order.service;

import com.toucan.shopping.modules.common.page.PageInfo;
import com.toucan.shopping.modules.order.entity.Order;
import com.toucan.shopping.modules.order.entity.OrderLog;
import com.toucan.shopping.modules.order.page.OrderPageInfo;
import com.toucan.shopping.modules.order.vo.OrderVO;

import java.util.List;

public interface OrderLogService {

    int save(OrderLog orderLog);

    int save(String batchId,String operateUserId,String appCode,String orderNo,String remark,Object oldObj,Object updateObj,Integer logType);


}
