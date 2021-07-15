package com.toucan.shopping.cloud.apps.web.controller.index;

import com.toucan.shopping.modules.common.properties.Toucan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 首页控制器
 */
@RestController("apiIndexController")
@RequestMapping("/api/index")
public class IndexApiController {

    private final Logger logger = LoggerFactory.getLogger(getClass());


    @Autowired
    private Toucan toucan;



    @Autowired
    private StringRedisTemplate stringRedisTemplate;




}
