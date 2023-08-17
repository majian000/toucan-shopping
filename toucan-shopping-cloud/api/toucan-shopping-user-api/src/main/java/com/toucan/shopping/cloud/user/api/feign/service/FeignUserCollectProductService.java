package com.toucan.shopping.cloud.user.api.feign.service;

import com.toucan.shopping.cloud.user.api.feign.fallback.FeignConsigneeAddressServiceFallbackFactory;
import com.toucan.shopping.cloud.user.api.feign.fallback.FeignUserCollectProductServiceFallbackFactory;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * 用户收藏商品
 */
@FeignClient(value = "toucan-shopping-gateway",path = "/toucan-shopping-user-proxy/user/collect/product",fallbackFactory = FeignUserCollectProductServiceFallbackFactory.class)
public interface FeignUserCollectProductService {



    @RequestMapping(value="/save",produces = "application/json;charset=UTF-8")
    ResultObjectVO save(@RequestBody RequestJsonVO requestJsonVO);



    /**
     * 删除指定
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/delete/id/userMainId/appCode",produces = "application/json;charset=UTF-8",method = RequestMethod.DELETE)
    ResultObjectVO deleteByIdAndUserMainIdAndAppCode(@RequestBody RequestJsonVO requestVo);



}
