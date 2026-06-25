package com.toucan.shopping.cloud.product.api.single;

import com.toucan.shopping.cloud.product.api.feign.service.FeignShopProductApproveService;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.common.vo.ResultTypeObjectVO;
import com.toucan.shopping.modules.product.business.service.ShopProductApproveBusinessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ShopProductApproveServiceSingleImpl implements FeignShopProductApproveService {

    @Autowired
    private ShopProductApproveBusinessService shopProductApproveBusinessService;

    @Override
    public ResultObjectVO publish(RequestJsonVO requestJsonVO) {
        return shopProductApproveBusinessService.publish(requestJsonVO);
    }

    @Override
    public ResultObjectVO republish(RequestJsonVO requestJsonVO) {
        return shopProductApproveBusinessService.republish(requestJsonVO);
    }

    @Override
    public ResultObjectVO queryListPage(RequestJsonVO requestJsonVO) {
        return shopProductApproveBusinessService.queryListPage(requestJsonVO);
    }

    @Override
    public ResultObjectVO queryByProductApproveId(RequestJsonVO requestJsonVO) {
        return shopProductApproveBusinessService.queryByProductApproveId(requestJsonVO);
    }

    @Override
    public ResultObjectVO queryByProductApproveIdAndShopId(RequestJsonVO requestJsonVO) {
        return shopProductApproveBusinessService.queryByProductApproveIdAndShopId(requestJsonVO);
    }

    @Override
    public ResultObjectVO reject(RequestJsonVO requestJsonVO) {
        return shopProductApproveBusinessService.reject(requestJsonVO);
    }

    @Override
    public ResultObjectVO pass(RequestJsonVO requestJsonVO) {
        return shopProductApproveBusinessService.pass(requestJsonVO);
    }

    @Override
    public ResultObjectVO deleteById(RequestJsonVO requestJsonVO) {
        return shopProductApproveBusinessService.deleteById(requestJsonVO);
    }

    @Override
    public ResultObjectVO deleteByProductApproveIdAndShopId(RequestJsonVO requestJsonVO) {
        return shopProductApproveBusinessService.deleteByProductApproveIdAndShopId(requestJsonVO);
    }

    @Override
    public ResultObjectVO queryNewestListByShopId(RequestJsonVO requestJsonVO) {
        return shopProductApproveBusinessService.queryNewestListByShopId(requestJsonVO);
    }

    @Override
    public ResultObjectVO findOneUnderReviewByFreightTemplateId(RequestJsonVO requestJsonVO) {
        return shopProductApproveBusinessService.findOneUnderReviewByFreightTemplateId(requestJsonVO);
    }

    @Override
    public ResultObjectVO queryApproveListByShopId(RequestJsonVO requestJsonVO) {
        return shopProductApproveBusinessService.queryApproveListByShopId(requestJsonVO);
    }

    @Override
    public ResultTypeObjectVO<Long> queryApproveCountByShopId(RequestJsonVO requestJsonVO) {
        return shopProductApproveBusinessService.queryApproveCountByShopId(requestJsonVO);
    }
}
