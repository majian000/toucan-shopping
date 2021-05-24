package com.toucan.shopping.cloud.area.api.feign.service;

import com.toucan.shopping.cloud.area.api.feign.fallback.FeignAreaServiceFallbackFactory;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(value = "toucan-shopping-gateway",path = "/toucan-shopping-area-proxy/area",fallbackFactory = FeignAreaServiceFallbackFactory.class)
public interface FeignAreaService {


    @RequestMapping(value="/save",method= RequestMethod.POST,produces = "application/json;charset=UTF-8")
    ResultObjectVO save(@RequestHeader(value = "toucan-sign-header",defaultValue = "-1") String signHeader, @RequestBody RequestJsonVO requestJsonVO);

    @RequestMapping(value="/query/all",produces = "application/json;charset=UTF-8")
    ResultObjectVO queryAll(@RequestHeader(value = "toucan-sign-header", defaultValue = "-1") String signHeader,@RequestBody RequestJsonVO requestJsonVO);


    /**
     * 查询树表格
     * @param signHeader
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/query/tree/table",produces = "application/json;charset=UTF-8",method = RequestMethod.POST)
    ResultObjectVO queryAreaTreeTable(@RequestHeader("toucan-sign-header") String signHeader, @RequestBody RequestJsonVO requestJsonVO);




    /**
     * 查询地区树
     * @param signHeader
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value = "/query/tree",method = RequestMethod.POST)
    ResultObjectVO queryTree(@RequestHeader("toucan-sign-header") String signHeader, @RequestBody RequestJsonVO requestJsonVO);




}
