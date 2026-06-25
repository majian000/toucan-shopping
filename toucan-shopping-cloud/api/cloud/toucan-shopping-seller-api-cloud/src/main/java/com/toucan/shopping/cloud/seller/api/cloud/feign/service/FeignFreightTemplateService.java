package com.toucan.shopping.cloud.seller.api.cloud.feign.service;

import com.toucan.shopping.cloud.seller.api.FreightTemplateServiceAPI;
import com.toucan.shopping.cloud.seller.api.cloud.feign.fallback.FeignFreightTemplateServiceFallbackFactory;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

/**
 * 运费模板
 * @author majian
 * @date 2022-9-21 14:14:06
 */
@FeignClient(value = "toucan-shopping-gateway", path = "/toucan-shopping-seller-proxy/freightTemplate", fallbackFactory = FeignFreightTemplateServiceFallbackFactory.class)
public interface FeignFreightTemplateService extends FreightTemplateServiceAPI {

    /**
     * 查询列表页
     * @param requestVo
     * @return
     */
    @PostMapping("/list/page")
    ResultObjectVO queryListPage(@RequestBody RequestJsonVO requestVo);

    /**
     * 保存
     * @param requestJsonVO
     * @return
     */
    @PostMapping("/save")
    ResultObjectVO save(@RequestBody RequestJsonVO requestJsonVO);

    /**
     * 根据ID列表查询
     * @param requestVo
     * @return
     */
    @PostMapping("/find/id/list")
    ResultObjectVO findByIdList(@RequestBody RequestJsonVO requestVo);

    /**
     * 修改
     * @param requestJsonVO
     * @return
     */
    @PostMapping("/update")
    ResultObjectVO update(@RequestBody RequestJsonVO requestJsonVO);

    /**
     * 根据ID和用户ID查询
     * @param requestVo
     * @return
     */
    @PostMapping("/find/id/userMainId")
    ResultObjectVO findByIdAndUserMainId(@RequestBody RequestJsonVO requestVo);

    /**
     * 根据ID删除
     * @param requestJsonVO
     * @return
     */
    @DeleteMapping("/delete/id")
    ResultObjectVO deleteById(@RequestHeader("toucan-sign-header") String signHeader,@RequestBody RequestJsonVO requestJsonVO);

    /**
     * 根据ID查询
     * @param requestVo
     * @return
     */
    @PostMapping("/find/id")
    ResultObjectVO findById(@RequestBody RequestJsonVO requestVo);

}
