package com.toucan.shopping.cloud.apps.admin.config;


import com.toucan.shopping.cloud.apps.admin.util.SearchUtils;
import com.toucan.shopping.cloud.search.helper.service.ProductSearchHelper;
import com.toucan.shopping.modules.common.context.ToucanApplicationContext;
import com.toucan.shopping.modules.common.properties.Toucan;
import com.toucan.shopping.modules.content.constant.BannerRedisKey;
import com.toucan.shopping.modules.user.util.LoginTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import jakarta.annotation.PostConstruct;

/**
 * 启动后参数替换
 */
@Configuration
public class AfterStartConfig {


    @Autowired
    public Toucan toucan;


    @Autowired
    private ProductSearchHelper productSearchHelper;


    @PostConstruct
    public void initAppCode()
    {
        SearchUtils.toucan=toucan;
        SearchUtils.productSearchHelper=productSearchHelper;
    }

}
