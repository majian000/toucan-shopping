package com.toucan.shopping.cloud.product.api;

import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;

/**
 * 品牌分类服务
 * @author majian
 */
public interface BrandCategoryServiceAPI {

    /**
     * 根据品牌ID查询
     * @param requestVo
     * @return
     */
    ResultObjectVO findByBrandId(RequestJsonVO requestVo);

}
