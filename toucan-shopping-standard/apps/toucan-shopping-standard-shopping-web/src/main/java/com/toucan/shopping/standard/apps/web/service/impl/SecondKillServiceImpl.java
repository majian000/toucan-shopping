package com.toucan.shopping.standard.apps.web.service.impl;

import com.toucan.shopping.standard.apps.web.service.SecondKillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class SecondKillServiceImpl implements SecondKillService {


    @Autowired
    private StringRedisTemplate redisTemplate;



}
