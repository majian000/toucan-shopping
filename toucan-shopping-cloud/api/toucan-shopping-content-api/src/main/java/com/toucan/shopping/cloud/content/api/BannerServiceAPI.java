package com.toucan.shopping.cloud.content.api;

import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;

public interface BannerServiceAPI {

    ResultObjectVO queryListPage(RequestJsonVO requestJsonVO);

    ResultObjectVO queryList(RequestJsonVO requestJsonVO);

    ResultObjectVO flushWebIndexCache(RequestJsonVO requestVo);

    ResultObjectVO queryIndexList(RequestJsonVO requestJsonVO);

    ResultObjectVO clearWebIndexCache(RequestJsonVO requestVo);

    ResultObjectVO save(RequestJsonVO requestVo);

    ResultObjectVO update(RequestJsonVO requestVo);

    ResultObjectVO findById(RequestJsonVO requestVo);

    ResultObjectVO deleteById(RequestJsonVO requestVo);

    ResultObjectVO deleteByIds(RequestJsonVO requestVo);

}
