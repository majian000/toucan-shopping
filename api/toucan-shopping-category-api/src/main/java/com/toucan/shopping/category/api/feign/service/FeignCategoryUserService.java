package com.toucan.shopping.category.api.feign.service;

import com.toucan.shopping.category.api.feign.fallback.FeignCategoryServiceFallbackFactory;
import com.toucan.shopping.category.export.entity.Category;
import com.toucan.shopping.common.vo.RequestJsonVO;
import com.toucan.shopping.common.vo.ResultObjectVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(value = "toucan-shopping-gateway",path = "/toucan-shopping-category-proxy/category/user",fallbackFactory = FeignCategoryServiceFallbackFactory.class)
public interface FeignCategoryUserService {



    @RequestMapping(value = "/query/id",method= RequestMethod.POST,produces = "application/json;charset=UTF-8")
    ResultObjectVO queryById(@RequestHeader("toucan-sign-header") String signHeader,@RequestBody RequestJsonVO requestJsonVO);


    @RequestMapping(value = "/query/ids",method= RequestMethod.POST,produces = "application/json;charset=UTF-8")
    ResultObjectVO queryByIdList(@RequestHeader("toucan-sign-header") String signHeader,@RequestBody RequestJsonVO requestJsonVO);


    @RequestMapping(value = "/query/tree/area/code",method= RequestMethod.POST,produces = "application/json;charset=UTF-8")
    ResultObjectVO queryCategoryTreeByAreaCode(@RequestHeader("toucan-sign-header") String signHeader,@RequestBody RequestJsonVO requestJsonVO);


}
