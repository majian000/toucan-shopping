package com.toucan.shopping.cloud.seller.api.cloud.feign.service;

import com.toucan.shopping.cloud.seller.api.SellerDesignerPageModelServiceAPI;
import com.toucan.shopping.cloud.seller.api.cloud.feign.fallback.FeignSellerDesignerPageModelServiceFallbackFactory;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

/**
 * 店铺装修页面模型
 * @author majian
 */
@FeignClient(value = "toucan-shopping-gateway", path = "/toucan-shopping-seller-proxy/seller/designer/page/model", fallbackFactory = FeignSellerDesignerPageModelServiceFallbackFactory.class)
public interface FeignSellerDesignerPageModelService extends SellerDesignerPageModelServiceAPI {

    /**
     * 只保存一条
     * @param requestJsonVO
     * @return
     */
    @PostMapping("/onlySaveOne")
    ResultObjectVO onlySaveOne(@RequestBody RequestJsonVO requestJsonVO);

    /**
     * 查询最近一条
     * @param requestJsonVO
     * @return
     */
    @PostMapping("/queryLastOne")
    ResultObjectVO queryLastOne(@RequestBody RequestJsonVO requestJsonVO);

    /**
     * 查询列表页
     * @param requestVo
     * @return
     */
    @GetMapping("/list/page")
    ResultObjectVO queryListPage(@RequestBody RequestJsonVO requestVo);

    /**
     * 管理员根据ID删除
     * @param requestJsonVO
     * @return
     */
    @DeleteMapping("/admin/delete/id")
    ResultObjectVO deleteByIdForAdmin(@RequestBody RequestJsonVO requestJsonVO);

}
