package com.toucan.shopping.cloud.content.api.cloud.feign.service;

import com.toucan.shopping.cloud.content.api.ArticleImageServiceAPI;
import com.toucan.shopping.cloud.content.api.cloud.feign.fallback.FeignArticleImageServiceFallbackFactory;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(value = "toucan-shopping-gateway", path = "/toucan-shopping-content-proxy/articleImage", fallbackFactory = FeignArticleImageServiceFallbackFactory.class)
public interface FeignArticleImageService extends ArticleImageServiceAPI {

    @Override
    @RequestMapping(value = "/save", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    ResultObjectVO save(@RequestBody RequestJsonVO requestJsonVO);

    @Override
    @RequestMapping(value = "/deleteInvalidData", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    ResultObjectVO deleteInvalidData(@RequestBody RequestJsonVO requestJsonVO);

    @Override
    @RequestMapping(value = "/query/list/page", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    ResultObjectVO queryListPage(@RequestBody RequestJsonVO requestJsonVO);

    @Override
    @RequestMapping(value = "/delete/id", produces = "application/json;charset=UTF-8", method = RequestMethod.DELETE)
    ResultObjectVO deleteById(@RequestBody RequestJsonVO requestVo);

    @Override
    @RequestMapping(value = "/delete/ids", produces = "application/json;charset=UTF-8", method = RequestMethod.DELETE)
    ResultObjectVO deleteByIds(@RequestBody RequestJsonVO requestVo);

}
