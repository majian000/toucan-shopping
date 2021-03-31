package com.toucan.shopping.user.scheduler.app.config;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.toucan.shopping.center.user.api.feign.service.FeignUserService;
import com.toucan.shopping.center.user.export.page.UserPageInfo;
import com.toucan.shopping.center.user.service.UserElasticSearchService;
import com.toucan.shopping.common.generator.RequestJsonVOGenerator;
import com.toucan.shopping.common.properties.Toucan;
import com.toucan.shopping.common.util.SignUtil;
import com.toucan.shopping.common.vo.RequestJsonVO;
import com.toucan.shopping.common.vo.ResultObjectVO;
import com.toucan.shopping.common.vo.ResultVO;
import com.toucan.shopping.user.scheduler.thread.UserEsCacheThread;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.stereotype.Component;

/**
 * 用户初始化到ES
 */
@Component
@Order(value = 1)
public class ElasticSearchUserCache implements ApplicationRunner {


    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private UserEsCacheThread userEsCacheThread;



    @Override
    public void run(ApplicationArguments args)  {
        userEsCacheThread.start();
    }
}
