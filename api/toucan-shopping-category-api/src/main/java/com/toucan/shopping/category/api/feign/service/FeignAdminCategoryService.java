package com.toucan.shopping.category.api.feign.service;

import com.toucan.shopping.category.api.feign.fallback.FeignCategoryServiceFallbackFactory;
import com.toucan.shopping.category.export.entity.Category;
import com.toucan.shopping.common.vo.RequestJsonVO;
import com.toucan.shopping.common.vo.ResultObjectVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(value = "toucan-shopping-gateway",path = "/toucan-shopping-category-proxy/category/admin",fallbackFactory = FeignCategoryServiceFallbackFactory.class)
public interface FeignAdminCategoryService {


    @RequestMapping(value = "/save",method= RequestMethod.POST,produces = "application/json;charset=UTF-8")
    ResultObjectVO save(@RequestHeader("bbs-sign-header") String signHeader, @RequestBody RequestJsonVO requestJsonVO);

    @RequestMapping(value = "/query/id",method= RequestMethod.POST,produces = "application/json;charset=UTF-8")
    ResultObjectVO queryById(@RequestHeader("bbs-sign-header") String signHeader,@RequestBody RequestJsonVO requestJsonVO);


    @RequestMapping(value = "/query/ids",method= RequestMethod.POST,produces = "application/json;charset=UTF-8")
    ResultObjectVO queryByIdList(@RequestHeader("bbs-sign-header") String signHeader,@RequestBody RequestJsonVO requestJsonVO);

}
