package com.toucan.shopping.cloud.seller.api.single;

import com.toucan.shopping.cloud.seller.api.feign.service.FeignSellerShopService;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.seller.business.service.SellerShopBusinessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SellerShopServiceSingleImpl implements FeignSellerShopService {

    @Autowired
    private SellerShopBusinessService sellerShopBusinessService;

    @Override
    public ResultObjectVO save(String signHeader, RequestJsonVO requestJsonVO) {
        return sellerShopBusinessService.save(requestJsonVO);
    }

    @Override
    public ResultObjectVO queryListPage(String signHeader, RequestJsonVO requestVo) {
        return sellerShopBusinessService.queryListPage(requestVo);
    }

    @Override
    public ResultObjectVO findByUser(String signHeader, RequestJsonVO requestVo) {
        return sellerShopBusinessService.findByUser(requestVo);
    }

    @Override
    public ResultObjectVO findByIdList(RequestJsonVO requestVo) {
        return sellerShopBusinessService.findByIdList(requestVo);
    }

    @Override
    public ResultObjectVO flushCache(RequestJsonVO requestJsonVO) {
        return sellerShopBusinessService.flushCache(requestJsonVO);
    }

    @Override
    public ResultObjectVO disabledEnabled(String signHeader, RequestJsonVO requestVo) {
        return sellerShopBusinessService.disabledEnabled(requestVo);
    }

    @Override
    public ResultObjectVO deleteByIds(String signHeader, RequestJsonVO requestVo) {
        return sellerShopBusinessService.deleteByIds(requestVo);
    }

    @Override
    public ResultObjectVO deleteById(String signHeader, RequestJsonVO requestJsonVO) {
        return sellerShopBusinessService.deleteById(requestJsonVO);
    }

    @Override
    public ResultObjectVO findById(String signHeader, RequestJsonVO requestVo) {
        return sellerShopBusinessService.findById(requestVo);
    }

    @Override
    public ResultObjectVO update(String signHeader, RequestJsonVO requestJsonVO) {
        return sellerShopBusinessService.update(requestJsonVO);
    }

    @Override
    public ResultObjectVO updateLogo(String signHeader, RequestJsonVO requestJsonVO) {
        return sellerShopBusinessService.updateLogo(requestJsonVO);
    }

    @Override
    public ResultObjectVO updateInfo(String signHeader, RequestJsonVO requestJsonVO) {
        return sellerShopBusinessService.updateInfo(requestJsonVO);
    }

}
