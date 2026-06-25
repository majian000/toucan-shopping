package com.toucan.shopping.cloud.content.api;

import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;

/**
 * 热门商品服务
 */
public interface HotProductServiceAPI {

    ResultObjectVO queryListPage(RequestJsonVO requestJsonVO);

    ResultObjectVO save(RequestJsonVO requestJsonVO);

    ResultObjectVO findById(RequestJsonVO requestVo);

    ResultObjectVO update(RequestJsonVO requestJsonVO);

    ResultObjectVO deleteById(RequestJsonVO requestJsonVO);

    ResultObjectVO queryPcIndexHotProducts(RequestJsonVO requestVo);
}
