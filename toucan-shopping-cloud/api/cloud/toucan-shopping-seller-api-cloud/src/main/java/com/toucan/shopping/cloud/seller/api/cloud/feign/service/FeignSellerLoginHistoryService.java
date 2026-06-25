package com.toucan.shopping.cloud.seller.api.cloud.feign.service;

import com.toucan.shopping.cloud.seller.api.cloud.feign.fallback.FeignSellerLoginHistoryServiceFallbackFactory;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

/**
 * 商家登录历史
 * @author majian
 */
@FeignClient(value = "toucan-shopping-gateway", path = "/toucan-shopping-seller-proxy/seller/loginHistory", fallbackFactory = FeignSellerLoginHistoryServiceFallbackFactory.class)
public interface FeignSellerLoginHistoryService {

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
     * 查询最近10条
     * @param signHeader
     * @param requestVo
     * @return
     */
    @GetMapping("/query/list/latest/10")
    ResultObjectVO queryListByLatest10(@RequestHeader("toucan-sign-header") String signHeader, @RequestBody RequestJsonVO requestVo);

}
