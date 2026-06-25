package com.toucan.shopping.cloud.seller.api.single;

import com.toucan.shopping.cloud.seller.api.SellerShopServiceAPI;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.seller.business.service.SellerShopBusinessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SellerShopServiceSingleImpl implements SellerShopServiceAPI {

    @Autowired
    private SellerShopBusinessService sellerShopBusinessService;

    @Override
    public ResultObjectVO save(RequestJsonVO requestJsonVO) {
        return sellerShopBusinessService.save(requestJsonVO);
    }

    @Override
    public ResultObjectVO queryListPage(RequestJsonVO requestVo) {
        return sellerShopBusinessService.queryListPage(requestVo);
    }

    @Override
    public ResultObjectVO findByUser(RequestJsonVO requestVo) {
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
    public ResultObjectVO disabledEnabled(RequestJsonVO requestVo) {
        return sellerShopBusinessService.disabledEnabled(requestVo);
    }

    @Override
    public ResultObjectVO deleteByIds(RequestJsonVO requestVo) {
        return sellerShopBusinessService.deleteByIds(requestVo);
    }

    @Override
    public ResultObjectVO deleteById(RequestJsonVO requestJsonVO) {
        return sellerShopBusinessService.deleteById(requestJsonVO);
    }

    @Override
    public ResultObjectVO findById(RequestJsonVO requestVo) {
        return sellerShopBusinessService.findById(requestVo);
    }

    @Override
    public ResultObjectVO update(RequestJsonVO requestJsonVO) {
        return sellerShopBusinessService.update(requestJsonVO);
    }

    @Override
    public ResultObjectVO updateLogo(RequestJsonVO requestJsonVO) {
        return sellerShopBusinessService.updateLogo(requestJsonVO);
    }

    @Override
    public ResultObjectVO updateInfo(RequestJsonVO requestJsonVO) {
        return sellerShopBusinessService.updateInfo(requestJsonVO);
    }

}
