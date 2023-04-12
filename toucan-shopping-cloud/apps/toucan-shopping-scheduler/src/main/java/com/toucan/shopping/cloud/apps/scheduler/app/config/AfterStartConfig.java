package com.toucan.shopping.cloud.apps.scheduler.app.config;


import com.toucan.shopping.modules.common.context.ToucanApplicationContext;
import com.toucan.shopping.modules.common.properties.Toucan;
import com.toucan.shopping.modules.search.service.ProductSearchService;
import com.toucan.shopping.modules.user.util.LoginTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

/**
 * 启动后参数替换
 */
@Configuration
public class AfterStartConfig {

    @Autowired
    private Toucan toucan;

    @Autowired
    private ProductSearchService productSearchService;



    @PostConstruct
    public void initAppCode()
    {
        if(!productSearchService.existsIndex())
        {
            productSearchService.createIndex();
        }

    }

}
