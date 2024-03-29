package com.toucan.shopping.cloud.user.api.feign.service;

import com.toucan.shopping.cloud.user.api.feign.fallback.FeignConsigneeAddressServiceFallbackFactory;
import com.toucan.shopping.cloud.user.api.feign.fallback.FeignUserCollectProductServiceFallbackFactory;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

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
    @RequestMapping(value="/delete/productSkuId/userMainId/appCode",produces = "application/json;charset=UTF-8",method = RequestMethod.POST)
    ResultObjectVO deleteBySkuIdAndUserMainIdAndAppCode(@RequestBody RequestJsonVO requestVo);



    /**
     * 删除指定
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/delete/id",produces = "application/json;charset=UTF-8",method = RequestMethod.DELETE)
    ResultObjectVO deleteById(@RequestBody RequestJsonVO requestVo);

    /**
     * 查询收藏列表
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/queryCollectProducts",produces = "application/json;charset=UTF-8",method = RequestMethod.POST)
    ResultObjectVO queryCollectProducts(@RequestBody RequestJsonVO requestVo);



    /**
     * 查询列表页
     */
    @RequestMapping(value="/list/page",produces = "application/json;charset=UTF-8")
    ResultObjectVO queryListPage(@RequestBody RequestJsonVO requestJsonVO);



    /**
     * 批量删除
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/delete/ids",produces = "application/json;charset=UTF-8",method = RequestMethod.DELETE)
    ResultObjectVO deleteByIds(@RequestBody RequestJsonVO requestVo);


}
