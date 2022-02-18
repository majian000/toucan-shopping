package com.toucan.shopping.cloud.seller.api.feign.service;

import com.toucan.shopping.cloud.seller.api.feign.fallback.FeignSellerShopServiceFallbackFactory;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(value = "toucan-shopping-gateway",path = "/toucan-shopping-seller-proxy/sellerShop",fallbackFactory = FeignSellerShopServiceFallbackFactory.class)
public interface FeignSellerShopService {


    /**
     * 保存店铺
     * @param signHeader
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/save",produces = "application/json;charset=UTF-8")
    ResultObjectVO save(@RequestHeader("toucan-sign-header") String signHeader, @RequestBody RequestJsonVO requestJsonVO);


    /**
     * 查询列表页
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/list/page",produces = "application/json;charset=UTF-8")
    ResultObjectVO queryListPage(@RequestHeader("toucan-sign-header") String signHeader,@RequestBody RequestJsonVO requestVo);


    /**
     * 查询指定用户下店铺(启用的店铺)
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/find/by/user",produces = "application/json;charset=UTF-8")
    ResultObjectVO findByUser(@RequestHeader("toucan-sign-header") String signHeader,@RequestBody RequestJsonVO requestVo);




    /**
     * 刷新缓存
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/flushCache",produces = "application/json;charset=UTF-8")
    ResultObjectVO flushCache(@RequestBody RequestJsonVO requestJsonVO);

    /**
     * 启用禁用店铺
     * @param signHeader
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/disabled/enabled",produces = "application/json;charset=UTF-8")
    ResultObjectVO disabledEnabled(@RequestHeader("toucan-sign-header") String signHeader,@RequestBody RequestJsonVO requestVo);


    /**
     * 批量删除店铺
     * @param signHeader
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/delete/ids",produces = "application/json;charset=UTF-8",method = RequestMethod.DELETE)
    ResultObjectVO deleteByIds(@RequestHeader("toucan-sign-header") String signHeader,@RequestBody RequestJsonVO requestVo);


    /**
     * 删除店铺
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/delete/id",produces = "application/json;charset=UTF-8",method = RequestMethod.DELETE)
    ResultObjectVO deleteById(@RequestHeader("toucan-sign-header") String signHeader,@RequestBody RequestJsonVO requestJsonVO);


    /**
     * 根据ID查询
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/find/id",produces = "application/json;charset=UTF-8",method = RequestMethod.POST)
    ResultObjectVO findById(@RequestHeader("toucan-sign-header") String signHeader,@RequestBody RequestJsonVO requestVo);



    /**
     * 更新
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/update",produces = "application/json;charset=UTF-8")
    ResultObjectVO update(@RequestHeader("toucan-sign-header") String signHeader,@RequestBody RequestJsonVO requestJsonVO);



    /**
     * 更新图标
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/update/logo",produces = "application/json;charset=UTF-8")
    ResultObjectVO updateLogo(@RequestHeader("toucan-sign-header") String signHeader,@RequestBody RequestJsonVO requestJsonVO);


    /**
     * 更新店铺信息
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/update/info",produces = "application/json;charset=UTF-8")
    ResultObjectVO updateInfo(@RequestHeader("toucan-sign-header") String signHeader,@RequestBody RequestJsonVO requestJsonVO);


}
