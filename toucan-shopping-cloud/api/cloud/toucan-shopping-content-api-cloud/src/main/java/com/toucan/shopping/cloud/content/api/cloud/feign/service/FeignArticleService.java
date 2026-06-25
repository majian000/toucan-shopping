package com.toucan.shopping.cloud.content.api.cloud.feign.service;

import com.toucan.shopping.cloud.content.api.cloud.feign.fallback.FeignArticleServiceFallbackFactory;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.common.vo.ResultTypeObjectVO;
import com.toucan.shopping.modules.content.vo.ArticleVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(value = "toucan-shopping-gateway", path = "/toucan-shopping-content-proxy/article", fallbackFactory = FeignArticleServiceFallbackFactory.class)
public interface FeignArticleService extends com.toucan.shopping.cloud.content.api.feign.service.FeignArticleService {

    @Override
    @RequestMapping(value = "/query/list/page", produces = "application/json;charset=UTF-8")
    ResultObjectVO queryListPage(@RequestBody RequestJsonVO requestJsonVO);

    @Override
    @RequestMapping(value = "/save", produces = "application/json;charset=UTF-8")
    ResultObjectVO save(@RequestBody RequestJsonVO requestJsonVO);

    @Override
    @RequestMapping(value = "/queryMaxSort", produces = "application/json;charset=UTF-8")
    ResultTypeObjectVO<Long> queryMaxSort(@RequestBody RequestJsonVO requestJsonVO);

    @Override
    @RequestMapping(value = "/findById", produces = "application/json;charset=UTF-8")
    ResultTypeObjectVO<ArticleVO> findById(@RequestBody RequestJsonVO requestJsonVO);

    @Override
    @RequestMapping(value = "/update", produces = "application/json;charset=UTF-8")
    ResultObjectVO update(@RequestBody RequestJsonVO requestJsonVO);

    @Override
    @RequestMapping(value = "/deleteById", produces = "application/json;charset=UTF-8")
    ResultObjectVO deleteById(@RequestBody RequestJsonVO requestJsonVO);

    @Override
    @RequestMapping(value = "/delete/ids", produces = "application/json;charset=UTF-8", method = RequestMethod.DELETE)
    ResultObjectVO deleteByIds(@RequestBody RequestJsonVO requestJsonVO);

}
