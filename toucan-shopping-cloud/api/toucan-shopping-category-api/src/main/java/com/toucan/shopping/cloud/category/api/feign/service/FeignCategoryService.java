package com.toucan.shopping.cloud.category.api.feign.service;

import com.toucan.shopping.cloud.category.api.feign.fallback.FeignCategoryServiceFallbackFactory;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(value = "toucan-shopping-gateway",path = "/toucan-shopping-category-proxy/category",fallbackFactory = FeignCategoryServiceFallbackFactory.class)
public interface FeignCategoryService {


    @RequestMapping(value = "/save",method= RequestMethod.POST,produces = "application/json;charset=UTF-8")
    ResultObjectVO save(@RequestHeader("toucan-sign-header") String signHeader, @RequestBody RequestJsonVO requestJsonVO);

    @RequestMapping(value = "/query/id",method= RequestMethod.POST,produces = "application/json;charset=UTF-8")
    ResultObjectVO queryById(@RequestHeader("toucan-sign-header") String signHeader,@RequestBody RequestJsonVO requestJsonVO);


    @RequestMapping(value = "/query/ids",method= RequestMethod.POST,produces = "application/json;charset=UTF-8")
    ResultObjectVO queryByIdList(@RequestHeader("toucan-sign-header") String signHeader,@RequestBody RequestJsonVO requestJsonVO);

    @RequestMapping(value = "/query/tree/area/code",method= RequestMethod.POST,produces = "application/json;charset=UTF-8")
    ResultObjectVO queryCategoryTreeByAreaCode(@RequestHeader("toucan-sign-header") String signHeader,@RequestBody RequestJsonVO requestJsonVO);


    /**
     * 根据ID查询
     * @param signHeader
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/find/id",method = RequestMethod.POST)
    ResultObjectVO findById(@RequestHeader("toucan-sign-header") String signHeader, @RequestBody RequestJsonVO requestVo);


    /**
     * 查询树表格
     * @param signHeader
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/query/tree/table",produces = "application/json;charset=UTF-8",method = RequestMethod.POST)
    ResultObjectVO queryTreeTable(@RequestHeader("toucan-sign-header") String signHeader, @RequestBody RequestJsonVO requestJsonVO);




    /**
     * 编辑
     * @param signHeader
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/update",method = RequestMethod.POST)
    ResultObjectVO update(@RequestHeader("toucan-sign-header") String signHeader, @RequestBody RequestJsonVO requestVo);



    /**
     * 查询树
     * @param signHeader
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value = "/query/tree",method = RequestMethod.POST)
    ResultObjectVO queryTree(@RequestHeader("toucan-sign-header") String signHeader, @RequestBody RequestJsonVO requestJsonVO);



    /**
     * 批量删除
     * @param signHeader
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/delete/ids",method = RequestMethod.DELETE)
    ResultObjectVO deleteByIds(@RequestHeader("toucan-sign-header") String signHeader, @RequestBody RequestJsonVO requestVo);



    /**
     * 根据ID删除
     * @param signHeader
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/delete/id",method = RequestMethod.DELETE)
    ResultObjectVO deleteById(@RequestHeader("toucan-sign-header") String signHeader, @RequestBody RequestJsonVO requestVo);





}
