package com.toucan.shopping.cloud.seller.api.cloud.feign.service;

import com.toucan.shopping.cloud.seller.api.cloud.feign.fallback.FeignSellerShopServiceFallbackFactory;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

/**
 * 商家店铺
 * @author majian
 */
@FeignClient(value = "toucan-shopping-gateway", path = "/toucan-shopping-seller-proxy/sellerShop", fallbackFactory = FeignSellerShopServiceFallbackFactory.class)
public interface FeignSellerShopService {

    /**
     * 保存
     * @param signHeader
     * @param requestJsonVO
     * @return
     */
    @PostMapping("/save")
    ResultObjectVO save(@RequestHeader("toucan-sign-header") String signHeader, @RequestBody RequestJsonVO requestJsonVO);

    /**
     * 查询列表页
     * @param signHeader
     * @param requestVo
     * @return
     */
    @GetMapping("/list/page")
    ResultObjectVO queryListPage(@RequestHeader("toucan-sign-header") String signHeader, @RequestBody RequestJsonVO requestVo);

    /**
     * 根据用户查询
     * @param signHeader
     * @param requestVo
     * @return
     */
    @GetMapping("/find/by/user")
    ResultObjectVO findByUser(@RequestHeader("toucan-sign-header") String signHeader, @RequestBody RequestJsonVO requestVo);

    /**
     * 根据ID列表查询
     * @param requestVo
     * @return
     */
    @PostMapping("/find/idList")
    ResultObjectVO findByIdList(@RequestBody RequestJsonVO requestVo);

    /**
     * 刷新缓存
     * @param requestJsonVO
     * @return
     */
    @PostMapping("/flushCache")
    ResultObjectVO flushCache(@RequestBody RequestJsonVO requestJsonVO);

    /**
     * 启用/禁用
     * @param signHeader
     * @param requestVo
     * @return
     */
    @PostMapping("/disabled/enabled")
    ResultObjectVO disabledEnabled(@RequestHeader("toucan-sign-header") String signHeader, @RequestBody RequestJsonVO requestVo);

    /**
     * 根据ID列表删除
     * @param signHeader
     * @param requestVo
     * @return
     */
    @DeleteMapping("/delete/ids")
    ResultObjectVO deleteByIds(@RequestHeader("toucan-sign-header") String signHeader, @RequestBody RequestJsonVO requestVo);

    /**
     * 根据ID删除
     * @param signHeader
     * @param requestJsonVO
     * @return
     */
    @DeleteMapping("/delete/id")
    ResultObjectVO deleteById(@RequestHeader("toucan-sign-header") String signHeader, @RequestBody RequestJsonVO requestJsonVO);

    /**
     * 根据ID查询
     * @param signHeader
     * @param requestVo
     * @return
     */
    @PostMapping("/find/id")
    ResultObjectVO findById(@RequestHeader("toucan-sign-header") String signHeader, @RequestBody RequestJsonVO requestVo);

    /**
     * 修改
     * @param signHeader
     * @param requestJsonVO
     * @return
     */
    @PostMapping("/update")
    ResultObjectVO update(@RequestHeader("toucan-sign-header") String signHeader, @RequestBody RequestJsonVO requestJsonVO);

    /**
     * 修改Logo
     * @param signHeader
     * @param requestJsonVO
     * @return
     */
    @PostMapping("/update/logo")
    ResultObjectVO updateLogo(@RequestHeader("toucan-sign-header") String signHeader, @RequestBody RequestJsonVO requestJsonVO);

    /**
     * 修改信息
     * @param signHeader
     * @param requestJsonVO
     * @return
     */
    @PostMapping("/update/info")
    ResultObjectVO updateInfo(@RequestHeader("toucan-sign-header") String signHeader, @RequestBody RequestJsonVO requestJsonVO);

}
