package com.toucan.shopping.cloud.seller.api.cloud.feign.service;

import com.toucan.shopping.cloud.seller.api.cloud.feign.fallback.FeignSellerDesignerImageServiceFallbackFactory;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

/**
 * 店铺装修图片
 * @author majian
 */
@FeignClient(value = "toucan-shopping-gateway", path = "/toucan-shopping-seller-proxy/seller/designer/image", fallbackFactory = FeignSellerDesignerImageServiceFallbackFactory.class)
public interface FeignSellerDesignerImageService {

    /**
     * 根据ID查询
     * @param requestVo
     * @return
     */
    @PostMapping("/find/id")
    ResultObjectVO findById(@RequestBody RequestJsonVO requestVo);

    /**
     * 查询列表页
     * @param requestJsonVO
     * @return
     */
    @GetMapping("/query/list/page")
    ResultObjectVO queryListPage(@RequestBody RequestJsonVO requestJsonVO);

    /**
     * 保存
     * @param requestJsonVO
     * @return
     */
    @PostMapping("/save")
    ResultObjectVO save(@RequestBody RequestJsonVO requestJsonVO);

    /**
     * 根据ID删除
     * @param requestJsonVO
     * @return
     */
    @DeleteMapping("/delete/id")
    ResultObjectVO deleteById(@RequestBody RequestJsonVO requestJsonVO);

    /**
     * 修改
     * @param requestJsonVO
     * @return
     */
    @PostMapping("/update")
    ResultObjectVO update(@RequestBody RequestJsonVO requestJsonVO);

    /**
     * 管理员根据ID删除
     * @param requestJsonVO
     * @return
     */
    @DeleteMapping("/admin/delete/id")
    ResultObjectVO deleteByIdForAdmin(@RequestBody RequestJsonVO requestJsonVO);

}
