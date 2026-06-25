package com.toucan.shopping.cloud.seller.api.cloud.feign.service;

import com.toucan.shopping.cloud.seller.api.SellerLoginHistoryServiceAPI;
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
public interface FeignSellerLoginHistoryService extends SellerLoginHistoryServiceAPI {

    /**
     * 保存
     * @param requestJsonVO
     * @return
     */
    @PostMapping("/save")
    ResultObjectVO save(@RequestBody RequestJsonVO requestJsonVO);

    /**
     * 查询列表页
     * @param requestVo
     * @return
     */
    @GetMapping("/list/page")
    ResultObjectVO queryListPage(@RequestBody RequestJsonVO requestVo);

    /**
     * 查询最近10条
     * @param requestVo
     * @return
     */
    @GetMapping("/query/list/latest/10")
    ResultObjectVO queryListByLatest10(@RequestBody RequestJsonVO requestVo);

}
