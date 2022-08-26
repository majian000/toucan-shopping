package com.toucan.shopping.cloud.user.api.feign.service;

import com.toucan.shopping.cloud.user.api.feign.fallback.FeignConsigneeAddressServiceFallbackFactory;
import com.toucan.shopping.cloud.user.api.feign.fallback.FeignUserTrueNameApproveServiceFallbackFactory;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * 收货地址服务
 */
@FeignClient(value = "toucan-shopping-gateway",path = "/toucan-shopping-user-proxy/consignee/address",fallbackFactory = FeignConsigneeAddressServiceFallbackFactory.class)
public interface FeignConsigneeAddressService {



    @RequestMapping(value="/save",produces = "application/json;charset=UTF-8")
    ResultObjectVO save(@RequestBody RequestJsonVO requestJsonVO);

}
