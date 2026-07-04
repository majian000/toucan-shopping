package com.toucan.shopping.cloud.user.api.cloud.feign.service;

import com.toucan.shopping.cloud.user.api.UserBuyCarServiceAPI;
import com.toucan.shopping.cloud.user.api.cloud.feign.fallback.FeignUserBuyCarServiceFallbackFactory;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * 用户购物车
 * @author majian
 */
@FeignClient(value = "toucan-shopping-gateway",path = "/toucan-shopping-user-proxy/userBuyCar",fallbackFactory = FeignUserBuyCarServiceFallbackFactory.class)
public interface FeignUserBuyCarService extends UserBuyCarServiceAPI {


    @Override
    @RequestMapping(value="/save", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    ResultObjectVO save(@RequestBody RequestJsonVO requestJsonVO);


    /**
     * 移除购物车
     * @param requestVo
     * @return
     */
    @Override
    @RequestMapping(value="/remove/buy/car", produces = "application/json;charset=UTF-8", method = RequestMethod.DELETE)
    ResultObjectVO removeBuyCar(@RequestBody RequestJsonVO requestVo);


    @Override
    @RequestMapping(value="/list/userMainId", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    ResultObjectVO listByUserMainId(@RequestBody RequestJsonVO requestJsonVO);



    /**
     * 清空指定用户的购物车
     * @param requestVo
     * @return
     */
    @Override
    @RequestMapping(value="/clear/buy/car/userMainId", produces = "application/json;charset=UTF-8", method = RequestMethod.DELETE)
    ResultObjectVO clearByUserMainId(@RequestBody RequestJsonVO requestVo);



    @Override
    @RequestMapping(value="/updates", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    ResultObjectVO updates(@RequestBody RequestJsonVO requestJsonVO);


    @Override
    @RequestMapping(value="/update", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    ResultObjectVO update(@RequestBody RequestJsonVO requestJsonVO);

}
