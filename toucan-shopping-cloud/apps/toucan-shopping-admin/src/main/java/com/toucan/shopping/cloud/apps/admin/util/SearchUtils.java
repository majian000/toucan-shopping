package com.toucan.shopping.cloud.apps.admin.util;

import com.alibaba.fastjson.JSONObject;
import com.toucan.shopping.cloud.common.data.api.feign.service.FeignCategoryService;
import com.toucan.shopping.cloud.product.api.feign.service.FeignAttributeKeyValueService;
import com.toucan.shopping.cloud.product.api.feign.service.FeignBrandService;
import com.toucan.shopping.cloud.search.api.feign.service.FeignProductSearchService;
import com.toucan.shopping.cloud.search.helper.service.ProductSearchHelper;
import com.toucan.shopping.cloud.seller.api.feign.service.FeignShopCategoryService;
import com.toucan.shopping.modules.category.vo.CategoryVO;
import com.toucan.shopping.modules.common.generator.RequestJsonVOGenerator;
import com.toucan.shopping.modules.common.properties.Toucan;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.product.vo.*;
import com.toucan.shopping.modules.search.vo.ProductSearchAttributeVO;
import com.toucan.shopping.modules.search.vo.ProductSearchResultVO;
import com.toucan.shopping.modules.seller.vo.ShopCategoryVO;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.security.NoSuchAlgorithmException;
import java.util.*;

public class SearchUtils {



    public static Toucan toucan;

    public static ProductSearchHelper productSearchHelper;


    /**
     * 刷新到搜索
     * @param productSkuVO
     */
    public static void flushToSearch(ProductSkuVO productSkuVO) throws Exception
    {
        productSearchHelper.refresh(productSkuVO);
    }
}
