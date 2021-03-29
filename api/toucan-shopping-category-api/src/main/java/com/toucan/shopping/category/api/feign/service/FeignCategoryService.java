package com.toucan.shopping.category.api.feign.service;

import com.toucan.shopping.category.api.feign.fallback.FeignCategoryServiceFallbackFactory;
import com.toucan.shopping.category.export.entity.Category;
import com.toucan.shopping.common.vo.RequestJsonVO;
import com.toucan.shopping.common.vo.ResultObjectVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(value = "toucan-shopping-gateway",path = "/toucan-shopping-category-proxy/category",fallbackFactory = FeignCategoryServiceFallbackFactory.class)
public interface FeignCategoryService {



    @RequestMapping(value = "/query/id",method= RequestMethod.POST,produces = "application/json;charset=UTF-8")
    ResultObjectVO queryById(@RequestHeader("bbs-sign-header") String signHeader,@RequestBody RequestJsonVO requestJsonVO);


    @RequestMapping(value = "/query/ids",method= RequestMethod.POST,produces = "application/json;charset=UTF-8")
    ResultObjectVO queryByIdList(@RequestHeader("bbs-sign-header") String signHeader,@RequestBody RequestJsonVO requestJsonVO);


    @RequestMapping(value = "/query/tree/area/code",method= RequestMethod.POST,produces = "application/json;charset=UTF-8")
    ResultObjectVO queryCategoryTree(@RequestHeader("bbs-sign-header") String signHeader,@RequestBody RequestJsonVO requestJsonVO);


}
