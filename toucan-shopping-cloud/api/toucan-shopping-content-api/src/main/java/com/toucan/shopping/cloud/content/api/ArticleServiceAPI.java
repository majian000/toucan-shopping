package com.toucan.shopping.cloud.content.api;

import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.common.vo.ResultTypeObjectVO;
import com.toucan.shopping.modules.content.vo.ArticleVO;

public interface ArticleServiceAPI {

    ResultObjectVO queryListPage(RequestJsonVO requestJsonVO);

    ResultObjectVO save(RequestJsonVO requestJsonVO);

    ResultTypeObjectVO<Long> queryMaxSort(RequestJsonVO requestJsonVO);

    ResultTypeObjectVO<ArticleVO> findById(RequestJsonVO requestJsonVO);

    ResultObjectVO update(RequestJsonVO requestJsonVO);

    ResultObjectVO deleteById(RequestJsonVO requestJsonVO);

    ResultObjectVO deleteByIds(RequestJsonVO requestJsonVO);

}
