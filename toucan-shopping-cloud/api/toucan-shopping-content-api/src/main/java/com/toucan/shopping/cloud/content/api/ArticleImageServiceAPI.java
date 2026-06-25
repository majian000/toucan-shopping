package com.toucan.shopping.cloud.content.api;

import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;

public interface ArticleImageServiceAPI {

    ResultObjectVO save(RequestJsonVO requestJsonVO);

    ResultObjectVO deleteInvalidData(RequestJsonVO requestJsonVO);

    ResultObjectVO queryListPage(RequestJsonVO requestJsonVO);

    ResultObjectVO deleteById(RequestJsonVO requestVo);

    ResultObjectVO deleteByIds(RequestJsonVO requestVo);

}
