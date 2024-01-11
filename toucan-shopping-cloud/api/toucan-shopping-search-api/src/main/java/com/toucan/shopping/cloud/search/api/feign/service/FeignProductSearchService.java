package com.toucan.shopping.cloud.search.api.feign.service;

import com.toucan.shopping.cloud.search.api.feign.fallback.FeignProductSearchServiceFallbackFactory;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient(value = "toucan-shopping-gateway",path = "/toucan-shopping-search-proxy/productSearch",fallbackFactory = FeignProductSearchServiceFallbackFactory.class)
public interface FeignProductSearchService {



    /**
     * 搜索商品
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/search",produces = "application/json;charset=UTF-8")
    ResultObjectVO search(@RequestBody RequestJsonVO requestJsonVO);


    /**
     * 保存到搜索
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/save",produces = "application/json;charset=UTF-8")
    ResultObjectVO save(@RequestBody RequestJsonVO requestJsonVO);



    /**
     * 根据SKUID查询
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/queryBySkuId",produces = "application/json;charset=UTF-8")
    ResultObjectVO queryBySkuId(@RequestBody RequestJsonVO requestJsonVO);



    /**
     * 更新到搜索
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/update",produces = "application/json;charset=UTF-8")
    ResultObjectVO update(@RequestBody RequestJsonVO requestJsonVO);



    /**
     * 从搜索中删除
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/removeById",produces = "application/json;charset=UTF-8")
    ResultObjectVO removeById(@RequestBody RequestJsonVO requestJsonVO);


    /**
     * 清空
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/clear",produces = "application/json;charset=UTF-8")
    ResultObjectVO clear(@RequestBody RequestJsonVO requestJsonVO);
}
