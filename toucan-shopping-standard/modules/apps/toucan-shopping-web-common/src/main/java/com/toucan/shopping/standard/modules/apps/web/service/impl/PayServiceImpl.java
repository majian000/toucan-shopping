package com.toucan.shopping.standard.modules.apps.web.service.impl;

import com.toucan.shopping.modules.order.entity.Order;
import com.toucan.shopping.standard.modules.apps.web.service.PayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class PayServiceImpl implements PayService {


    @Autowired
    private StringRedisTemplate redisTemplate;


    @Override
    public boolean orderPay(Order order) {
        return true;
    }
}
