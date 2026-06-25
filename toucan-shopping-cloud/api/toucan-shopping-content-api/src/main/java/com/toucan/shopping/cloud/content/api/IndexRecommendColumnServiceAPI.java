package com.toucan.shopping.cloud.content.api;

import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;

/**
 * PC首页栏目服务
 */
public interface IndexRecommendColumnServiceAPI {

    ResultObjectVO queryListPage(RequestJsonVO requestJsonVO);

    ResultObjectVO save(RequestJsonVO requestJsonVO);

    ResultObjectVO update(RequestJsonVO requestJsonVO);

    ResultObjectVO deleteById(RequestJsonVO requestJsonVO);

    ResultObjectVO queryPcIndexColumns(RequestJsonVO requestVo);

    ResultObjectVO findById(RequestJsonVO requestVo);
}
