package com.toucan.shopping.cloud.seller.api.single;

import com.toucan.shopping.cloud.seller.api.feign.service.FeignShopBannerService;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.seller.business.service.ShopBannerBusinessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ShopBannerServiceSingleImpl implements FeignShopBannerService {

    @Autowired
    private ShopBannerBusinessService shopBannerBusinessService;

    @Override
    public ResultObjectVO queryListPage(RequestJsonVO requestJsonVO) {
        return shopBannerBusinessService.queryListPage(requestJsonVO);
    }

    @Override
    public ResultObjectVO save(RequestJsonVO requestJsonVO) {
        return shopBannerBusinessService.save(requestJsonVO);
    }

    @Override
    public ResultObjectVO deleteById(RequestJsonVO requestJsonVO) {
        return shopBannerBusinessService.deleteById(requestJsonVO);
    }

    @Override
    public ResultObjectVO findById(RequestJsonVO requestVo) {
        return shopBannerBusinessService.findById(requestVo);
    }

    @Override
    public ResultObjectVO update(RequestJsonVO requestJsonVO) {
        return shopBannerBusinessService.update(requestJsonVO);
    }

    @Override
    public ResultObjectVO deleteByIdForAdmin(RequestJsonVO requestJsonVO) {
        return shopBannerBusinessService.deleteByIdForAdmin(requestJsonVO);
    }

    @Override
    public ResultObjectVO queryIndexList(RequestJsonVO requestVo) {
        return shopBannerBusinessService.queryIndexList(requestVo);
    }

}
