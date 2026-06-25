package com.toucan.shopping.cloud.product.api.single;

import com.toucan.shopping.cloud.product.api.ShopProductApproveSkuServiceAPI;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.product.business.service.ShopProductApproveSkuBusinessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ShopProductApproveSkuServiceSingleImpl implements ShopProductApproveSkuServiceAPI {

    @Autowired
    private ShopProductApproveSkuBusinessService shopProductApproveSkuBusinessService;

    @Override
    public ResultObjectVO queryById(RequestJsonVO requestJsonVO) {
        return shopProductApproveSkuBusinessService.queryById(requestJsonVO);
    }

    @Override
    public ResultObjectVO queryByIdList(RequestJsonVO requestJsonVO) {
        return shopProductApproveSkuBusinessService.queryByIdList(requestJsonVO);
    }

    @Override
    public ResultObjectVO queryListPage(RequestJsonVO requestJsonVO) {
        return shopProductApproveSkuBusinessService.queryListPage(requestJsonVO);
    }

    @Override
    public ResultObjectVO queryByIdForFront(RequestJsonVO requestJsonVO) {
        return shopProductApproveSkuBusinessService.queryByIdForFront(requestJsonVO);
    }

    @Override
    public ResultObjectVO queryOneByProductApproveIdForFront(RequestJsonVO requestJsonVO) {
        return shopProductApproveSkuBusinessService.queryOneByProductApproveIdForFront(requestJsonVO);
    }
}
