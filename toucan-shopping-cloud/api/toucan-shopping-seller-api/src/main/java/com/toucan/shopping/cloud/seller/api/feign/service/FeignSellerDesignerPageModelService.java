package com.toucan.shopping.cloud.seller.api.feign.service;

import com.toucan.shopping.cloud.seller.api.feign.fallback.FeignSellerDesignerPageModelServiceFallbackFactory;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(value = "toucan-shopping-gateway",path = "/toucan-shopping-seller-proxy/seller/designer/page/model",fallbackFactory = FeignSellerDesignerPageModelServiceFallbackFactory.class)
public interface FeignSellerDesignerPageModelService {



    /**
     * 保存设计器页面
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/onlySaveOne",produces = "application/json;charset=UTF-8")
    ResultObjectVO onlySaveOne(@RequestBody RequestJsonVO requestJsonVO);



    /**
     * 查询最后一个模型
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/queryLastOne",produces = "application/json;charset=UTF-8")
    ResultObjectVO queryLastOne(@RequestBody RequestJsonVO requestJsonVO);

    /**
     * 查询列表页
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/list/page",produces = "application/json;charset=UTF-8")
    ResultObjectVO queryListPage(@RequestBody RequestJsonVO requestVo);


    /**
     * 根据ID删除
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/admin/delete/id",produces = "application/json;charset=UTF-8",method = RequestMethod.DELETE)
    ResultObjectVO deleteByIdForAdmin(@RequestBody RequestJsonVO requestJsonVO);

}
