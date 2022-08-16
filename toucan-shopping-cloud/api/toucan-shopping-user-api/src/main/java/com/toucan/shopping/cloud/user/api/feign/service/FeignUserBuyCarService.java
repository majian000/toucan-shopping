package com.toucan.shopping.cloud.user.api.feign.service;

import com.toucan.shopping.cloud.user.api.feign.fallback.FeignUserBuyCarServiceFallbackFactory;
import com.toucan.shopping.cloud.user.api.feign.fallback.FeignUserServiceFallbackFactory;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.user.entity.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * 用户购物车
 * @author majian
 */
@FeignClient(value = "toucan-shopping-gateway",path = "/toucan-shopping-user-proxy/userBuyCar",fallbackFactory = FeignUserBuyCarServiceFallbackFactory.class)
public interface FeignUserBuyCarService {


    @RequestMapping(value="/save",produces = "application/json;charset=UTF-8")
    ResultObjectVO save(@RequestBody RequestJsonVO requestJsonVO);


    /**
     * 删除指定
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/delete/id",produces = "application/json;charset=UTF-8",method = RequestMethod.DELETE)
    ResultObjectVO deleteById(@RequestBody RequestJsonVO requestVo);


    @RequestMapping(value="/list/userMainId",produces = "application/json;charset=UTF-8")
    ResultObjectVO listByUserMainId(@RequestBody RequestJsonVO requestJsonVO);

}
