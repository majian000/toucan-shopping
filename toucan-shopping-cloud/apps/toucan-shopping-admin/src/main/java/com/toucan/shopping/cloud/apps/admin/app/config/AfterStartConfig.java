package com.toucan.shopping.cloud.apps.admin.app.config;


import com.toucan.shopping.cloud.apps.admin.util.SearchUtils;
import com.toucan.shopping.cloud.common.data.api.feign.service.FeignCategoryService;
import com.toucan.shopping.cloud.product.api.feign.service.FeignAttributeKeyValueService;
import com.toucan.shopping.cloud.product.api.feign.service.FeignBrandService;
import com.toucan.shopping.cloud.search.api.feign.service.FeignProductSearchService;
import com.toucan.shopping.cloud.search.helper.service.ProductSearchHelper;
import com.toucan.shopping.cloud.seller.api.feign.service.FeignShopCategoryService;
import com.toucan.shopping.modules.common.context.ToucanApplicationContext;
import com.toucan.shopping.modules.common.properties.Toucan;
import com.toucan.shopping.modules.content.constant.BannerRedisKey;
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
    public FeignProductSearchService feignProductSearchService;

    @Autowired
    public Toucan toucan;

    @Autowired
    public FeignCategoryService feignCategoryService;

    @Autowired
    public FeignBrandService feignBrandService;

    @Autowired
    private FeignAttributeKeyValueService feignAttributeKeyValueService;

    @Autowired
    private FeignShopCategoryService feignShopCategoryService;

    @Autowired
    private ProductSearchHelper productSearchHelper;


    @PostConstruct
    public void initAppCode()
    {
        SearchUtils.toucan=toucan;
        SearchUtils.productSearchHelper=productSearchHelper;
    }

}
